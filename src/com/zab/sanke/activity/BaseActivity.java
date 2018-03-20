package com.zab.sanke.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;






import com.zab.sanke.MyApplication;
import com.zab.sanke.R;
import com.zab.sanke.R.drawable;
import com.zab.sanke.R.id;
import com.zab.sanke.R.layout;
import com.zab.sanke.R.style;
import com.zab.sanke.db.DBUtils;
import com.zab.sanke.entity.TaskEntity;
import com.zab.sanke.entity.UserDataEntity;
import com.zab.sanke.util.ConfigUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class BaseActivity extends Activity implements ConfigUtil{
	protected int [] dataHeader={R.drawable.chongzitou};
	protected int [] dataBody={R.drawable.green,R.drawable.pic01,
			R.drawable.pic02,R.drawable.pic03
			,R.drawable.pic04,R.drawable.pic05};
	
	protected int [] data={R.drawable.green,R.drawable.redstar,
			R.drawable.chongzitou};
	protected int [] dataEnd={R.drawable.redstar,R.drawable.yellowstar};



	protected UserDataEntity user;
	private DBUtils userDBUtil;
	private void initUser(){
		userDBUtil =new DBUtils(this, SQL_NAME);
		user=userDBUtil.getUserData();
	}	

	protected void upUserData(UserDataEntity user){
		userDBUtil.upUserData(user);
	}

	protected UserDataEntity getUserDataEntity() {
		user=userDBUtil.getUserData();
		return user;
	}

	protected DBUtils dbUtil;
	protected   int  [] weekId={
			R.id.week_monday,R.id.week_tuesday,
			R.id.week_wednesday,R.id.week_thursday,
			R.id.week_friday,R.id.week_staurday,
			R.id.week_sunday,R.id.week_week,
	};
	protected   int  [] weekl={
			R.id.week_week_l,R.id.week_sunday_l,R.id.week_monday_l,R.id.week_tuesday_l,
			R.id.week_wednesday_l,R.id.week_thursday_l,
			R.id.week_friday_l,R.id.week_staurday_l,

	};
	protected   int  [] weekm={
			R.id.week_week_m,R.id.week_sunday_m,R.id.week_monday_m,R.id.week_tuesday_m,
			R.id.week_wednesday_m,R.id.week_thursday_m,
			R.id.week_friday_m,R.id.week_staurday_m,

	};
	private int index;
	/**
	 * 获得当前星期几
	 * @return 0 周一 ... 周日 6
	 */
	protected int getWeek() {
		Calendar cal = Calendar.getInstance();  
		index = cal.get(Calendar.DAY_OF_WEEK); 
		return index;
	}

	protected void signWeekBig(){
		MyApplication.signNumber[7]=true;
		int count=0;
		for (int i = 0; i < MyApplication.signNumber.length; i++) {
			if(MyApplication.signNumber[i]==true){
				count++;
			}
		}

		int g=new Random().nextInt(10)*count;
		saveGold(g);
		initGoldOrSore();

	}

	protected void signWeekDay(){
		for (int i = 0; i < 7; i++) {
			if(index==i){
				MyApplication.signNumber[i]=true;
			}
		}
		saveGold(20);
		initGoldOrSore();
	}
	protected void initWeekView(SignActivity activity) {
		for (int i = 0; i < weekl.length; i++) {
			if(index==i||index==7){
				activity.findViewById(weekl[i]).setAlpha(1);
				activity.findViewById(weekl[i]).setEnabled(true);
				if(MyApplication.instace.isSign){
					TextView tv=(TextView) activity.findViewById(weekm[i]);
					if(i==7){
						tv.setText("神秘大奖已领取");
					}else{
						tv.setText("+20￥已领取");
					}
				}
				continue;
			}
			activity.findViewById(weekl[i]).setAlpha(0.5f);
			activity.findViewById(weekl[i]).setEnabled(false);
		}
		for (int i = 0; i < weekm.length; i++) {
			if(MyApplication.signNumber[i]){
				TextView tv=(TextView) activity.findViewById(weekm[i]);
				if(i==7){
					tv.setText("神秘大奖已领取");
				}else{
					tv.setText("+20￥已领取");
				}
			}
		}
	}


	public static final String SP_NAME="dabao";


	//无敌的接口回调对象
	private InvinciblehandlerMessage message;
	//加血的接口回调对象
	private BackBloodhandlerMessage bMessage;
	//无敌的hanlder对象 负责InvinciblehanlderMessage回调接口
	protected Handler Invinciblehandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			message.message(msg.what);
		};
	};
	//加血的hanlder对象 负责BackBloodhanlderMessage回调接口
	protected Handler BackBloodhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			bMessage.message(msg.what);
		};
	};
	/**
	 * 设置无敌回调接口，一般直接在Activity中直接调用，相当于初始化回调，重写的方法即时间更新操作
	 * @param message 
	 */
	protected void setInvincibleHandlerMessage(InvinciblehandlerMessage message) {
		this.message=message;
	}
	/**
	 * 设置回血的回调接口，一般直接在Activity中调用，相当于初始化回调接口，重写方法为时间的更新操作
	 * @param message
	 */
	protected void setBackBloodHandlerMessage(BackBloodhandlerMessage message) {
		this.bMessage=message;
	}
	interface InvinciblehandlerMessage{
		/**
		 * 无敌时间回调方法
		 * @param time 当前倒计时的时间
		 */
		void message(int time);
	}
	interface BackBloodhandlerMessage{
		/**
		 * 回血时间回调方法
		 * @param time 倒计时时间
		 */
		void message(int time);
	}



	protected Handler timeDownHanlder=new Handler(){
		public void handleMessage(android.os.Message msg) {
			timeDownlistener.timeDown(getTimeDown(msg.what));
		};
	};

	private TimeDownListener timeDownlistener;
	protected void  setTimeDownListener(TimeDownListener timeDownlistener) {
		this.timeDownlistener=timeDownlistener;
	}

	protected String getTimeDown(int time) {
		// TODO Auto-generated method stub
		return null;
	}
	public interface TimeDownListener{
		void timeDown(String time);
	}







	private long time,startTime;
	/**
	 * 获得当前游戏时间
	 * @return  分：秒  格式的字符串
	 */
	protected String getGameTime() {
		time=System.currentTimeMillis()-startTime;
		SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
		String sTime=sdf.format(new Date(time));
		return sTime;
	}
	/**
	 * 开始游戏时间
	 */
	protected void startGameTime(){
		startTime=System.currentTimeMillis();
	}
	protected static final int GAME_TIME=-100;
	protected Handler  timeHanlder=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==GAME_TIME){
				timeListener.GameTime(getGameTime());
			}
		};
	};
	protected void onDestroy() {
		super.onDestroy();
		timeHanlder.removeCallbacks(null);
		Invinciblehandler.removeCallbacks(null);
		BackBloodhandler.removeCallbacks(null);

	};

	private GameTimeListener timeListener;
	protected void setGameTimeListener(GameTimeListener timeListener) {
		this.timeListener=timeListener;
	}
	/**
	 * 游戏时间回调接口
	 * @author Administrator
	 *
	 */
	public interface GameTimeListener{
		/**
		 * 当前游戏时间回调方法
		 * @param time  游戏时间
		 */
		void GameTime(String time);
	}
	private int num3=0;
	private boolean isLuck;
	protected  void showGoldLuck(final int num) {
		final ArrayList<Button> buttons=new ArrayList<Button>();
		final Dialog dialog=new Dialog(this,R.style.NormalDialogStyle);
		View view =View.inflate(this, R.layout.dialog_luck_gold, null);
		buttons.add((Button)view.findViewById(R.id.btn_1));
		buttons.add((Button)view.findViewById(R.id.btn_2));
		buttons.add((Button)view.findViewById(R.id.btn_3));
		buttons.add((Button)view.findViewById(R.id.btn_4));
		buttons.add((Button)view.findViewById(R.id.btn_5));
		buttons.add((Button)view.findViewById(R.id.btn_6));
		buttons.add((Button)view.findViewById(R.id.btn_7));
		buttons.add((Button)view.findViewById(R.id.btn_8));
		buttons.add((Button)view.findViewById(R.id.btn_luck));
		//		int num=(int)(Math.random()*8);//0--7		
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);;
		WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		view.setMinimumHeight( (int)(dm.heightPixels*0.23f));
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (dm.widthPixels* 0.75f);
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);

		final CountDownTimer timer=new CountDownTimer(2000+num*300,100) {

			@Override
			public void onTick(long millisUntilFinished) {
				for (int i = 0; i < buttons.size()-1; i++) {
					if(num3 % 8==i){
						buttons.get(i).setTextColor(Color.YELLOW);
					}else{
						buttons.get(i).setTextColor(Color.BLACK);
					}
				}
				num3++;


			}
			@Override
			public void onFinish() {
				buttons.get(8).setEnabled(true);
			}
		};
		isLuck=true;
		buttons.get(8).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isLuck){
					timer.start();
					isLuck=false;
					buttons.get(8).setText("领取");
					buttons.get(8).setEnabled(false);

				}else{
					dialog.dismiss();
					if(listener!=null){
						listener.goldLuckNum(num3);
						num3=0;
					}
				}
			}
		});
		dialog.show();
	}
	private GoldLuckListener listener;
	protected void  setGoldLuckListener(GoldLuckListener listener) {
		this.listener=listener;
	}
	public interface GoldLuckListener{
		void goldLuckNum(int num);
	}



	protected int goldCurr=0;
	protected int soreCurr=0;
	protected int game_count=0;
	protected SharedPreferences sp;
	private Editor edit;
	public static String GOLD_KEY="goldkey";
	public static String SORE_KEY="sorekey";
	public static String GAME_CONUNT_KEY="gamecountkey";
	private int count;
	private int score;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGoldOrSore();
		count=MyApplication.instace.getGameCount();
		score=MyApplication.instace.getScore();
		dbUtil=MyApplication.instace.getDBUtil();
		upData();
		getWeek();
		initUser();
	}
	private void upData() {
		ArrayList<TaskEntity> data=dbUtil.getData();

		if(count==1){
			data.get(1).setComplete(true);
		}else if(count==2){
			data.get(1).setComplete(true);
			data.get(2).setComplete(true);
		}else if(count>=3){
			data.get(1).setComplete(true);
			data.get(2).setComplete(true);
			data.get(3).setComplete(true);
		}
		if(score>=5&&score<20){
			data.get(4).setComplete(true);
		}else if(score>=20&&score<=50){
			data.get(5).setComplete(true);
			data.get(4).setComplete(true);
		}else if(score>=50){
			data.get(4).setComplete(true);
			data.get(5).setComplete(true);
			data.get(6).setComplete(true);
		}
		for (TaskEntity taskEntity : data) {
			dbUtil.upData(taskEntity);
		}


	}
	private void initGoldOrSore() {
		sp=MyApplication.instace.sp;
		edit=sp.edit();
		goldCurr=sp.getInt(GOLD_KEY, 100);
		soreCurr=sp.getInt(SORE_KEY, 0);
		game_count=sp.getInt(GAME_CONUNT_KEY, 0);
	}

	protected int  getGameCount() {
		int gold=sp.getInt(GAME_CONUNT_KEY, 0);
		return gold;
	}
	protected void  saveGameCount(int count) {
		edit.putInt(GAME_CONUNT_KEY, count).commit();
	}
	protected int  getGold() {
		int gold=sp.getInt(GOLD_KEY, 100);
		return gold;
	}
	protected int  getSore() {
		int sore=sp.getInt(SORE_KEY, 0);
		return sore;
	}
	protected void saveGold(TaskEntity entity){
		edit.putInt(GOLD_KEY, entity.getGold()+getGold());
		edit.commit();


	}
	protected void saveGold(int gold){
		int g=sp.getInt(GOLD_KEY, 100);
		edit.putInt(GOLD_KEY, gold+=g);
		edit.commit();
	}
	protected void saveSore(int sore){
		int s=sp.getInt(SORE_KEY, 0);
		edit.putInt(SORE_KEY, sore+=s);
		edit.commit();
	}


}
