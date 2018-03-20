package com.zab.sanke.messager.im;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.exception.BmobException;
/**
 * 发送添加好友请求接口回调
 * @author Administrator
 *
 */
public interface ChartAddFriendListener {
	/**
	 * 发送成功
	 * @param msg 成功的消息
	 */
	void onSendSuccess(BmobIMMessage msg);
	/**
	 * 发送失败
	 * @param  e 失败异常对象
	 */
	void onSendError(BmobException e);
}
