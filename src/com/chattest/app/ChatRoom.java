package com.chattest.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chattest.app.controller.DatabaseManager;
import com.chattest.app.view.MessageAdapter;

public class ChatRoom extends Fragment {

	private View view;
	
	private ListView list_messages;
	
	private MessageAdapter adapter;
	
	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.chat_room_fragment, container);
		
		activity = (MainActivity)getActivity();
		
		list_messages = (ListView)view.findViewById(R.id.list_messages);
				
		DatabaseManager manager = new DatabaseManager(getActivity());
		
		manager.retrieveMessages(0);
		
		adapter = new MessageAdapter(activity, DatabaseManager.getMessage_List(false));
		
		list_messages.setAdapter(adapter);
		
		return view;
	}
	
	public void appendMessage()
	{						
		adapter.notifyDataSetChanged();				
	}
	
}
