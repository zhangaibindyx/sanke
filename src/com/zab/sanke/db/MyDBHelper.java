package com.zab.sanke.db;

import com.zab.sanke.util.ConfigUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper implements ConfigUtil {

	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 String sql="create table "+SQL_TABL_NAME_TASK+
				 "(id int primary key,"
		 		+ "number int ,complete int ,"
		 		+ "taskcontent varchar(200),conunt int , "
		 		+ "mode int ,isget int)";
		 db.execSQL(sql);
		  sql="create table "+SQL_TABL_NAME_USER+
				 "(id int primary key,"
				 + "gold int ,"
				 + "sore int ,"
				 +"signnumber int"
				 + "experience int ,"
				 + "grade int , "
				 + "name varchar(30) ,imageId int)";
		 db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
