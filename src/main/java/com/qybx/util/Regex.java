package com.qybx.util;
/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年12月8日 上午10:12:50   
 */
public class Regex {
	
	public static final String Chinese = "[\u4e00-\u9fa5]";
	
	public static final String English = "[a-zA-Z]";
	
	public static final String Number = "[0-9]+";
	
	public static final String ChineseEnglishNumber = "^[\u4e00-\u9fa5a-zA-Z0-9]+$";
	
	public static final String TwoOrFourChinese ="^[\u4E00-\u9FA5]{2,4}$";
	
	
	
	

}
