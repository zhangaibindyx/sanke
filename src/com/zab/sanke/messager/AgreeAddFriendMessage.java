package com.zab.sanke.messager;

import cn.bmob.newim.bean.BmobIMExtraMessage;

public class AgreeAddFriendMessage extends BmobIMExtraMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 发出方uid	 */
	private String uid;
	/** 发出时间	 */
	private long time;
	/** 发出方信息*/
	private String msg;



	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "AgreeAddFriendMessage [uid=" + uid + ", time=" + time
				+ ", msg=" + msg + "]";
	}
	public AgreeAddFriendMessage(String uid, long time, String msg) {
		super();
		this.uid = uid;
		this.time = time;
		this.msg = msg;
	}
	public AgreeAddFriendMessage() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getMsgType() {
		// TODO Auto-generated method stub
		return "agree";
	}
	@Override
	public boolean isTransient() {
		//设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
		//设置为false,则会保存到指定会话的数据库中
		return false;
	}
}
