package com.chattest.app.controller;

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.chattest.app.ChatRoom;
import com.chattest.app.MainActivity;
import com.chattest.app.model.Message;
import com.chattest.app.utility.Constant;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.emitter.Emitter.Listener;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class SocketController {
	
	private Socket socket;
	
	private MainActivity activity;
	
	private boolean isConnected;
	
	public SocketController(Activity activity) {
		
		this.activity = (MainActivity)activity;	
		
		this.isConnected = false;
		
		try {
			socket = IO.socket(Constant.WEBSOCKET_SERVER_URL);						
			
			socket.on("old_messages", onOldMessages);
			socket.on("message", onMessage);
			socket.on("message_received", onMessageReceived);
			socket.on("user_joined", onUserJoined);
			socket.on("user_left", onUserLeft);
			socket.on("typing", onTyping);
			socket.on("stop_typing", onStopTyping);
			socket.on(Socket.EVENT_CONNECT, onConnect);
			socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
			socket.connect();
			
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}			
		
	}
	
	public Emitter.Listener onOldMessages = new Emitter.Listener() {
		
		@Override
		public void call(Object... messages) {
			
			JSONObject data = (JSONObject)messages[0];
			
			final DatabaseManager manager = new DatabaseManager(activity);
						
			try {
				JSONArray old_messages = data.getJSONArray("messages");						
				
				for(int i = 0; i < old_messages.length(); i++)
				{
					final Message message = new Message();
					message.setMessageId(old_messages.getJSONObject(i).getInt("_id"));
					message.setAuthor(old_messages.getJSONObject(i).getString("name"));
					message.setMessage(old_messages.getJSONObject(i).getString("message"));
					message.setDate(old_messages.getJSONObject(i).getLong("date"));
					message.setState(Constant.MESSAGE_ARRIVED_IN_SERVER);
					message.setFlag(old_messages.getJSONObject(i).getString("flag"));
					
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							message.setId((int)manager.insertMessage(message));
							
						}
					});										
					
				}
				((ChatRoom)activity.screens[MainActivity.CHATSCREEN]).appendMessage();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Log.d("Login", data.toString());
		}
	};
	
	public Emitter.Listener onUserJoined = new Emitter.Listener() {
		
		@Override
		public void call(Object... args) {
			
			JSONObject user_data = (JSONObject)args[0];				
			
			Log.d("SocketController", user_data.toString());
						
			try {
				
				JSONObject data = (JSONObject)user_data.getJSONObject("data");
				
				if(activity.getPreferences().getString(Constant.USER_NAME, "").equals(data.getString("name")) && !activity.getPreferences().getBoolean(Constant.FIRST_TIME_SETUP, false))
				{
					//First Time
					activity.getPreferences().edit().putBoolean(Constant.FIRST_TIME_SETUP, true).commit();
					emit("get_first_time_messages");
					activity.showFragment(MainActivity.CHATSCREEN);
				}
				else if(activity.getPreferences().getString(Constant.USER_NAME, "").equals(data.getString("name")) && activity.getPreferences().getBoolean(Constant.FIRST_TIME_SETUP, false))
				{								
					emit("get_old_messages", activity.databaseManager.getLastMessageId());					
				}							
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			Log.d("Socket", args[0].toString());			
			
		}
	};
	
	public Emitter.Listener onUserLeft = new Emitter.Listener() {
		
		@Override
		public void call(Object... args) {
			
			JSONObject user_data = (JSONObject)args[0];
			
			Log.d("Socket", args[0].toString());
			
			Message message = new Message();
			
			DatabaseManager manager = new DatabaseManager(activity);
			
			Log.d("SocketController", user_data.toString());
			
			try {
				
				JSONObject data = (JSONObject)user_data.getJSONObject("data");
								
				message.setId(data.getInt("_id"));
				message.setAuthor(data.getString("name"));
				message.setMessage(data.getString("message"));
				message.setDate(data.getLong("date"));	
				message.setFlag(data.getString("flag"));
				
//				if(manager.insertMessage(message) != 0)
//				{
//					ChatRoom room = (ChatRoom)activity.screens[MainActivity.CHATSCREEN];
//					room.appendMessage();
//				}
				
				Log.d("SocketController", message.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
	};
	
	public Emitter.Listener onMessage = new Emitter.Listener() {
		
		@Override
		public void call(Object... args) {
			
			JSONObject data = (JSONObject)args[0];
			
			final Message message = new Message();
			
			final DatabaseManager manager = new DatabaseManager(activity);
			
			Log.d("SocketController", data.toString());
			
			try {
												
				message.setAuthor(data.getString("name"));
				message.setMessage(data.getString("message"));
				message.setState(Constant.MESSAGE_ARRIVED_IN_SERVER);
				message.setDate(data.getLong("date"));	
				message.setFlag(data.getString("flag"));
				
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						message.setId((int)manager.insertMessage(message));
						
					}
				});			
				
				if(message.getId() != -1)
				{				
					ChatRoom room = (ChatRoom)activity.screens[MainActivity.CHATSCREEN];
					room.appendMessage();
				}
				
				Log.d("SocketController", message.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
			
		}
	};
	
	public Emitter.Listener onTyping = new Emitter.Listener() {
		
		@Override
		public void call(Object... args) {
			
			Log.d("Socket", args[0].toString()+ " is typing?");
			
			
			
		}
	};
	
	public Emitter.Listener onStopTyping = new Emitter.Listener() {
		
		@Override
		public void call(Object... args) {
			
			JSONObject data = (JSONObject)args[0];
			
			
			
		}
	};
	
	public Emitter.Listener onConnect = new Emitter.Listener() {
		
		@Override
		public void call(Object... args) {	
			
			isConnected = true;			
			if(!activity.getPreferences().getString(Constant.USER_NAME, "").equals(""))
			{
				socket.emit("setNickname", activity.getPreferences().getString(Constant.USER_NAME, ""));
			}			
			Log.d("Socket", "Conneted");
			
		}
	};
	
	public Emitter.Listener onDisconnect = new Emitter.Listener() {
		
		@Override
		public void call(Object... arg0) {
			
			isConnected = false;
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
				
					while(!Thread.interrupted() || isConnected)
					{
						try {
							
							Thread.sleep(1000);
							Log.d("Socket", "Connection Attempt");							
							socket.connect();
							
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
				}
			});
			
		}
	};
	
	public Emitter.Listener onMessageReceived = new Emitter.Listener() {
		
		@Override
		public void call(Object... message) {

			try {
				JSONObject json_data = new JSONObject(message[0].toString());
				
				ChatRoom manager = (ChatRoom)activity.screens[MainActivity.CHATSCREEN];
				manager.updateMessage(json_data.getInt("message_id"), json_data.getInt("_id"));
			} catch (JSONException e) {			
				e.printStackTrace();
			}
			
			Log.d("Socket", "Message has arrived in the server: "+message[0]);
			
		}
	};
	
	public void sendAttempt(Message new_message)
	{
		String message = new_message.getMessage()+Constant.SEPARATOR+new_message.getId()+Constant.SEPARATOR+new_message.getDate();
		
		Log.d("Socket", "attempt to send: "+message);
		
		socket.emit("message", message);
	}	
	
	public void emit(String tag)
	{
		Log.d("Socket", "emitiu: tag: ");
		
		socket.emit(tag);
	}
	
	public void emit(String tag, String message)
	{
		Log.d("Socket", "emitiu: tag: " + tag + " message: " + message);
		
		socket.emit(tag, message);
	}
	
	public void emit(String tag, int value)
	{
		Log.d("Socket", "emitiu: tag: " + tag + " value: " + value);
		
		socket.emit(tag, value);
	}

	public boolean isConnected() {		
		
		return isConnected;
	}

	public Socket getSocket() {		
		return socket;
	}		

}
