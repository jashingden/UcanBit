package com.ucan.common.response;

import java.util.Date;

/**
 * Created by eddyteng on 2015/4/29.
 */
public class QuoteItem {

    public int api;
    public int coin;

    public String time;
    public String buy;
    public String sell;
    public String high;
    public String low;
    public String price;
    public String volume;
    public String avgPrice;

    public String prev_price;
    public String currency;

    public QuoteItem(int api, int coin) {
        this.api = api;
        this.coin = coin;
    }

    public int getChangeFlag() {
        if (price == null || prev_price == null) {
            return 0;
        } else {
            try {
                float last = Float.parseFloat(price);
                float prev = Float.parseFloat(prev_price);
                if (last == prev) {
                    return 0;
                } else {
                    return last > prev ? 1 : 2;
                }
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
