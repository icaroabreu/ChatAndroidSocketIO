package com.chattest.app.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chattest.app.model.Message;
import com.chattest.app.utility.Constant;

public class DatabaseManager {
	
	private Context context;
	
	private DatabaseHelper databaseHelper;
	
	private SQLiteDatabase database;
	
	private static ArrayList<Message> Message_List = new ArrayList<Message>();

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
		Message_List.add(message);
		
		database = databaseHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Constant.COLUMN__ID, message.getId());
		values.put(Constant.COLUMN_AUTHOR, message.getAuthor());
		values.put(Constant.COLUMN_MESSAGE, message.getMessage());
		values.put(Constant.COLUMN_DATE, message.getDate());
		
		return database.insert(Constant.TABLE_NAME, "NULL", values);
	}
	
	public void retrieveMessages(int last_id)
	{
		database = databaseHelper.getReadableDatabase();
		
//		JSONArray results = new JSONArray();
		
		Cursor cursor = database.rawQuery("SELECT * FROM " + Constant.TABLE_NAME + " WHERE '" + Constant.COLUMN__ID + "' > " + last_id + " LIMIT " + Constant.MESSAGE_LIMIT, null);			
		
		if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
            	
//            	JSONObject row = new JSONObject();
            	
            		
            		Message message = new Message();
            		message.setId(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN__ID)));
            		message.setAuthor(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_AUTHOR)));
            		message.setDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE))));
            		message.setMessage(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_MESSAGE)));
            		
//            		row.put("_id", message.getId());
//					row.put("name", message.getAuthor());
//					row.put("message", message.getMessage());
//					row.put("date", message.getDate());					
									
					Message_List.add(message);
//					results.put(row);
        	            
                
                cursor.moveToNext();
            }
        }			
		
		//return new JSONObject().put("messages", results);
	}

	public static List<Message> getMessage_List(boolean full) {
		
		if(full)		
			return Message_List;
		else 
		{
			int last_id = (Message_List.size() > 30) ? 29 : (Message_List.size() - 1);			
			return Message_List.subList(0, last_id);
		}
	}
	
	

}
