package com.ucan.common.request;

import com.ucan.common.ServerType;
import com.ucan.common.network.HttpData;
import com.ucan.common.network.IHttpCallback;
import com.ucan.common.response.JSONWrapper;
import com.ucan.common.response.QuoteItem;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by eddyteng on 2015/4/29.
 */
public class QuoteRequest extends Request {

    private int[] api;
    private int coin;

    public QuoteRequest(int api, int coin) {
        this(new int[]{api}, coin);
    }

    public QuoteRequest(int[] api, int coin) {
        this.api = api;
        this.coin = coin;
    }

    public void send(final IRequestCallback callback) {
        for (int i = 0; i < api.length; i++) {
            final int a = api[i];
            String server = getServer(a, coin);
            String command = getCommand(a);
            if (server.length() > 0 && command.length() > 0) {
                httpGet(server, command, new IHttpCallback() {
                    @Override
                    public void callback(HttpData httpData) {
                        if (callback != null) {
                            callback.callback(a, coin, httpData);
                        }
                    }

                    @Override
                    public void exception(String key, int code, String message) {
                        if (callback != null) {
                            callback.exception(a, coin, code, message);
                        }
                    }
                });
            } else {
                if (callback != null) {
                    callback.exception(a, coin, -1, "NO API");
                }
            }
        }
    }

    private String getCommand(int api) {
        switch (api) {
        case ServerType.API_HUOBI:
            if (coin == ServerType.COIN_BTC) {
                return "ticker_btc_json.js";
            } else if (coin == ServerType.COIN_LTC) {
                return "ticker_ltc_json.js";
            }
        case ServerType.API_OKCOIN:
            if (coin == ServerType.COIN_BTC) {
                return "ticker.do?ok=1";
            } else if (coin == ServerType.COIN_LTC) {
                return "ticker.do?symbol=ltc_usd&ok=1";
            }
        case ServerType.API_BITSTAMP:
            if (coin == ServerType.COIN_BTC) {
                return "ticker/";
            } else if (coin == ServerType.COIN_LTC) {
                return "";
            }
        default:
            return "";
        }
    }

    public QuoteItem parse(int api, int coin, String result) {
        switch (api) {
        case ServerType.API_HUOBI:
            return parseHUOBI(api, coin, result);
        case ServerType.API_OKCOIN:
            return parseOKCoin(api, coin, result);
        case ServerType.API_BITSTAMP:
            return parseBITSTAMP(api, coin, result);
        default:
            return null;
        }
    }

    private QuoteItem parseHUOBI(int api, int coin, String result) {
        QuoteItem item;
        try {
            JSONObject json = new JSONObject(result);
            JSONWrapper data = new JSONWrapper(json);
            
            item = new QuoteItem(api, coin);
            item.time = data.getString("time");

            JSONWrapper ticker = JSONWrapper.getJSONWrapper(data.getJSONObject(), "ticker");
            item.buy = ticker.getString("buy");
            item.sell = ticker.getString("sell");
            item.high = ticker.getString("high");
            item.low = ticker.getString("low");
            item.price = ticker.getString("last");
            item.volume = ticker.getString("vol");
            item.currency = "CNY";
        } catch (Exception ex) {
            item = null;
        }
        return item;
    }

    private QuoteItem parseOKCoin(int api, int coin, String result) {
        QuoteItem item;
        try {
            JSONObject json = new JSONObject(result);
            JSONWrapper data = new JSONWrapper(json);

            item = new QuoteItem(api, coin);
            item.time = data.getString("date");

            JSONWrapper ticker = JSONWrapper.getJSONWrapper(data.getJSONObject(), "ticker");
            item.buy = ticker.getString("buy");
            item.sell = ticker.getString("sell");
            item.high = ticker.getString("high");
            item.low = ticker.getString("low");
            item.price = ticker.getString("last");
            item.volume = ticker.getString("vol");
            item.currency = "USD";
        } catch (Exception ex) {
            item = null;
        }
        return item;
    }

    private QuoteItem parseBITSTAMP(int api, int coin, String result) {
        QuoteItem item;
        try {
            JSONObject json = new JSONObject(result);
            JSONWrapper ticker = new JSONWrapper(json);

            item = new QuoteItem(api, coin);
            item.time = ticker.getString("timestamp");

            item.buy = ticker.getString("bid");
            item.sell = ticker.getString("ask");
            item.high = ticker.getString("high");
            item.low = ticker.getString("low");
            item.price = ticker.getString("last");
            item.volume = ticker.getString("volume");
            item.avgPrice = ticker.getString("vwap");
            item.currency = "USD";
        } catch (Exception ex) {
            item = null;
        }
        return item;
    }
}
