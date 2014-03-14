package it.uniroma2.pulsesensor.activity;

import it.uniroma2.pulsesensorchart.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private SharedPreferences prefs;
	public static String ipAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(getApplicationContext(), Settings.class);
			startActivityForResult(intent, 0);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	public void realTime(View view){
		Intent intent = new Intent(view.getContext(), DynamicGraphActivity.class);
		startActivity(intent); 
	}
	
	public void chooseDate(View view){
		Intent intent = new Intent(view.getContext(), ChooseDate.class);
		startActivityForResult(intent, 0);
	}
	
}
