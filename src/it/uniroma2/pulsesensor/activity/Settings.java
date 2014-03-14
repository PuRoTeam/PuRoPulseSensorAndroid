package it.uniroma2.pulsesensor.activity;

import it.uniroma2.pulsesensorchart.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity implements OnClickListener{
	
	private SharedPreferences prefs;
	private Button save;
	private EditText edit_ip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		edit_ip = (EditText) findViewById(R.id.ipaddress);
		edit_ip.setText(prefs.getString("ip", ""));
		
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
	}
	
	public void salvapreferenze(String testo) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("ip", testo);
		editor.commit();
	}

	@Override
	public void onClick(View v) {

		if (v == save){
			String ipChoosed = edit_ip.getText().toString();
			salvapreferenze(ipChoosed);
			Toast.makeText(getApplicationContext(), ipChoosed, Toast.LENGTH_LONG).show();
			
			Intent back = new Intent(this, LoginActivity.class);
			startActivity(back);
		}
	}

}
