package com.ucan.common.network;

import android.os.Process;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Http Post
 * @version 20141211[李欣駿]	加入解壓縮功能。
 * @author 李欣駿
 */
public class MitakeHttpPost implements Runnable
{
	private MitakeHttpParams params;
	
	public MitakeHttpPost(MitakeHttpParams params)
	{
		this.params = params;
	}
	
	public void run()
	{
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

	    HttpPost httpPost = new HttpPost(params.url);

        if(null != params.headers)
        {
            for(int i = 0 ; i < params.headers.length ; i++)
            {
                httpPost.addHeader(params.headers[i][0], params.headers[i][1]);
            }
        }

	    HttpClient httpClient = null;
	    HttpData httpData = new HttpData();
        httpData.requestKey = params.requestKey;

        try
		{	
			httpClient = MitakeHttpClient.getInstance();

			if(null != params.userAgent)
			{
				httpClient.getParams().setParameter("http.useragent", params.userAgent);
			}
			
			if(null != params.proxyIP)
			{
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(params.proxyIP, params.proxyPort));
			}
			
			if(MitakeHttpParams.C_S_DATA_TYPE_STRING == params.C2SDataType)
		    {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				
				for(Enumeration<String> e = params.kv.keys() ; e.hasMoreElements() ; )
				{
					String key = e.nextElement();
					String value = params.kv.get(key);
					
					nvps.add(new BasicNameValuePair(key, value));
				}
				
				httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
		    }
			else
			{
				ByteArrayEntity byteArrayEntity = new ByteArrayEntity(params.b);
			    byteArrayEntity.setContentType("multipart/form-data");
			    
				httpPost.setEntity(byteArrayEntity);
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			{
				httpData.code = HttpData.OK;
                /**
                 * 因Server會將部份資訊由Header帶入,故把Header內的值回傳
                 */
                Header timestamp = httpResponse.getFirstHeader("Timestamp");

                if(null != timestamp)
                {
                    httpData.timestamp = timestamp.getValue();
                }
				/**
				 * 判斷回傳資料是否有壓縮
				 */
				Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");

				if(null != contentEncoding && contentEncoding.getValue().equalsIgnoreCase("gzip"))
				{
					ByteArrayInputStream bais = new ByteArrayInputStream(EntityUtils.toByteArray(httpResponse.getEntity()));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					GZIPInputStream gzip = new GZIPInputStream(bais, 256);
				    
				    byte[] data = new byte[256];
				    int c;
				    
				    while((c = gzip.read(data)) != -1) 
				    {
				    	baos.write(data, 0, c);
				    }
				    
				    gzip.close();
				    bais.close();
					
					if(MitakeHttpParams.S_C_DATA_TYPE_STRING == params.S2CDataType)
					{
						httpData.data = new String(baos.toByteArray(), "utf-8");
					}
					else if(MitakeHttpParams.S_C_DATA_TYPE_BYTES == params.S2CDataType)
					{
						httpData.b = baos.toByteArray();
					}
				}
				else
				{
					if(MitakeHttpParams.S_C_DATA_TYPE_STRING == params.S2CDataType)
					{
						httpData.data = EntityUtils.toString(httpResponse.getEntity());
//                        Logger.log("回傳==" + httpData.data);
					}
					else if(MitakeHttpParams.S_C_DATA_TYPE_BYTES == params.S2CDataType)
					{
						httpData.b = EntityUtils.toByteArray(httpResponse.getEntity());
					}
				}

				params.callback.callback(httpData);
			}
			else 
			{
//                Logger.log("回傳錯誤==" + params.url + "==" + httpResponse.getStatusLine().getStatusCode() + "==" + httpResponse.getStatusLine().toString());
				params.callback.exception(params.requestKey, httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().toString());
			}
		}
		catch(ClientProtocolException e)
		{
			params.callback.exception(params.requestKey, -1, "ClientProtocolException");
		}
		catch(ParseException e)
		{
			params.callback.exception(params.requestKey, -2, "ParseException");
		}
        catch(ConnectTimeoutException e) 
        {
			params.callback.exception(params.requestKey, -3, "ConnectTimeoutException");
        }
        catch(SocketTimeoutException e) 
        {
			params.callback.exception(params.requestKey, -4, "SocketTimeoutException");
        }
		catch(IOException e)
		{
			params.callback.exception(params.requestKey, -5, "IOException");
		}
		catch(KeyManagementException e)
		{
			params.callback.exception(params.requestKey, -6, "KeyManagementException");
		}
		catch(KeyStoreException e)
		{
			params.callback.exception(params.requestKey, -7, "KeyStoreException");
		}
		catch(NoSuchAlgorithmException e)
		{
			params.callback.exception(params.requestKey, -8, "NoSuchAlgorithmException");
		}
		catch(CertificateException e)
		{
			params.callback.exception(params.requestKey, -9, "CertificateException");
		}
		catch(UnrecoverableKeyException e)
		{
			params.callback.exception(params.requestKey, -10, "UnrecoverableKeyException");
		}
        finally
        {
        	httpClient.getConnectionManager().shutdown();
        }
	}
}