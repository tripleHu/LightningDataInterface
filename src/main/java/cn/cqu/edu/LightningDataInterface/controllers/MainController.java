package cn.cqu.edu.LightningDataInterface.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import cn.cqu.edu.LightningDataInterface.entity.LigthingActiveStatistic_Month_Year;
import cn.cqu.edu.LightningDataInterface.entity.LigthingCricleActiveStatistic_Month_Year;
import cn.cqu.edu.LightningDataInterface.entity.LigthingRectangleActiveStatistic_Month_Year;
import cn.cqu.edu.LightningDataInterface.entity.SimpleLightingInfo;
import cn.cqu.edu.LightningDataInterface.services.hibernate.ADTDService;
import cn.cqu.edu.LightningDataInterface.services.hibernate.ADTDServiseForFljsjg2;


@Controller
public class MainController {
	@Autowired
	private ADTDServiseForFljsjg2 adtdServiseForFljsjg2;
	
	String separator=System.getProperty("file.separator");//文件路径间隔符，为了windows和linux兼容
	
	SimpleDateFormat Dateformatter = new SimpleDateFormat("yyyy-MM-dd");
	 SimpleDateFormat Monthformat = new SimpleDateFormat("yyyy-MM");
	 SimpleDateFormat Yearformat = new SimpleDateFormat("yyyy");
	 
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
	/**
	 * 选定行政区域雷电活动情况
	 *  Date
	 *  可以为三种类型xxxx代表年xxxx-xx代表年月xxxx-xx-xx代表年月日
	 *  AreaIndex
	 *  0-城口县 1-巫溪县 2- 巫山县 3-开县 4-云阳县 5-奉节县 6-万州区 7-梁平县 8-忠县 9-石柱县 10-垫江县 11-丰都县 12-彭水县 13-黔江区 14-酉阳县 15-秀山县 16-大渡口区 17-武隆县 18-涪陵区 19-长寿区 20-南川区
     * 21-万盛经开区 22-綦江区 23-江津区 24-璧山区 25-北碚区 26-渝北区 27-巴南区 28-南岸区 29-江北区 30-合川区 31-沙坪坝区 32-九龙坡区 33-荣昌区 34-永川区 35-大足区 36-铜梁区 37-潼南县 38-渝中区 39-双桥区 
	 *  40-重庆全市
	 * @param AreaIndex
	 * @param Date
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value="service/adtd/AreaStatistic",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String GetLightningAreaStatistic(int AreaIndex,String Date) throws NumberFormatException, IOException, ParseException, SQLException
	{
		 
		 int DateType=CheckDateType(Date);

		 if(DateType==0)//参数错误
		 {
			 String result="[{\"fail\":\"Parameter is not valid\"}]";
			 return result;
		 }
		 else if(DateType==1)//yyyy型
		 {
			 Date Year = Yearformat.parse(Date);//转换为Date类型
			 LigthingActiveStatistic_Month_Year YearStatistic=adtdServiseForFljsjg2.GetLightningAreaStatisticByYear(AreaIndex, Year);
			 Gson gson = new Gson();  
		      String jsonData = gson.toJson(YearStatistic);  
			 return jsonData;
		 }
		 else if(DateType==2)//yyyy-MM型
		 {
			 Date Month = Monthformat.parse(Date);//转换为Date类型
			 LigthingActiveStatistic_Month_Year MonthStatistic=adtdServiseForFljsjg2.GetLightningAreaStatisticByMonth(AreaIndex, Month);
			 Gson gson = new Gson();  
		      String jsonData = gson.toJson(MonthStatistic);  
			 return jsonData;
		 }
		 else//yyyy-MM-dd型
		 {
			 Date date = Dateformatter.parse(Date);//转换为Date类型
			 adtdServiseForFljsjg2.GetLightningAreaStatisticByDate(AreaIndex, date, date);
			 String path=new FileTools().GetProjectRootPath()+"resources"+separator+"json"+separator+"thunderActive"+separator+"area-"+AreaIndex+"-date-"+Dateformatter.format(date)+".json";
			 String jsonData = new FileTools().ReadFileAsString(path);//取出文件数据
			 return jsonData;
		 }
		
		 
	}
	/**
	 * 生成圆形区域雷电活动统计数据
	 *  *  Date
	 *  可以为三种类型xxxx代表年xxxx-xx代表年月xxxx-xx-xx代表年月日
	 * @param CricleLatitude
	 * @param CricleLongitude
	 * @param Radium
	 * @param Date
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value="service/adtd/CricleStatistic",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String GetLightingCricleStatisticByDate(double CricleLatitude,double CricleLongitude,float Radium,String Date)throws NumberFormatException, IOException, ParseException, SQLException
	{
		int DateType=CheckDateType(Date);

		 if(DateType==0)//参数错误
		 {
			 String result="[{\"fail\":\"Parameter is not valid\"}]";
			 return result;
		 }
		 else if(DateType==1)//yyyy型
		 {
			 Date Year = Yearformat.parse(Date);//转换为Date类型
			 LigthingCricleActiveStatistic_Month_Year YearStatistic=adtdServiseForFljsjg2.GetLightningCricleStatisticByYear(CricleLatitude, CricleLongitude, Radium, Year);
			 Gson gson = new Gson();  
		      String jsonData = gson.toJson(YearStatistic);  
			 return jsonData;
		 }
		 else if(DateType==2)//yyyy-MM型
		 {
			 Date Month = Monthformat.parse(Date);//转换为Date类型
			 LigthingCricleActiveStatistic_Month_Year MonthStatistic=adtdServiseForFljsjg2.GetLightningCricleStatisticByMonth(CricleLatitude, CricleLongitude, Radium, Month);
			 Gson gson = new Gson();  
		      String jsonData = gson.toJson(MonthStatistic);  
			 return jsonData;
		 }
		 else//yyyy-MM-dd型
		 {
			 Date date = Dateformatter.parse(Date);//转换为Date类型
			 String jsonData =adtdServiseForFljsjg2.GetLightingCricleStatisticByDate(CricleLatitude, CricleLongitude, Radium, date);
			
			 return jsonData;
		 }
		
	}
	/**
	 *  生成矩形区域雷电活动统计数据
	 *   *  Date
	 *  可以为三种类型xxxx代表年xxxx-xx代表年月xxxx-xx-xx代表年月日
	 * @param latitudeLower
	 * @param latitudeUpper
	 * @param longitudeLeft
	 * @param longitudeRight
	 * @param Date
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value="service/adtd/RectangleStatistic",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String GetLightingRectangleStatisticByDate(double latitudeLower,double latitudeUpper,double longitudeLeft,double longitudeRight,String Date)throws NumberFormatException, IOException, ParseException, SQLException
	{
		int DateType=CheckDateType(Date);

		 if(DateType==0)//参数错误
		 {
			 String result="[{\"fail\":\"Parameter is not valid\"}]";
			 return result;
		 }
		 else if(DateType==1)//yyyy型
		 {
			 Date Year = Yearformat.parse(Date);//转换为Date类型
			 LigthingRectangleActiveStatistic_Month_Year YearStatistic=adtdServiseForFljsjg2.GetLightningRectangleStatisticByYear(latitudeLower, latitudeUpper, longitudeLeft, longitudeRight, Year);
			 Gson gson = new Gson();  
		      String jsonData = gson.toJson(YearStatistic);  
			 return jsonData;
		 }
		 else if(DateType==2)//yyyy-MM型
		 {
			 Date Month = Monthformat.parse(Date);//转换为Date类型
			 LigthingRectangleActiveStatistic_Month_Year MonthStatistic=adtdServiseForFljsjg2.GetLightningRectangleStatisticByMonth(latitudeLower, latitudeUpper, longitudeLeft, longitudeRight, Month);
			 Gson gson = new Gson();  
		      String jsonData = gson.toJson(MonthStatistic);  
			 return jsonData;
		 }
		 else//yyyy-MM-dd型
		 {
			 Date date = Dateformatter.parse(Date);//转换为Date类型
			 String jsonData =adtdServiseForFljsjg2.GetLightingRectangleStatisticByDate(latitudeLower, latitudeUpper, longitudeLeft, longitudeRight, date);
			
			 return jsonData;
		 }
		
	}
	/**
	 *  返回给定时间段闪电活动数据
	 * @param StartTime
	 * @param EndTime
	 * @return
	 */
	@RequestMapping(value="service/adtd/LightningActiveByTime",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String GetLightningActiveByDatetime(long StartTime,long EndTime)
	{
		
		List<SimpleLightingInfo> LightingInfo=adtdServiseForFljsjg2.GetLightningActiveByDatetime(StartTime, EndTime);
	
		String jsonResult = new Gson().toJson(LightingInfo); 
		if(LightingInfo!=null)
		{
			return jsonResult;
		}
		else
		{
			String a="[{\"fail\":\"no data\"}]";
			return a;
		}
		
	}
	/**
	 * 获取客户端访问的IP地址
	 * @param request
	 * @return
	 */
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
	/**
	 *  根据时间字符串确定其表示类型
	 *  xxxx代表年
	 *	xxxx-xx代表年月
	 *	xxxx-xx-xx代表年月日
	 * @param String Date
	 * @return 类型
	 *  0——不符合要求
	 *  1——年
	 *  2——年月
	 *  3——年月日
	 */
	private int CheckDateType(String Date)
	{
		String[] date=Date.split("-");//将字符串分割
		//xxxx型
		 if(date.length==1)
		 {
			 //年份须在2009-今年
			 if(2008<Integer.valueOf(date[0]).intValue()&&Integer.valueOf(date[0]).intValue()<=Calendar.getInstance().get(Calendar.YEAR))
			 {
				 return 1;//输入字符串表示年
			 }
			 else
			 {
				 return 0;//格式不符
			 }
		 }
		//xxxx-xx型
		 else if(date.length==2)
		 {
			 //年份须在2009-今年
			 if(2008<Integer.valueOf(date[0]).intValue()&&Integer.valueOf(date[0]).intValue()<=Calendar.getInstance().get(Calendar.YEAR))
			 {
				 //如果年份在今年的，月份须小于等于当前月
				 if(Integer.valueOf(date[0]).intValue()==Calendar.getInstance().get(Calendar.YEAR))
				 {
					//月份须小于等于当前月
					 if(1<=Integer.valueOf(date[1]).intValue()&&Integer.valueOf(date[1]).intValue()<=Calendar.getInstance().get(Calendar.MONTH)+1)
					 {
						return 2;//输入字符串表示年月
					 }
					 else//如果月份大于当月
					 {
						 return 0;
					 }
				 }
				 // 年份在小于今年的，月份需要满足1-12
				 else if(1<=Integer.valueOf(date[1]).intValue()&&Integer.valueOf(date[1]).intValue()<=12)
				 {
					 return 2;//输入字符串表示年月
				 }
				 else//输入月份不满足要求的
				 {
					 return 0;
				 }
			 }
			 else//年份不符合要求
			 {
				 return 0;
			 }
		 }
		//xxxx-xx-xx型
		else if(date.length==3)
		 {
			 //年份须在2009-今年
			 if(2008<Integer.valueOf(date[0]).intValue()&&Integer.valueOf(date[0]).intValue()<=Calendar.getInstance().get(Calendar.YEAR))
			 {
				 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				 Date tempdate = null;
				 try 
				 {
					   tempdate = format.parse(Date);
					   //System.out.println(tempdate);
					   if(tempdate.getTime()>new Date().getTime()-2*3600*1000)//时间应在当前时间的两个小时前
					   {
						   return 0;
					   }
				  } 
				  catch (ParseException e) //转换错误，表明格式不正确
				  {
						return 0;
				  }
				  return 3;//输入字符串表示年月日
			 }
		
		 }
		else//date的长度不为1或2或3
		{
			return 0;
		}
		return 0;
	}
}
