package com.zab.sanke.messager;

import cn.bmob.newim.bean.BmobIMExtraMessage;

public class AddFriendMessager extends BmobIMExtraMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddFriendMessager() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getMsgType() {
		// TODO Auto-generated method stub
		return "add";
	}
	@Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
        //设置为false,则会保存到指定会话的数据库中
        return true;
    }
}
