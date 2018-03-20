package com.zab.sanke.entity;

public class TaskEntity {
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int gold;
	private boolean isComplete;
	private String taskContent;
	private int count;
	private int mode;// 1 场次，2 积分
	private boolean isGet=false;

	public boolean isGet() {
		return isGet;
	}

	public void setGet(boolean isGet) {
		this.isGet = isGet;
	}

	public TaskEntity(int id, int gold, boolean 
			isComplete, String taskContent,
			int count, int mode, boolean isGet) {
		super();
		this.id = id;
		this.gold = gold;
		this.isComplete = isComplete;
		this.taskContent = taskContent;
		this.count = count;
		this.mode = mode;
		this.isGet = isGet;
	}

	public TaskEntity(int gold, boolean isComplete, String taskContent,
			int count, int mode) {
		super();
		this.gold = gold;
		this.isComplete = isComplete;
		this.taskContent = taskContent;
		this.count = count;
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public TaskEntity() {
	}

	public TaskEntity(int gold, boolean isComplete, String taskContent,
			int count) {
		super();
		this.gold = gold;
		this.isComplete = isComplete;
		this.taskContent = taskContent;
		this.count = count;
	}

	public TaskEntity(int gold, boolean isComplete, String taskContent) {
		super();
		this.gold = gold;
		this.isComplete = isComplete;
		this.taskContent = taskContent;
	}


	@Override
	public String toString() {
		return "TaskEntity [id=" + id + ", gold=" + gold + ", isComplete="
				+ isComplete + ", taskContent=" + taskContent + ", count="
				+ count + ", mode=" + mode + ", isGet=" + isGet + "]";
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

}
