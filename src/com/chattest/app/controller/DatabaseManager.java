package com.chattest.app.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chattest.app.model.Message;
import com.chattest.app.utility.Constant;

public class DatabaseManager {
	
	private Context context;
	
	private DatabaseHelper databaseHelper;
	
	private SQLiteDatabase database;

	public DatabaseManager(Context context) {		
		this.context = context;
		
		databaseHelper = new DatabaseHelper(context);
	}
	
	public void createDatabase()
	{
		//databaseHelper.onCreate(database);
	}
	
	public long insertMessage(Message message)
	{
		
		database = databaseHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Constant.COLUMN_AUTHOR, message.getAuthor());
		values.put(Constant.COLUMN_MESSAGE, message.getMessage());
		values.put(Constant.COLUMN_DATE, message.getDate());
		
		return database.insert(Constant.TABLE_NAME, "NULL", values);
	}
	
	public JSONObject retrieveMessages(int last_id) throws JSONException
	{
		database = databaseHelper.getReadableDatabase();
		
		JSONArray results = new JSONArray();
		
		Cursor cursor = database.rawQuery("SELECT * FROM " + Constant.TABLE_NAME + " LIMIT " + Constant.MESSAGE_LIMIT + " OFFSET " + last_id, null);			
		
		if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
            	
            	JSONObject row = new JSONObject();
            	
            	try {
					row.put("name", cursor.getString(cursor.getColumnIndex(Constant.COLUMN_AUTHOR)));
					row.put("message", cursor.getString(cursor.getColumnIndex(Constant.COLUMN_MESSAGE)));
					row.put("date", cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE)));					
									
					results.put(row);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            	            
                
                cursor.moveToNext();
            }
        }
		
		Log.d("Results", results.toString());
		
		return new JSONObject().put("messages", results);
	}

}
