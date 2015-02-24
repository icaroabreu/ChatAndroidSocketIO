package com.chattest.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONArray;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

@SuppressWarnings("deprecation")
public class Login extends Fragment {

	private View view;

	private Socket socket;

	private EditText name;

	private Button btn_join;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.login_fragment, container, false);

		name = (EditText) view.findViewById(R.id.name);
		btn_join = (Button) view.findViewById(R.id.btnJoin);

		getActivity().getActionBar().hide();

		SocketIOClient.connect("http://sucket.herokuapp.com/", new ConnectCallback() {
			
			@Override
			public void onConnectCompleted(Exception ex, SocketIOClient client) {
				
				Log.d("Login", "conection on");
				
				client.emit("setNickname", new JSONArray().put("Icaro"));							
				
				client.on("old_messages", new EventCallback() {
					
					@Override
					public void onEvent(String event, JSONArray argument,
							Acknowledge acknowledge) {
						
						Log.d("Login", event + " " + argument.toString());
						
					}
				});							
				
			}
		}, new Handler());

		btn_join.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return view;
	}

}
