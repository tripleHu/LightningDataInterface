package cn.cqu.edu.LightningDataInterface.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvert {
	/**
	* 日期转换成字符串
	* @param date 
	* @return str "yyyy-MM-dd HH:mm:ss"
	*/
	public static String DateToStr(Date date) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String str = format.format(date);
	   return str;
	} 
	/**
	* 日期转换成字符串
	* @param date 
	* @return str "yyyyMMddHHmmss"
	*/
	public static String DateToStr1(Date date) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	   String str = format.format(date);
	   return str;
	} 
	/**
	* 字符串转换成日期
	* @param str "yyyy-MM-dd HH:mm:ss"
	* @return date
	*/
	public static Date StrToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	/**
	* 字符串转换成日期
	* @param str "yyyyMMddHHmmss"
	* @return date
	*/
	public static Date StrToDate1(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
}
