package com.ucan.common.network;

/**
 * Created by Hsinchun on 2015/4/25.
 */
public interface IHttpCallback
{
    public void callback(HttpData httpData);
    public void exception(String key, int code, String message);
}
