package com.ucan.ucanbit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ucan.common.utility.PhoneUtility;
import com.ucan.common.utility.UICalculator;
import com.ucan.function.BaseFragment;

public class MainActivity extends BaseActivity {

    private int[] menuImage = {R.drawable.menu4, R.drawable.menu2, R.drawable.menu3, R.drawable.menu5};

    private LinearLayout bottomMenuLinearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //設定下方快捷列layout
        bottomMenuLinearLayout = (LinearLayout) findViewById(R.id.content_bottom_menu);
        setBottomMenuIcon();

        /**
         * 讀取手機端資料
         */
        PhoneUtility.init(this);
        
        if (savedInstanceState == null) {
            goLoginPage();
        } else {

        }
    }

    private void goLoginPage() {
//        /**
//         * 判断是否有进来过导览，有进来过直接进Loading画面
//         */
//        DBUtility.loadData(this, "NAVIGATE_VERSION", new DBUtility.ICallback() {
//            @Override
//            public void callback(String data) {
                Bundle bundle = new Bundle();

//                if (data != null && data.equals(NavigationFragment.NAVIGATE_VERSION)) {
                    bundle.putString("FunctionType", "LoginManager");
//                } else {
//                    Bundle config = new Bundle();
//                    config.putBoolean("Back", false);
//                    bundle.putBundle("Config", config);
//                    bundle.putString("FunctionType", "Navigation");
//                }

                doFunctionEvent(bundle);
//            }
//        });
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    /**
     * 設定快捷列按鈕
     */
    private void setBottomMenuIcon() {
        String[] menuCode = new String[]{"Coin", "News", "Trade", "Me"};
        String[] menuName = {"行情", "资讯", "交易", "我"};

        LinearLayout.LayoutParams bottomLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bottomLayoutParams.weight = 1;

        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams((int) UICalculator.getRatioWidth(this, 22), (int) UICalculator.getRatioWidth(this, 22));
        imageLayoutParams.topMargin = (int) UICalculator.getRatioWidth(this, 4);

        LayoutInflater inflater = this.getLayoutInflater();

        for (int i = 0; i < menuCode.length; i++) {
            LinearLayout button = (LinearLayout) inflater.inflate(R.layout.bottom_menu_button, null);
            button.setTag(menuCode[i]);

            ImageView img = (ImageView) button.findViewById(R.id.menuImg);
            img.setImageResource(menuImage[i]);
            img.setLayoutParams(imageLayoutParams);

            TextView text = (TextView) button.findViewById(R.id.menuText);
            text.setText(menuName[i]);

            bottomMenuLinearLayout.addView(button, bottomLayoutParams);
        }

        // 行情
        bottomMenuLinearLayout.findViewWithTag("Coin").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int count = getFragmentManager().getBackStackEntryCount();
//                boolean flag = false;
//                for (int i = 0; i < count; i++) {
//                    if (getFragmentManager().getBackStackEntryAt(i).getName().equals("HomeTab_Markets")) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!(getFragmentManager().findFragmentById(R.id.content_frame) instanceof EastMarkets)) {
//                    mButtonTag = "Markets";
//                    if (flag) {
//                        getFragmentManager().popBackStack("HomeTab_Markets", 0);
//                    } else {
//                        Bundle bundle = new Bundle();
//                        Bundle config = new Bundle();
//                        bundle.putString("FunctionType", "EventManager");
//                        bundle.putString("FunctionEvent", "Markets");
//                        config.putString("BackKey", "HomeTab_Markets");
//                        bundle.putBundle("Config", config);
//                        doFunctionEvent(bundle);
//                    }
//                }
            }
        });
        // 资讯
        bottomMenuLinearLayout.findViewWithTag("News").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int count = getFragmentManager().getBackStackEntryCount();
