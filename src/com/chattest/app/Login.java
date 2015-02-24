package com.chattest.app;

import java.net.URISyntaxException;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

@SuppressWarnings("deprecation")
public class Login extends Fragment {

	private View view;

	private EditText name;

	private Button btn_join;

	private Socket socket;
	{
		try {
			socket = IO.socket("http://sucket.herokuapp.com/");
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.login_fragment, container, false);

		name = (EditText) view.findViewById(R.id.name);
		btn_join = (Button) view.findViewById(R.id.btnJoin);

		getActivity().getActionBar().hide();

		setHasOptionsMenu(false);
		
		socket.on("old_messages", onOldMessages);
		socket.connect();
		socket.emit("setNickname", "Icaro");
		

		btn_join.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return view;
	}
	
	public Emitter.Listener onOldMessages = new Emitter.Listener() {
		
		@Override
		public void call(Object... messages) {
			
			JSONObject data = (JSONObject)messages[0];
			
			Log.d("Login", data.toString());
		}
	};

}
