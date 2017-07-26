package com.atm.spring.activiti.controller;

import com.atm.spring.activiti.model.UserModel;
import com.atm.spring.activiti.service.UserService;
import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by admin on 18/07/2017.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getAllUserList() {
        return userService.getAllUserList();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Object findUserById(@PathVariable("userId") String userId) {
        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        return userService.findUserById(userModel);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object checkAccount(@RequestBody UserModel userModel) {
        return userService.login(userModel);
    }
}
