package com.zab.sanke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.net.ParseException;
import cn.bmob.newim.BmobIM;

import com.zab.sanke.activity.BaseActivity;
import com.zab.sanke.db.DBUtils;
import com.zab.sanke.entity.TaskEntity;
import com.zab.sanke.entity.UserDataEntity;
import com.zab.sanke.util.ConfigUtil;

@SuppressLint("SimpleDateFormat")
public class MyApplication extends Application implements ConfigUtil{
	//今日签到标记
	public  boolean isSign=false;
	//七天签到标记
	public static boolean [] signNumber=new boolean[8];
	
	public SharedPreferences sp;
	public static MyApplication instace;
	
	public String shopingTag="0";
	public static final String SHOPING_KEY="shoping";
	public static final String DATE_KEY="com.tedu.date";
	public static final String SIGIN_KEY="com.tedu.sign_key";
	private boolean isSaveSQL=true;
	public DBUtils getDBUtil(){
		if(dbUtil!=null){
			return dbUtil;
		}else{
			dbUtil=new DBUtils(this,SQL_NAME);
			return dbUtil;
		}
	}
	public MyApplication() {
		getData();
	}
	private DBUtils dbUtil;
	
	public int [] getShopingTag(){
		String s=sp.getString(SHOPING_KEY, "0");
		if("0".equals(s)){
			return new int[]{0};
		}else{
			String [] shop=s.split(",");
			int [] datas=new int[shop.length];
			for (int i = 0; i < shop.length; i++) {
				datas[i]=Integer.parseInt(shop[i]);
			}
			return datas;
		}
	}
	
	public void savaShopingTag(int position){
		String s=sp.getString(SHOPING_KEY, "0");
		s=s+","+position;
		sp.edit().putString(SHOPING_KEY, s).commit();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		sp=getSharedPreferences(BaseActivity.SP_NAME, MODE_PRIVATE);
		if(instace==null){
			instace=this;
		}
		long time=sp.getLong(DATE_KEY, 0);
		shopingTag=sp.getString(shopingTag, "0");
		
		isSign=sp.getBoolean(SIGIN_KEY, false);
		dbUtil=new DBUtils(this, SQL_NAME);
		isSaveSQL=sp.getBoolean("issavesql", true);
		if(isSaveSQL){
			initDataSql();
			isSaveSQL=false;
			sp.edit().putBoolean("issavesql", false).commit();
		}
		getJudgetDay(time);
		
		if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new ChartMessageHanlder(this));
        }
	}
	 /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	private void initDataSql() {
		getData();
		dbUtil.insertData(data);
		UserDataEntity entity=new UserDataEntity();
		entity.setId(1);
		entity.setGold(100);
		entity.setExperience(0);
		entity.setGrade(1);
		entity.setImageId(R.drawable.dabao);
		entity.setName("达宝");
		entity.setSignNumber(0);
		entity.setSore(0);
		dbUtil.saveUser(entity);
	}
	public  ArrayList<TaskEntity> data=new ArrayList<TaskEntity>();
	public ArrayList<TaskEntity> getData() {
		data=new ArrayList<TaskEntity>();
		data.add(new TaskEntity(1,2, true, "登录领奖",0,1,false));
		data.add(new TaskEntity(2,10, false, "完成一次游戏",1,2,false));
		data.add(new TaskEntity(3,20, false, "完成两次游戏",2,2,false));
		data.add(new TaskEntity(4,30, false, "完成三次游戏",3,2,false));
		data.add(new TaskEntity(5,20, false, "完成5个积分",5,1,false));
		data.add(new TaskEntity(6,50, false, "完成20个积分",20,1,false));
		data.add(new TaskEntity(7,100, false, "完成50个积分",50,1,false));
		
		return data;
	}
	
	
	
	public void savaIsSigin(){
		sp.edit().putBoolean(SIGIN_KEY, true).commit();
		isSign=sp.getBoolean(SIGIN_KEY, false);
	}
	
	public   boolean IsToday(String day) throws ParseException, java.text.ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }
	private   ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
	  public   SimpleDateFormat getDateFormat() {
	        if (null == DateLocal.get()) {
	            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
	        }
	        return DateLocal.get();
	    }
	private  void getJudgetDay(long currentTimeMillis) {
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//得到当前的时间
        Date curDate = new Date(currentTimeMillis);
        String str = formatter.format(curDate);
        try {
        	
			boolean isToday=IsToday(str);
			
			if(isToday){
			    sp.edit().putBoolean(SIGIN_KEY, false).commit();
			    sp.edit().putInt(BaseActivity.SORE_KEY, 0).commit();
				sp.edit().putInt(BaseActivity.GAME_CONUNT_KEY, 0).commit();
				sp.edit().putLong(DATE_KEY, System.currentTimeMillis());
				
				for (TaskEntity taskEntity : data) {
					dbUtil.upData(taskEntity);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	public int getGameCount(){
		int count=sp.getInt(BaseActivity.GAME_CONUNT_KEY, 0);
		return count;
	}
	
	public int getScore(){
		int score=sp.getInt(BaseActivity.SORE_KEY, 0);
		return score;
	}
	public int getGold(){
		int gold=sp.getInt(BaseActivity.GOLD_KEY, 100);
		return gold;
	}
}
