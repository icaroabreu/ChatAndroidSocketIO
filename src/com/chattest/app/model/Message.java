package com.chattest.app.model;

import android.app.Activity;

import com.chattest.app.MainActivity;
import com.chattest.app.utility.Constant;
import com.github.nkzawa.socketio.client.Ack;

public class Message {
	
	private String author;
	private String message;
	private int id;
	private int message_id;
	private int state;
	private long date;
	private String flag;
	private boolean sendAttemptFailed;
	
	public Message() {
		super();
	}

	public Message(String author, String message, int id, int state, long date,
			String flag,int message_id) {
		super();
		this.author = author;
		this.message = message;
		this.id = id;
		this.message_id = message_id;
		this.state = state;
		this.date = date;
		this.flag = flag;
		this.sendAttemptFailed = false;
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
	
	public int getMessageId() {
		return message_id;
	}

	public void setMessageId(int message_id) {
		this.message_id = message_id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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
				+ id + ", state=" + state + ", date=" + date + ", flag=" + flag
				+ "]";
	}

	public boolean isSendAttemptFailed() {
		return sendAttemptFailed;
	}

	public void setSendAttemptFailed(boolean sendAttemptFailed) {
		this.sendAttemptFailed = sendAttemptFailed;
	}	

}
