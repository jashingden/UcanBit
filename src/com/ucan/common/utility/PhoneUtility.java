package com.ucan.common.utility;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

import com.ucan.common.PhoneInfo;

/**
 * @author eddyteng
 */
public class PhoneUtility
{
	private static final String NOTHING = "無";
	private static final String PHONE_STATUS_SETTING_ON = "已開啟";
	private static final String PHONE_STATUS_SETTING_OFF = "未開啟";
	private static final String UNKNOW = "UNKNOW";

	public static class PhoneType {
	    public final static String UNKNOWN = "UNKNOWN";
	    public final static String CDMA = "CDMA";
	    public final static String GSM = "GSM";
	    public final static String NONE = "NONE";
	}
	
	public static class NetworkType
	{
	    public final static String UNKNOWN = "UNKNOWN";
	    public final static String EDGE = "EDGE";
	    public final static String GPRS = "GPRS";
	    public final static String UMTS = "UMTS";
	    public final static String _1xRTT = "1xRTT";
	    public final static String CDMA = "CDMA";
	    public final static String EVDO_0 = "EVDO_0";
	    public final static String EVDO_A = "EVDO_A";
	    public final static String HSDPA = "HSDPA";
	    public final static String HSPA = "HSPA";
	    public final static String HSUPA = "HSUPA";
	    public final static String IDEN = "IDEN";
	}
	
	public static class ScreenSizeType {
		public final static String SCREENLAYOUT_SIZE_XLARGE = "SCREENLAYOUT_SIZE_XLARGE";
		public final static String SCREENLAYOUT_SIZE_LARGE = "SCREENLAYOUT_SIZE_LARGE";
		public final static String SCREENLAYOUT_SIZE_NORMAL = "SCREENLAYOUT_SIZE_NORMAL";
		public final static String SCREENLAYOUT_SIZE_SMALL = "SCREENLAYOUT_SIZE_SMALL";
	}
	
