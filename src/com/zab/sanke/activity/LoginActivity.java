package com.zab.sanke.activity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.zab.sanke.R;
import com.zab.sanke.R.id;
import com.zab.sanke.R.layout;
import com.zab.sanke.R.menu;
import com.zab.sanke.entity.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText etName,etPwd;
	private Button btnCommit;
	private  TextView tvRegist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initListener();
	}
	private void initListener() {
		btnCommit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				registEvent();
			}
		});
		tvRegist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,RegistActivity.class));
				finish();
			}
		});
	}
	protected void registEvent() {
		String name=etName.getText().toString();
		String pwd=etPwd.getText().toString();
		if("".equals(name)){
			etName.setError("请输入名称");
			return;
		}
		if("".equals(pwd)){
			etPwd.setError("请输入密码");
			return;
		}
		if (name.length() < 6 || name.length() > 15) {
			etName.setError("请输入正确账号");
			etName.setText("");
			etName.setHint("账号长度为6-15个字符");
			return;
		}
		if (pwd.length() < 6 || pwd.length() > 15) {
			etPwd.setError("请输入正确密码");
			etPwd.setText("");
			etPwd.setHint("账号长度为6-15个字符");
			return;
		}
		
	User user=new User();
	user.setUsername(name);
	user.setPassword(pwd);
	user.login(new SaveListener<User>() {
		public void done(User arg0, BmobException arg1) {
			if(arg1==null){
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
				startActivity(new Intent(LoginActivity.this,MainActivity.class));
				finish();
			}else{
				Toast.makeText(LoginActivity.this, "登录失败"+arg1.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	});
		
		
	}
	private void initView() {
		etName=(EditText) findViewById(R.id.login_et_name);
		etPwd=(EditText) findViewById(R.id.login_et_pwd);
		btnCommit=(Button) findViewById(R.id.login_btn_commit);
		tvRegist=(TextView) findViewById(R.id.login_tv_login);

	}
}
