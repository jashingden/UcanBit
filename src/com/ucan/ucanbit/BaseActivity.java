package com.ucan.ucanbit;

import com.ucan.common.EventCenter;
import com.ucan.common.IFunction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * @author eddyteng
 */
public class BaseActivity extends FragmentActivity implements IFunction
{
    protected int contentLayoutId = R.id.content_frame;
    protected Bundle config;

    public void setContentLayoutId(int id) {
        contentLayoutId = id;
    }

    public int getContentLayoutId() {
        return contentLayoutId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            config = extras;
        } else {
            config = new Bundle();
        }
    }

    @Override
    public void doFunctionEvent(Bundle bundle) {
        doFunctionEvent(bundle, 0, null);
    }

    @Override
    public void doFunctionEvent(Bundle bundle, FragmentManager fragmentManager,int layoutID) {
        doFunctionEvent(bundle, fragmentManager, layoutID, 0, null);
    }

    @Override
    public void doFunctionEvent(final Bundle bundle, final int requestCode, final Fragment targetFragment) {
        doFunctionEvent(bundle, getSupportFragmentManager(), contentLayoutId, requestCode, targetFragment);
    }

    @Override
    public void doFunctionEvent(final Bundle bundle, final FragmentManager fragmentManager, final int layoutId, final int requestCode,final Fragment targetFragment)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                EventCenter.doFunction(BaseActivity.this, fragmentManager, layoutId, bundle, targetFragment, requestCode);
            }
        });

    }

    @Override
    public boolean isPortrait() {
        Configuration config = this.getResources().getConfiguration();
        return (config.orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    @Override
    public void rotateScreen() {
        this.setRequestedOrientation(isPortrait() ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                                                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public boolean isProgressDialogShowing() {
        return false;
    }

    @Override
    public void setBottomMenuEnable(boolean isEnable, String function) {

    }

}
