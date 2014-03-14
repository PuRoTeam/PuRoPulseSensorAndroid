package it.uniroma2.pulsesensor.activity;

import it.uniroma2.pulsesensor.graph.ReplayGraph;
import it.uniroma2.pulsesensor.secure.SSLHttpClient;
import it.uniroma2.pulsesensorchart.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.achartengine.chartdemo.ChartDemo;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class ChooseDate extends FragmentActivity{
	
	private Calendar from;
	private Calendar fromSelected;
	private Calendar to;
	private Calendar toSelected;
	
	private EditText fromDisplay;
	private EditText toDisplay;
    
	private Button fromPickDate;
	private Button fromPickTime;
	private Button toPickDate;
	private Button toPickTime;
	
	private Spinner spinnerUid;
    
	private int fromYear;
	private int fromMonth;
	private int fromDay;
	private int fromHour;
	private int fromMinute;
    
	private int toYear;
	private int toMonth;
	private int toDay;
	private int toHour;
	private int toMinute;
	
	private int uidSelect;
	
	private ProgressDialog mProgressDialog;
	
	   @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date);
		
		fromDisplay = (EditText) findViewById(R.id.editText1);
        toDisplay = (EditText) findViewById(R.id.editText2);
        
        fromPickDate = (Button) findViewById(R.id.date_button_1);
        fromPickTime = (Button) findViewById(R.id.time_button_1);
        toPickDate = (Button) findViewById(R.id.date_button_2);
        toPickTime = (Button) findViewById(R.id.time_button_2);
        
        uidSelect = -1;
        
        spinnerUid = (Spinner) findViewById(R.id.spinUid);
        List<String> lsSpinUid = new ArrayList<String>();
        lsSpinUid.add("");
        lsSpinUid.add("1");
        lsSpinUid.add("2");
        
        ArrayAdapter<String> aspnUid = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lsSpinUid);
        aspnUid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUid.setAdapter(aspnUid);
        // Set up a callback for the spinner
        spinnerUid.setOnItemSelectedListener(
        	new OnItemSelectedListener() {
        		public void onNothingSelected(AdapterView<?> arg0) {}
	            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
	                // Code that does something when the Spinner value changes
	            	if(position == 0)
	            		uidSelect = -1;
	            	else
	            		uidSelect = position;
//	            	Toast.makeText(parent.getContext(), ""+uidSelect, Toast.LENGTH_LONG).show(); 
	            }
	        });
        
        mProgressDialog = new ProgressDialog(this);
    	mProgressDialog.setIndeterminate(false);
    	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        
        from = Calendar.getInstance();
        fromYear = from.get(Calendar.YEAR);
        fromMonth = from.get(Calendar.MONTH);
        fromDay = from.get(Calendar.DAY_OF_MONTH);
        fromHour = from.get(Calendar.HOUR);
        fromMinute = from.get(Calendar.MINUTE);
        
        to = Calendar.getInstance();
        toYear = from.get(Calendar.YEAR);
        toMonth = from.get(Calendar.MONTH);
        toDay = from.get(Calendar.DAY_OF_MONTH);
        toHour = from.get(Calendar.HOUR);
        toMinute = from.get(Calendar.MINUTE);
        
        updateFromDisplay();
        updateToDisplay();
        
        fromSelected = from;
        toSelected = to;
        
        updateFrom();
        updateTo();
	}
	
	public void showDialog(View v) {
		switch(v.getId()) {
			case R.id.date_button_1:
		    	showDialog(0);
			break;
			case R.id.time_button_1:
		    	showDialog(1);
			break;
			case R.id.date_button_2:
				showDialog(2);
			break;
			case R.id.time_button_2:
		    	showDialog(3);
			break;
	    }

    }
	
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
	    	case 0:
	    		return new DatePickerDialog(this, fromDateSetListener, fromYear, fromMonth, fromDay);
		    case 1:
				return new TimePickerDialog(this, fromTimeSetListener, fromHour, fromMinute, true);
		    case 2:
	    		return new DatePickerDialog(this, toDateSetListener, toYear, toMonth, toDay);
		    case 3:
				return new TimePickerDialog(this, toTimeSetListener, toHour, toMinute, true);
		}
    	return null;
    }
    
    private TimePickerDialog.OnTimeSetListener fromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			fromHour = hourOfDay;
			fromMinute = minute;
			updateFromDisplay();
		}
	};
	
	private DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			fromYear = year;
			fromMonth = monthOfYear;
			fromDay = dayOfMonth;
			updateFromDisplay();
		}
	};
	
	private TimePickerDialog.OnTimeSetListener toTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			toHour = hourOfDay;
			toMinute = minute;
			updateToDisplay();
		}
	};
	
	private DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			toYear = year;
			toMonth = monthOfYear;
			toDay = dayOfMonth;
			updateToDisplay();
		}
	};
	
	public void updateFromDisplay(){
		fromDisplay.setText(fromDay+"/"+fromMonth+"/"+fromYear+" "+fromHour+":"+fromMinute);
	}
	
	public void updateToDisplay(){
		toDisplay.setText(toDay+"/"+toMonth+"/"+toYear+" "+toHour+":"+toMinute);
	}
	
	public void updateFrom(){
		fromSelected.set(fromYear, fromMonth, fromDay, fromHour, fromMinute);
	}
	
	public void updateTo(){
		toSelected.set(toYear, toMonth, toDay, toHour, toMinute);
	}
	
	public void chartDemo(View view){

		if(uidSelect != -1) {
			
			MyAsyncTask mAsync = new MyAsyncTask();
			mAsync.execute();

		}else {
			Toast.makeText(getBaseContext(), "Parameters not valid", Toast.LENGTH_LONG).show();
		}
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) 
	    		getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    // if no network is available networkInfo will be null
	    // otherwise check if we are connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	} 
	
	/*
	 * Params: the type of the parameters sent to the task upon execution.
	 * Progress: the type of the progress units published during the background computation.
	 * Result: the type of the result of the background computation.
	 */
	private class MyAsyncTask extends AsyncTask<Void, Void, String> {
		
		@Override
        protected void onPreExecute(){
            mProgressDialog.show();
        }
		
		@Override
		protected String doInBackground(Void... params) {
			return postData();
		}
		
		@Override
		protected void onPostExecute(String result){
		    mProgressDialog.dismiss();
			Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();

			ReplayGraph replayGraph = new ReplayGraph(result, uidSelect);
			Intent intent = replayGraph.getIntent(getApplicationContext());

//			Intent intent = new Intent(getApplicationContext(), ChartDemo.class);
//			Intent intent = new Intent(getApplicationContext(), Prova.class);

			Log.w("tank", "lancio activity");
			startActivity(intent);
		}
		
		/*
		 * Contatta la servlet e gli invia i parametri della query replay
		 * Ritorna l'oggetto JSON contenente i punti da graficare
		 */
		public String postData() {
			// Create a new HttpClient and Post Header
			String server_addr = LoginActivity.ipAddress; //localhost
			String url = "https://"+server_addr+":8443/RestServlet/PulseSensor/getPointsFromDatabase.jsp";
			
			updateFrom();
			updateTo();
			
			Log.w("tank", ""+uidSelect);
			Log.w("tank", "from: "+fromSelected.getTimeInMillis());
			Log.w("tank", "to: "+toSelected.getTimeInMillis());
			
			String jsonR = "{uid:"+uidSelect+",dateFrom:"+
							fromSelected.getTimeInMillis()+
							",dateTo:"+toSelected.getTimeInMillis()+"}";
			
			JSONObject r = null;
			JSONArray jsonarray = null;
			String resultString = "";
			
			try {
				
				HttpClient httpclient = new SSLHttpClient(getBaseContext());
//				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httpPost = new HttpPost(url);
				httpPost.setHeader("Accept", "application/json");
//				httpPost.setHeader("Content-type", "application/json");
//				httpPost.setHeader("Accept-Encoding", "gzip"); // only set this parameter if you would like
				
//				r = new JSONObject(jsonR);
				
//				StringEntity se = new StringEntity(jsonR);

//				// Set HTTP parameters
//				httpPost.setEntity(se);
				
				//Alternativa per creare {uid: selectedUid, dateFrom: from_date, dateTo: to_date}
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("uid", ""+uidSelect ));
				nameValuePairs.add(new BasicNameValuePair("dateFrom", ""+fromSelected.getTimeInMillis()));
				nameValuePairs.add(new BasicNameValuePair("dateTo", ""+toSelected.getTimeInMillis()));

				// Set HTTP parameters
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); //, "UTF-8"));
				 
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpPost);
				
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
					// Read the content stream
					InputStream instream = entity.getContent();
					Header contentEncoding = response.getFirstHeader("Content-Encoding");
					if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
						instream = new GZIPInputStream(instream);
					}

					// convert content stream to a String
					resultString = convertStreamToString(instream);
					instream.close();
					resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

					// Transform the String into a JSONObject
//					JSONObject jsonObjRecv = new JSONObject(resultString);
					
					jsonarray = new JSONArray(resultString);
					
					// Raw DEBUG output of our received JSON object:
					Log.e("tank",""+jsonarray.length());

				} 
			 
			} catch (ClientProtocolException e) {
				
				e.printStackTrace();
				Log.e("tank","clientprotocolexception");
				
			} catch (IOException e) {
				
				e.printStackTrace();
				Log.e("tank","ioexception");
				
			} catch (JSONException e) {
				
				e.printStackTrace();
				Log.e("tank","jsonexception");
			
			}
			
			Log.w("tank", "prima di ritornare l'arrayjson");
			return resultString;
		}
		
		private String convertStreamToString(InputStream is) {
			/*
			* To convert the InputStream to String we use the BufferedReader.readLine()
			* method. We iterate until the BufferedReader return null which means
			* there's no more data to read. Each line will appended to a StringBuilder
			* and returned as String.
			*
			* (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
			*/
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}
 
	}
}
