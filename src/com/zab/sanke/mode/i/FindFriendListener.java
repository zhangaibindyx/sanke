package com.zab.sanke.mode.i;

import java.util.List;

import com.zab.sanke.entity.Friend;

/**
 * 查询服务器端好友列表回调接口
 * @author Administrator
 *
 */
public interface FindFriendListener {
	/**
	 * 查询到所有联系人回调方法
	 * @param data 联系人集合
	 */
  void onSuccess(int size,List<Friend> data);
  /**
   * 当没有联系人回调此接口
   * @param size 联系人数量
   * @param s  暂无联系人
   */
  void onError(int size,String s);
}
