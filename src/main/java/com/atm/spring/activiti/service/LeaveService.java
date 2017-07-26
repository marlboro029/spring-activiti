package com.atm.spring.activiti.service;

import com.atm.spring.activiti.dao.LeaveDao;
import com.atm.spring.activiti.model.LeaveModel;
import com.atm.spring.activiti.utils.GenUtils;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.impl.variable.SerializableType;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

/**
 * Created by admin on 19/07/2017.
 */
@Service
public class LeaveService extends BaseActService {

    @Autowired
    private UserService userService;

    @Autowired
    private LeaveDao leaveDao;

    @Transactional
    public LeaveModel applyLeave(LeaveModel leaveModel) {
        identityService.setAuthenticatedUserId(leaveModel.getUserId());

        String businessKey = GenUtils.uuid();
        leaveModel.setId(businessKey);
        leaveModel.setApplyTime(new Date());

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("formData", leaveModel);
        runtimeService.startProcessInstanceByKey("leave", businessKey, variables);

        leaveDao.insertLeave(leaveModel);

        return leaveModel;
    }

    @Transactional
    public LeaveModel modifyLeave(LeaveModel leaveModel) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("formData", leaveModel);
        variables.put("reApply", true);
        taskService.complete(leaveModel.getTaskId(), variables);

        leaveDao.updateLeave(leaveModel);

        return leaveModel;
    }

    public List<LeaveModel> getTasksByUserId(String userId, boolean isClaimTask) {
        List<LeaveModel> leaveList = new ArrayList<LeaveModel>();
        // 获取当前用户权限
        String role = userService.getUserRole(userId);

        // 获取用户任务列表
        List<Task> tasks;
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (isClaimTask) {
            tasks = taskQuery.taskCandidateGroup(role).list();//"dept"
        } else {
            tasks = taskQuery.taskAssignee(userId).list();
        }

        // 获取流程实例Id列表
        List<String> ids = new ArrayList<String>();
        Map<String, String> taskIdMap = new HashMap<String, String>();
        for (Task task : tasks) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            ids.add(processInstance.getBusinessKey());
            taskIdMap.put(processInstance.getBusinessKey(), task.getId());
        }

        if (!ids.isEmpty()) {
            // 检索业务数据
            leaveList = leaveDao.getLeaveListByIds(ids);
        }

        for (LeaveModel leave : leaveList) {
            leave.setTaskId(taskIdMap.get(leave.getId()));
        }
        return leaveList;
    }

    @Transactional
    public void leaveReport(LeaveModel leaveModel) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("formData", leaveModel);
        taskService.complete(leaveModel.getTaskId(), variables);
        leaveDao.updateLeave(leaveModel);
    }

    /**
     * 获取历史数据
     * @param businessKey
     * @param activityIds
     * @return
     */
    public List<LeaveModel> getLeaveHistoryList(String businessKey, List<String> activityIds) {
        List<LeaveModel> lst = new ArrayList<LeaveModel>();

        // 根据业务ID获取流程实例Id
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        if (historicProcessInstance == null) {
            return lst;
        }
        String processInstanceId = historicProcessInstance.getId();

        // 根据节点名获取历史活动实例Id
        List<String> activityInstanceIds = new ArrayList<String>();
        if (activityIds != null && !activityIds.isEmpty()) {
            HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
            historicActivityInstanceQuery.processInstanceId(processInstanceId);
            for (String activityId : activityIds) {
                List<HistoricActivityInstance> historicActivityInstances = historicActivityInstanceQuery.activityId(activityId)
                        .list();
                for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                    activityInstanceIds.add(historicActivityInstance.getId());
                }
            }
        }

        // 获取历史详细信息
        HistoricDetailQuery historicDetailQuery = historyService.createHistoricDetailQuery();
        historicDetailQuery.processInstanceId(processInstanceId);
        if (!activityInstanceIds.isEmpty()) {
            for (String activityInstanceId : activityInstanceIds) {
                List<HistoricDetail> historicDetails = historicDetailQuery.activityInstanceId(activityInstanceId).list();
                addLeave(historicDetails, lst);
            }
        } else {
            List<HistoricDetail> historicDetails = historicDetailQuery.list();
            addLeave(historicDetails, lst);
        }

        return lst;
    }

    private void addLeave(List<HistoricDetail> historicDetails, List<LeaveModel> leaveList) {
        for (HistoricDetail historicDetail : historicDetails) {
            HistoricDetailVariableInstanceUpdateEntity entity = (HistoricDetailVariableInstanceUpdateEntity)historicDetail;
            // 序列化对象 + formData对象
            if (SerializableType.TYPE_NAME.equals(entity.getVariableTypeName()) && "formData".equals(entity.getName())) {
                Object o = toObject(entity.getBytes());
                LeaveModel leave = (LeaveModel)o;
                leaveList.add(leave);
            }
        }
    }

    private Object toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
}
