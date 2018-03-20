package com.zab.sanke.entity;

public class UserDataEntity {
	private int id;
	private int gold;//金币
	private int sore;//积分
	private int signNumber;//签到次数
	private int experience;//经验
	private int grade;//等级
	
	public UserDataEntity(int id, int gold, int sore, int signNumber,
			int experience, int grade, String name, int imageId) {
		super();
		this.id = id;
		this.gold = gold;
		this.sore = sore;
		this.signNumber = signNumber;
		this.experience = experience;
		this.grade = grade;
		this.name = name;
		this.imageId = imageId;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getGold() {
		return gold;
	}


	public void setGold(int gold) {
		this.gold = gold;
	}


	public int getSore() {
		return sore;
	}


	public void setSore(int sore) {
		this.sore = sore;
	}


	public int getSignNumber() {
		return signNumber;
	}


	public void setSignNumber(int signNumber) {
		this.signNumber = signNumber;
	}


	public int getExperience() {
		return experience;
	}


	public void setExperience(int experience) {
		this.experience = experience;
	}


	public int getGrade() {
		return grade;
	}


	public void setGrade(int grade) {
		this.grade = grade;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getImageId() {
		return imageId;
	}


	public void setImageId(int imageId) {
		this.imageId = imageId;
	}


	private String name;//姓名
	private int imageId;//图片id
	
	
	public UserDataEntity() {
		// TODO Auto-generated constructor stub
	}

}
