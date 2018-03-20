package com.zab.sanke.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;

import com.zab.sanke.R;

public class AddNewFriendActivity extends Activity {
	private  EditText etFrined;
	private Button btnAdd;
	private ListView lvToFriend,lvFromFriend;
	private ImageView ivBack;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_friend);
		initView();
		
		initAddFriend();
	}


	private void initAddFriend() {
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String friend=etFrined.getText().toString();
				if("".equals(friend)){
					etFrined.setError("请输入好友名称");
					etFrined.setHint("好友名称");
					return;
				}
				// 发送好友请求信息
				
				
			}
		});
	
	}


	private void initView() {
	    etFrined=(EditText) findViewById(R.id.et_add_friend);
	    btnAdd=(Button) findViewById(R.id.btn_add_friend);
	    lvToFriend=(ListView) findViewById(R.id.lv_add_to_friend);
	    lvFromFriend=(ListView) findViewById(R.id.lv_add_from_friend);
	    ivBack=(ImageView) findViewById(R.id.iv_friend_eixt);
	}

}
