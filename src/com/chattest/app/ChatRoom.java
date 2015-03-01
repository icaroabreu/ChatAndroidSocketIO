package com.chattest.app;

import java.util.Date;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chattest.app.controller.DatabaseManager;
import com.chattest.app.utility.Constant;
import com.chattest.app.view.MessageAdapter;

public class ChatRoom extends Fragment {

	private View view;
	
	private ListView list_messages;
	
	private MessageAdapter adapter;
	
	private MainActivity activity;
	
	private EditText msg_input;
	
	private Button btn_send;
	
	private boolean imTyping;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.chat_room_fragment, container);
		
		activity = (MainActivity)getActivity();			
		
		list_messages = (ListView)view.findViewById(R.id.list_messages);	
		msg_input = (EditText)view.findViewById(R.id.msg_input);
		btn_send = (Button)view.findViewById(R.id.btn_send);
		
		adapter = new MessageAdapter(activity, DatabaseManager.getMessage_List());
		
		list_messages.setAdapter(adapter);
		
		DatabaseManager manager = new DatabaseManager(getActivity());
		
		int databaseSize = manager.databaseSize(); 
		
		if(databaseSize < 30)
			manager.retrieveMessages(0);
		else
		{
			manager.retrieveMessages(manager.lastId() - 31);
		}
		
		appendMessage();	
		
		btn_send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				JSONObject messageData = new JSONObject();
				
				try {									
					
					activity.getSocketController().sendAttempt(msg_input.getText().toString()+Constant.SEPARATOR+new Date().getTime());
					msg_input.setText("");
				} catch (Exception e) {
					Toast.makeText(activity, "Não foi possível enviar mensagem.", Toast.LENGTH_SHORT).show();
					
					e.printStackTrace();
				}
				
			}
		});	
		
		msg_input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if(!imTyping)
				{
					imTyping = true;
					activity.getSocketController().emit("typing");
				}
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return view;
	}	
	
	public void appendMessage()
	{		
		Log.d("List Size", "Size: "+DatabaseManager.getMessage_List().size());
		
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() 
			{			
				adapter.notifyDataSetChanged();
			}
		});				
	}	
	
}
