package com.ucan.function;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Properties;

import com.ucan.common.IFragmentEvent;
import com.ucan.common.IFunction;
import com.ucan.common.utility.CommonUtility;

/**
 * @author eddyteng
 */
public class BaseFragment extends Fragment implements IFragmentEvent
{
	private final static boolean DEBUG = false;
	private final static String TAG = "BaseFragment";

	private ActionBar actionBar;
	protected Bundle config;
	protected IFunction function;
	protected Activity activity;
    protected Fragment fragment;
	protected Properties messageProperties;
	protected Properties configProperties;
    protected SharedPreferences sharedPreferences;
    /**因為綜合報價會有很多層 所以用一個隨機參數來判斷LOG到底是來自哪一層*/
	protected int rand = 0;
	
	/**
	 * 判斷是否功能屬於綜合報價內
	 */
	protected boolean isComposite = false;
    /**
     * 因為有些綜合報價的Fragment會被包含到第二層，而且邏輯上會不同，故增加此參數
     */
    protected boolean isCompositeChild = false;

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		/*
		 * 進入每個功能之前先做GC，否則系統可能認為記憶體足夠而沒GC
		 * 但在如選擇權畫面，物件過多的情況下可能造成Out of Memory
		 * 導致程式crash
		 */
		this.activity = activity;
        
		try 
		{
			actionBar =  activity.getActionBar();
			setActionBarDefault(actionBar);
		} 
		catch (Exception e) 
		{
			actionBar = null;
		}
		
		try 
		{
			function = (IFunction)activity;
		} 
		catch (ClassCastException e) 
		{
			function = null;
		}

        if(getArguments() != null)
        {
            config = getArguments();
        }

//		config = (getArguments() == null) ? new Bundle() : getArguments();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		/**
		 * 20131211[李欣駿]
		 * 加入IKeyEventListener介面,讓Fragment可以偵測實體按鍵的動作。
		 */
		return false;
	}
	
	/**
	 * 取得ActionBar
	 * @return ActionBar
	 */
	protected ActionBar getSupportActionBar()
    {
        if(actionBar == null)
        {
            try
            {
                actionBar = activity.getActionBar();
                setActionBarDefault(actionBar);
            }
            catch(Exception e)
            {
                actionBar = null;
            }
        }

        return actionBar;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        rand = (int)(Math.random()*100);
        setHasOptionsMenu(true);
        fragment = this;
        sharedPreferences = CommonUtility.getSharedPreferences(activity);
        configProperties = CommonUtility.getConfigProperties(activity);
        messageProperties = CommonUtility.getMessageProperties(activity);

        if(savedInstanceState == null)
        {
            config = getArguments();

            if(config != null)
            {
                if (config.containsKey("Composite"))
                {
                    isComposite = config.getBoolean("Composite");
                }

                if(config.containsKey("CompositeChild"))
                {
                    isCompositeChild = config.getBoolean("CompositeChild");
                }
            }
        }
        else
        {
            if(activity == null)
                activity =getActivity();
            if(fragment == null)
                fragment = this;
            if(function == null) {
                try {
                    function = (IFunction)activity;
                } catch (ClassCastException e) {
                    function = null;
                }
            }
            if(savedInstanceState.containsKey("Config")) {
                config = savedInstanceState.getBundle("Config");
                if (config.containsKey("Composite")) {
                    isComposite = config.getBoolean("Composite");
                }
                isCompositeChild = config.getBoolean("CompositeChild");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if(config!=null){
        	outState.putBundle("Config", config);
        }
    }

	@Override
	public void onDetach() 
	{
		super.onDetach();
		messageProperties = null;
        configProperties = null;
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		//20150209[star] 離開任何畫面都關掉progressbar
		function.dismissProgressDialog();
	}

    /**
     * 轉換Fragment
     * @param type FunctionType
     * @param event FunctionEvent
     * @param config 轉換Fragment需夾帶的數值
     */
    protected void setNewFragment(String type, String event, Bundle config)
    {
        Bundle bundle = new Bundle();
        bundle.putString("FunctionType", type);
        bundle.putString("FunctionEvent", event);
        bundle.putBundle("Config", config);
        function.doFunctionEvent(bundle);
    }

    /**
     * 轉換Fragment
     * @param event FunctionEvent
     * @param config 轉換Fragment需夾帶的數值
     */
    protected void setNewFragment(String event, Bundle config)
    {
        setNewFragment("EventManager", event, config);
    }

    protected String[] getConfigPropertyByKey(String key)
    {
        if(configProperties == null)
        {
            configProperties = CommonUtility.getConfigProperties(activity);
        }

        String[] result = null;
        try
        {
            result = configProperties.getProperty(key) == null ? (String[])configProperties.get(key) : configProperties.getProperty(key).split(",");
        }
        catch (Exception e)
        {
        }

        return result;
    }

    protected boolean checkConfigPropertyByKey(String key)
    {
        if(configProperties == null)
        {
            configProperties = CommonUtility.getConfigProperties(activity);
        }

        return configProperties.containsKey(key);
    }
    
    private void setActionBarDefault(ActionBar ab){
    	ab.setDisplayShowTitleEnabled(false);
    }

//    @Override
//    public void setSTKItem(QuoteItem stk) {
//
//    }

    @Override
    public void changeStock() {

    }

    @Override
    public void refreshData() {

    }

//    @Override
//    public void pushStock(QuoteItem stk, QuoteItem update) {
//        if (DEBUG){Log.d(TAG,rand+"++++++baseFragment收到pushStock ");}
//
//    }

    @Override
    public void restoreLayout() {

    }
   
}