	public static void init(Context context)
	{
		/*
		 * 取得手機資訊
		 */
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		/*
		 * 20120622駿的日記: 因部份亞太電信手機會無法取得SimOperator,但卻可以由NetworkOperator取得相同的值
		 */
		PhoneInfo.simOperator = telephonyManager.getSimOperator() == null ? "" : telephonyManager.getSimOperator();
		PhoneInfo.subscriberId = telephonyManager.getSubscriberId() == null ? "" : telephonyManager.getSubscriberId();
		PhoneInfo.networkOperator = telephonyManager.getNetworkOperator() == null ? "" : telephonyManager.getNetworkOperator();
		/*
		 * 取得手機號碼
		 */
		PhoneInfo.line1Number = telephonyManager.getLine1Number();
		/*
		 * 電信網路國別
		 */
		if(telephonyManager.getNetworkCountryIso().equals(""))
		{
			PhoneInfo.countryIso = NOTHING;
		}
		else
		{
			PhoneInfo.countryIso = telephonyManager.getNetworkCountryIso();
		}
		/*
		 * 電信公司代號
		 */
		if(telephonyManager.getNetworkOperator().equals(""))
		{
			PhoneInfo.operator = NOTHING;
		}
		else
		{
			PhoneInfo.operator = telephonyManager.getNetworkOperator();
		}
		/*
		 * 電信公司名稱
		 */
		if(telephonyManager.getNetworkOperatorName().equals(""))
		{
			PhoneInfo.operatorName = NOTHING;
		}
		else
		{
			PhoneInfo.operatorName = telephonyManager.getNetworkOperatorName();
		}
		/*
		 * 行動通訊類型
		 */
		if(PhoneInfo.isSupportCDMANetwork && telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA)
		{
			PhoneInfo.phoneType = PhoneType.CDMA;
		}
		else if(telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM)
		{
			PhoneInfo.phoneType = PhoneType.GSM;
		}
		else
		{
			PhoneInfo.phoneType = PhoneType.NONE;
		}
		/*
		 * 網路類型
		 */
		PhoneInfo.networkType = NetworkType.UNKNOWN;
		if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE)
		{
			PhoneInfo.networkType = NetworkType.EDGE;
		}
		else if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS)
		{
			PhoneInfo.networkType = NetworkType.GPRS;
		}
		else if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS)
		{
			PhoneInfo.networkType = NetworkType.UMTS;
		}

		if(PhoneInfo.isSupportCDMANetwork)
		{
			if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_1xRTT)
			{
				PhoneInfo.networkType = NetworkType._1xRTT;
			}
			else if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA)
			{
				PhoneInfo.networkType = NetworkType.CDMA;
			}
			else if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EVDO_0)
			{
				PhoneInfo.networkType = NetworkType.EVDO_0;
			}
			else if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EVDO_A)
			{
				PhoneInfo.networkType = NetworkType.EVDO_A;
			}
		}

		if(PhoneInfo.isSupportHSDPANetwork)
		{// ANDROID 2.0 AND LATER SUPPORT.
			if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA)
			{
				PhoneInfo.networkType = NetworkType.HSDPA;
			}
			else if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA)
			{
				PhoneInfo.networkType = NetworkType.HSPA;
			}
			else if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA)
			{
				PhoneInfo.networkType = NetworkType.HSUPA;
			}
		}

		if(PhoneInfo.isSupportIDENNetwork)
		{
			if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_IDEN)
			{
				PhoneInfo.networkType = NetworkType.IDEN;
			}
		}

		/*
		 * 漫遊狀態
		 */
		if(telephonyManager.isNetworkRoaming())
		{
			PhoneInfo.networkRoaming = PHONE_STATUS_SETTING_ON;
		}
		else
		{
			PhoneInfo.networkRoaming = PHONE_STATUS_SETTING_OFF;
		}

		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// IMEI
		String imei = telephonyManager.getDeviceId();

		if(null == imei || imei.length() < 10)
		{
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			imei = wifiInfo.getMacAddress();

			if(null == imei || imei.length() < 10)
			{
				imei = "Mitake" + System.currentTimeMillis();
			}
		}
		PhoneInfo.imei = imei;

		/*
		 * 20110608駿的日記: 有Android機器無法取得IMEI,即用MAC Address來取代
		 * 但因有些平板電腦回傳IMEI為非null,如N/A,所以加強判斷IMEI長度
		 */
		if(null == PhoneInfo.imei || PhoneInfo.imei.length() < 10)
		{
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			PhoneInfo.imei = wifiInfo.getMacAddress();
		}

		/*
		 * IMEI SV
		 */
		PhoneInfo.imeiSV = telephonyManager.getDeviceSoftwareVersion() == null ? "" : telephonyManager.getDeviceSoftwareVersion();
		/*
		 * IMSI
		 */
		PhoneInfo.imsi = telephonyManager.getSubscriberId() == null ? "" : telephonyManager.getSubscriberId();

		/*
		 * Bluetooth
		 */
		ContentResolver cv = context.getContentResolver();
		String bt = Settings.System.getString(cv, Settings.System.BLUETOOTH_ON);
		if(null != bt)
		{
			if(bt.equals("1"))
			{
				PhoneInfo.bluetooth = PHONE_STATUS_SETTING_ON;
			}
			else
			{
				PhoneInfo.bluetooth = PHONE_STATUS_SETTING_OFF;
			}
		}
		else
		{
			PhoneInfo.bluetooth = UNKNOW;
		}

		/*
		 * 20111205駿的日記: 先記錄程式啟動時,手機端wifi是否有開啟 以防電信版時,使用者操作到一半就變更連網方式
		 * 20130610[李欣駿] 取消此種確認連線是否走Wifi的方式,因為手機端會自動切換連線模式
		 */
		PhoneInfo.wifiStatus = wifiManager.isWifiEnabled();
		/*
		 * WiFi 狀態
		 */
		PhoneInfo.wifiStatusText = PhoneInfo.wifiStatus ? PHONE_STATUS_SETTING_ON : PHONE_STATUS_SETTING_OFF;
		/*
		 * 飛行模式
		 */
		String air = Settings.System.getString(cv, Settings.System.AIRPLANE_MODE_ON);
		if(null != air)
		{
			PhoneInfo.airMode = air.equals("1") ? PHONE_STATUS_SETTING_ON : PHONE_STATUS_SETTING_OFF;
		}
		else
		{
			PhoneInfo.airMode = UNKNOW;
		}
		/*
		 * 數據漫遊
		 */
		String ro = Settings.System.getString(cv, Settings.System.DATA_ROAMING);
		if(null != ro)
		{
			PhoneInfo.dataRoaming = ro.equals("1") ? PHONE_STATUS_SETTING_ON : PHONE_STATUS_SETTING_OFF;
		}
		else
		{
			PhoneInfo.dataRoaming = UNKNOW;
		}

		/*
		 * 手機IP
		 */
		PhoneInfo.clientIp = getLocalIpAddress();
		
		Configuration config = context.getResources().getConfiguration();
		int size = config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if(size == Configuration.SCREENLAYOUT_SIZE_XLARGE)
		{
			PhoneInfo.screenLayoutSize = ScreenSizeType.SCREENLAYOUT_SIZE_XLARGE;
		}
		else if(size == Configuration.SCREENLAYOUT_SIZE_LARGE)
		{
			PhoneInfo.screenLayoutSize = ScreenSizeType.SCREENLAYOUT_SIZE_LARGE;
		}
		else if(size == Configuration.SCREENLAYOUT_SIZE_NORMAL)
		{
			PhoneInfo.screenLayoutSize = ScreenSizeType.SCREENLAYOUT_SIZE_NORMAL;
		}
		else if(size == Configuration.SCREENLAYOUT_SIZE_SMALL)
		{
			PhoneInfo.screenLayoutSize = ScreenSizeType.SCREENLAYOUT_SIZE_SMALL;
		}

	}

	/**
	 * 取得網路IP
	 * 
	 * @return
	 */
	public static String getLocalIpAddress()
	{
		try
		{
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if(!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch(SocketException ex)
		{
		}
		return null;
	}

	/**
	 * 取得螢幕解析度
	 * 
	 * @param activity
	 * @return
	 */
	public static float getDpi(Activity activity)
	{
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	/**
	 * 取得系統時間
	 * 
	 * @param format
	 *            <li>yyyyMMddhhmmss
	 * @return
	 */
	public static String getSystemDate(String format)
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 20110517駿的日記: 取得手機端各項音量設定值 通話音量:AudioManager.STREAM_VOICE_CALL
	 * 系统音量:AudioManager.STREAM_SYSTEM 鈴聲音量:AudioManager.STREAM_RING
	 * 音樂音量:AudioManager.STREAM_MUSIC 提示聲音音量:AudioManager.STREAM_ALARM
	 * 
	 * @param context
	 * @return
	 */
	public static int getPhoneVolume(final Context context, final int mediaType)
	{
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		// int max = am.getStreamMaxVolume(mediaType);
		return am.getStreamVolume(mediaType);
	}

	/**
	 * 20140923 simon 判斷是否有電話功能
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isPhone(Context context)
	{
		PackageManager pm = context.getPackageManager();
		boolean isAPhone = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
		return isAPhone;
	}
	
	/**
	 * 震動功能
	 */
	public static void vibrate(Context context) {
		Vibrator myVibrate = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		// Do a short (100ms) vibration.
		myVibrate.vibrate(500);
	}

}