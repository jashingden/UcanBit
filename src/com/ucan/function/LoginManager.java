package com.ucan.function;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.ucan.ucanbit.R;

/**
 * @author eddyteng
 */
public class LoginManager extends BaseFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        activity.getActionBar().hide();
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_login_manager, null);

        handler.sendEmptyMessageDelayed(0, 1500);
        return layout;
    }
    
    private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
            Bundle config = new Bundle();
            config.putBoolean("Back", false);
			setNewFragment("Coin", config);
		}
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
        	new AlertDialog.Builder(activity)
            .setTitle(messageProperties.getProperty("MSG_NOTIFICATION"))
            .setMessage(messageProperties.getProperty("THANK_USE_SYSTEM"))
            .setNegativeButton(messageProperties.getProperty("EXIT_PROGRAM"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                	activity.finish();
                }
            }).show();
        }

        return false;
    }
    
}