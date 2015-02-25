package com.chattest.app.model;

public class Message {
	
	private String author;
	private String message;
	private int id;
	private String date;
	
	public Message() {
		super();
	}

	public Message(String author, String message, int id, String date) {
		super();
		this.author = author;
		this.message = message;
		this.id = id;
		this.date = date;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Message [author=" + author + ", message=" + message + ", id="
				+ id + ", date=" + date + "]";
	}
		
}
