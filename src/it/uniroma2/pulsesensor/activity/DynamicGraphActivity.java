package it.uniroma2.pulsesensor.activity;

import it.uniroma2.pulsesensor.graph.MockData;
import it.uniroma2.pulsesensor.graph.Point;
import it.uniroma2.pulsesensor.graph.RealGraph;
import it.uniroma2.pulsesensor.secure.SSLHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

import org.achartengine.GraphicalView;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class DynamicGraphActivity extends Activity {

	private static GraphicalView view;
	private RealGraph real = new RealGraph();
	private static Thread thread;
	private Point point; 
//	private MyAsyncTask mAsync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Looper.prepare();
				getPoint();
				Looper.loop();
			}
		}){
			/*
			public void run() {
				
				for (int i = 0; i < 50; i++) {
	
					try {
						//TODO impostare refresh
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					Point p = MockData.getDataFromReceiver(i); //We got new data!
					if(point != null)
						real.addNewPoint(point);
					view.repaint();
					
				}
			}
			*/
		};
		
		thread.start();
	}

	@Override
	protected void onStart() {
		super.onStart();
		view = real.getView(this);
		setContentView(view);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		thread.stop();
		super.onBackPressed();
	}
	
	public void getPoint(){
		
		String result = "";
		JSONArray jarr;
		
		for(;;) {
			result = postData();
			
			try {
				jarr = new JSONArray(result);
				JSONObject jobj = jarr.getJSONObject(0);
				double x = jobj.getLong("timestamp");
				double y = jobj.getDouble("value");
				point = new Point(x, y);
				
				if(point != null)
					real.addNewPoint(point);

			
				view.repaint();
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Contatta la servlet e gli invia i parametri della query replay
	 * Ritorna l'oggetto JSON contenente i punti da graficare
	 */
	public String postData() {
		// Create a new HttpClient and Post Header
		String server_addr = LoginActivity.ipAddress; //localhost
		String url = "https://"+server_addr+":8443/RestServlet/PulseSensor/getPointsFromShared.jsp";
//		String url = "http://"+server_addr+":8080/RestServlet/examples/getPointsFromShared.jsp";
		
		
		JSONObject r = null;
		JSONArray jsonarray = null;
		String resultString = "";
		
		try {
			
			HttpClient httpclient = new SSLHttpClient(getBaseContext());

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");
//			httpPost.setHeader("Content-type", "application/json");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("uid", ""+2 ));

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
//				JSONObject jsonObjRecv = new JSONObject(resultString);
				
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
	
	public String convertStreamToString(InputStream is) {
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
	
	/*
	private class MyExecutor implements Executor{

		@Override
		public void execute(Runnable command) {
			// TODO Auto-generated method stub
			getPoint();
		}
		
		
		
	}
	*/

}
