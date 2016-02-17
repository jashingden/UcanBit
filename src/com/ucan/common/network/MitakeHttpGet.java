package com.ucan.common.network;

import android.os.Process;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;
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
import java.util.zip.GZIPInputStream;

/**
 * Http Get
 * @version 20141211[李欣駿]	加入解壓縮功能。
 * @author 李欣駿
 */
public class MitakeHttpGet implements Runnable
{
	private MitakeHttpParams params;
	
	public MitakeHttpGet(MitakeHttpParams params)
	{
		this.params = params;
	}
	
	public void run()
	{	
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		HttpGet httpGet = new HttpGet(params.url);
//        Logger.log("----------------------------------");
//        Logger.log("發送連結==" + params.url);

        if(null != params.headers)
        {
            for(int i = 0 ; i < params.headers.length ; i++)
            {
//                Logger.log("header----------------------------------");
//                Logger.log(params.headers[i][0] + "==" + params.headers[i][1]);
                httpGet.addHeader(params.headers[i][0], params.headers[i][1]);
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
			
			HttpResponse httpResponse = httpClient.execute(httpGet);

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

                        String apiName = null;
                        if(params.url.contains("/"))
						{
                            String str[] = params.url.split("/");
                            if(str!=null && str.length>0)
							{
                                apiName = str[str.length-1];
                            }
                        }

                        if(apiName == null)
						{
                            apiName = "";
                        }
//                        Logger.log("回傳資料 "+apiName+" == " + httpData.data);
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
            e.printStackTrace();
			params.callback.exception(params.requestKey, -1, "ClientProtocolException");
		}
		catch(ParseException e)
		{
            e.printStackTrace();
			params.callback.exception(params.requestKey, -2, "ParseException");
		}
        catch(ConnectTimeoutException e) 
        {
            e.printStackTrace();
			params.callback.exception(params.requestKey, -3, "ConnectTimeoutException");
        }
        catch(SocketTimeoutException e) 
        {
            e.printStackTrace();
			params.callback.exception(params.requestKey, -4, "SocketTimeoutException");
        }
		catch(IOException e)
		{
            e.printStackTrace();
			params.callback.exception(params.requestKey, -5, "IOException");
		}
		catch(KeyManagementException e)
		{
            e.printStackTrace();
			params.callback.exception(params.requestKey, -6, "KeyManagementException");
		}
		catch(KeyStoreException e)
		{
            e.printStackTrace();
			params.callback.exception(params.requestKey, -7, "KeyStoreException");
		}
		catch(NoSuchAlgorithmException e)
		{
			params.callback.exception(params.requestKey, -8, "NoSuchAlgorithmException");
		}
		catch(CertificateException e)
		{
            e.printStackTrace();
			params.callback.exception(params.requestKey, -9, "CertificateException");
		}
		catch(UnrecoverableKeyException e)
		{
            e.printStackTrace();
			params.callback.exception(params.requestKey, -10, "UnrecoverableKeyException");
		}
		catch(IllegalStateException e)
		{
            e.printStackTrace();
			params.callback.exception(params.requestKey, -11, "IllegalStateException");
		}
        finally
        {
        	httpClient.getConnectionManager().shutdown();
        }
	}
}