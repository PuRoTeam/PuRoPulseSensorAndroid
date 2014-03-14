package it.uniroma2.pulsesensor.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pulsesensorchart.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Prova extends Activity{
	
	ArrayAdapter<String> mAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prova);
		
//		TextView t = (TextView) findViewById(R.id.textprova);
		
		Intent intent = getIntent();
		String json = intent.getStringExtra("json");
		
		//t.setText(json);
		
		
		ListView listView = (ListView) findViewById(R.id.listViewDemo);
		
		JSONArray objarr = null;
		
		String[] points = {"fff", "Cla", "Arduino","",""};
		
//		fromColumns[2] = intent.getStringExtra("uid");
//		fromColumns[3] = intent.getStringExtra("from");
//		fromColumns[4] = intent.getStringExtra("to");
		try {
			objarr = new JSONArray(json);
		
			ArrayList<String> arr = new ArrayList<String>();
			
			for (int i = 0; i < objarr.length(); i++)
				arr.add(objarr.get(i).toString());
			
			points = arr.toArray(points);
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		/*
        try 
		{
			JSONArray jarray = new JSONArray(json);
			ArrayList<Long> allUidsInJsonArray = new ArrayList<Long>();
			
			int lenght = jarray.length();
			
			//salvo tutti gli uid (una sola volta) che trovo nell'array di punti
			for(int i = 0; i < lenght; i++) 
			{
				JSONObject curJsonElement = jarray.getJSONObject(i);
				
				long curUid = curJsonElement.getLong("uid");
				
				if(!allUidsInJsonArray.contains(curUid))
					allUidsInJsonArray.add(curUid);
			}
			
			//creo un array (per ogni uid) contenente tutti i punti con stesso uid
			for(int i = 0; i < allUidsInJsonArray.size(); i++) 
			{
				long curUidInArray = allUidsInJsonArray.get(i);
				
				for(int j = 0; j < lenght; j++)
				{
					JSONObject curJsonElement = jarray.getJSONObject(j);					
					long curUidJsonElement = curJsonElement.getLong("uid");
					
					if(curUidJsonElement == curUidInArray)
					{
						double curValue = curJsonElement.getDouble("value");
						long curTimestamp = curJsonElement.getLong("timestamp");
						
					}
					
				}
			}			
		} 
		catch (JSONException e) 
		{ e.printStackTrace(); } 
        */
        
        // For the cursor adapter, specify which columns go into which views
		mAdapter = new ArrayAdapter(this, R.layout.row, R.id.textViewList, points);
		
        listView.setAdapter(mAdapter);
	}

}
