package com.chattest.app.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chattest.app.utility.Constant;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION); 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.d("SQL TABLE", Constant.SQL_CREATE_TABLE_QUERY);
		
		db.execSQL(Constant.SQL_CREATE_TABLE_QUERY);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
