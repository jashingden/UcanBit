package com.ucan.common;

/**
 * @author eddyteng
 */
public class EnumSet
{
    public enum EventType
    {
        /**
         * 導覽頁
         */
        NAVIGATION,
        /**
         * 首页
         */
        HOME,
        /**
         * 资讯
         */
        NEWS,
        /**
         * 東方资讯內容
         */
        EAST_FINANCE_NEWS_DETAIL,
        /**
         * 自选
         */
        TRADE,

        /**
         * 行情
         */
        COIN,

        /**
         * 分類
         */
        CLASSIFICATION,
        /**
         * 股吧
         */
        COMMUNITY,
        /**
         * 我
         */
        ME,
        /**
         * 管理
         */
        EAST_FINANCE_LIST_EDIT,
        /**
         * 警示设定
         */
        EAST_ALERT_SETTING,
        /**
         * 新闻内文
         */
        EAST_NEWS_DETAIL,
        /**
         * 報價列表
         */
        EAST_QUTATION,
        /**
         * 新闻more
         */
        EastFinanceMoreFragment,
        /**
         * F10 摘要
         */
        MODE_ABSTRACT,
        /**
         * F10  簡況
         */
        MODE_PROFILES,
        /**
         * F10  財務
         */
        MODE_FINANCE,
        /**
         * F10  股東
         */
        MODE_STOCKHOLDER,
        /**
         * F10 行情刷新頻率
         */
        REFRESH_RATE_SETTING,
         /**
         * F10  字體大小設置
         */
        TEXT_SIZE_SETTING,
        /**
         * 版權訊息
         */
        COPY_RIGHT,
        /**
         * 十档
         */
        BEST_TEN,
        /**
         *经济席位
         */
        StockBankerInfoFragment,
        /**
         * 登入連線
         */
        LOGIN_MANAGER, CASSIFICATION,
        /**
         * 首頁WEB View
         */
        HOME_WEB_VIEW,
        /**
         * 板塊的更多 View
         */
        BLOCK_MORE_VIEW,
    }


    public enum ObserverType {
        /**
         * 視窗狀態，詳細請參考{@link com.mitake.variable.object.WindowChangeKey} <br/>
         * example: bundle.putInt(WindowChangeKey.BEFORE_STATUS,WindowChangeKey.STATUS_UP) <br/>
         */
        WINDOW_CHANGE,
        /**
         * 商品切換，在接收到商品的時候，請勿直接oldBundle = newBundle，
         * 應該要先oldBundle.clear();再oldBundle.putAll(newBundle)，以避免因為相同一份記憶體會互相影響到。<br/>
         */
        STOCK_CHANGE,
        /**
         * (參考同WINDOW_CHANGE):僅通知要改變視窗狀態，
         * 通常用於功能點兩下要放大或縮小時，通知要改變狀態，目前只有StockDetailFrame會需要監聽，
         * 使用時須要送本身是位於上視窗或是下視窗(StockDetailFrame在建立功能時會給予)<br/>
         * example: bundle.putString(WindowChangeKey.FRAME,WindowChangeKey.UP) <br/>
         */
        NOTIFICATION_WINDOW_CHANGE,
        /**
         * 通知StockDetailFrame裡的viewPager是否能滑動
         */
        NOTIFICATION_ENABLE_VIEW_PAGER_SCROLL,
        /**
         * 通知商品更新
         */
        STOCK_PUSH,
        /**
         * 通知頁數改變
         */
        PAGE_CHANGE,
        /**
         * 通知視窗按住或釋放
         */
        WINDOW_TOUCH,
        NOTIFICATION_RECEIVER,
    }
}
