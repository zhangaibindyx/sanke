package com.zab.sanke.mode;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import com.zab.sanke.entity.User;
import com.zab.sanke.mode.i.ChartConnectListener;

import android.content.Context;

public class UserMode {
	
	private static UserMode mode=new UserMode();
	
	public static UserMode getInstace(){
		return mode;
	}
	
	private UserMode(){
		
	}
	
	public  void  getConnectStatus(){
		BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
			public void onChange(ConnectionStatus arg0) {
				//分为：连接中 连接上 断开连接 网络不可用  
					
			}
		});
	}
	
	
	
	/**
	 * 断开聊天服务器，即不在线
	 */
	public   void serviceDisConnect(){
		BmobIM.getInstance().disConnect();
	}
	
	
	/**
	 * 连接聊天服务器。一般启动时即连接服务器
	 * @param context 上下文对象
	 * @param listener 连接后的回调接口
	 */
	public   void serviceConnect(Context context ,final ChartConnectListener listener){
		User user=BmobUser.getCurrentUser(User.class);
		BmobIM.connect(user.getObjectId(),new ConnectListener() {
			public void done(String uid, BmobException e) {
				if(e!=null){
					listener.connectSuccess(uid);
				}else{
					listener.connectError(e);
				}
			}
		});
	}


}
