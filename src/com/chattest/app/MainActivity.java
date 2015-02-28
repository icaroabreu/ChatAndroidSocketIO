package com.chattest.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chattest.app.controller.DatabaseManager;
import com.chattest.app.controller.SocketController;
import com.chattest.app.utility.Constant;

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
        		fm.beginTransaction().show(screens[LOGINSCREEN]).commit();
        		fm.beginTransaction().hide(screens[CHATSCREEN]).commit();
        	}
        	else
        	{        		
        		fm.beginTransaction().show(screens[CHATSCREEN]).commit();
        		fm.beginTransaction().hide(screens[LOGINSCREEN]).commit();
        		
        		socketController.emit("setNickname", preferences.getString(Constant.USER_NAME, ""));
        	}
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    
    public SocketController getSocketController() {
		return socketController;
	}


	public SharedPreferences getPreferences() {
		return preferences;
	}	
   
}
