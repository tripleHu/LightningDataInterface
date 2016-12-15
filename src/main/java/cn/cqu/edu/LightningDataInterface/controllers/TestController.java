package cn.cqu.edu.LightningDataInterface.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import cn.cqu.edu.LightningDataInterface.Tools.FileTools;
import cn.cqu.edu.LightningDataInterface.entity.AreaInfo;
import cn.cqu.edu.LightningDataInterface.services.hibernate.ADTDService;
import cn.cqu.edu.LightningDataInterface.services.hibernate.ADTDServiseForFljsjg2;


@Controller
public class TestController {
	@Autowired
	private ADTDServiseForFljsjg2 adtdServiseForFljsjg2;
	
	@RequestMapping(value = "/")
	@ResponseBody
	public ModelAndView login()  
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		System.out.println("aaaaaaa");
		return mv; 
	}
	/**
	 * 获取雷电密度数据
	 * @param year
	 * @param radium
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value="service/adtd/LightningDensity",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String GetLightningDensityForWholeProvice(int year,int radium,HttpServletRequest request) throws NumberFormatException, IOException, SQLException
	{
		
		System.out.println("客户端IP: "+getIpAddr(request)); 
        System.out.println("LightningDataInterface接口开始处理... ..."); 
		String aString=adtdServiseForFljsjg2.GetLightningDensityForWholeProvice(year, radium);//生成雷电密度数据，并返回生成的文件在服务器内的绝对路径地址
		System.out.println("返回文件："+aString);//打印地址
		String jsonData = new FileTools().ReadFileAsString(aString);//取出文件数据
		JsonObject root = new JsonParser().parse(jsonData).getAsJsonObject();//解析为json
		System.out.println("LightningDataInterface接口处理完成... ..."); 
		return root.toString();	//json转为字符串返回
	}
	
	@RequestMapping(value="inde/inde")
	public ModelAndView Register()
	{
		ModelAndView mv=new ModelAndView();
		mv.setViewName("inde");
		return mv;
	}
	public String getIpAddr(HttpServletRequest request) {
		 String ip = request.getHeader("x-forwarded-for");
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		  ip = request.getHeader("Proxy-Client-IP");
		 }
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		  ip = request.getHeader("WL-Proxy-Client-IP");
		 }
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		  ip = request.getRemoteAddr();
		 }
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		  ip = request.getHeader("http_client_ip");
		 }
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		  ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		 }
		 // 如果是多级代理，那么取第一个ip为客户ip
		 if (ip != null && ip.indexOf(",") != -1) {
		  ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		 }
		 return ip;
		}
}
