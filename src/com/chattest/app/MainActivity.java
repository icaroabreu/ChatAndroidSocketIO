package com.chattest.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chattest.app.controller.DatabaseHelper;
import com.chattest.app.controller.DatabaseManager;
import com.chattest.app.utility.Constant;



public class MainActivity extends Activity {
	
	private SharedPreferences preferences;
	public DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        preferences = getSharedPreferences(Constant.PREFERENCE_SHEET, MODE_PRIVATE);
        databaseManager = new DatabaseManager(getBaseContext());
        
        if(!preferences.getBoolean(Constant.FIRST_TIME_SETUP, false))
        {
        	Editor editor = preferences.edit();
        	editor.putBoolean(Constant.FIRST_TIME_SETUP, true);
        	if(editor.commit())
        	{        		
        		databaseManager.createDatabase();
        	}
        }                              
        
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new Login())
                    .commit();
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

   
}
