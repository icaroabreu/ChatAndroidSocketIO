package com.chattest.app.model;

import android.app.Activity;

import com.chattest.app.MainActivity;
import com.chattest.app.utility.Constant;

public class Message {
	
	private String author;
	private String message;
	private int id;
	private long date;
	private String flag;
	
	public Message() {
		super();
	}

	public Message(String author, String message, int id, long date, String flag) {
		super();
		this.author = author;
		this.message = message;
		this.id = id;
		this.date = date;
		this.flag = flag;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public boolean isMyMessage(MainActivity activity) {
		if(activity.getSharedPreferences(Constant.PREFERENCE_SHEET,Activity.MODE_PRIVATE).getString(Constant.USER_NAME, "").equals(author))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public String toString() {
		return "Message [author=" + author + ", message=" + message + ", id="
				+ id + ", date=" + date + ", flag=" + flag + "]";
	}	
	
}
