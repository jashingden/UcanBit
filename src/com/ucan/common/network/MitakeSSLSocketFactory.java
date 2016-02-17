package com.ucan.common.network;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @version 20141103[李欣駿]	1.修正SSL 3.0的設計漏洞。<br>
 * 說明:<br/>
 * 在2014年10月，Google發布在SSL 3.0中發現設計缺陷。
 * 攻擊者可以向TLS發送虛假錯誤提示，然後將安全協定強行降級到古老的SSL 3.0，然後就可以利用其中的設計漏洞竊取資料。
 * http://stackoverflow.com/questions/16531807/android-client-server-on-tls-v1-2
 * @version 20141111[李欣駿]	1.將TLS呼叫方式改回去,因為有手機不支援。<br>
 * @author 李欣駿
 */

public class MitakeSSLSocketFactory extends SSLSocketFactory
{
	private SSLContext sslContext;

	public MitakeSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
	{
		super(null);

		sslContext = SSLContext.getInstance("TLS");
//		sslContext = SSLContext.getInstance("TLSv1.2");
		
		TrustManager tm = new X509TrustManager()
		{
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
			}

			public X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}
		};

		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException
	{
		return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		
//		SSLSocket s = (SSLSocket)sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//        s.setEnabledProtocols(new String[] {"TLSv1.2"});
//        return s;
	}

	@Override
	public Socket createSocket() throws IOException
	{
		return sslContext.getSocketFactory().createSocket();
	}
}