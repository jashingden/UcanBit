package com.ucan.common;

import android.os.Build;
import android.os.Bundle;

public class PhoneInfo
{
    /**
     * The name of the industrial design
     */
    public final static String device = Build.DEVICE;
    /**
     * The end-user-visible name for the end product
     */
    public final static String model = Build.MODEL;
    /**
     * The name of the overall product
     */
    public final static String product = Build.PRODUCT;
    /**
     * The user-visible SDK version of the framework; its possible values are defined in {@link android.os.Build.VERSION_CODES http://developer.android.com/reference/android/os/Build.VERSION_CODES.html}
     */
    public final static int sdkVersionCode = Build.VERSION.SDK_INT;
    /**
     * Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal digits.
     */
    public static String simOperator;
    /**
     * Returns the unique subscriber ID, for example, the IMSI for a GSM phone. Return null if it is unavailable.
     */
    public static String subscriberId;
    /**
     * Returns the numeric name (MCC+MNC) of current registered operator.
     */
    public static String networkOperator;
    /**
     * 取得手機號碼
     */
    public static String line1Number;
    /**
     * 電信網路國別
     */
    public static String countryIso;
    /**
     * 電信公司代號
     */
    public static String operator;
    /**
     * 電信公司名稱
     */
    public static String operatorName;
    /**
     * 網路類型
     */
    public static String networkType;
    /**
     * 行動通訊類型
     */
    public static String phoneType;
    /**
     * 漫遊狀態
     */
    public static String networkRoaming;
    /**
     * IMEI<br>Note:有Android機器無法取得IMEI,即用MAC Address來取代
     */
    public static String imei;
    /**
     * IMEI SV
     */
    public static String imeiSV;
    /**
     * IMSI
     */
    public static String imsi;
    /**
     * Bluetooth
     */
    public static String bluetooth;
    /**
     * Wifi狀態
     */
    public static boolean wifiStatus;
    /**
     * 飛航模式
     */
    public static String airMode;
    /**
     * 數據漫遊
     */
    public static String dataRoaming;
    /**
     * 手機IP
     */
    public static String clientIp;
    /**
     * See Supporting Multiple Screens for more information.
     */
    public static String screenLayoutSize;

    /**
     * 支援CDMA網路
     */
    public final static boolean isSupportCDMANetwork = sdkVersionCode >= 3;
    /**
     * 支援3.5G網路
     */
    public final static boolean isSupportHSDPANetwork = sdkVersionCode >= 5;
    /**
     * 支援IDEN網路
     */
    public final static boolean isSupportIDENNetwork = sdkVersionCode >= 8;
    /**
     * WebView支援Cache功能
     */
    public final static boolean isSupportWebViewAppCacheEnabled = sdkVersionCode >= 7;
    /**
     * WebView支援ZoomControl
     */
    public final static boolean isSupportWebViewBuiltInZoomControls = sdkVersionCode >= 3;

    public static String wifiStatusText;

    public static Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("Product",product);
        bundle.putString("SimOperator",simOperator);
        bundle.putString("SubscriberId",subscriberId);
        bundle.putString("NetworkOperator",networkOperator);
        bundle.putString("Line1Number",line1Number);
        bundle.putString("CountryIso",countryIso);
        bundle.putString("Operator",operator);
        bundle.putString("OperatorName",operatorName);
        bundle.putString("NetworkType",networkType);
        bundle.putString("PhoneType",phoneType);
        bundle.putString("NetworkRoaming",networkRoaming);
        bundle.putString("Imei",imei);
        bundle.putString("ImeiSV",imeiSV);
        bundle.putString("Imsi",imsi);
        bundle.putString("Bluetooth",bluetooth);
        bundle.putBoolean("WifiStatus",wifiStatus);
        bundle.putString("AirMode",airMode);
        bundle.putString("DataRoaming",dataRoaming);
        bundle.putString("ClientIp",clientIp);
        bundle.putString("ScreenLayoutSize",screenLayoutSize);
        bundle.putString("WifiStatusText",wifiStatusText);
        return bundle;
    }

    public static void restoreData(Bundle bundle) {
        simOperator = bundle.getString("SimOperator");
        subscriberId = bundle.getString("SubscriberId");
        networkOperator = bundle.getString("NetworkOperator");
        line1Number = bundle.getString("Line1Number");
        countryIso = bundle.getString("CountryIso");
        operator = bundle.getString("Operator");
        operatorName = bundle.getString("OperatorName");
        networkType = bundle.getString("NetworkType");
        phoneType = bundle.getString("PhoneType");
        networkRoaming = bundle.getString("NetworkRoaming");
        imei = bundle.getString("Imei");
        imeiSV = bundle.getString("ImeiSV");
        imsi = bundle.getString("Imsi");
        bluetooth = bundle.getString("Bluetooth");
        wifiStatus = bundle.getBoolean("WifiStatus");
        airMode = bundle.getString("AirMode");
        dataRoaming = bundle.getString("DataRoaming");
        clientIp = bundle.getString("ClientIp");
        screenLayoutSize = bundle.getString("ScreenLayoutSize");
        wifiStatusText = bundle.getString("WifiStatusText");
    }

}
