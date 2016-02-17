package com.ucan.common.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Frank Wang
 * 包裝 JSONObject，不要有 Exception 比較好用
 */
public class JSONWrapper{

	private JSONObject obj = null;
	public JSONWrapper(JSONObject obj)
	{
		this.obj = obj;
	}
	
	public JSONWrapper(JSONArray array, int index)
	{
		this.obj = getJSONObject(array, index);
	}
	
	public JSONObject getJSONObject() {
		return obj;
	}
	
	public String getString(String name)
	{
		try {
			return obj.getString(name);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public JSONArray getJSONArray(String name)
	{
		return getJSONArray(obj, name);
	}
	
	public JSONObject put(String name, String value)
	{
		try {
			return obj.put(name, value);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public JSONObject put(String name, JSONWrapper wrapper)
	{
		try {
			return obj.put(name, wrapper.obj);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String toString()
	{
		if (obj != null)
			return obj.toString();
		else
			return "";
	}
	
	/**
	 * 判斷如果是 JSONObject，包成 JSONArray 往外傳，如果都不是，就回傳 null，因為 Server 的格式不固定
	 * @param obj 內含 JSONArray 的 JSONObject
	 * @param name name of JSONArray
	 * @return null or JSONArray
	 */
	public static JSONArray getJSONArray(JSONObject obj, String name)
	{
		if (obj == null) {
			return null;
		}
		
		JSONArray a = obj.optJSONArray(name);
		
		if (a == null) {
			a = new JSONArray();
			JSONObject o = obj.optJSONObject(name);
			if (o != null) {
				a.put(o);
				return a;
			}
		}
		
		return a;
	}
	
	public static String getString(JSONObject obj, String name)
	{
		try {
			return obj.getString(name);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static String getStringFromArray(JSONArray array, int index, String name)
	{
		return getJSONObject(array, index).optString(name);
	}
	
	public static JSONObject getJSONObject(JSONArray array, int index)
	{
		try {
			return array.getJSONObject(index);
		} catch (JSONException e) {
			return null;
		}
	}
	
	/**
	 * 依指定欄位的值比對，從 JSONArray 取出第一個符合的 JSONObject
	 * @param array source JSONArray
	 * @param element 比對欄位
	 * @param elementValue 比對值
	 * @return 符合指定值的 JSONObject
	 */
	public static JSONObject getJSONObjectByElement(JSONArray array, String element, String elementValue)
	{
		for (int i = 0 ; i < array.length() ; i++)
		{
			JSONObject obj = array.optJSONObject(i);
			if (obj.optString(element).equals(elementValue))
			{
				return obj;
			}
		}
		return null;
	}
	
	public static JSONWrapper getJSONWrapper(JSONArray array, int index)
	{
		JSONObject obj = getJSONObject(array, index);
		if (obj == null)
		{
			return null;
		} else {
			return new JSONWrapper(obj);
		}
		
	}
	
	public static JSONWrapper getJSONWrapper(JSONObject obj, String name)
	{
		try {
			return new JSONWrapper(obj.getJSONObject(name));
		} catch (JSONException e) {
			return null;
		}
	}
	
	/**
	 * 把 JSONArray 轉成 ArrayList
	 * @param arr 轉成的 ArrayList
	 */
	public static ArrayList<JSONObject> toArrayList(JSONArray arr)
	{
		if (arr == null) {
			return new ArrayList<JSONObject>();
		}
		
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		int length = arr.length();
		for (int i = 0 ; i < length ; i++)
		{
			result.add(arr.optJSONObject(i));
		}
		return result;
	}
	
	/**
	 * 把 JSONArray 轉成 ArrayList
	 * @param arr 轉成的 ArrayList
	 * @param comparator 排序用的 Comparator
	 */
	public static ArrayList<JSONObject> toArrayList(JSONArray arr, Comparator<JSONObject> comparator)
	{
		if (arr == null) {
			return null;
		}
		
		if (comparator == null) {
			return toArrayList(arr);
		}
		
		ArrayList<JSONObject> result = toArrayList(arr);
		
		Collections.sort(result, comparator);
		return result;
	}
	
}
