package it.uniroma2.pulsesensor.secure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
 
public class RestClient extends HttpURLConnection {
 
	protected RestClient(URL url) {
		super(url);
		// TODO Auto-generated constructor stub
	}

	// http://localhost:8080/RESTfulExample/json/product/get
	public void df(String[] args) {
		
		String server_addr = "192.168.1.101"; //localhost
		
		try {
 
			URL url = new URL("http://"+server_addr+":8080/RestServelet/getPointsFromDatabase.jsp");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			 
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
	 
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	 
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			 
			conn.disconnect();
			 
		  } catch (MalformedURLException e) {
	 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
	 
		  }
	}
		
	public void writeJSON() {
		JSONObject object = new JSONObject();
		try {
		    object.put("name", "Jack Hack");
		    object.put("score", new Integer(200));
		    object.put("current", new Double(152.32));
		    object.put("nickname", "Hacker");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(object);
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean usingProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connect() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
//	public void upload(){
//		
//		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//		try {
//			urlConnection.setDoOutput(true);
//			urlConnection.setChunkedStreamingMode(0);
//
//			OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//			writeStream(out);
//
//			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//			readStream(in);
//			finally {
//				urlConnection.disconnect();
//			}
//		}
//	}
	
//	public void sendGet() {
//		
//		try {
//			HttpClient httpClient = new SSLHttpClient(ctx);
//			HttpGet httpPost = new HttpGet(strUrl);
//			HttpResponse httpResponse = httpClient.execute(httpPost);
//			HttpEntity httpEntity = httpResponse.getEntity();
//			String JSONResp = EntityUtils.toString(httpEntity);
//			
//			JSONArray arr = new JSONArray(JSONResp);
//			JSONObject obj = arr.getJSONObject(0);
//			
//			boolean bError = obj.getBoolean("error");
//			
//			if(bError){
//				int iErrorCode = obj.getInt("code");
//				String strErrorMsg = obj.getString("message");
//				
//			}
//			
//			else{
//				ArrayList<String> alResp = new ArrayList<String>();
//				alResp.add("");
//				alResp.add(obj.getString("message"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	//pagina 177
	public static String doPost(String urlString, Map<String, String> nameValuesPairs) 
		throws IOException
	{
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		
		PrintWriter out = new PrintWriter(connection.getOutputStream());
		
		Scanner in;
		StringBuilder response = new StringBuilder();
		try {
			in = new Scanner(connection.getInputStream());
		}
		catch (IOException e) {
			if(!(connection instanceof HttpURLConnection)) throw e;
			InputStream err = ((HttpURLConnection)connection).getErrorStream();
			if(err == null) throw e;
			in = new Scanner(err);
		}
		while(in.hasNextLine())
		{
			response.append(in.nextLine());
			response.append("\n");
		}
		
		in.close();
		return response.toString();
	}
	
	
	
 
}
