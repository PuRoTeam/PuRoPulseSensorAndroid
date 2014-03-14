package it.uniroma2.pulsesensor.secure;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class SSLHttpClient extends DefaultHttpClient {
    
	final Context context;
 
    public SSLHttpClient(Context context) {
        this.context = context;
    }
 
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        
        try {
			String defaultType = KeyStore.getDefaultType();
			KeyStore trustedStore = KeyStore.getInstance(defaultType);
//			InputStream certificateStream = context.getAssets().open("keystore_tank.bks");
			InputStream certificateStream = context.getAssets().open("keystore_pup.bks");
			
			try {
				trustedStore.load(certificateStream, "sii_2013".toCharArray());
				certificateStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

				SSLSocketFactory sslSocketFactory = new SSLSocketFactory(trustedStore);
				sslSocketFactory.setHostnameVerifier((X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
 
				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
				registry.register(new Scheme("https", sslSocketFactory, 8443));
 
				return new SingleClientConnManager(getParams(), registry);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        return null;
    }
    
}
