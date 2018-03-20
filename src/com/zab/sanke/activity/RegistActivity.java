package com.zab.sanke.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.zab.sanke.R;
import com.zab.sanke.entity.User;

public class RegistActivity extends Activity {
	private EditText etName,etPwd,etPwdAgin;
	private Button btnCommit;
	private  TextView tvLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		initView();
		initListener();
	}
	private void initListener() {
		btnCommit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				registEvent();
			}
		});
		tvLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(RegistActivity.this,LoginActivity.class));
				finish();
			}
		});
	}
	protected void registEvent() {
		String name=etName.getText().toString();
		String pwd=etPwd.getText().toString();
		String pwd_again=etPwdAgin.getText().toString();
		if("".equals(name)){
			etName.setError("请输入名称");
			return;
		}
		if("".equals(pwd)){
			etPwd.setError("请输入密码");
			return;
		}
		if("".equals(pwd_again)){
			etPwdAgin.setError("请输入确认密码");
			return;
		}
		if(name.length()<6||name.length()>15){
			etName.setError("请输入正确账号");
			etName.setText("");
			etName.setHint("账号长度为6-15个字符");
			return;
		}
		if(pwd.length()<6||pwd.length()>15){
			etPwd.setError("请输入正确密码");
			etPwd.setText("");
			etPwd.setHint("账号长度为6-15个字符");
			return;
		}
		if(!pwd.equals(pwd_again)){
			etPwdAgin.setError("两次密码不一致");
			etPwdAgin.setText("");
			etPwdAgin.setHint("请输入正确的确认密码");
			return;
		}
		
	User user=new User();
	user.setUsername(name);
	user.setPassword(pwd_again);
	user.signUp(new SaveListener<User>() {
		public void done(User arg0, BmobException arg1) {
			if(arg1==null){
				Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_LONG).show();
				startActivity(new Intent(RegistActivity.this,LoginActivity.class));
				finish();
			}else{
				Toast.makeText(RegistActivity.this, "注册失败"+arg1.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	});
		
		
	}
	private void initView() {
		etName=(EditText) findViewById(R.id.regist_et_name);
		etPwd=(EditText) findViewById(R.id.regist_et_pwd);
		etPwdAgin=(EditText) findViewById(R.id.regist_et_pwd_agin);
		btnCommit=(Button) findViewById(R.id.regist_btn_commit);
		tvLogin=(TextView) findViewById(R.id.regist_tv_login);

	}
	
}
