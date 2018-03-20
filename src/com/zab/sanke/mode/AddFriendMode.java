package com.zab.sanke.mode;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import com.zab.sanke.entity.NewFriend;
import com.zab.sanke.entity.User;
import com.zab.sanke.messager.AddFriendMessager;
import com.zab.sanke.messager.AgreeAddFriendMessage;
import com.zab.sanke.messager.im.ChartAddFriendListener;

public class AddFriendMode {

	private AddFriendMode() {
		// TODO Auto-generated constructor stub
	}
	private static AddFriendMode mode=new  AddFriendMode();

	public static AddFriendMode getInstace(){
		return  mode;
	}
	/**
	 * 发送添加好友请求
	 * @param info 当前用户对象 一般首先获得需要添加的User对象，然后封装成BmobIMuserInfo对象
	 * @param listener 添加成功或失败时的回调
	 */
	public void sendAddFriendMessage(BmobIMUserInfo info,final ChartAddFriendListener listener){
		BmobIMConversation c=BmobIM.getInstance().
				startPrivateConversation(info, true, null);
		BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
		AddFriendMessager msg=new AddFriendMessager();
		User user=BmobUser.getCurrentUser(User.class);
		msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
		Map<String,Object> map =new HashMap<>();
		map.put("name", user.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
		map.put("avatar",user.getAvatar());//发送者的头像
		map.put("uid",user.getObjectId());//发送者的uid
		msg.setExtraMap(map);
		conversation.sendMessage(msg, new MessageSendListener() {
			public void done(BmobIMMessage msg, BmobException e) {
				if(e!=null){
					listener.onSendSuccess(msg);
				}else{
					listener.onSendError(e);
				}
			}
		});

	}
	/**
	 * 实现对好友请求的同意处理
	 * @param add
	 * @param listener
	 */
	public void sendAgreeAddFriendMessage(final NewFriend add,final ChartAddFriendListener listener){
		BmobIMUserInfo info=new BmobIMUserInfo(add.getUid(),add.getName(),add.getAvater());
		BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(info, true, null);
		new BmobIMConversation();
		BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
		AgreeAddFriendMessage msg=new AgreeAddFriendMessage();
		User user=BmobUser.getCurrentUser(User.class);
		msg.setContent("我通过了你的好友验证请求，我们可以开始聊天了!");
		Map<String,Object> map =new HashMap<>();
		map.put("msg",user.getUsername()+"同意添加你为好友");//显示在通知栏上面的内容
		map.put("uid",add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
		map.put("time", add.getTime());//添加好友的请求时间
		msg.setExtraMap(map);
		conversation.sendMessage(msg, new MessageSendListener() {
			public void done(BmobIMMessage msg, BmobException e) {
				if(e!=null){
					listener.onSendSuccess(msg);

				}else{
					listener.onSendError(e);
				}
			}
		});
	}




}
