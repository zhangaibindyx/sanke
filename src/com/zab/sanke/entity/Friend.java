package com.zab.sanke.entity;

import cn.bmob.v3.BmobObject;

public class Friend extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private User friendUser;
	
	
	public Friend(User user, User friendUser) {
		super();
		this.user = user;
		this.friendUser = friendUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getFriendUser() {
		return friendUser;
	}

	public void setFriendUser(User friendUser) {
		this.friendUser = friendUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Friend() {
		// TODO Auto-generated constructor stub
	}

	public Friend(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}

}
