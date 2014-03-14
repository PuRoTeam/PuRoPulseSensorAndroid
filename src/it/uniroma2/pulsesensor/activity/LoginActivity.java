package it.uniroma2.pulsesensor.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

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

import it.uniroma2.pulsesensor.secure.SHA256;
import it.uniroma2.pulsesensor.secure.SSLHttpClient;
import it.uniroma2.pulsesensorchart.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	private ProgressDialog mProgressDialog;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	private SharedPreferences prefs;
	public static String ipAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		ipAddress = prefs.getString("ip", "");
		Toast.makeText(this, "###"+ipAddress, Toast.LENGTH_LONG).show();

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.user);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);

		mProgressDialog = new ProgressDialog(this);
    	mProgressDialog.setIndeterminate(false);
    	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
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
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(getApplicationContext(), Settings.class);
			startActivityForResult(intent, 0);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, String> {
		
		private JSONObject jsonObj;
		
		@Override
		protected void onPreExecute() {
			mProgressDialog.show();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// attempt authentication against a network service.
			Log.e("tank", "postdata");
			return postData();
		}

		@Override
		protected void onPostExecute(final String result) {
			mAuthTask = null;
			mProgressDialog.dismiss();
			Log.w("onpostexe", result);
			try {
				this.jsonObj = new JSONObject(result);
				boolean success = Boolean.parseBoolean(jsonObj.getString("res"));
				Log.w("onpostexe", ""+success);
				if (success) {
					Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
					startActivity(intent);
				} else {
					mPasswordView.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

//		@Override
//		protected void onCancelled() {
//			mAuthTask = null;
//			mProgressDialog.dismiss();
//		}
	}
	
	/*
	 * Contatta la servlet e gli invia i parametri della query replay
	 * Ritorna l'oggetto JSON contenente i punti da graficare
	 */
	public String postData() {
		// Create a new HttpClient and Post Header
		String server_addr = LoginActivity.ipAddress;
		String url = "https://"+server_addr+":8443/RestServlet/PulseSensor/androidLogin.jsp";
//		String url = "https://"+server_addr+":8443/RestServlet/examples/androidLogin.jsp";
		
		String resultString = "";
		
		try {
			
			HttpClient httpclient = new SSLHttpClient(getBaseContext());

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("sUserName", mEmail ));
			String hashPass = SHA256.getMsgDigest(mPassword);
			Log.w("hash", hashPass);
			nameValuePairs.add(new BasicNameValuePair("sPwd", hashPass));

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

				Log.w("result", resultString);

				// Raw DEBUG output of our received JSON object:
			} 
		 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("tank","clientprotocolexception");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("tank","ioexception");
		}
		
		Log.w("tank", "prima di ritornare il json");
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

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
			Log.w("tank","passwordlenght");
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mAuthTask = new UserLoginTask();
			mAuthTask.execute();
		}
	}
}
