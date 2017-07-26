package com.atm.spring.activiti.controller;

import org.activiti.engine.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 20/07/2017.
 */
public class BaseActController extends BaseController {
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

//    public void test() {
//        runtimeService.createProcessInstanceQuery().or();
//
//        //runtimeService.createNativeProcessInstanceQuery().sql()
//    }
}
