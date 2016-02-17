package com.ucan.common;

/**
 * @author eddyteng
 */
public interface IFragmentEvent {
//    public void setSTKItem(QuoteItem stk);
//    public Fragment getFragment();
    public void changeStock();
    public void refreshData();
//    public void pushStock(QuoteItem stk, QuoteItem update);
    public void restoreLayout();
}
