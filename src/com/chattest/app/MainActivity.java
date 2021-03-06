package com.chattest.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.chattest.app.controller.DatabaseManager;
import com.chattest.app.controller.SocketController;
import com.chattest.app.utility.Constant;
import com.github.nkzawa.socketio.client.Socket;

public class MainActivity extends Activity {

	public static final int LOGINSCREEN = 0;
	public static final int CHATSCREEN = 1;
	public static final int SCREENCOUNT = CHATSCREEN + 1;

	private SharedPreferences preferences;
	public DatabaseManager databaseManager;

	public Fragment[] screens = new Fragment[SCREENCOUNT];

	private SocketController socketController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preferences = getSharedPreferences(Constant.PREFERENCE_SHEET, MODE_PRIVATE);
		databaseManager = new DatabaseManager(getBaseContext());          
		socketController = new SocketController(this);

		if (savedInstanceState == null) {
			FragmentManager fm = getFragmentManager();
			screens[LOGINSCREEN] = fm.findFragmentById(R.id.login_screen);
			screens[CHATSCREEN] = fm.findFragmentById(R.id.chat_screen);        	

			if(preferences.getString(Constant.USER_NAME, "").equals(""))
			{        		
				
				//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
				fm.beginTransaction().show(screens[LOGINSCREEN]).commit();
				fm.beginTransaction().hide(screens[CHATSCREEN]).commit();
			}
			else
			{        		
				fm.beginTransaction().show(screens[CHATSCREEN]).commit();
				fm.beginTransaction().hide(screens[LOGINSCREEN]).commit();			
				//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);				
			}
		}
	}

	public void showFragment(int fragmentIndex)
	{
		if(fragmentIndex == LOGINSCREEN)
		{        		
			getFragmentManager().beginTransaction().show(screens[LOGINSCREEN]).commit();
			getFragmentManager().beginTransaction().hide(screens[CHATSCREEN]).commit();
		}
		else if(fragmentIndex == CHATSCREEN)
		{        		
			getFragmentManager().beginTransaction().show(screens[CHATSCREEN]).commit();
			getFragmentManager().beginTransaction().hide(screens[LOGINSCREEN]).commit();
		}
	}            

	@Override
	protected void onDestroy() {

		socketController.getSocket().disconnect();
		socketController.getSocket().off("old_messages", socketController.onOldMessages);
		socketController.getSocket().off("message", socketController.onMessage);		
		socketController.getSocket().off("user_joined", socketController.onUserJoined);
		socketController.getSocket().off("user_left", socketController.onUserLeft);
		socketController.getSocket().off("typing", socketController.onTyping);
		socketController.getSocket().off("stop_typing",socketController.onStopTyping);
		socketController.getSocket().off(Socket.EVENT_CONNECT, socketController.onConnect);
		socketController.getSocket().off(Socket.EVENT_DISCONNECT, socketController.onDisconnect);

		super.onDestroy();
	}


	public SocketController getSocketController() {
		return socketController;
	}


	public SharedPreferences getPreferences() {
		return preferences;
	}	

}
