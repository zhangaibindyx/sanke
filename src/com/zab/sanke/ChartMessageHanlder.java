package com.zab.sanke;

import android.content.Context;

import com.zab.sanke.util.ConfigUtil;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
/**
 * 消息接受器，所有消息都是次消息接受器接受，
 * @author Administrator
 * 当接受到信息后通过广播将消息发送给指定界面
 */
public class ChartMessageHanlder extends BmobIMMessageHandler implements ConfigUtil{
	
	private Context context;
	
	 public ChartMessageHanlder(Context context) {
		  this.context=context;
	 }
	 /***
	  * 此方法负责接受消息，当接收到服务器发来的消息时，此方法被调用
	  */
	@Override
	public void onMessageReceive(MessageEvent arg0) {
		
		
	}
	/***
	  * 此方法负责接受离线消息，当每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
	  */
	@Override
	public void onOfflineReceive(OfflineMessageEvent arg0) {
		
		
		
	}

}
