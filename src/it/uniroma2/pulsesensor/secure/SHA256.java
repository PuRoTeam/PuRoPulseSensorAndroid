package it.uniroma2.pulsesensor.secure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class SHA256 
{	
	public static String getMsgDigest(String msg)
	{
		byte byteDigest[] = getByteMsgDigest(msg);
		String msgDigest = new String(Hex.encodeHex(byteDigest));
        		
        return msgDigest;
	}
	
	public static byte[] getByteMsgDigest(String msg)
	{
        MessageDigest md;
        byte byteDigest[] = null;
		try 
		{
			md = MessageDigest.getInstance("SHA-256");
			md.update(stringToBytes(msg)); 
			byteDigest = md.digest();
		}
		catch (NoSuchAlgorithmException e) 
		{ e.printStackTrace(); }
        
        return byteDigest;
	}
	
	public static byte[] stringToBytes(String s)
	{
		byte[] a = new byte[s.length()];
		for(int i = 0; i < s.length(); i++)		
			a[i] = (byte)s.charAt(i);
		return a;
	} 
    
}
