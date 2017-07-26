package com.atm.spring.activiti.service;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 20/07/2017.
 */
public class BaseActService extends BaseService {

    @Autowired
    protected ProcessEngine processEngine;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected IdentityService identityService;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected HistoryService historyService;




    public byte[] traceProcessImage(String processInstanceId) {
        String taskId = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getId();
        if (StringUtils.isBlank(taskId))
            throw new IllegalArgumentException("任务ID不能为空！");
        // 当前任务节点
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
            throw new IllegalArgumentException("任务不存在！");

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        // List<String> activeActivityIds = runtimeService.getActiveActivityIds(task.getExecutionId());

        // 必须添加此行才能取到配置文件中的字体，待根本解决问题后删除
        // Context.setProcessEngineConfiguration(processEngineConfiguration);
        // return ProcessDiagramGenerator.generateDiagram(bpmnModel, "PNG", activeActivityIds);

        // 经过的节点
        List<String> activeActivityIds = new ArrayList<String>();
        List<String> finishedActiveActivityIds = new ArrayList<String>();

        // 已执行完的任务节点
        List<HistoricActivityInstance> finishedInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).finished().list();
        for (HistoricActivityInstance hai : finishedInstances) {
            finishedActiveActivityIds.add(hai.getActivityId());
        }

        // 已完成的节点+当前节点
        activeActivityIds.addAll(finishedActiveActivityIds);
        activeActivityIds.addAll(runtimeService.getActiveActivityIds(task.getProcessInstanceId()));

        // 经过的流
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
        List<String> highLightedFlows = new ArrayList<String>();
        getHighLightedFlows(processDefinitionEntity.getActivities(), highLightedFlows, activeActivityIds);

        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator pdg = processEngineConfiguration.getProcessDiagramGenerator();


        InputStream inputStream = pdg.generateDiagram(bpmnModel,
                "PNG",
                finishedActiveActivityIds,
                highLightedFlows,
                processEngineConfiguration.getActivityFontName(),
                processEngineConfiguration.getLabelFontName(),
                processEngineConfiguration.getAnnotationFontName(),
                processEngineConfiguration.getClassLoader(),
                1.0
        );

        try {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("生成流程图异常！", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void getHighLightedFlows(List<ActivityImpl> activityList, List<String> highLightedFlows, List<String> historicActivityInstanceList) {
        for (ActivityImpl activity : activityList) {
            if (activity.getProperty("type").equals("subProcess")) {
                // get flows for the subProcess
                getHighLightedFlows(activity.getActivities(), highLightedFlows, historicActivityInstanceList);
            }

            if (historicActivityInstanceList.contains(activity.getId())) {
                List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
                for (PvmTransition pvmTransition : pvmTransitionList) {
                    String destinationFlowId = pvmTransition.getDestination().getId();
                    if (historicActivityInstanceList.contains(destinationFlowId)) {
                        highLightedFlows.add(pvmTransition.getId());
                    }
                }
            }
        }
    }
}
