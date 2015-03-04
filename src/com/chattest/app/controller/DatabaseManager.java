package com.chattest.app.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chattest.app.model.Message;
import com.chattest.app.utility.Constant;

public class DatabaseManager {

	private DatabaseHelper databaseHelper;

	private SQLiteDatabase database;

	private static List<Message> Message_List = new ArrayList<Message>();

	public DatabaseManager(Context context) {			

		databaseHelper = new DatabaseHelper(context);
	}

	public long insertMessage(Message message)
	{		
		database = databaseHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(Constant.COLUMN__ID, message.getId());
		values.put(Constant.COLUMN_AUTHOR, message.getAuthor());
		values.put(Constant.COLUMN_MESSAGE, message.getMessage());
		values.put(Constant.COLUMN_DATE, message.getDate());
		values.put(Constant.COLUMN_STATE, message.getState());	
		values.put(Constant.COLUMN_FLAG, message.getFlag());
		
		if(message.getMessageId() != 0)
		{
			values.put(Constant.COLUMN_MESSAGE_ID, message.getMessageId());
		}
		
		long new_message_id = database.insert(Constant.TABLE_NAME, "NULL", values);
				
		if(new_message_id != -1)
		{
			message.setId((int)new_message_id);
			
			Message_List.add(message);
		}

		return new_message_id;
	}

	public void retrieveMessages(int fist_id)
	{
		database = databaseHelper.getReadableDatabase();		

		String query = "SELECT * FROM " + Constant.TABLE_NAME + " WHERE " + Constant.COLUMN__ID + 
				" > " + fist_id + " LIMIT " + Constant.MESSAGE_LIMIT;

		Log.d("Database" , query);

		Cursor cursor = database.rawQuery(query, null);		
		
		List<Message> temp_list = new ArrayList<Message>();

		if (cursor.moveToFirst()) {

			while (cursor.isAfterLast() == false) {				

				Message message = new Message();
				message.setId(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN__ID)));
				message.setMessageId(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_MESSAGE_ID)));
				message.setAuthor(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_AUTHOR)));
				message.setDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE))));
				message.setMessage(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_MESSAGE)));
				message.setFlag(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_FLAG))); 
				message.setState(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_STATE)));          							

				temp_list.add(message);  							

				cursor.moveToNext();
			}
		}	
		
		Message_List.addAll(0, temp_list);
	
	}
	
	public void retrieveOldMessages(int fist_id)
	{
		database = databaseHelper.getReadableDatabase();		

		String query = "SELECT * FROM " + Constant.TABLE_NAME + " WHERE " + Constant.COLUMN__ID + 
				" BETWEEN " + (fist_id - Integer.parseInt(Constant.MESSAGE_LIMIT))+ " AND " + (fist_id - 1); 

		Log.d("Database" , query);

		Cursor cursor = database.rawQuery(query, null);		
		
		List<Message> temp_list = new ArrayList<Message>();
		
		Log.d("Database" , "Cursor Count: "+cursor.getCount());

		if (cursor.moveToFirst()) {

			while (cursor.isAfterLast() == false) {

				Message message = new Message();
				message.setId(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN__ID)));
				message.setMessageId(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_MESSAGE_ID)));
				message.setAuthor(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_AUTHOR)));
				message.setDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE))));
				message.setMessage(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_MESSAGE)));
				message.setFlag(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_FLAG))); 
				message.setState(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_STATE)));             							

				Message_List.add(message);      	            

				cursor.moveToNext();
			}
		}					
		
		Message_List.addAll(0, temp_list);
		
		Collections.sort(Message_List, new Comparator<Message>() {

			@Override
			public int compare(Message msg1, Message msg2) {
				// TODO Auto-generated method stub				
				
				return Integer.compare(msg1.getId(), msg2.getId());
			}
			
		});
	
	}
	
	public int changeMessageState(int message_id, int message_id_server, int message_state)
	{
		database = databaseHelper.getWritableDatabase();		
		
		ContentValues values = new ContentValues();
		
		values.put(Constant.COLUMN_STATE, message_state);
		values.put(Constant.COLUMN_MESSAGE_ID, message_id_server);
		
		return database.update(Constant.TABLE_NAME, values, Constant.COLUMN__ID + " = " + message_id, null);
	}

	public int databaseSize()
	{
		database = databaseHelper.getReadableDatabase();		

		Cursor cursor = database.rawQuery("SELECT * FROM " + Constant.TABLE_NAME , null);

		Log.d("Database", "Database SIZE: "+cursor.getCount());

		return cursor.getCount();
	}

	public int lastId()
	{
		database = databaseHelper.getReadableDatabase();		


		Cursor cursor = database.rawQuery("SELECT MAX(" + Constant.COLUMN__ID + 
				") AS LASTID FROM " + Constant.TABLE_NAME  , null);
		
		cursor.moveToFirst();

		Log.d("Database", "last id: "+cursor.getInt(cursor.getColumnIndex("LASTID")));

		return cursor.getInt(cursor.getColumnIndex("LASTID"));
	}
	
	public int getLastMessageId()
	{
		database = databaseHelper.getReadableDatabase();		

		Cursor cursor = database.rawQuery("SELECT MAX(" + Constant.COLUMN_MESSAGE_ID + 
				") AS LAST_MESSAGE_ID FROM " + Constant.TABLE_NAME  , null);
		
		cursor.moveToFirst();

		Log.d("Database", "LAST_MESSAGE_ID: "+cursor.getInt(cursor.getColumnIndex("LAST_MESSAGE_ID")));

		return cursor.getInt(cursor.getColumnIndex("LAST_MESSAGE_ID"));
	}

	public static List<Message> getMessage_List() {

		return Message_List;		
	}



}
