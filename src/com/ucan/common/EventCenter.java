package com.ucan.common;

import com.ucan.common.EnumSet.EventType;
import com.ucan.function.BaseFragment;
import com.ucan.function.CoinList;
import com.ucan.function.LoginManager;
import com.ucan.ucanbit.CoinActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @author eddyteng
 */
public class EventCenter {

    public static void doFunction(Activity activity, FragmentManager fragmentManager, int layoutId, Bundle bundle, Fragment targetFragment, int requestCode) {
        if (bundle.containsKey("FunctionType")) {
            String functionType = bundle.getString("FunctionType");

            if (functionType == null) {
                return;
            }

            if(functionType.equals("Navigation"))
            {
                Bundle config = bundle.containsKey("Config") ? bundle.getBundle("Config") : new Bundle();
                EventCenter.changeFragment(activity, fragmentManager, EventType.NAVIGATION, config, targetFragment, requestCode, layoutId);
            }
            else if(functionType.equals("LoginManager"))
            {
                Bundle config = new Bundle();
                config.putBoolean("Back", false);
                EventCenter.changeFragment(activity, fragmentManager, EventType.LOGIN_MANAGER, config, targetFragment, requestCode, layoutId);
            }
            else if (functionType.equals("Activity")) {
                String functionEvent = bundle.getString("FunctionEvent");
                if (functionEvent == null) return;
                Bundle config = bundle.containsKey("Config") ? bundle.getBundle("Config") : new Bundle();
                if (functionEvent.equals("Coin")) {
                    Intent coinIntent = new Intent(activity, CoinActivity.class);
                    coinIntent.putExtras(config);
                    activity.startActivity(coinIntent);
                }
            } else if (functionType.equals("EventManager")) {
                String functionEvent = bundle.getString("FunctionEvent");
                if (functionEvent == null) return;
                Bundle config = bundle.containsKey("Config") ? bundle.getBundle("Config") : new Bundle();
                if (functionEvent.equals("Home")) { // 首页
                    EventCenter.changeFragment(activity, fragmentManager, EventType.HOME, config, targetFragment, requestCode, layoutId);
                } else if (functionEvent.equals("News")) { // 资讯
                    EventCenter.changeFragment(activity, fragmentManager, EventType.NEWS, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("EastFinanceNewsDetail")) { //東方资讯內容
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.EAST_FINANCE_NEWS_DETAIL, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("Portfolio")) { // 自选
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.PORTFOLIO, config, targetFragment, requestCode, layoutId);
                } else if (functionEvent.equals("Coin")) { // 行情
                    EventCenter.changeFragment(activity, fragmentManager, EventType.COIN, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("Classification")) { // 類別
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.CLASSIFICATION, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("Community")) { // 股吧
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.COMMUNITY, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("Me")) { // 我
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.ME, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("EastFinanceListEdit")) { // 管理
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.EAST_FINANCE_LIST_EDIT, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("EastAlertSetting")) { // 警示设定
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.EAST_ALERT_SETTING, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("EastNewsDetailFragment")) { // 新闻内文
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.EAST_NEWS_DETAIL, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("EastQuotationFragment")) { // 報價列表
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.EAST_QUTATION, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("EastFinanceMoreFragment")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.EastFinanceMoreFragment, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("F10Abstract")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.MODE_ABSTRACT, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("F10Profiles")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.MODE_PROFILES, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("F10Finance")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.MODE_FINANCE, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("F10Stockholder")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.MODE_STOCKHOLDER, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("EstWebFragment")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.HOME_WEB_VIEW, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("RefreshRateSetting")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.REFRESH_RATE_SETTING, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("TextSizeSetting")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.TEXT_SIZE_SETTING, config, targetFragment, requestCode, layoutId);
//                } else if (functionEvent.equals("CopyRight")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.COPY_RIGHT, config, targetFragment, requestCode, layoutId);
//                }else if (functionEvent.equals("BlockMore")) { // Block More
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.BLOCK_MORE_VIEW, config, targetFragment, requestCode, layoutId);
//                }else if (functionEvent.equals("StockBestTen")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.BEST_TEN, config, targetFragment, requestCode, layoutId);
//                }else if (functionEvent.equals("StockBankerInfoFragment")) { // news more
//                    EventCenter.changeFragment(activity, fragmentManager, EventType.StockBankerInfoFragment, config, targetFragment, requestCode, layoutId);
                }

            }
        }
    }

    public static void changeFragment(Activity activity,FragmentManager fragmentManager, EventType eventType, Bundle bundle,int layoutID)
    {
        changeFragment(activity,fragmentManager,eventType,bundle,null,0,layoutID);
    }

	/*
	 * 加入新頁面流程
	 *
	 * public class EnumSet 裡面設定標籤
	 * public class EventUtility 設定流程
	 * public class EventCenter 設定啟動哪一隻class
	 * 再跳轉的Listener中觸發setNewFragment(String event, Bundle config)
	 */


    /**
     * 切換fragment
     * @param fragmentManager fragmentManager
     * @param eventType 事件類型
     * @param bundle 夾帶的資料
     * @param targetFragment 返回的fragment
     * @param layoutID 顯示內容的layoutID
     */
    public static void changeFragment(Activity activity,FragmentManager fragmentManager, EventType eventType, Bundle bundle, Fragment targetFragment,int requestCode, int layoutID) {
        Fragment fragment = null;

        switch (eventType)
        {
            case NAVIGATION:
//                fragment = new NavigationFragment();
                break;
            case LOGIN_MANAGER:
                fragment = new LoginManager();
                break;
//            case HOME:
//                fragment = new Home();
//                break;
//            case PORTFOLIO:
//                fragment = new EastFinanceListMain();
//                break;
//            case EAST_FINANCE_LIST_EDIT:
//                fragment = new EastFinanceListEdit();
//                break;
//            case NEWS:
//                fragment = new EastNewsFrameV3();
//                break;
//            case EAST_FINANCE_NEWS_DETAIL:
//                fragment = new EastFinanceNewsDetail();
//                break;
//
            case COIN:
                fragment = new CoinList();
                break;
//            case CLASSIFICATION:
//                fragment = new EastClassificationView();
//                break;
//            case ME:
//                fragment = new EastMeFragment();
//                break;
//            case EAST_ALERT_SETTING:
//                fragment = new EastAlertSetting();
//                break;
//            case EAST_NEWS_DETAIL:
//                fragment = new EastNewsDetailFragment();
//                break;
//            case EAST_QUTATION:
//                fragment = new EastQuotationFragment();
//                break;
//            case EastFinanceMoreFragment:
//                fragment = new EastFinanceMoreFragment();
//                break;
//            case MODE_ABSTRACT:
//                fragment = new StockF10AbstractFragment();
//                break;
//            case MODE_PROFILES:
//                fragment = new StockF10ProfilesFragment();
//                break;
//            case MODE_FINANCE:
//                fragment = new StockF10FinanceFragment();
//                break;
//            case MODE_STOCKHOLDER:
//                fragment = new StockF10StockholderFragment();
//                break;
//            case HOME_WEB_VIEW:
//                fragment = new EastWebFragment();
//                break;
//            case BLOCK_MORE_VIEW:
//                fragment = new EastMoreBlockFragment();
//                break;
//            case REFRESH_RATE_SETTING:
//                fragment = new EastRefreshRateFragment();
//                break;
//            case TEXT_SIZE_SETTING:
//                fragment = new EastTextSizeFragment();
//                break;
//            case COPY_RIGHT:
//                fragment = new EastCopyRightFragment();
//                break;
//            case BEST_TEN:
//                fragment = new StockBestTen();
//                break;
//            case StockBankerInfoFragment:
//                fragment = new StockBankerInfoFragment();
//                break;
            default:
                fragment = new BaseFragment();
                break;
        }

        changeFragment(activity, fragmentManager, eventType, fragment, bundle, targetFragment, requestCode, layoutID);
    }

    public static void changeFragment(Activity activity, final FragmentManager fragmentManager, final EventType eventType, final Fragment fragment, final Bundle bundle, final Fragment targetFragment, final int requestCode, final int layoutID)
    {
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            public void run()
            {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                bundle.putInt("EventType",eventType.ordinal());
                bundle.putString("EventTypeName",eventType.name());
                fragment.setArguments(bundle);

                if(targetFragment != null)
                {
                    fragment.setTargetFragment(targetFragment,requestCode);
                }

                if(bundle.getBoolean("AddFragment"))
                {
                    transaction.add(layoutID, fragment,eventType.name());
                }
                else
                {
                    transaction.replace(layoutID, fragment, eventType.name());
                }

                if(bundle.getBoolean("Back", true))
                {
                    if(bundle.containsKey("BackKey"))
                    {
                        transaction.addToBackStack(bundle.getString("BackKey"));
                    }
                    else
                    {
                        transaction.addToBackStack(eventType.name());
                    }
                }
                else
                {
                    /**
                     * 201409021645[Ruaddick] 加入此機制，若不加 back則先popBackStack再加入前一個
                     */
                    int count = fragmentManager.getBackStackEntryCount();
                    if(count>0)
                    {
                        transaction.addToBackStack(fragmentManager.getBackStackEntryAt(count - 1).getName());
                        fragmentManager.popBackStack();
                    }
                }

                transaction.commitAllowingStateLoss();
            }
        });
    }
}
