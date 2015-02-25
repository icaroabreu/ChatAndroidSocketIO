package com.chattest.app;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chattest.app.controller.DatabaseManager;
import com.chattest.app.controller.SocketController;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

@SuppressWarnings("deprecation")
public class Login extends Fragment {

	private View view;

	private EditText name;

	private Button btn_join;
	
	private SocketController socketController;
	
	private MainActivity main;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.login_fragment, container, false);

		name = (EditText) view.findViewById(R.id.name);
		btn_join = (Button) view.findViewById(R.id.btnJoin);
		
		main = (MainActivity)getActivity();

		main.getActionBar().hide();

		setHasOptionsMenu(false);
		
		socketController = new SocketController(getActivity());

		btn_join.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {									
				
				try {
					main.databaseManager.retrieveMessages(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		return view;
	}	

}
