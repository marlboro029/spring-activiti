package com.atm.spring.activiti.dao;

import com.atm.spring.activiti.model.UserModel;

import java.util.List;

public interface UserDao {
	List<UserModel> getAllUserList();

	UserModel findUserById(UserModel userModel);

	void insertUser(UserModel user);
}
