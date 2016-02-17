package com.ucan.ucanbit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.ucan.common.ServerType;
import com.ucan.function.CoinPage;

/**
 * Created by eddyteng on 2015/4/30.
 */
public class CoinActivity extends BaseActivity {

    private View mLayout;
    private View mActionbar;
    private View mProgressBar;
    private View mBtnRefresh;
    private ViewPager mCoinPager;
    private CoinPagerAdapter mCoinAdapter;
    private int mCoinPosition;
    private int mCoinType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCoinPosition = config.getInt("Position", 0);
        mCoinType = config.getInt("Coin", 0);

        setContentLayoutId(R.id.activity_root);
        setContentView(R.layout.activity_coin);

        getActionBar().hide();

        mLayout = (ViewGroup)this.findViewById(getContentLayoutId());

        mCoinPager = (ViewPager) this.findViewById(R.id.viewpager);
        mCoinAdapter = new CoinPagerAdapter(getSupportFragmentManager());
        mCoinPager.setAdapter(mCoinAdapter);
        mCoinPager.setOnPageChangeListener(mCoinAdapter);
        mCoinPager.setCurrentItem(mCoinPosition);
    }

    private class CoinPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

		public CoinPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
        public Fragment getItem(int position) {
            CoinPage page = new CoinPage();
            Bundle config = new Bundle();
            config.putInt("Position", position);
            config.putInt("API", position);
            config.putInt("Coin", mCoinType);
            page.setArguments(config);
            return page;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int pageNumber) {
            mCoinPosition = pageNumber;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

}
