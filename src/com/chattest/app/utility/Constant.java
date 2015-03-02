package com.chattest.app.utility;

public class Constant {
	
	public static final String PREFERENCE_SHEET = "PreferenceSheet";	
	
	/* Triggers */
	
	public static final String USER_NAME = "UserName";
	public static final String FIRST_TIME_SETUP = "FirstTimeSetup";
	
	/* Server URL */
	
	public static final String WEBSOCKET_SERVER_URL = "https://sucket.herokuapp.com/";
	//public static final String WEBSOCKET_SERVER_URL = "http://192.168.1.114:5000";
	
	/* Message Types */
	
	public static final String MESSAGE_TYPE_MESSAGE = "message";
	public static final String MESSAGE_TYPE_USER_JOINED = "user_joined";
	public static final String MESSAGE_TYPE_USER_LEFT = "user_left"; 		
	
	/* Triggers */
		
	public static final int MESSAGE_HAS_NOT_ARRIVED_IN_SERVER = 0;
	public static final int MESSAGE_ARRIVED_IN_SERVER = 1;
	
	/* Database */	
	
	public static final String DATABASE_NAME = "Chat.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NAME = "messages";	
    public static final String COLUMN__ID = "_id";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_FLAG = "flag";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_DATE = "date";    
    
    public static final String MESSAGE_LIMIT = "30";
    
    /* queries */
    
    public static final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + 
    		TABLE_NAME + "(" + 
    		COLUMN__ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
    		COLUMN_MESSAGE_ID + " INTEGER," +
    		COLUMN_STATE + " INTEGER NOT NULL," +
    		COLUMN_AUTHOR + " CHAR(50) NOT NULL," +
    		COLUMN_FLAG + " CHAR(50) NOT NULL," +
    		COLUMN_MESSAGE + " TEXT NOT NULL, " +
    		COLUMN_DATE + " CHAR(45) " +
    		");";
    
    /* Misc */
    
    public static final String SEPARATOR = "¬SEPARATOR¬";

}
