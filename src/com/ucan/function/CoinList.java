package com.ucan.function;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ucan.common.ServerType;
import com.ucan.common.network.HttpData;
import com.ucan.common.request.QuoteRequest;
import com.ucan.common.request.Request;
import com.ucan.common.response.QuoteItem;
import com.ucan.common.utility.CoinUtility;
import com.ucan.common.utility.UICalculator;
import com.ucan.ucanbit.R;

import java.util.ArrayList;

/**
 * @author eddyteng
 */
public class CoinList extends BaseFragment {

    private View mLayout;
    private View mActionbar;
    private RadioGroup mModeGroup;
    private ListView mCoinList;
    private CoinAdapter mCoinAdapter;

    private int mCoinType = ServerType.COIN_BTC;
    private int[] mAPI;
    private ArrayList<QuoteItem> mCoinArray = new ArrayList<QuoteItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAPI = new int[]{ServerType.API_HUOBI, ServerType.API_OKCOIN, ServerType.API_BITSTAMP};

        mCoinArray.add(new QuoteItem(ServerType.API_HUOBI, mCoinType));
        mCoinArray.add(new QuoteItem(ServerType.API_OKCOIN, mCoinType));
        mCoinArray.add(new QuoteItem(ServerType.API_BITSTAMP, mCoinType));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActionbar = inflater.inflate(R.layout.actionbar_coin, container, false);
        mActionbar.findViewById(R.id.BtnLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mActionbar.findViewById(R.id.BtnRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mModeGroup = (RadioGroup)mActionbar.findViewById(R.id.ModeGroup);
        mModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.buttonBTC) {
                    mCoinType = ServerType.COIN_BTC;
                } else if (checkedId == R.id.buttonLTC) {
                    mCoinType = ServerType.COIN_LTC;
                }
                refreshData();
            }
        });
        ActionBar actionBar = activity.getActionBar();
        actionBar.show();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(null);
        actionBar.setCustomView(mActionbar);

        function.setBottomMenuEnable(true, "Coin");

        mLayout = inflater.inflate(R.layout.fragment_coin_list, container, false);
        mCoinList = (ListView)mLayout.findViewById(R.id.list);
        mCoinList.setDividerHeight(UICalculator.getDimensionPixelSize(activity, 5));
        mCoinAdapter = new CoinAdapter(activity);
        mCoinList.setAdapter(mCoinAdapter);
        mCoinList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("FunctionType","Activity");
                bundle.putString("FunctionEvent","Coin");
                Bundle config = new Bundle();
                config.putInt("Position", position);
                config.putInt("Coin", mCoinType);
                bundle.putBundle("Config", config);
                function.doFunctionEvent(bundle);
            }
        });

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
                    handler.sendMessage(handler.obtainMessage(api, item));
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
                int position = msg.what;
                QuoteItem item = (QuoteItem)msg.obj;
                if (position >= 0 && position < mCoinArray.size()) {
                    QuoteItem prev = mCoinArray.get(position);
                    item.prev_price = prev.price;
                    mCoinArray.set(position, item);
                }
                mCoinAdapter.notifyDataSetChanged();

                handler.sendEmptyMessageDelayed(100, 3000);
            }
        }
    };

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

    private class CoinAdapter extends BaseAdapter {

        private Context context;

        private CoinAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mCoinArray.size();
        }

        @Override
        public Object getItem(int position) {
            if (position >= 0 && position < mCoinArray.size()) {
                return mCoinArray.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            QuoteItem item = (QuoteItem)getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.coin_listview_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.setName(item.api, item.coin);
            holder.setPrice(item.price, item.getChangeFlag());
            holder.High.setText(item.high);
            holder.Low.setText(item.low);
            String volume = item.volume;
            if (volume != null) {
                int idx = volume.indexOf(".");
                if (idx >= 0) {
                    volume = volume.substring(idx + 1);
                }
            }
            holder.Volume.setText(volume);
            holder.Currency.setText(item.currency);
            return convertView;
        }
    }

}
