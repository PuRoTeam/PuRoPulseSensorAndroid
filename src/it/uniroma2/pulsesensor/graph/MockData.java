package it.uniroma2.pulsesensor.graph;


import it.uniroma2.pulsesensor.activity.HomeActivity;
import it.uniroma2.pulsesensor.secure.SSLHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class MockData {
	
	public static Point getDataFromReceiver(int x)
	{
		return new Point(x, generateRandomData());
	}
	
	private static int generateRandomData()
	{
		Random random = new Random();
		return random.nextInt(40);
	}
	
}
