package com.ucan.common.utility;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.view.Surface;

/**
 * @author eddyteng
 */
public class AutoRotateScreenUtility {

	public final static int ROTATE_FLAG_AUTO = 0;
	public final static int ROTATE_FLAG_MANUAL = 1;
	
	public final static int ROTATE_FLAG_MAX = 2;
	
	private Uri mUri;
	private Handler mHandler;
	private OnAutoRotateScreenListener mListener;
	private AutoRotateScreenUtility.Observer mObserver;
	
	public AutoRotateScreenUtility(OnAutoRotateScreenListener listener) {
		/**
		 * content://settings/system/accelerometer_rotation
		 */
		mUri = Settings.System.CONTENT_URI.buildUpon().appendEncodedPath(Settings.System.ACCELEROMETER_ROTATION).build();
		
		mHandler = new Handler();
		mListener = listener;
		mObserver = new Observer(mHandler);
	}
	
	public void addAutoRotateScreenListener(Activity activity)
	{
		ContentResolver resolver = activity.getContentResolver();
		resolver.registerContentObserver(mUri, false, mObserver);
	}
	
	public void removeAutoRotateScreenListener(Activity activity)
	{
		ContentResolver resolver = activity.getContentResolver();
		resolver.unregisterContentObserver(mObserver);
	}
	
	public static void checkActivityOrientation(Activity activity)
	{
		int rotateFlag = getAutoRotateScreenFlag(activity);
		if (isAutoRotateScreen(activity) == false)
		{
			// 調整Flag和系統設定的值為一致
			rotateFlag = ROTATE_FLAG_MANUAL;
			setAutoRotateScreenFlag(activity, rotateFlag);
		}
		
		if (rotateFlag != ROTATE_FLAG_AUTO)
		{
			int requestedOrientation = getActivityOrientation(activity);
			activity.setRequestedOrientation(requestedOrientation);
		}
	}
	
	public static int getActivityOrientation(Activity activity)
	{
		int orientation = activity.getResources().getConfiguration().orientation;
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		
		// 一般來說, 平板的"natural" orientation為橫式(Landscape)
		boolean naturalLand = true;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE &&
			rotation != Surface.ROTATION_0 && rotation != Surface.ROTATION_180)
		{
			naturalLand = false;
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT &&
				 rotation != Surface.ROTATION_90 && rotation != Surface.ROTATION_270)
		{
			naturalLand = false;
		}

		// 以逆時針方向計算角度(counter-clockwise direction)
		if (naturalLand)
		{
			switch (rotation)
			{
			case Surface.ROTATION_0:
				return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			case Surface.ROTATION_180:
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
			case Surface.ROTATION_90:
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
			case Surface.ROTATION_270:
				return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			default:
				return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			}
		}
		else
		{
			switch (rotation)
			{
			case Surface.ROTATION_0:
				return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			case Surface.ROTATION_180:
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
			case Surface.ROTATION_90:
				return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			case Surface.ROTATION_270:
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
			default:
				return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			}
		}
	}
	
	public static void setAutoRotateScreenFlag(Activity activity, int rotateFlag)
	{
		if (rotateFlag >= 0 && rotateFlag < ROTATE_FLAG_MAX)
		{
	    	String val = String.valueOf(rotateFlag);
	    	CommonUtility.saveFile(activity, "autoRotateScreen", CommonUtility.readBytes(val));
		}
    }
    
	public static int getAutoRotateScreenFlag(Activity activity)
	{
  	  	byte[] rotateFlag = CommonUtility.loadFile(activity, "autoRotateScreen");
  	  	if (null != rotateFlag)
  	  		return Integer.parseInt(CommonUtility.readString(rotateFlag));
  	  	else
  	  	{
  	  		int flag = isAutoRotateScreen(activity) ? ROTATE_FLAG_AUTO : ROTATE_FLAG_MANUAL;
  	  		setAutoRotateScreenFlag(activity, flag);
  	  		return flag;
  	  	}
    }
	
	/**
	 * 讀取系統設定的"自動旋轉螢幕"的值,
	 * (註: 目前實測結果, 硬體的螢幕鎖定開關也是修改這個值)
	 */
	public static boolean isAutoRotateScreen(Activity activity)
	{
		try
		{
	    	ContentResolver resolver = activity.getContentResolver();
	    	int rotation = Settings.System.getInt(resolver, Settings.System.ACCELEROMETER_ROTATION);
	    	return rotation == 1;
		}
		catch (Exception ex)
		{
			return true;
		}
	}
	
	public static void restoreAutoRotateScreen(Activity activity)
	{
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}
	
	public static void setAutoRotateScreen(Activity activity, boolean bAuto)
	{
//    	ContentResolver resolver = activity.getContentResolver();
//    	Settings.System.putInt(resolver, Settings.System.ACCELEROMETER_ROTATION, bAuto ? 1 : 0);
//    	restoreAutoRotateScreen(activity);
		if (bAuto)
		{
			if (isAutoRotateScreen(activity))
			{
				restoreAutoRotateScreen(activity);
				setAutoRotateScreenFlag(activity, ROTATE_FLAG_AUTO);
			}
		}
		else
		{
			setAutoRotateScreenFlag(activity, ROTATE_FLAG_MANUAL);
			int requestedOrientation = getActivityOrientation(activity);
			activity.setRequestedOrientation(requestedOrientation);
		}
	}
	
	public static void rotateScreen(Activity activity)
	{
		activity.setRequestedOrientation(CommonUtility.isPortrait(activity) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
																			   ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public static void rotateScreenLeft(Activity activity)
	{
		int currentOrientation = getActivityOrientation(activity);
		switch (currentOrientation)
		{
		case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
			break;
		case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
			break;
		case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		}
	}
	
	public static void rotateScreenRight(Activity activity)
	{
		int currentOrientation = getActivityOrientation(activity);
		switch (currentOrientation)
		{
		case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
			break;
		case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
			break;
		case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		}
	}
	
	
	class Observer extends ContentObserver {

		public Observer(Handler handler) {
			super(handler);
		}
	
		@Override
		public void onChange(boolean selfChange) {
			if (mListener != null)
				mListener.onAutoRotateScreenChange();
		}
	}
	
	public static interface OnAutoRotateScreenListener {
		public void onAutoRotateScreenChange();
	}
	
}

