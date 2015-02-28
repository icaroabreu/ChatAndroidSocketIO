package com.chattest.app.model;

import com.chattest.app.MainActivity;
import com.chattest.app.utility.Constant;

public class Message {
	
	private String author;
	private String message;
	private int id;
	private long date;
	private boolean myMessage;
	
	public Message() {
		super();
	}

	public Message(String author, String message, int id, long date,
			boolean myMessage) {
		super();
		this.author = author;
		this.message = message;
		this.id = id;
		this.date = date;
		this.myMessage = myMessage;
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

	public boolean isMyMessage(MainActivity activity) {
		if(activity.getSharedPreferences(Constant.PREFERENCE_SHEET,activity.MODE_PRIVATE).getString(Constant.USER_NAME, "").equals(author))
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
				+ id + ", date=" + date + ", myMessage=" + myMessage + "]";
	}
	
}
