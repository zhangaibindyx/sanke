package com.zab.sanke.entity;

import java.io.Serializable;

public class NewFriend implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewFriend() {
		// TODO Auto-generated constructor stub
	}
	private long id;
	/**用户uid*/
	private String uid;	
	/**留言信息*/
	private String msg;
	/**用户名*/
	private String name;
	/**头像*/
	private String avater;

	/**状态 未读 已读 已添加 已拒绝*/
	private Integer status;
	/**请求时间*/
	private long time;

	public NewFriend(long id, String uid, String msg, String name, String avater,
			Integer status, long time) {
		super();
		this.id = id;
		this.uid = uid;
		this.msg = msg;
		this.name = name;
		this.avater = avater;
		this.status = status;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvater() {
		return avater;
	}

	public void setAvater(String avater) {
		this.avater = avater;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "NewFriend [id=" + id + ", uid=" + uid + ", msg=" + msg + ", name="
				+ name + ", avater=" + avater + ", status=" + status + ", time="
				+ time + "]";
	}




}
