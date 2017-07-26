package com.atm.spring.activiti.model;

import java.io.Serializable;

public class UserModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private String userName;
	
	private String password;

	private String role;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Sets new role.
	 *
	 * @param role New value of role.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets role.
	 *
	 * @return Value of role.
	 */
	public String getRole() {
		return role;
	}
}
