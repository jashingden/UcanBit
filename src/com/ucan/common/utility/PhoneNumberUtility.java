package com.ucan.common.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * 透過正則表達式，判斷是否為手機或家用電話
 * <ol>return false:<li>輸入包含非數字字元<li>輸入全形<li>號碼長度小於9或大於10</ol>
 * <ol>return true:<li>僅輸入半形數字，且符合全部規則</ol>
 * @author star
 * @version 201401208[star] created<br>
 * @version 201401216[李欣駿]	加入判斷大陸手機的方法。<br>
 */
public class PhoneNumberUtility {
	private final static String TAG = "PhoneNumberUtility";
	private final static boolean DEBUG = false;
	
	/**
	 * 判斷兩岸三地手機號碼格式是否正確
	 * @param num
	 * @return
	 */
	public static boolean checkMobilePhoneNumber(String num)
	{
		if(num.length() < 9 || num.length() > 13)
		{
			return false;
		}
		else if(isMobileNum(num) || isCNMobileNum(num))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判斷所有手機號碼與家用電話
	 * @param num
	 * @return 
	 * <ol>true:<li>可能屬於(因為手機號碼太多間無法依依判斷)</ol>
	 * <ol>false:<li>不符合手機與家用號碼規則<li>號碼長度小於9或大於10</ol>
	 */
	public static boolean checkAllPhoneNumber(String num){ 
		if(num.length()<9||num.length()>13){
			if(DEBUG) Log.d(TAG, "Error! num.length():"+num.length());
			return false;
		}
		else if(isWiMAXMobileNum(num)
				||isMobileNum(num)
				||isHomePhoneNumberOne(num)
				||isHomePhoneNumberTwo(num)
				||isHomePhoneNumberThree(num)
				||isHomePhoneNumberFour(num)
				||isHomePhoneNumberFive(num)
				||isHomePhoneNumberSix(num)
				||isHomePhoneNumberSeven(num)
				||isHomePhoneNumberEight(num)
				||isHomePhoneNumberNine(num)
				||isHomePhoneNumberTen(num)
				||isHomePhoneNumberEleven(num)
				||isHomePhoneNumberTwelve(num)
				||isHomePhoneNumberThirteen(num)
				||isHomePhoneNumberFourteen(num)
				||isCNMobileNum(num))
			return true;
		else {
		    if(DEBUG) Log.d(TAG, "Not match any rule! Please check compliance with the rules? Maybe need to add more RULES to match "+num+"! or "+num+" is illegal phone number!");
			return false;
		}
    }
	
	/**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * @param mobile 移动、联通、电信运营商的号码段
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *<p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isCNMobileNum(String num) 
    { 
        String regex = "8[65](\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex, num); 
    } 
    
	/**
	 * 判斷是否為手機號碼
	 * @param num
	 * @return
	 * <li>09開頭
	 * <li>第3碼排除0和4
	 * <li>第4碼開始可以是0-9
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isMobileNum(String num){ 
        String str="[0]{1}[9]{1}[1-3|5-9]{1}[0-9]{7}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isMobileNum:"+m.matches());
        return  m.matches(); 
    }
	
	/**
	 * 判斷是否為WiMAX手機號碼
	 * @param num
	 * @return
	 * <li>0900開頭
	 * <li>第5碼只可以是0-6
	 * <li>第6碼開始填0-9，總共10碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isWiMAXMobileNum(String num){ 
        String str="[0]{1}[9]{1}[0]{1}[0]{1}[0-6]{1}[0-9]{5}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isWiMAXMobileNum:"+m.matches());
        return  m.matches(); 
    }
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>判斷馬祖(0836)xxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberOne(String num){
		String str="[0]{1}[8]{1}[3]{1}[6]{1}[0-9]{5}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberOne:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>判斷烏坵(0826)xxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberTwo(String num){
		String str="[0]{1}[8]{1}[2]{1}[6]{1}[0-9]{5}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberTwo:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>判斷金門(082)xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberThree(String num){
		String str="[0]{1}[8]{1}[2]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberThree:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>判斷台東(089)xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberFour(String num){
		String str="[0]{1}[8]{1}[9]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberFour:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>判斷南投(049)xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberFive(String num){
		String str="[0]{1}[4]{1}[9]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG)  Log.d(TAG, "isHomePhoneNumberFive:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>判斷苗栗(037)xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberSix(String num){
		String str="[0]{1}[3]{1}[7]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberSix:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>桃園(03)[2-4]xxxxxx，總共9碼
	 * <li>新竹(03)[5-6]xxxxxx，總共9碼
	 * <li>花蓮(03)[8]xxxxxx，總共9碼
	 * <li>宜蘭(03)[9]xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberSeven(String num){
		String str="[0]{1}[3]{1}[2-6|8|9]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberSeven:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>彰化(04)[7-8]xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberEight(String num){
		String str="[0]{1}[4]{1}[7|8]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberEight:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>雲林(05)[5-7]xxxxxx，總共9碼
	 * <li>嘉義(05)[2-3]xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberNine(String num){
		String str="[0]{1}[5]{1}[2|3|5-7]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberNine:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>台南(06)[2-7]xxxxxx，總共9碼
	 * <li>澎湖(06)[9]xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberTen(String num){
		String str="[0]{1}[6]{1}[2-7|9]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberTen:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>高雄、東沙、南沙(07)[0-9]xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberEleven(String num){
		String str="[0]{1}[7]{1}[0-9]{7}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberEleven:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>屏東(08)[7-8]xxxxxx，總共9碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberTwelve(String num){
		String str="[0]{1}[8]{1}[7|8]{1}[0-9]{6}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberTwelve:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>基隆、台北(02)[0-9]，總共10碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberThirteen(String num){
		String str="[0]{1}[2]{1}[0-9]{8}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberThirteen:"+m.matches());
        return  m.matches();
	}
	
	/**
	 * 判斷是否為市話號碼
	 * @param num
	 * @return
	 * <li>台中(04)[2-3]xxxxxxx，總共10碼
	 * <li>使用正則表達式判斷，詳細請上Google查詢，因為有點複雜
	 */
	public static boolean isHomePhoneNumberFourteen(String num){
		String str="[0]{1}[4]{1}[2|3]{1}[0-9]{7}";
        Pattern p=Pattern.compile(str); 
        Matcher m=p.matcher(num); 
        if(DEBUG) Log.d(TAG, "isHomePhoneNumberFourteen:"+m.matches());
        return  m.matches();
	}
}
