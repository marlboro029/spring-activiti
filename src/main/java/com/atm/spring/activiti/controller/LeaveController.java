package com.atm.spring.activiti.controller;

import com.atm.spring.activiti.model.LeaveModel;
import com.atm.spring.activiti.service.LeaveService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 19/07/2017.
 */
@RestController
@RequestMapping("/leaves")
public class LeaveController extends BaseActController {

    @Autowired
    private LeaveService leaveService;

    /**
     * 申请假期
     * @param leaveModel
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object applyLeave(@RequestBody LeaveModel leaveModel) {
        return leaveService.applyLeave(leaveModel);
    }

    /**
     * 修改假期
     * @param leaveModel
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Object modifyLeave(@RequestBody LeaveModel leaveModel) {
        return leaveService.modifyLeave(leaveModel);
    }

    /**
     * 获取任务列表
     *
     * @param userId
     * @param claim 签收任务标识  1：签收任务 以外：正在处理的任务
     * @return
     */
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public Object getTasksByUserId(@RequestParam String userId, @RequestParam(required = false) String claim) {
        boolean isClaimTask = false;
        if ("1".equals(claim)) {
            isClaimTask = true;
        }
        return leaveService.getTasksByUserId(userId, isClaimTask);
    }

    /**
     * 完成任务
     * @param taskId
     * @param variables
     * @return
     */
    @RequestMapping(value = "/tasks/{taskId}", method = RequestMethod.POST)
    public Object complete(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        taskService.complete(taskId, variables);
        return "";
    }

    /**
     * 销假处理
     * @param leaveModel
     * @return
     */
    @RequestMapping(value = "/leave_report", method = RequestMethod.POST)
    public Object leaveReport(@RequestBody LeaveModel leaveModel) {
        leaveService.leaveReport(leaveModel);
        return "";
    }

    /**
     * 签收
     * @param leaveModel
     * @return
     */
    @RequestMapping(value = "/claim", method = RequestMethod.POST)
    public Object claim(@RequestBody LeaveModel leaveModel) {
        taskService.claim(leaveModel.getTaskId(), leaveModel.getUserId());
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/process_image/{businessKey}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getFile(@PathVariable String businessKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        byte[] bytes = leaveService.traceProcessImage(processInstance.getProcessInstanceId());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=aaa.png");
        header.setContentLength(bytes.length);

        return new HttpEntity<byte[]>(bytes, header);
    }

    /**
     * 流程部署
     * @return
     */
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public Object deploy() {
        // 部署流程
        Deployment deployment = repositoryService.createDeployment()    // 流程部署Builder
                .addClasspathResource("diagrams/leave.bpmn")            // 设定bpmn文件位置（通过classpath下）
                .deploy();                                              // 部署bpmn文件

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()  // 流程定义查询
                .deploymentId(deployment.getId())                                               // 查询条件部署ID
                .singleResult();                                                                // 返回单一结果

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("processDefinitionId", processDefinition.getId());
        map.put("processDefinitionName", processDefinition.getName());

        return map;
    }

    /**
     * 获取假期历史
     *
     * @param businessKey
     * @return
     */
    @RequestMapping(value = "/history/{businessKey}", method = RequestMethod.GET)
    public Object getLeaveHistoryList(@PathVariable String businessKey, @RequestParam(value="activityId", required=false) List<String> activityIds) {
        return leaveService.getLeaveHistoryList(businessKey, activityIds);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Object test() {

        //historyService.create
        //processEngine.
        //repositoryService.
        historyService.createHistoricProcessInstanceQuery();
        runtimeService.createProcessInstanceQuery().or().processInstanceBusinessKey("a").processInstanceId("b").endOr().list();

        return null;
    }
}
