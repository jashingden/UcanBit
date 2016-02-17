package com.ucan.common.network;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class MitakeHttpClient
{
    private MitakeHttpClient() {}
    
    public static synchronized HttpClient getInstance() throws IOException, KeyManagementException, KeyStoreException, KeyManagementException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException
    {
    	int connectTimeout = 30000;
    	int connectManagerTimeout = 30000;
    	int socketTimeout = 30000;
    	
    	ThreadSafeClientConnManager threadSafeClientConnManager = null;
        // 初始化工作
    	HttpParams httpParams = new BasicHttpParams();

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(httpParams, false);
        HttpConnectionParams.setConnectionTimeout(httpParams, connectTimeout);
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        
        ConnManagerParams.setTimeout(httpParams, connectManagerTimeout);
        
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        MitakeSSLSocketFactory mitakeSSLSocketFactory = new MitakeSSLSocketFactory();
        mitakeSSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        schemeRegistry.register(new Scheme("https", mitakeSSLSocketFactory, 443));
        
        threadSafeClientConnManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        return new DefaultHttpClient(threadSafeClientConnManager, httpParams);
    }
}
