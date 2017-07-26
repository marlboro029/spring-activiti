package com.atm.spring.activiti.dao;

import com.atm.spring.activiti.model.LeaveModel;
import com.atm.spring.activiti.model.UserModel;

import java.util.List;

/**
 * Created by admin on 19/07/2017.
 */
public interface LeaveDao {

    void insertLeave(LeaveModel leave);

    List<LeaveModel> getLeaveListByIds(List<String> processInstanceIds);

    void updateLeave(LeaveModel leave);

    LeaveModel getLeaveById(LeaveModel leave);
}
