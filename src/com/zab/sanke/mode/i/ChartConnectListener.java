package com.zab.sanke.mode.i;

import cn.bmob.v3.exception.BmobException;
/**
 * 连接服务器回调接口
 * @author Administrator
 *
 */
public interface ChartConnectListener {
	/**
	 * 连接成功
	 * @param uid 当前连接成功的objectId
	 */
	void connectSuccess(String uid);
	/**
	 * 连接失败
	 * @param e 失败原因
	 */
	void connectError(BmobException e);

}
