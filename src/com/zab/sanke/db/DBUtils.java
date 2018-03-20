package com.zab.sanke.db;

import java.util.ArrayList;
import java.util.List;

import com.zab.sanke.entity.TaskEntity;
import com.zab.sanke.entity.UserDataEntity;
import com.zab.sanke.util.ConfigUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBUtils implements ConfigUtil {
	MyDBHelper dbHelper;
	public DBUtils(Context context,String dataName) {
		dbHelper=new MyDBHelper(context, dataName, null, 1);
	}

	public ArrayList<TaskEntity>  getData(){
		ArrayList<TaskEntity> data=new ArrayList<TaskEntity>();
		SQLiteDatabase write = dbHelper.getWritableDatabase();
		Cursor cursor=write.query(SQL_TABL_NAME_TASK, null, null, null, null, null, null);

		while(cursor.moveToNext()){
			TaskEntity entity=new TaskEntity();
			entity.setId(cursor.getInt(cursor.getColumnIndex(TASK_TABL_ID_KEY)));
			entity.setCount(cursor.getInt(cursor.getColumnIndex(TASK_TABL_COUNT_KEY)));
			entity.setMode(cursor.getInt(cursor.getColumnIndex(TASK_TABL_MODE_KEY)));
			entity.setTaskContent(cursor.getString(cursor.getColumnIndex(TASK_TABL_TASKCONTENT_KEY)));
			int complete=cursor.getInt(cursor.getColumnIndex(TASK_TABL_COMPLETE_KEY));
			boolean isComplete=complete==0?false:true;
			entity.setComplete(isComplete);
			int get=cursor.getInt(cursor.getColumnIndex(TASK_TABL_ISGET_KEY));
			boolean isGet=get==0?false:true;
			entity.setGet(isGet);
			entity.setGold(cursor.getInt(cursor.getColumnIndex(TASK_TABL_NUMBER_KEY)));
			data.add(entity);
		}
		cursor.close();
		write.close();
		return data;
	}


	public UserDataEntity getUserData(){
		SQLiteDatabase write=dbHelper.getWritableDatabase();
		Cursor c = write.query(SQL_TABL_NAME_USER, null, null, null, null, null, null);
		UserDataEntity user=null;
		while(c.moveToNext()){
			user=new UserDataEntity();
			user.setId(c.getInt(c.getColumnIndex(USER_TABL_ID_KEY)));
			user.setGold(c.getInt(c.getColumnIndex(USER_TABL_GOLD_KEY)));
			user.setGrade(c.getInt(c.getColumnIndex(USER_TABL_GRADE_KEY)));
			user.setImageId(c.getInt(c.getColumnIndex(USER_TABL_IMAGE_KEY)));
			user.setName(c.getString(c.getColumnIndex(USER_TABL_NAME_KEY)));
			user.setSignNumber(c.getInt(c.getColumnIndex(USER_TABL_SIGNNUMBER_KEY)));
			user.setSore(c.getInt(c.getColumnIndex(USER_TABL_SORE_KEY)));
			c.close();
			write.close();
		}
		return user;
	}

	public void saveUser(UserDataEntity entity){
		SQLiteDatabase write=dbHelper.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(USER_TABL_ID_KEY, entity.getId());
		cv.put(USER_TABL_GOLD_KEY, entity.getGold());
		cv.put(USER_TABL_SORE_KEY, entity.getSore());
		cv.put(USER_TABL_SIGNNUMBER_KEY, entity.getSignNumber());
		cv.put(USER_TABL_EXPERIENCE_KEY, entity.getExperience());
		cv.put(USER_TABL_GRADE_KEY, entity.getGrade());
		cv.put(USER_TABL_NAME_KEY, entity.getName());
		cv.put(USER_TABL_IMAGE_KEY, entity.getImageId());
		write.insert(SQL_TABL_NAME_USER, null, cv);
		write.close();

	}
	public void upUserData(UserDataEntity entity){
		SQLiteDatabase write=dbHelper.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(USER_TABL_ID_KEY, entity.getId());
		cv.put(USER_TABL_GOLD_KEY, entity.getGold());
		cv.put(USER_TABL_SORE_KEY, entity.getSore());
		cv.put(USER_TABL_SIGNNUMBER_KEY, entity.getSignNumber());
		cv.put(USER_TABL_EXPERIENCE_KEY, entity.getExperience());
		cv.put(USER_TABL_GRADE_KEY, entity.getGrade());
		cv.put(USER_TABL_NAME_KEY, entity.getName());
		cv.put(USER_TABL_IMAGE_KEY, entity.getImageId());
		write.update(SQL_TABL_NAME_USER, cv, "id=?", new String[]{""+entity.getId()});
	}


	public boolean upData(TaskEntity entity){
		SQLiteDatabase write = dbHelper.getWritableDatabase();

		ContentValues cv=new ContentValues();
		cv.put(TASK_TABL_ID_KEY, entity.getId());
		cv.put(TASK_TABL_COMPLETE_KEY, entity.isComplete()?1:0);
		cv.put(TASK_TABL_COUNT_KEY, entity.getCount());
		cv.put(TASK_TABL_ISGET_KEY, entity.isGet()?1:0);
		cv.put(TASK_TABL_MODE_KEY, entity.getMode());
		cv.put(TASK_TABL_NUMBER_KEY, entity.getGold());
		cv.put(TASK_TABL_TASKCONTENT_KEY, entity.getTaskContent());

		int n=write.update(SQL_TABL_NAME_TASK, cv, "id=?", new String []{""+entity.getId()});
		write.close();
		if(n>=1){
			return true;
		}else{
			return false;
		}

	}

	public void insertData(ArrayList<TaskEntity> data){
		SQLiteDatabase write = dbHelper.getWritableDatabase();
		
		for (int i = 0; i < data.size(); i++) {
			TaskEntity entity=data.get(i);
			Log.i("tedu", entity.toString()+"==="+i);
			ContentValues cv=new ContentValues();
			cv.put(TASK_TABL_ID_KEY, entity.getId());
			cv.put(TASK_TABL_COMPLETE_KEY, entity.isComplete()?1:0);
			cv.put(TASK_TABL_COUNT_KEY, entity.getCount());
			cv.put(TASK_TABL_ISGET_KEY, entity.isGet()?1:0);
			cv.put(TASK_TABL_MODE_KEY, entity.getMode());
			cv.put(TASK_TABL_NUMBER_KEY, entity.getGold());
			cv.put(TASK_TABL_TASKCONTENT_KEY, entity.getTaskContent());
			write.insert(SQL_TABL_NAME_TASK, null, cv);
		}
		write.close();
	}

}
