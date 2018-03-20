package com.zab.sanke.mode;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;

import com.zab.sanke.mode.i.UserInfoListener;
/***
 * 更新本地数据库中的好友信息
 * @author Administrator
 *
 */
public class UserManager {

	private static UserManager manage=new UserManager();
	private UserManager(){
		
	}
	public static UserManager getInstace(){
		return manage;
	}
	
	/**
	 * 获得本地数据中的用户信息
	 * @param id 用户的userId
	 * @param listener 回调接口
	 */
	public void getUserInfo(String id,UserInfoListener listener){
		BmobIMUserInfo info = BmobIM.getInstance().getUserInfo(id);
		if(info!=null){
			listener.getSuccessInfo(info);
		}else{
			listener.getErrorInfo("查无此人");
		}
		
	}
	/**
	 * 更新本地数据库中指定用戶信息，通常收到用户发来新信息时更新
	 * @param info 用户信息
	 */
	public void updataInfo(BmobIMUserInfo info){
		BmobIM.getInstance().updateUserInfo(info);
	}
	/**
	 * 批量更新本地用户信息,通常是登录后更新，首先需要调用此方法更新
	 * @param list 本地用户信息所有信息
	 */
	public void updataInfos(List<BmobIMUserInfo> list){
		BmobIM.getInstance().updateBatchUserInfo(list);
	}
	
	
	
	
	
	
	
	
	
	
}

