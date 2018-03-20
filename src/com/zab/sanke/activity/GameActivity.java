package com.zab.sanke.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zab.sanke.R;
import com.zab.sanke.activity.BaseActivity.GoldLuckListener;
import com.zab.sanke.view.MyGame;
import com.zab.sanke.view.RockerView;
import com.zab.sanke.view.SpeedButton;
import com.zab.sanke.view.MyGame.BooldStatListener;
import com.zab.sanke.view.MyGame.GameOverListener;
import com.zab.sanke.view.MyGame.GoldAppleListener;
import com.zab.sanke.view.MyGame.GoldInterface;
import com.zab.sanke.view.MyGame.ScoreUpListener;
import com.zab.sanke.view.RockerView.Direction;
import com.zab.sanke.view.RockerView.OnShakeListener;
import com.zab.sanke.view.SpeedButton.GameClickListener;
/**
 * 此版本游戏为第七版本
 * 主要实现吃到金苹果时，将会开启抽奖
 * @author Administrator
 *
 */

public class GameActivity extends  BaseActivity implements ScoreUpListener,GoldLuckListener,GameOverListener,GoldInterface,GoldAppleListener{
	MyGame  myGame;
	private TextView tvInvincible;
	private TextView tvBackBlood;
	private int gold=100;
	private TextView tvGold;
	private ImageView ivExit;
	private TextView tvTime;
	public static int  GAME_MODE=1;//1 标准模式 2 计时模式
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		count=getGameCount();
		gold=getGold();
		initGame();
		initGameSpeed();
		initGameInvincible();	
		initGameBackBlood();
		initGameOver();
		initGameExit();
		initGold();
		initUpGold();
		initGoldApple();
		initScoreUp();
		initGameMode();
	}
	private void initGameMode() {
		tvTime=(TextView) findViewById(R.id.tv_time);
		//如果是1 则不用操作任何动作，如果是2 则启动倒计时 3分钟
		if(GAME_MODE==2){
			initGameTimeDown();
		}else if(GAME_MODE==1){		
			initGameTime();
		}else if(GAME_MODE==3){
			initGameManthine();
		}


	}
	private void initGameManthine() {
		myGame.setMachine();

	}
	int time=300;
	private CountDownTimer timer;
	@SuppressLint("SimpleDateFormat")
	private void initGameTimeDown() {
		startGameTime();
		timer=new CountDownTimer(300*1000,1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");      
				String time=sdf.format(new Date(millisUntilFinished));
				tvTime.setText(time);
			}

			@Override
			public void onFinish() {
				tvTime.setText("游戏结束");
				myGame.setMode(MyGame.LOSE);
			}
		}.start();

	}
	private void initGold() {
		tvGold=(TextView) findViewById(R.id.tv_gold);
		tvGold.setText("金币"+gold);

	}
	private  void initScoreUp(){
		myGame.setScoreUpListener(this);
	}
	private void initGoldApple() {
		// TODO Auto-generated method stub
		myGame.setGoldAppleListener(this);
	}
	/**
	 * 初始化金币事件处理
	 */
	private void initUpGold() {
		// TODO Auto-generated method stub
		myGame.setGoldInterface(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isTime=false;
		myGame.setMode(MyGame.LOSE);
	}
	private boolean isTime=true;
	private AnimationSet animationSet;
	/**
	 * 初始化游戏时间计时功能
	 * 每个1秒钟获得一次时间。
	 */
	private void initGameTime() {
		startGameTime();
		new Thread(){
			public void run() {
				while(isTime){
					SystemClock.sleep(1000);
					timeHanlder.sendEmptyMessage(GAME_TIME);
				}
			};
		}.start();
		setGameTimeListener(new GameTimeListener() {

			@Override
			public void GameTime(String time) {
				tvTime.setText(time);
			}
		});

	}
	/**
	 * 游戏退出按钮事件，此按钮代表游戏程序退出，直接回到手机主界面
	 */
	private void initGameExit() {
		ivExit=(ImageView) findViewById(R.id.iv_exit);
		ivExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				myGame.setGamePauseOrCountine(true);
				AlertDialog dialog=new AlertDialog.Builder(GameActivity.this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle("退出游戏")
				.setMessage("游戏是否退出或者继续")
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						saveGameCount(++count);
						myGame.setExitGame();
						if(timer!=null)
							timer.cancel();
						startActivity(new Intent(GameActivity.this,MainActivity.class));
						finish();
					}
				})
				.setPositiveButton("继续", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						myGame.setGamePauseOrCountine(false);
						dialog.dismiss();
					}
				})
				.setCancelable(false)
				.create();
				dialog.show();
			}
		});
	}
	/**
	 * 初始化游戏结束事件，同实现接口GameOverListener的重写方法GameOver一起使用。
	 * 只有此方法被调用才会回调游戏结束接口
	 */
	private void initGameOver() {
		// TODO Auto-generated method stub
		myGame.setGameOverListener(this);
	}
	/**
	 * 初始化血量变化的事件
	 * 1 当血量减少时，通过游戏的血量状态接口设置血量图标的显示和隐藏。
	 * 2 当点击加血按钮时，血量加1，同时启动CD30秒倒计时。
	 */
	private void initGameBackBlood() {

		myGame.setBooldStatListener(new BooldStatListener() {
			@Override
			public void onBooldStatListener(int boold) {
				if(boold>=0&&boold<=3){
					switch (boold) {
					case 3:
						findViewById(R.id.iv_3).setVisibility(View.VISIBLE);
						findViewById(R.id.iv_2).setVisibility(View.VISIBLE);
						findViewById(R.id.iv_1).setVisibility(View.VISIBLE);
						break;
					case 2:
						findViewById(R.id.iv_3).setVisibility(View.INVISIBLE);
						findViewById(R.id.iv_2).setVisibility(View.VISIBLE);
						break;
					case 1:
						findViewById(R.id.iv_2).setVisibility(View.INVISIBLE);
						findViewById(R.id.iv_1).setVisibility(View.VISIBLE);
						break;
					case 0:
						findViewById(R.id.iv_1).setVisibility(View.INVISIBLE);
					default:
						break;
					}
				}
			}
		});

		tvBackBlood=(TextView) findViewById(R.id.tv_back_blood);
		tvBackBlood.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tvBackBlood.setEnabled(false);
				myGame.setBoold();
				setBackBloodHandlerMessage(new BackBloodhandlerMessage() {
					public void message(int time) {
						if(time==0){
							tvBackBlood.setEnabled(true);
							tvBackBlood.setText("");
						}else{
							tvBackBlood.setText(""+time);
						}
					}
				});
				new Thread(){
					public void run() {
						for (int i = 30; i >=0; i--) {
							SystemClock.sleep(1000);
							BackBloodhandler.sendEmptyMessage(i);
						}
					};
				}.start();
			}
		});

	}
	/**
	 * 设置无敌按钮事件，点击无敌按钮后将有3秒钟的无敌时间（无敌不可装四周墙壁，可以抵消撞内部墙壁和自身）
	 * 3秒钟后将消失无敌按钮功能。
	 */
	private void initGameInvincible() {
		tvInvincible=(TextView) findViewById(R.id.tv_invincible);
		tvInvincible.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tvInvincible.setEnabled(false);
				myGame.setInvincible();
				setInvincibleHandlerMessage(new InvinciblehandlerMessage() {
					public void message(int time) {
						if(time==0){
							tvInvincible.setVisibility(View.GONE);
						}else{
							tvInvincible.setText(""+time);
						}
					}
				});
				new Thread(){
					public void run() {
						for (int i = 3; i >=0; i--) {
							Invinciblehandler.sendEmptyMessage(i);
							SystemClock.sleep(1000);
						}
					};
				}.start();
			}
		});

	}	
	/**
	 * 初始化游戏加速度按钮控件。按下加速，抬起停止加速
	 */
	private void initGameSpeed(){
		SpeedButton ib=(SpeedButton) findViewById(R.id.ib);
		ib.setOnGameClickListener(new GameClickListener() {
			public void onClickUp(View v) {
				myGame.setSpeedUp(false);
			}
			public void onClickDown(View v) {
				myGame.setSpeedUp(true);

			}
		});

	}
	/**
	 * 初始化游戏界面，并设置相关文本框和摇杆控件
	 */
	private void initGame() {
		//获得游戏界面
		myGame=(MyGame) findViewById(R.id.mygame);
		//添加分数控件
		TextView  tv=(TextView) findViewById(R.id.tv_store);
		myGame.setTextView(tv);
		//获得摇杆控件
		RockerView rv=(RockerView) findViewById(R.id.main_rockerView);
		//设置摇杆事件监听
		rv.setOnShakeListener(new OnShakeListener() {
			public void onFinish() {
			}
			public void direction(Direction direction) {
				switch (direction) {
				case UP://向上
					myGame.up();
					break;
				case DOWN://向下
					myGame.down();
					break;
				case LEFT://向左
					myGame.left();
					break;
				case RIGHT://向右
					myGame.right();
					break;
				default:
					break;
				}	
			}
		});
	}
	private int count=0;

	@Override
	public void GameOver() {
		AlertDialog dialog=new AlertDialog.Builder(this)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("游戏失败")
		.setMessage("游戏是否退出或者10金币复活")
		.setNegativeButton("金币复活", new DialogInterface.OnClickListener() {
			@SuppressLint("ShowToast")
			public void onClick(DialogInterface dialog, int which) {
				if(gold>10){
					myGame.setInitGame();
					dialog.dismiss();
					gold-=10;
					tvGold.setText(""+gold);
				}else{
					Toast.makeText(getApplicationContext(), "金币不足,重新开始游戏", 1).show();
					myGame.initGame();
					myGame.setGameOverListener(GameActivity.this);
					saveGameCount(++count);
				}
				if(timer!=null)
					timer.cancel();
				if(GAME_MODE==2){
					initGameTimeDown();
				}else{
					initGameTime();
				}
				startGameTime();

			}
		})
		.setPositiveButton("退出", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				saveGameCount(++count);
				startActivity(new Intent(GameActivity.this,MainActivity.class));
				finish();
			}
		})
		.setCancelable(false)
		.create();
		dialog.show();

	}
	@Override
	public void goldAddListener() {


		gold+=2;
		tvGold.setText(""+gold);
		saveGold(2);
		animationSet=(AnimationSet) AnimationUtils.loadAnimation(this, R.anim.gold_anim);
		tvGold.startAnimation(animationSet);
	}
	@Override
	public void goldAppleListener() {
		// 游戏暂停
		myGame.setGamePauseOrCountine(true);
		//开启抽奖模式
		int num=(int)(Math.random()*8);
		setGoldLuckListener(this);		
		showGoldLuck(num);

	}
	@Override
	public void goldLuckNum(int g) {
		myGame.setGamePauseOrCountine(false);	
		int n=0;
		if(g%8==0){
			n=8*10;
		}else{
			n=(g%8)*10;
		}
		tvGold.setText(""+(gold+n));
		saveGold(n);
		tvGold.startAnimation(animationSet);
	}
	@Override
	public void scoreUpListener(int score) {
		saveSore(score);
	}
}
