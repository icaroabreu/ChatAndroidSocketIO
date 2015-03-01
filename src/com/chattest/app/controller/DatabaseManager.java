package com.chattest.app.controller;

import java.util.ArrayList;
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
		Message_List.add(message);

		database = databaseHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Constant.COLUMN__ID, message.getId());
		values.put(Constant.COLUMN_AUTHOR, message.getAuthor());
		values.put(Constant.COLUMN_MESSAGE, message.getMessage());
		values.put(Constant.COLUMN_DATE, message.getDate());
		values.put(Constant.COLUMN_FLAG, message.getFlag());

		return database.insert(Constant.TABLE_NAME, "NULL", values);
	}

	public void retrieveMessages(int fist_id)
	{
		database = databaseHelper.getReadableDatabase();		

		String query = "SELECT * FROM " + Constant.TABLE_NAME + " WHERE " + Constant.COLUMN__ID + " > " + fist_id + " LIMIT " + Constant.MESSAGE_LIMIT;

		Log.d("Database" , query);

		Cursor cursor = database.rawQuery(query, null);			

		if (cursor.moveToFirst()) {

			while (cursor.isAfterLast() == false) {

				Message message = new Message();
				message.setId(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN__ID)));
				message.setAuthor(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_AUTHOR)));
				message.setDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE))));
				message.setMessage(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_MESSAGE)));
				message.setFlag(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_FLAG)));            							

				Message_List.add(message);      	            

				cursor.moveToNext();
			}
		}			

		//return new JSONObject().put("messages", results);
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

		Log.d("Database", "entrou no lastId'");

		Cursor cursor = database.rawQuery("SELECT MAX(" + Constant.COLUMN__ID + ") AS LASTID FROM " + Constant.TABLE_NAME  , null);
		cursor.moveToFirst();

		Log.d("Database", "last id: "+cursor.getInt(cursor.getColumnIndex("LASTID")));

		return cursor.getInt(cursor.getColumnIndex("LASTID"));
	}

	public static List<Message> getMessage_List() {

		return Message_List;		
	}



}
