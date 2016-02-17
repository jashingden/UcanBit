package com.ucan.common.utility;

import com.ucan.common.ServerType;
import com.ucan.ucanbit.R;

/**
 * Created by eddyteng on 2015/5/28.
 */
public class CoinUtility {

    public static int getAPINameId(int api) {
        if (api == ServerType.API_HUOBI) {
           return R.string.name_huobi;
        } else if (api == ServerType.API_OKCOIN) {
            return R.string.name_okcoin;
        } else if (api == ServerType.API_BITSTAMP) {
            return R.string.name_bitstamp;
        } else {
            return R.string.name_noapi;
        }
    }
}
