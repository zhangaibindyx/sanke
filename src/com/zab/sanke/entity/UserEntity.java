package com.zab.sanke.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {
	private String roomId;
	private String roomName;
	private String behavior;
	private String action;
	private String other;
	private String friend;
	private String number;
	@Override
	public String toString() {
		return   roomId + ":" + roomName
				+ ":" + behavior + ":" + action + ":"
				+ other + ":" + friend + ":" + number;
	}
	public String toDataString(){
		return   roomId + ":" + roomName
				+ ":" + behavior + ":" + action + ":"
				+ other + ":" + friend;
	}
	public UserEntity(String roomId, String roomName, String behavior,
			String action, String other, String friend, String number) {
		super();
		this.roomId = roomId;
		this.roomName = roomName;
		this.behavior = behavior;
		this.action = action;
		this.other = other;
		this.friend = friend;
		this.number = number;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getBehavior() {
		return behavior;
	}
	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public UserEntity() {
		// TODO Auto-generated constructor stub
	}

}
