package com.zab.sanke.mode;

import java.util.List;

import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.zab.sanke.entity.Friend;
import com.zab.sanke.entity.User;
import com.zab.sanke.mode.i.FindFriendListener;
/**
 * 处理服务器端好友列表，
 *  查询好友数据
 *  删除指定好友
 * @author Administrator
 *
 */
public class FriendUserManager {

	private static FriendUserManager manager=new FriendUserManager();

	public static FriendUserManager getInstace(){
		return manager;
	}
	private FriendUserManager() {
	}
	/**
	 * 查询服务器端所有好友信息，一般都会调用更新本地好友数据库
	 * @param listener
	 */
	public void queryFriends(final FindFriendListener listener){
		BmobQuery<Friend> query=new BmobQuery<Friend>();
		User user=BmobUser.getCurrentUser(User.class);// 当前用户信息
		query.addWhereEqualTo("user", user);//查询数据库中时本用户信息的数据列
		query.include("friendUser");
		query.order("-updatedAt");
		query.findObjects(new FindListener<Friend>() {
			@SuppressWarnings("null")
			public void done(List<Friend> data, BmobException e) {
				if(e!=null){
					if(data!=null&&data.size()>0){
						listener.onSuccess(data.size(), data);
					}else{
						listener.onError(-1, "无联系人");
					}
				}else{
					listener.onError(e.getErrorCode(), e.getMessage());
				}
			}
		});
		
	}
	/**
	 * 刪除指定好友
	 * @param f 指定好友
	 * @param listener 刪除回调接口
	 */
	public void deleteFriend(Friend f,UpdateListener listener){
		Friend friend=new Friend();
		friend.delete(f.getObjectId(), listener);
	}
	
	
	
}
