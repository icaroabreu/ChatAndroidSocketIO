package com.chattest.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.chattest.app.utility.Constant;

@SuppressWarnings("deprecation")
public class Login extends Fragment {

	private View view;

	private EditText name;

	private Button btn_join;	
	
	private MainActivity main;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.login_fragment, container, false);

		name = (EditText) view.findViewById(R.id.name);
		btn_join = (Button) view.findViewById(R.id.btnJoin);
		
		main = (MainActivity)getActivity();	

		setHasOptionsMenu(false);			

		btn_join.setOnClickListener(new View.OnClickListener() {

			@Override
		public void onClick(View v) {																				
				
				main.getSocketController().emit("setNickname", name.getText().toString());
				
				main.getPreferences().edit().putString(Constant.USER_NAME, name.getText().toString()).commit();
				
			}
		});

		return view;
	}

}
