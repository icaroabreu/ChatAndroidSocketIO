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
			socket.on("user_joined", onUserJoined);
			socket.on("user_left", onUserLeft);
			socket.on("typing", onTyping);
			socket.on("stop_typing", onStopTyping);
			socket.on(Socket.EVENT_CONNECT, onConnect);
			socket.connect();
			
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}			
		
	}
	
	public Emitter.Listener onOldMessages = new Emitter.Listener() {
		
		@Override
		public void call(Object... messages) {
			
			JSONObject data = (JSONObject)messages[0];
			
			DatabaseManager manager = new DatabaseManager(activity);
			
			try {
				JSONArray old_messages = data.getJSONArray("messages");
				
				Log.d("old", old_messages.toString());
				
				for(int i = 0; i < old_messages.length(); i++)
				{
					Message message = new Message();
					message.setId(old_messages.getJSONObject(i).getInt("_id"));
					message.setAuthor(old_messages.getJSONObject(i).getString("name"));
					message.setMessage(old_messages.getJSONObject(i).getString("message"));
					message.setDate(old_messages.getJSONObject(i).getLong("date"));
					message.setFlag(old_messages.getJSONObject(i).getString("flag"));
					
					if(manager.insertMessage(message) != 0)
					{
						Log.d("Salvou", " SIM "+message.toString());
					}
					else
					{
						Log.d("Salvou", " NAO "+message.toString());
					}
				}
				
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
			
			Log.d("Socket", args[0].toString());
			
			activity.showFragment(MainActivity.CHATSCREEN);
			
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
			
			Message message = new Message();
			
			DatabaseManager manager = new DatabaseManager(activity);
			
			Log.d("SocketController", data.toString());
			
			try {
								
				message.setId(data.getInt("_id"));
				message.setAuthor(data.getString("name"));
				message.setMessage(data.getString("message"));
				message.setDate(data.getLong("date"));	
				message.setFlag(data.getString("flag"));
				
				if(manager.insertMessage(message) != 0)
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
			
			JSONObject data = (JSONObject)args[0];
			
			
			
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
			Log.d("Socket", "Conneted");
			
		}
	};
	
	public void sendAttempt(String message)
	{
		Log.d("Socket", "attempt to send: "+message);
		
		socket.emit("message", message, new Ack() {
			
			@Override
			public void call(Object... arg0) {
				
				Log.d("Socket", "Message has arrived in the server: "+arg0.toString());
				
			}
		});
	}
	
	public void emit(String tag, String message)
	{
		Log.d("Socket", "emitiu: tag: " + tag + " message: " + message);
		
		socket.emit(tag, message);
	}
	
	public void emit(String tag)
	{
		Log.d("Socket", "emitiu: tag: ");
		
		socket.emit(tag);
	}

	public boolean isConnected() {		
		
		return isConnected;
	}

	public Socket getSocket() {
		return socket;
	}	

}
