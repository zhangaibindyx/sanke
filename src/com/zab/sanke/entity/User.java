package com.zab.sanke.entity;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	private String avatar;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
