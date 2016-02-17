package com.ucan.function;

import com.ucan.common.ServerType;
import com.ucan.common.network.HttpData;
import com.ucan.common.request.QuoteRequest;
import com.ucan.common.request.Request;
import com.ucan.common.response.QuoteItem;
import com.ucan.common.utility.CoinUtility;
import com.ucan.ucanbit.R;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CoinPage extends BaseFragment {

    private ViewGroup mLayout;
    private View mActionbar;
    private int mAPI;
    private int mCoinType;
    private ViewHolder mHolder;
    private QuoteItem mItem;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAPI = config.getInt("API", 0);
        mCoinType = config.getInt("Coin", 0);
        mItem = new QuoteItem(mAPI, mCoinType);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = (ViewGroup)inflater.inflate(R.layout.fragment_coin_page, container, false);
        mHolder = new ViewHolder(mLayout);
    	return mLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void refreshData() {
        final QuoteRequest request = new QuoteRequest(mAPI, mCoinType);
        request.send(new Request.IRequestCallback() {
            @Override
            public void callback(int api, int coin, HttpData httpData) {
                QuoteItem item = request.parse(api, coin, httpData.data);
                if (item != null) {
                    handler.sendMessage(handler.obtainMessage(0, item));
                }
            }

            @Override
            public void exception(final int api, final int coin, final int code, final String message) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                handler.removeMessages(100);
                refreshData();
            } else {
                QuoteItem prev = mItem;
                mItem = (QuoteItem)msg.obj;
                mItem.prev_price = prev.price;
                updateData(mItem);

                handler.sendEmptyMessageDelayed(100, 3000);
            }
        }
    };

    private void updateData(QuoteItem item) {
        mHolder.setName(item.api, item.coin);
        mHolder.setPrice(item.price, item.getChangeFlag());
        mHolder.High.setText(item.high);
        mHolder.Low.setText(item.low);
        String volume = item.volume;
        if (volume != null) {
            int idx = volume.indexOf(".");
            if (idx >= 0) {
                volume = volume.substring(idx + 1);
            }
        }
        mHolder.Volume.setText(volume);
        mHolder.Currency.setText(item.currency);
    }

    private class ViewHolder {

        public TextView Name;
        public ImageView ChangeIcon;
        public TextView Price;
        public TextView High;
        public TextView Low;
        public TextView Volume;
        public TextView Currency;

        public ViewHolder(View layout) {
            Name = (TextView)layout.findViewById(R.id.name);
            ChangeIcon = (ImageView)layout.findViewById(R.id.changeIcon);
            Price = (TextView)layout.findViewById(R.id.price);
            High = (TextView)layout.findViewById(R.id.high);
            Low = (TextView)layout.findViewById(R.id.low);
            Volume = (TextView)layout.findViewById(R.id.volume);
            Currency = (TextView)layout.findViewById(R.id.currency);
        }

        public void setName(int api, int coin) {
            Name.setText(CoinUtility.getAPINameId(api));
        }

        public void setPrice(String price, int changeFlag) {
            Price.setText(price);
            if (changeFlag == 0) {
                Price.setTextColor(Color.BLACK);
                ChangeIcon.setVisibility(View.GONE);
            } else {
                ChangeIcon.setVisibility(View.VISIBLE);
                if (changeFlag == 1) {
                    Price.setTextColor(Color.RED);
                    ChangeIcon.setImageResource(R.drawable.ic_price_up);
                } else {
                    Price.setTextColor(Color.GREEN);
                    ChangeIcon.setImageResource(R.drawable.ic_price_down);
                }
            }
        }
    }

}
