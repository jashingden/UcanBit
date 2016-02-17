package com.ucan.common.utility;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加解密元件
 * @author 李欣駿
 */
public class DESUtility
{
	public static String encryptDES(String encryptString, String encryptKey, boolean urlEncoded) throws Exception
	{
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("UTF8"), "DES");

		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF8"));

		String result = new Base64().encode(encryptedData);
		
		return urlEncoded ? URLEncoder.encode(result, "UTF-8") : result;
	}

	public static String decryptDES(String decryptString, String decryptKey) throws Exception
	{
		byte[] byteMi = new Base64().decode(decryptString);

		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("UTF8"), "DES");
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData);
	}
}
