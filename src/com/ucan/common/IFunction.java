package com.ucan.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * 此介面透過Activity的幫助，來與非此Library做溝通 如呼叫帳務
 * @author ruaddick
 */
public interface IFunction
{
	public void doFunctionEvent(Bundle bundle);
    public void doFunctionEvent(Bundle bundle,FragmentManager fragmentManager,int layoutId);
    public void doFunctionEvent(Bundle bundle,int requestCode,Fragment targetFragment);
    public void doFunctionEvent(Bundle bundle,FragmentManager fragmentManager,int layoutId,int requestCode,Fragment targetFragment);
    public boolean isPortrait();
    public void rotateScreen();
    public void showProgressDialog();
    public void dismissProgressDialog();
    public boolean isProgressDialogShowing();
    public void setBottomMenuEnable(boolean isEnable, String function);
}
