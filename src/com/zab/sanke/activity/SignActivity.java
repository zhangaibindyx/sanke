package com.zab.sanke.activity;

import com.zab.sanke.MyApplication;
import com.zab.sanke.R;
import com.zab.sanke.R.id;
import com.zab.sanke.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SignActivity extends BaseActivity {
	private Button btnSign;
	private TextView tvWeek;
	private ImageView ivBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		initSign();
		initWeek();
		initBackMain();
	}
	private void initBackMain() {
		ivBack=(ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(SignActivity.this,MainActivity.class));
				finish();
			}
		});
	}
	private void initWeek() {
		tvWeek=(TextView) findViewById(R.id.week_week);
		tvWeek.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				signWeekBig();
				initWeekView(SignActivity.this);
			}
		});
	}
	private void initSign() {
		initWeekView(this);
		btnSign=(Button) findViewById(R.id.btn_sign);
		if(MyApplication.instace.isSign){
			btnSign.setText("已签到");
		}else{
			btnSign.setText("签到");
		}
		btnSign.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//如果签到过提示已签到否则根据星期签到
				if(!MyApplication.instace.isSign){
					signWeekDay();
					initWeekView(SignActivity.this);
					MyApplication.instace.savaIsSigin();
					btnSign.setText("已签到");
					Toast.makeText(SignActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(SignActivity.this,MainActivity.class));
					finish();
				}

			}
		});

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(SignActivity.this,MainActivity.class));
		finish();
	}

}
