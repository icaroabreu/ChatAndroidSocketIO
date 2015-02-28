package com.chattest.app.utility;

public class Constant {
	
	public static final String PREFERENCE_SHEET = "PreferenceSheet";	
	
	/* Triggers */
	
	public static final String USER_NAME = "UserName";
	public static final String FIRST_TIME_SETUP = "FirstTimeSetup";
	
	/* Server URL */
	
	public static final String WEBSOCKET_SERVER_URL = "https://sucket.herokuapp.com/";
	
	/* Database */	
	
	public static final String DATABASE_NAME = "Chat.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NAME = "messages";
    public static final String COLUMN__ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DATE = "date";    
    
    public static final String MESSAGE_LIMIT = "30";
    
    /* queries */
    
    public static final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + 
    		TABLE_NAME + "(" + COLUMN__ID + " INTEGER PRIMARY KEY NOT NULL," + 
    		COLUMN_AUTHOR + " CHAR(50) NOT NULL," +
    		COLUMN_MESSAGE + " TEXT NOT NULL, " +
    		COLUMN_DATE + " CHAR(45)" +
    		" );";

}
