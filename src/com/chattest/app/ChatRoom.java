package com.chattest.app;

import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.CalendarContract.Attendees;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.chattest.app.controller.DatabaseManager;
import com.chattest.app.model.Message;
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
				
				try {			
					
					Message new_message = new Message();
					
					String user = activity.getPreferences().getString(Constant.USER_NAME, "NaN");
					
					if(!user.equals("NaN"))
					{
						new_message.setAuthor(user);
						new_message.setDate(new Date().getTime());
						new_message.setFlag("message");
						new_message.setState(Constant.MESSAGE_HAS_NOT_ARRIVED_IN_SERVER);
						new_message.setMessage(msg_input.getText().toString());											
						
						if(activity.databaseManager.insertMessage(new_message) != -1)
						{
							activity.getSocketController().sendAttempt(new_message);
							msg_input.setText("");
							appendMessage();
						}
					
					}
					
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
		
		list_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(DatabaseManager.getMessage_List().get(position).getState() != Constant.MESSAGE_ARRIVED_IN_SERVER)
				{
					Message new_message = DatabaseManager.getMessage_List().get(position);								
					
					activity.getSocketController().sendAttempt(new_message);
				}
				
			}
			
			
		});
		
		return view;
	}	
	
	public void appendMessage()
	{				
		
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() 
			{			
				adapter.notifyDataSetChanged();
			}
		});				
	}
	
	public View getViewByPosition(int position, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (position < firstListItemPosition || position > lastListItemPosition ) {
			return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}
	
	public void updateMessage(final int message_id, final int message_id_server)
	{		
	
		for(int i = 0; i < DatabaseManager.getMessage_List().size(); i++)
		{			
			
			if(DatabaseManager.getMessage_List().get(i).getId() == message_id)
			{			
				Log.d("ChatRoom", "Update attempt");
				
				final int index = i;
				
				//final View messagem_item = getViewByPosition(i, list_messages);
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						if(activity.databaseManager.changeMessageState(message_id, message_id_server, Constant.MESSAGE_ARRIVED_IN_SERVER) > 0)
						{
							Log.d("Message", "Updated: " + index);
							//LinearLayout message_holder = (LinearLayout)messagem_item.findViewById(R.id.message_holder);
							//message_holder.setAlpha(1f);
							DatabaseManager.getMessage_List().get(index).setState(Constant.MESSAGE_ARRIVED_IN_SERVER);
						}						
						
					}
				});
												
			}
		}
		appendMessage();
	}
}
