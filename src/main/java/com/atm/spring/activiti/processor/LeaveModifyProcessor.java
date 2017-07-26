package com.atm.spring.activiti.processor;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * Created by admin on 12/05/2017.
 */
public class LeaveModifyProcessor implements TaskListener {
    public void notify(DelegateTask delegateTask) {
        System.out.println("LeaveModifyProcessor...");
    }
}
