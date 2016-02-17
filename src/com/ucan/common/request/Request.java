package com.ucan.common.request;

import android.util.Log;

import com.ucan.common.ServerType;
import com.ucan.common.network.HttpData;
import com.ucan.common.network.IHttpCallback;
import com.ucan.common.network.MitakeHttpGet;
import com.ucan.common.network.MitakeHttpParams;
import com.ucan.common.network.ThreadPoolManager;

/**
 * Created by eddyteng on 2015/4/29.
 */
public abstract class Request {
    private final static String TAG = "UCAN";
    private final static boolean DEBUG = false;

    public static interface IRequestCallback {
        public void callback(int api, int coin, HttpData httpData);
        public void exception(int api, int coin, int code, String message);
    }

    public boolean isBytes;

    public String getServer(int api, int coin) {
        switch (api) {
        case ServerType.API_HUOBI:
            return ServerType.SERVER_HUOBI;
        case ServerType.API_OKCOIN:
            return ServerType.SERVER_OKCOIN;
        case ServerType.API_BITSTAMP:
            return ServerType.SERVER_BITSTAMP;
        default:
            return "";
        }
    }

    public void httpGet(String server, String command, IHttpCallback callback) {
        MitakeHttpParams mitakeHttpParams = new MitakeHttpParams();
//        mitakeHttpParams.requestKey = request.key;
//        mitakeHttpParams.headers = request.headers;
        mitakeHttpParams.url = server + "/" + command;
        mitakeHttpParams.C2SDataType = MitakeHttpParams.C_S_DATA_TYPE_STRING;

        if (isBytes == true) {
            mitakeHttpParams.S2CDataType = MitakeHttpParams.S_C_DATA_TYPE_BYTES;
        } else {
            mitakeHttpParams.S2CDataType = MitakeHttpParams.S_C_DATA_TYPE_STRING;
        }

        mitakeHttpParams.callback = callback;

        ThreadPoolManager.execute(new MitakeHttpGet(mitakeHttpParams));

        if (DEBUG) {
            Log.d(TAG, "httpGet url="+mitakeHttpParams.url);
        }
    }
}
