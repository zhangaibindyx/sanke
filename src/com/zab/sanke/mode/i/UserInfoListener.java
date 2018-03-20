package com.zab.sanke.mode.i;

import cn.bmob.newim.bean.BmobIMUserInfo;
/**
 * 获得本地用户信息接口
 * @author Administrator
 *
 */
public interface UserInfoListener {
	/**
	 *  本地用户接口
	 * @param info 本地用户对象  userId name avater三个属性
	 */
	void getSuccessInfo(BmobIMUserInfo info);
	/**
	 * 获得失败 即没有此用户
	 * @param e 本次不存在此用户
	 */
	void getErrorInfo(String e);
}
