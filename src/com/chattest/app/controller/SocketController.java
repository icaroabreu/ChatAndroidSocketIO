package com.chattest.app.controller;

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chattest.app.model.Message;
import com.chattest.app.utility.Constant;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class SocketController {
	
	private Socket socket;	
	
	private Context context;
	
	public SocketController(Context context) {
		
		this.context = context;		
		
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
			
			DatabaseManager manager = new DatabaseManager(context);
			
			try {
				JSONArray old_messages = data.getJSONArray("messages");
				
				Log.d("old", old_messages.toString());
				
				for(int i = 0; i < old_messages.length(); i++)
				{
					Message message = new Message();
					message.setAuthor(old_messages.getJSONObject(i).getString("name"));
					message.setMessage(old_messages.getJSONObject(i).getString("message"));
					
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
			
			JSONObject data = (JSONObject)args[0];
			
			
			
		}
	};
	
	public Emitter.Listener onUserLeft = new Emitter.Listener() {
		
		@Override
		public void call(Object... args) {
			
			JSONObject data = (JSONObject)args[0];
			
			
			
		}
	};
	
	public Emitter.Listener onMessage = new Emitter.Listener() {
		
		@Override
		public void call(Object... args) {
			
			JSONObject data = (JSONObject)args[0];
			
			Message message = new Message();
			
			try {
								
				message.setAuthor(data.getString("name"));
				message.setMessage(data.getString("message"));			
				
				Log.d("SocketController", message.getAuthor() + " sent: "+message.getMessage());
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
			
			//socket.emit("setNickname", "Icaro");
			
			Log.d("Socket", "Conneted");
			
		}
	};

}