//                boolean flag = false;
//                for (int i = 0; i < count; i++) {
//                    if (getFragmentManager().getBackStackEntryAt(i).getName().equals("HomeTab_News")) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!(getFragmentManager().findFragmentById(R.id.content_frame) instanceof EastNewsFrameV3)) {
//                    mButtonTag = "News";
//                    if (flag) {
//                        getFragmentManager().popBackStack("HomeTab_News", 0);
//                    } else {
//                        Bundle bundle = new Bundle();
//                        Bundle config = new Bundle();
//                        bundle.putString("FunctionType", "EventManager");
//                        bundle.putString("FunctionEvent", "News");
//                        config.putString("BackKey", "HomeTab_News");
//                        bundle.putBundle("Config", config);
//                        doFunctionEvent(bundle);
//                    }
//                }
            }
        });
        // 交易
        bottomMenuLinearLayout.findViewWithTag("Trade").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int count = getFragmentManager().getBackStackEntryCount();
//                boolean flag = false;
//                for (int i = 0; i < count; i++) {
//                    if (getFragmentManager().getBackStackEntryAt(i).getName().equals("HomeTab_Portfolio")) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!(getFragmentManager().findFragmentById(R.id.content_frame) instanceof EastFinanceListMain)) {
//                    mButtonTag = "Portfolio";
//                    if (flag) {
//                        getFragmentManager().popBackStack("HomeTab_Portfolio", 0);
//                    } else {
//                        Bundle bundle = new Bundle();
//                        Bundle config = new Bundle();
//                        bundle.putString("FunctionType", "EventManager");
//                        bundle.putString("FunctionEvent", "Portfolio");
//                        config.putBoolean("custom", true);
//                        config.putBoolean("isHomeIn", true);
//                        config.putString("BackKey", "HomeTab_Portfolio");
//                        bundle.putBundle("Config", config);
//                        doFunctionEvent(bundle);
//                    }
//                }
            }
        });
        // 我
        bottomMenuLinearLayout.findViewWithTag("Me").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int count = getFragmentManager().getBackStackEntryCount();
//                boolean flag = false;
//                for (int i = 0; i < count; i++) {
//                    if (getFragmentManager().getBackStackEntryAt(i).getName().equals("HomeTab_Me")) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!(getFragmentManager().findFragmentById(R.id.content_frame) instanceof EastMeFragment)) {
//                    mButtonTag = "Me";
//                    if (flag) {
//                        getFragmentManager().popBackStack("HomeTab_Me", 0);
//                    } else {
//                        Bundle bundle = new Bundle();
//                        Bundle config = new Bundle();
//                        bundle.putString("FunctionType", "EventManager");
//                        bundle.putString("FunctionEvent", "Me");
//                        config.putString("BackKey", "HomeTab_Me");
//                        bundle.putBundle("Config", config);
//                        doFunctionEvent(bundle);
//                    }
//                }
            }
        });
    }

    @Override
    public void setBottomMenuEnable(boolean isEnable, String function) {
        if (bottomMenuLinearLayout != null) {
            if (isEnable) {
                bottomMenuLinearLayout.setVisibility(View.VISIBLE);

                for (int i = 0; i < bottomMenuLinearLayout.getChildCount(); i++) {
                    View child = bottomMenuLinearLayout.getChildAt(i);

                    if (child.getTag().equals(function)) {
                        child.setSelected(true);
                    } else {
                        child.setSelected(false);
                    }
                }
            } else {
                bottomMenuLinearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = getCurrentFragment();
        //20140820[Ruaddick] 修正真正為找前景的方式，並且加入判斷若是最後一個fragment則直接離開
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                if (fragment != null && fragment instanceof BaseFragment && ((BaseFragment) fragment).onKeyDown(keyCode, event)) {
                    return true;
                }
            } else {
                //20150407检查是否为tab功能，是的话直接回首页
                if (getFragmentManager().getBackStackEntryAt(count - 1).getName().contains("HomeTab")) {
                    getFragmentManager().popBackStack("HOME", 0);
                    return true;
                }
            }
        } else {
            if (fragment != null) {
                if (fragment instanceof BaseFragment && ((BaseFragment) fragment).onKeyDown(keyCode, event)) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
