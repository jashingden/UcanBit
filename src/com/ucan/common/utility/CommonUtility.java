package com.ucan.common.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.ucan.ucanbit.R;

/**
 * @author eddyteng
 */
public class CommonUtility
{
    private static Properties messageProperties;
	private static Properties configProperties;

	public static String readString(byte[] b)
	{
		return b == null?null:readString(b, 0, b.length);
	}

    public static SimpleDateFormat pullRefreshDateFormat = new SimpleDateFormat("MM-dd hh:mm");

	public static String readString(byte[] b, int position, int len)
	{
		try
		{
			return new String(b, position, len, "UTF-8").trim();
		}
		catch(UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
			return "";
		}
		catch(RuntimeException ex)
		{
			ex.printStackTrace();
			return "";
		}
	}

	public static byte[] readBytes(String src)
	{
		try
		{
			return src.getBytes("UTF-8");
		}
		catch(UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 讀取儲存於手機端的Properties檔案
	 * @param context
	 * @param propertiesID
	 * @param properties
	 */
	private static void loadProperties(Context context, int propertiesID, Properties properties)
	{
		InputStream is = context.getResources().openRawResource(propertiesID);

		if(Build.VERSION.SDK_INT >= 9)
		{
			InputStreamReader isr = null;

			try
			{
				isr = new InputStreamReader(is, "UTF-8");
			}
			catch (UnsupportedEncodingException e1)
			{}

			try
			{
				if(isr == null)
				{
					properties.load(is);
				}
				else
				{
					properties.load(isr);
				}
			}
			catch (IOException e)
			{}
		}
		else
		{
			try
			{
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] data = new byte[4096];
				int count = -1;

				while((count = is.read(data, 0, 4096)) != -1)
				{
					outStream.write(data, 0, count);
				}

				convertPropertiesEncode(properties, readString(outStream.toByteArray()));
			}
			catch (IOException e)
			{}
		}
	}

	/**
	 * 讀取文字設定檔
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public static synchronized Properties getMessageProperties(Context context)
	{
		if(messageProperties == null || messageProperties.size() <= 0)
		{
            if(messageProperties == null)
            {
                messageProperties = new Properties();
            }

			loadProperties(context, R.raw.message, messageProperties);
			/**
			 * 20141113[李欣駿]
			 * 加入可讀入MSG.txt檔案來替換原先儲存於手機端內的message.properties的訊息機制。
			 */
			byte[] b = loadFile(context, "MSG.txt");

			if(b != null)
			{
				String data = readString(b);
				String[] rows = data.split("\r\n");
				String[] tmp = null;

				for(int i = 0; i < rows.length; i++)
				{
					tmp = rows[i].split(";");

					if(tmp.length == 2)
					{
						messageProperties.setProperty(tmp[0], tmp[1]);
					}
				}
			}
    	}

		return messageProperties;
	}

	/**
	 * 將所有設定檔讀入ConfigProperties內
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public static synchronized Properties getConfigProperties(Context context)
	{
		if(configProperties == null || configProperties.size() <= 0)
		{
            if(configProperties == null)
            {
                configProperties = new Properties();
            }

			loadProperties(context, R.raw.config, configProperties);
		}

		return configProperties;
	}

	public static boolean existFile(Context context, String name)
	{
		boolean isExist = false;

		try
		{
			FileInputStream fis = context.openFileInput(name);
			if(fis.available() > 0)
			{
				isExist = true;
			}

			fis.close();
			return isExist;
		}
		catch(FileNotFoundException e)
		{
			return isExist;
		}
		catch(IOException e)
		{
			return isExist;
		}
		catch (NullPointerException e)
		{
			return isExist;
		}
	}

	public static byte[] loadFile(Context context, String name)
	{
		try
		{
			FileInputStream fis = context.openFileInput(name);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			return b;
		}
		catch(FileNotFoundException e)
		{
			return null;
		}
		catch(IOException e)
		{
			return null;
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	public static boolean deleteFile(Context context, String name)
	{
		return context.deleteFile(name);
	}

	/**
	 * 重登
	 */
	public static void clearAll()
	{
        /**
         * 20150316[eddyteng] 為避免外面已有人取得reference, 故改用clear的方式清除內容
         */
		if (messageProperties != null) {
            messageProperties.clear();
        }
		if (configProperties != null) {
            configProperties.clear();
        }
	}

	private static void convertPropertiesEncode(Properties properties,String data) {
		String [] temp = data.split("\r\n");
		int index;
		String tmp1;
		String tmp2;
		for(int i=0;i<temp.length;i++) {
			index=temp[i].indexOf("=");
			if(index==-1)
				continue;
			tmp1=temp[i].substring(0, index);
			tmp2=temp[i].substring(index+1, temp[i].length());
			properties.setProperty(tmp1,tmp2);
		}
	}

	/**
	 * 儲存資訊
	 * @param context
	 * @param name
	 * @param b
	 * @return <li>true:儲存成功<li>false:儲存失敗
	 */
	public static boolean saveFile(Context context, String name, byte[] b)
	{
		try
		{
			FileOutputStream fos = context.openFileOutput(name, Context.MODE_WORLD_WRITEABLE);
			fos.write(b);
			fos.flush();
			fos.close();
			return true;
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判斷螢幕直/橫
	 * @param activity
	 * @return <li>true:直<li>false:橫
	 */
	public static boolean isPortrait(Activity activity)
	{
		Configuration config = activity.getResources().getConfiguration();
		return (config.orientation == Configuration.ORIENTATION_PORTRAIT);
	}

	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_PRIVATE);
	}

	/**
	 * 手動關閉鍵盤,如果鍵盤已經關閉就不動作
	 * 
	 * @param v
	 */
	public static void hiddenKeyboard(Activity activity,View v) {
		// 取得輸入鍵盤狀態
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		/**
		 * 6640-20120914[李欣駿]
		 * 如果傳入的View是null,就不執行收鍵盤的動作。
		 */
		if(null != v){
			inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

    /**
     * 判斷手機端網路是否開通
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && true == networkInfo.isAvailable() && true == networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 確認是否為Wifi
     * @param context
     * @return
     */
    public static boolean checkNetworkInfoTypeIsWifi(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && true == networkInfo.isAvailable() && true == networkInfo.isConnected())
        {
            return ConnectivityManager.TYPE_WIFI == networkInfo.getType();
        }
        else
        {
            return false;
        }
    }
}