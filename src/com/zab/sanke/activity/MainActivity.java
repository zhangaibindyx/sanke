package com.zab.sanke.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.zab.sanke.MyApplication;
import com.zab.sanke.R;
import com.zab.sanke.adapter.TaskAdapter;
import com.zab.sanke.adapter.TaskAdapter.ReceiverListener;
import com.zab.sanke.entity.TaskEntity;
import com.zab.sanke.entity.User;

/**
 * 本次版本为第九版本 实现任务领取功能
 * 
 * @author Administrator
 *
 */
public class MainActivity extends BaseActivity {
	private ListView lvTask;
	private TaskAdapter adapter;
	private TextView tvGold, tvScore, tvGameCount, tvSign, tvShoping;
	private User user;

	private TextView tvFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initTask();
		initSign();
		initBmobUser();
		initListener();
	}

	private void initListener() {
		tvFriend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (BmobUser.getCurrentUser(User.class) == null) {
					startActivity(new Intent(MainActivity.this,
							LoginActivity.class));
					finish();
				} else {
					startActivity(new Intent(MainActivity.this,
							FriendActivity.class));
					finish();

				}
			}
		});
	}

	private void initBmobUser() {
		user = BmobUser.getCurrentUser(User.class);
	}

	private void initSign() {
		tvSign = (TextView) findViewById(R.id.tv_sign);
		tvSign.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SignActivity.class));
				finish();
			}
		});
		if (!MyApplication.instace.isSign) {
			// 弹出签到对话框
			startActivity(new Intent(MainActivity.this, SignActivity.class));
			finish();
		}

	}

	private void init() {
		tvFriend = (TextView) findViewById(R.id.tv_friend);
		tvGold = (TextView) findViewById(R.id.tv_gold);
		tvScore = (TextView) findViewById(R.id.tv_store);
		tvGameCount = (TextView) findViewById(R.id.tv_game_count);
		tvShoping = (TextView) findViewById(R.id.tv_shoping);
		tvShoping.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, ShopActivity.class));
			}
		});
		tvGold.setText("金币" + goldCurr);
		tvScore.setText("积分" + soreCurr);
		tvGameCount.setText("场次" + getGameCount() + "");
	}

	private void initTask() {
		lvTask = (ListView) findViewById(R.id.lv_task);
		ArrayList<TaskEntity> data = dbUtil.getData();

		adapter = new TaskAdapter(this, data, R.layout.task_item);
		lvTask.setAdapter(adapter);
		adapter.setOnReceiverListener(new ReceiverListener() {

			public void Receicer(TaskEntity entity, int position) {
				saveGold(entity);
				int currGold = getGold();
				tvGold.setText("金币" + currGold);
				AnimationSet animationSet = (AnimationSet) AnimationUtils
						.loadAnimation(MainActivity.this, R.anim.gold_anim);
				tvGold.startAnimation(animationSet);
				adapter.notifyDataSetChanged();
				entity.setGet(true);
				boolean b = dbUtil.upData(entity);
				if (b) {
					Toast.makeText(MainActivity.this, "领取成功", 1).show();
				} else {
					Toast.makeText(MainActivity.this, "领取失败", 1).show();
				}
			}
		});

	}

	public void gameStart(View v) {
		switch (v.getId()) {
		case R.id.btn_standard:
			startActivity(new Intent(this, GameActivity.class));
			GameActivity.GAME_MODE = 1;
			break;
		case R.id.btn_time:
			startActivity(new Intent(this, GameActivity.class));
			GameActivity.GAME_MODE = 2;
			break;
		case R.id.btn_machine:
			GameActivity.GAME_MODE = 3;
			startActivity(new Intent(this, GameActivity.class));
			break;
		case R.id.btn_pk:
			startActivity(new Intent(this, PKActivity.class));
			GameActivity.GAME_MODE = 4;
			break;
		case R.id.iv_exit:
			this.finish();
			break;
		default:
			break;
		}
		finish();
	}

}
