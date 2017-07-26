package com.atm.spring.activiti.service;

import com.atm.spring.activiti.dao.UserDao;
import com.atm.spring.activiti.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 19/07/2017.
 */
@Service
public class UserService extends BaseService {

    @Autowired
    private UserDao userDao;

    public List<UserModel> getAllUserList() {
        return userDao.getAllUserList();
    }

    public UserModel findUserById(UserModel userModel) {
        return userDao.findUserById(userModel);
    }

    public UserModel login(UserModel userModel) {
        UserModel user = userDao.findUserById(userModel);
        if (user == null || !user.getPassword().equals(userModel.getPassword())) {
            return new UserModel();
        }

        return user;
    }

    public String getUserRole(String userId) {
        String ret = "";
        List<UserModel> users = getUsers();
        for (UserModel user : users) {
            if (userId.equals(user.getUserId())) {
                ret = user.getRole();
                break;
            }
        }
        return ret;
    }

    private List<UserModel> getUsers() {
        List<UserModel> users = new ArrayList<UserModel>();
        users.add(createUser("user1", ""));
        users.add(createUser("manager1", "dept"));
        users.add(createUser("manager2", "dept"));
        users.add(createUser("hr1", "hr"));
        return users;
    }

    private UserModel createUser(String userId, String role) {
        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setRole(role);
        return userModel;
    }
}
