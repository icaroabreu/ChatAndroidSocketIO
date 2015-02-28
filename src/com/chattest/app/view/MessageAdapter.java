package com.chattest.app.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chattest.app.MainActivity;
import com.chattest.app.R;
import com.chattest.app.model.Message;

public class MessageAdapter extends BaseAdapter {

	private MainActivity activity;

	private List<Message> messageItems;

	public MessageAdapter(Activity activity, List<Message> messageItems) {
		super();
		this.activity = (MainActivity)activity;
		this.messageItems = messageItems;
	}

	@Override
	public int getCount() {						
		return messageItems.size();
	}
	
	@Override
	public Object getItem(int position) {	
		return messageItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Message message = messageItems.get(position);

		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);					
		
		if(message.isMyMessage(activity))
		{
			convertView = inflater.inflate(R.layout.message_item_right, null);
		}
		else
		{
			convertView = inflater.inflate(R.layout.message_item_left, null);
		}
		
		TextView authors_name = (TextView)convertView.findViewById(R.id.message_author);
		TextView message_text = (TextView)convertView.findViewById(R.id.message_text);
		TextView message_date = (TextView)convertView.findViewById(R.id.message_date);
		
		authors_name.setText(message.getAuthor());
		message_text.setText(message.getMessage());
		message_date.setText(new SimpleDateFormat("dd/mm/yyyy").format(new Date(message.getDate())));

		return convertView;
	}

}
