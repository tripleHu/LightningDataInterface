package cn.cqu.edu.LightningDataInterface.services.hibernate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.cqu.edu.LightningDataInterface.Tools.DateConvert;
import cn.cqu.edu.LightningDataInterface.Tools.FileTools;
import cn.cqu.edu.LightningDataInterface.Tools.MapPoint;
import cn.cqu.edu.LightningDataInterface.Tools.MapTools;
import cn.cqu.edu.LightningDataInterface.domain.ADTD;

import cn.cqu.edu.LightningDataInterface.entity.AreaInfo;
import cn.cqu.edu.LightningDataInterface.entity.LigthingActiveStatistic_Day;
import cn.cqu.edu.LightningDataInterface.entity.LigthingActiveStatistic_Month_Year;
import cn.cqu.edu.LightningDataInterface.entity.LigthingCricleActiveStatistic_Day;
import cn.cqu.edu.LightningDataInterface.entity.LigthingCricleActiveStatistic_Month_Year;
import cn.cqu.edu.LightningDataInterface.entity.LigthingRectangleActiveStatistic_Day;
import cn.cqu.edu.LightningDataInterface.entity.LigthingRectangleActiveStatistic_Month_Year;
import cn.cqu.edu.LightningDataInterface.entity.SimpleLightingInfo;
import cn.cqu.edu.LightningDataInterface.entity.TimePeriod;
import javassist.expr.NewArray;



@Service
public class ADTDServiseForFljsjg2 extends BaseService
{
	
	@Autowired
	private ADTDService adtdService;
	
	 String separator=System.getProperty("file.separator");//文件路径间隔符，为了windows和linux兼容
	 String BasePath;
	 SimpleDateFormat Dateformatter = new SimpleDateFormat("yyyy-MM-dd");
	 SimpleDateFormat Monthformat = new SimpleDateFormat("yyyy-MM");
	 SimpleDateFormat Yearformat = new SimpleDateFormat("yyyy");
	/**
	 * 获取雷电密度数据
	 * @param year
	 * @param radium
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	@Transactional
	public String GetLightningDensityForWholeProvice(int year,int radium)throws IOException, NumberFormatException, SQLException
	{
		
		BasePath=new FileTools().GetProjectRootPath()+"resources"+separator+"json"+separator+"thunderDistribute"+separator;//设置路径地址
		//判断该文件是否生成，若未生成则开始生成，否则返回
		String path=BasePath+year+"-radium-"+radium+".json";
		File file=new File(path);    	
		if(file.exists())    
		{    
			System.out.println("file exists");//若已经生成则返回
			return path;
		} 
		else//生成该年的所有半径文件
		{
			 GenerateOneYearData(year);
			 return path;
		}
		
	}
	/**
	 *  返回给定时间段闪电活动数据
	 *  但由于adtdService中listbyDate的问题，返回的是StartTime和EndTime之间全天日期的数据
	 *  如StartTime表示2015-01-01 12:00:00 EndTime表示2015-01-02 12:00:00 总计24小时
	 *  但返回的是2015-01-01 至 2015-01-02  总计48小时数据
	 * @param StartTime 距离1970年1月1日的毫秒数
	 * @param EndTime  距离1970年1月1日的毫秒数
	 * @return  给定时间段闪电数据
	 */
	public List<SimpleLightingInfo>GetLightningActiveByDatetime(long StartTime,long EndTime)
	{
		List<SimpleLightingInfo> LightingInfos=new ArrayList<SimpleLightingInfo>();
		
		Date sdate=new Date(StartTime);//将long型时间转换为Date
		Date edate=new Date(EndTime);//将long型时间转换为Date
		List<ADTD> ADTD=adtdService.listbyDate(sdate, edate);
		
		double[] BorderArray=null;//重庆边界坐标信息
		try {
			BorderArray=MapTools.getAreaPointsArray(40);//40为重庆市的索引值
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(ADTD.size()>0)
		{
			for(int i=0;i<ADTD.size();i++)
			{
				MapPoint point=new MapPoint();
				point.setLatitude(ADTD.get(i).getLatitude());
				point.setLongitude(ADTD.get(i).getLongitude());
				boolean InChongQing = MapTools.isInPolygon(point,BorderArray);//判断ADTD数据是否在重庆范围内
				if(!InChongQing)//不在重庆范围内
				{
					continue;
				}
				Calendar TimeCalendar =Calendar.getInstance();//使用Calendar处理时间数据
				TimeCalendar.setTime(ADTD.get(i).getDate());//设置日期
				TimeCalendar.set(Calendar.HOUR_OF_DAY, ADTD.get(i).getHour());//设置小时
				TimeCalendar.set(Calendar.MINUTE, ADTD.get(i).getMinute());//设置分钟
				TimeCalendar.set(Calendar.SECOND, (int)ADTD.get(i).getSecond());//设置秒
		    	
				SimpleLightingInfo Info=new SimpleLightingInfo();
				Info.setDatetime(TimeCalendar.getTime().getTime());
				Info.setIntensity(ADTD.get(i).getIntensity().floatValue());
				Info.setLatitude(ADTD.get(i).getLatitude());
				Info.setLongitude(ADTD.get(i).getLongitude());
				LightingInfos.add(Info);
			}
			return LightingInfos;
		}
		else
		{
			return null;
		}
		
	}
	/**
	 * 按天生成雷电活动统计数据
	 * @param AreaIndex
	 *  0-城口县 1-巫溪县 2- 巫山县 3-开县 4-云阳县 5-奉节县 6-万州区 7-梁平县 8-忠县 9-石柱县 10-垫江县 11-丰都县 12-彭水县 13-黔江区 14-酉阳县 15-秀山县 16-大渡口区 17-武隆县 18-涪陵区 19-长寿区 20-南川区
     * 21-万盛经开区 22-綦江区 23-江津区 24-璧山区 25-北碚区 26-渝北区 27-巴南区 28-南岸区 29-江北区 30-合川区 31-沙坪坝区 32-九龙坡区 33-荣昌区 34-永川区 35-大足区 36-铜梁区 37-潼南县 38-渝中区 39-双桥区 
	 *  40-重庆全市
	 * @param StartDate
	 * @param EndDate
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	@Transactional
	public void GetLightningAreaStatisticByDate(int AreaIndex,Date StartDate,Date EndDate)throws IOException, NumberFormatException, SQLException
	{
		//System.out.println(StartDate);
		//System.out.println(EndDate);
		 //若index超界，则直接令index为0
		  if(AreaIndex<0||AreaIndex>=41)
		  {
			  AreaIndex=0;
		  }
		List<ADTD> ADTD; 
	
		String sDate=StartDate.toString();
		String eEnd=EndDate.toString();
		int Days=(int)((EndDate.getTime()-StartDate.getTime())/(1000*60*60*24))+1;//计算开始时间到结束时间之间的天数
		
		//从开始日期开始生成文件
		for(long day=0;day<Days;day++)
		{
			Date date=new Date(StartDate.getTime()+(1000*60*60*24)*day);//从开始日期开始生成文件
			
			Calendar   cal=Calendar.getInstance();//获取当前日期 
			cal.set(Calendar.HOUR_OF_DAY, 0);//设置小时
			cal.set(Calendar.MINUTE, 0);//设置分钟
			cal.set(Calendar.SECOND, 0);//设置秒
			cal.set(Calendar.MILLISECOND, 0);//设置毫秒
			
			//如果待生成文件的日期大于等于当前日期，则不生成该文件，直接跳过
			if(date.getTime()>=cal.getTime().getTime())
			{
				continue;
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String dateString = formatter.format(date);//生成日期字符串
		    //定义一系列统计值
		    float AreaMaxIntensity=0;//区域最大雷电值（不区分正负）
		    float AreaPositiveMaxIntensity=0;//区域最大正雷电值
		    float AreaNegativeMaxIntensity=0;//区域最大负雷电值
		    int AreaLightningCount=0;//区域雷电次数
		    int AreaPositiveCount=0;//区域正雷电次数
		    int AreaNegativeCount=0;//区域负雷电次数
		    Date LightingStartTime=new Date();//雷电初时
		    Date LightingEndTime=new Date();//雷电终时
		    //判断文件是否生成
		    String path=new FileTools().GetProjectRootPath()+"resources"+separator+"json"+separator+"thunderActive"+separator+"area-"+AreaIndex+"-date-"+dateString+".json";
			File file=new File(path);    	
			if(file.exists())    //文件存在
			{    
				//System.out.println("file exists");
				continue;
			} 
			else
			{
				 file.createNewFile();//生成文件
			}
			
			String aString2=new FileTools().ReadFileAsString((new FileTools().GetProjectRootPath()+"resources"+separator+"area.json"));//获取本地文件里的区域信息
			AreaInfo foo[] = new Gson().fromJson(aString2, AreaInfo[].class);//将json文本信息转换为对象
			AreaInfo AreaTemp=foo[AreaIndex];//获取待查询区域信息
			
			ADTD=adtdService.listbyDate(date, date);//获取ADTD数据
			if(ADTD.size()>0)//当天有雷电数据时,初始化雷电初时和雷电终时
			{
            	Calendar Tempcalendar =Calendar.getInstance();//使用Calendar处理时间数据
 				Tempcalendar.setTime(ADTD.get(0).getDate());//设置日期
 				Tempcalendar.set(Calendar.HOUR_OF_DAY, 23);//设置小时
 				Tempcalendar.set(Calendar.MINUTE, 59);//设置分钟
 				Tempcalendar.set(Calendar.SECOND, 59);//设置秒
 				LightingStartTime=Tempcalendar.getTime();//初始化雷电初时
 				Tempcalendar.set(Calendar.HOUR_OF_DAY, 0);//设置小时
 				Tempcalendar.set(Calendar.MINUTE, 0);//设置分钟
 				Tempcalendar.set(Calendar.SECOND, 0);//设置秒
 				LightingEndTime=Tempcalendar.getTime();//初始化雷电终时
			}
			
			double []AreaBoundaryArray=MapTools.getAreaPointsArray(AreaIndex); //获取区域边界数组
			
			FileWriter fos=new FileWriter(path);
			//开始写文件
			String title="["+"";
		    fos.write(title);
		    fos.write("\r\n");
		    String title2="{"+"\"areaid\":"+AreaTemp.getfId()+",\"name\":\""+AreaTemp.getName()+"\","+"\"date\":\""+dateString+"\","+"\"time\":"+"[";                   
			fos.write(title2);
			fos.write("\r\n"); 
		    //遍历所有时间段，一天共24个时间段每1小时一个时间段
			for(int section_index=0;section_index<24;section_index++)
			{
				//确定是否是section的第一行数据（确保json格式的正确）
				boolean bFirstTimeInThisSection=true;
				  //定义一系列统计值
				int count=0;
				int PositiveCount=0;
				int NegativeCount=0;
				float MaxIntensity=0;
				float PositiveMaxIntensity=0;
				float NegativeMaxIntensity=0;
				
				String StartTime=section_index+":"+"00";
				String EndTime=section_index+1+":"+"00";
				String  str ="{\"section\":"+section_index+","+"\"starttime\":\""+StartTime+"\","+"\"endtime\":\""+EndTime+"\","+"\"positions\":[";
	    		fos.write(str);
	            fos.write("\r\n"); 
	            
	           
	           
				//遍历ADTD数据
				for(int data_index=0;data_index<ADTD.size();data_index++)
				{
					//ADTD数据是否在section_index的小时时间段内
					if((int)(section_index)==ADTD.get(data_index).getHour())
					{
						
				    	
				    
				    	//判断ADTD数据是否在该区域内
						MapPoint PointForCheck=new MapPoint();//初始化经纬度
						PointForCheck.setLatitude(ADTD.get(data_index).getLatitude());//设置ADTD数据经纬度
						PointForCheck.setLongitude(ADTD.get(data_index).getLongitude());//设置ADTD数据经纬度
						//ADTD数据是否在该区域内
						if(MapTools.isInPolygon(PointForCheck, AreaBoundaryArray))
						{
							//获取当前ADTD数据的时间
							Calendar Datecalendar =Calendar.getInstance();//使用Calendar处理时间数据
					    	Datecalendar.setTime(ADTD.get(data_index).getDate());//设置日期
					    	Datecalendar.set(Calendar.HOUR_OF_DAY, ADTD.get(data_index).getHour());//设置小时
					    	Datecalendar.set(Calendar.MINUTE, ADTD.get(data_index).getMinute());//设置分钟
					    	Datecalendar.set(Calendar.SECOND, (int)ADTD.get(data_index).getSecond());//设置秒
							if(LightingStartTime.getTime()>=Datecalendar.getTime().getTime())//计算雷电初时
					    	{
					    		LightingStartTime=Datecalendar.getTime();
					    	}
					    	if(LightingEndTime.getTime()<=Datecalendar.getTime().getTime())//计算雷电终时
					    	{
					    		LightingEndTime=Datecalendar.getTime();
					    	}
							//若生成的不是第一行数据（确保json格式的正确）
							if(!bFirstTimeInThisSection)
							{
								fos.write(",");
								fos.write("\r\n"); 
							}
							String  str1 ="{\"latitude\":"+ADTD.get(data_index).getLatitude()+","+"\"longitude\":"+ADTD.get(data_index).getLongitude()+","+"\"intensity\":"+ADTD.get(data_index).getIntensity()+",\"datetime\":"+Datecalendar.getTime().getTime()+"}";
				    		fos.write(str1);
				    		bFirstTimeInThisSection=false;
				    		
				    		//计算统计数据，雷电次数
				    		count++;
				    		AreaLightningCount++;
				    		if(ADTD.get(data_index).getIntensity().floatValue()>=0)
				    		{
				    			PositiveCount++;
				    			AreaPositiveCount++;
				    		}
				    		else
				    		{
				    			NegativeCount++;
				    			AreaNegativeCount++;
				    		}
				    		//找出section和Area里雷电幅值，雷电正幅值，雷电负幅值
				    		AreaMaxIntensity=Math.max(AreaMaxIntensity, Math.abs(ADTD.get(data_index).getIntensity().floatValue()));
				    		AreaPositiveMaxIntensity=Math.max(AreaPositiveMaxIntensity, ADTD.get(data_index).getIntensity().floatValue());
				    		AreaNegativeMaxIntensity=Math.min(AreaNegativeMaxIntensity, ADTD.get(data_index).getIntensity().floatValue());
				    		
				    		MaxIntensity=Math.max(MaxIntensity, Math.abs(ADTD.get(data_index).getIntensity().floatValue()));
				    		PositiveMaxIntensity=Math.max(PositiveMaxIntensity, ADTD.get(data_index).getIntensity().floatValue());
				    		NegativeMaxIntensity=Math.min(NegativeMaxIntensity, ADTD.get(data_index).getIntensity().floatValue());
						}
					}
				}
				fos.write("],");
				fos.write("\r\n"); 
				String title3;
				//写入时间段统计信息
				if(section_index==23)//最后一个时间段，行尾加反方括号和逗号
					title3="\"LightningCount\":"+count+",\"PositiveCount\":\""+PositiveCount+"\","+"\"NegativeCount\":"+NegativeCount+","+"\"MaxIntensity\":"+MaxIntensity+","+"\"PositiveMaxIntensity\":"+PositiveMaxIntensity+","+"\"NegativeMaxIntensity\":"+NegativeMaxIntensity+"}],";                   
				else//中间时间段，行尾加逗号
					title3="\"LightningCount\":"+count+",\"PositiveCount\":\""+PositiveCount+"\","+"\"NegativeCount\":"+NegativeCount+","+"\"MaxIntensity\":"+MaxIntensity+","+"\"PositiveMaxIntensity\":"+PositiveMaxIntensity+","+"\"NegativeMaxIntensity\":"+NegativeMaxIntensity+"},";                   
				fos.write(title3);
				fos.write("\r\n"); 
			}
			//写入区域统计信息
			String title4;
			if(ADTD.size()>0)//当天有雷电数据时
			{
				title4="\"AreaMaxIntensity\":"+AreaMaxIntensity+","+"\"AreaPositiveMaxIntensity\":"+AreaPositiveMaxIntensity+","+"\"AreaNegativeMaxIntensity\":"+AreaNegativeMaxIntensity+",\"AreaLightningCount\":\""+AreaLightningCount+"\","+"\"AreaPositiveCount\":"+AreaPositiveCount+","+"\"AreaNegativeCount\":"+AreaNegativeCount+",\"LightingStartTime\":\""+DateConvert.DateToStr(LightingStartTime)+"\","+"\"LightingEndTime\":\""+DateConvert.DateToStr(LightingEndTime)+"\"";                   
			}
			else//当天无雷电数据时,雷电初时和雷电终时设置为0
			{
				title4="\"AreaMaxIntensity\":"+AreaMaxIntensity+","+"\"AreaPositiveMaxIntensity\":"+AreaPositiveMaxIntensity+","+"\"AreaNegativeMaxIntensity\":"+AreaNegativeMaxIntensity+",\"AreaLightningCount\":\""+AreaLightningCount+"\","+"\"AreaPositiveCount\":"+AreaPositiveCount+","+"\"AreaNegativeCount\":"+AreaNegativeCount+",\"LightingStartTime\":\""+0+"\","+"\"LightingEndTime\":\""+0+"\"";                   
			}
			fos.write(title4);
			fos.write("\r\n"); 
			//结束写文件
			fos.write("}]");
			fos.write("\r\n"); 
			fos.close();
		}
	}
	/**
	 * 按月生成雷电活动统计数据
	 * @param AreaIndex
	 *  0-城口县 1-巫溪县 2- 巫山县 3-开县 4-云阳县 5-奉节县 6-万州区 7-梁平县 8-忠县 9-石柱县 10-垫江县 11-丰都县 12-彭水县 13-黔江区 14-酉阳县 15-秀山县 16-大渡口区 17-武隆县 18-涪陵区 19-长寿区 20-南川区
     * 21-万盛经开区 22-綦江区 23-江津区 24-璧山区 25-北碚区 26-渝北区 27-巴南区 28-南岸区 29-江北区 30-合川区 31-沙坪坝区 32-九龙坡区 33-荣昌区 34-永川区 35-大足区 36-铜梁区 37-潼南县 38-渝中区 39-双桥区 
	 *  40-重庆全市
	 * @param StartDate
	 * @param EndDate
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws ParseException 
	 */
	@Transactional
	public LigthingActiveStatistic_Month_Year GetLightningAreaStatisticByMonth(int AreaIndex,Date Month) throws NumberFormatException, IOException, SQLException, ParseException
	{
	    
		Date FirstDay=Month; //获取Month的第一天
		Calendar   cal=Calendar.getInstance();//获取当前日期 
		cal.setTime(Month);
		//为了获得Month的最后一天，首先将月份加1，之后将天数减1，便能得到Month的最后一天
		cal.add(Calendar.MONTH, +1);
		Date NextMonthFirstDay=cal.getTime();//Month下个月的第一天
		cal.add(Calendar.DATE, -1);
		Date LastDay=cal.getTime(); //获取Month的最后一天
		GetLightningAreaStatisticByDate(AreaIndex,FirstDay,LastDay);
		int days=cal.getActualMaximum(Calendar.DATE);
		
		
		int areaid=AreaIndex;//区域索引
		String name="无数据";//区域名称
		String StatisticDate=Monthformat.format(Month);//日期
		float AreaMaxIntensity=0;//雷电幅值
		float AreaPositiveMaxIntensity=0;//雷电正幅值
		float AreaNegativeMaxIntensity=0;//雷电负幅值
		int AreaLightningCount=0;//雷电次数
		int AreaPositiveCount=0;//正闪电次数
		int AreaNegativeCount=0;//负闪电次数
		int LightingDays=0;//雷电天数
		String LightingStartTime="2099-12-31 00:00:00";//闪电开始时间
		String LightingEndTime="1970-01-01 00:00:00";//闪电结束时间
		List<TimePeriod> PeriodInfo=new ArrayList<TimePeriod>();
		//遍历该月的所有天
		for(long day=0;day<days;day++)
		{
			Date date=new Date(FirstDay.getTime()+(1000*60*60*24)*day);//设置取数据的查询日期
			Calendar CurDate=Calendar.getInstance();//获取当前日期 
			CurDate.set(Calendar.HOUR_OF_DAY, 0);//设置小时
			CurDate.set(Calendar.MINUTE, 0);//设置分钟
			CurDate.set(Calendar.SECOND, 0);//设置秒
			CurDate.set(Calendar.MILLISECOND, 0);//设置毫秒
			//如果查询日期大于当前日期，则不查询该文件，直接跳过
			if(date.getTime()>=CurDate.getTime().getTime())
			{
				continue;
			}
			
			String path=new FileTools().GetProjectRootPath()+"resources"+separator+"json"+separator+"thunderActive"+separator+"area-"+AreaIndex+"-date-"+Dateformatter.format(date)+".json";
			
			String jsonData = new FileTools().ReadFileAsString(path);//取出单独一天的文件数据
			LigthingActiveStatistic_Day foo[] = new Gson().fromJson(jsonData, LigthingActiveStatistic_Day[].class);//将json文本信息转换为对象
			TimePeriod temp=new TimePeriod();
			name=foo[0].getName();
			//取出单独一天的雷电统计文件中所需的数据
			temp.setDate(Dateformatter.format(date));//日期
			
			temp.setLightningCount(foo[0].getAreaLightningCount());//闪电次数
			temp.setPositiveCount(foo[0].getAreaPositiveCount());//正闪电次数
			temp.setNegativeCount(foo[0].getAreaNegativeCount());//负闪电次数
			
			temp.setMaxIntensity(foo[0].getAreaMaxIntensity());//幅值
			temp.setPositiveMaxIntensity(foo[0].getAreaPositiveMaxIntensity());//正幅值
			temp.setNegativeMaxIntensity(foo[0].getAreaNegativeMaxIntensity());//负幅值
			
			if(foo[0].getAreaLightningCount()>0)//设置雷电天数，由于是单日数据，所以雷电数大于0时设置雷电天数为1
			{
				temp.setLightingDays(1);
			}
			else 
			{
				temp.setLightingDays(0);
			}
			
			
			AreaLightningCount += foo[0].getAreaLightningCount();//累加雷电次数
			AreaPositiveCount += foo[0].getAreaPositiveCount();//累加正闪电次数
			AreaNegativeCount += foo[0].getAreaNegativeCount();//累加负闪电次数
			
			if(foo[0].getAreaLightningCount()>0)//雷电数大于0时才有计算雷电初终时的必要
			{
				temp.setLightingStartTime(foo[0].getLightingStartTime());//雷电初时
				temp.setLightingEndTime(foo[0].getLightingEndTime());//雷电终时
				LightingDays++;
			}
			
			PeriodInfo.add(temp);//添加到数组中
			
			if(foo[0].getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingStartTime).getTime()>=DateConvert.StrToDate(foo[0].getLightingStartTime()).getTime())//确定当月雷电初时
			{
				LightingStartTime=foo[0].getLightingStartTime();
			}
			
			if(foo[0].getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingEndTime).getTime()<=DateConvert.StrToDate(foo[0].getLightingEndTime()).getTime())//确定当月雷电终时
			{
				LightingEndTime=foo[0].getLightingEndTime();
			}
			
			//找出section和Area里雷电幅值，雷电正幅值，雷电负幅值
    		AreaMaxIntensity=Math.max(AreaMaxIntensity, Math.abs( foo[0].getAreaMaxIntensity()));
    		AreaPositiveMaxIntensity=Math.max(AreaPositiveMaxIntensity,  foo[0].getAreaPositiveMaxIntensity());
    		AreaNegativeMaxIntensity=Math.min(AreaNegativeMaxIntensity,  foo[0].getAreaNegativeMaxIntensity());
		}
		//生成返回的对象
		LigthingActiveStatistic_Month_Year MonthStatistic=new  LigthingActiveStatistic_Month_Year();
		MonthStatistic.setAreaid(areaid);
		MonthStatistic.setName(name);
		MonthStatistic.setDate(StatisticDate);
		MonthStatistic.setAreaMaxIntensity(AreaMaxIntensity);
		MonthStatistic.setAreaPositiveMaxIntensity(AreaPositiveMaxIntensity);
		MonthStatistic.setAreaNegativeMaxIntensity(AreaNegativeMaxIntensity);
		MonthStatistic.setAreaLightningCount(AreaLightningCount);
		MonthStatistic.setAreaPositiveCount(AreaPositiveCount);
		MonthStatistic.setAreaNegativeCount(AreaNegativeCount);
		MonthStatistic.setLightingStartTime(LightingStartTime);
		MonthStatistic.setLightingEndTime(LightingEndTime);
		MonthStatistic.setTime(PeriodInfo);
		MonthStatistic.setLightingDays(LightingDays);
		
		return MonthStatistic;
		
	}
	/**
	 * 按年生成雷电活动统计数据
	 * @param AreaIndex
	 *  0-城口县 1-巫溪县 2- 巫山县 3-开县 4-云阳县 5-奉节县 6-万州区 7-梁平县 8-忠县 9-石柱县 10-垫江县 11-丰都县 12-彭水县 13-黔江区 14-酉阳县 15-秀山县 16-大渡口区 17-武隆县 18-涪陵区 19-长寿区 20-南川区
     * 21-万盛经开区 22-綦江区 23-江津区 24-璧山区 25-北碚区 26-渝北区 27-巴南区 28-南岸区 29-江北区 30-合川区 31-沙坪坝区 32-九龙坡区 33-荣昌区 34-永川区 35-大足区 36-铜梁区 37-潼南县 38-渝中区 39-双桥区 
	 *  40-重庆全市
	 * @param StartDate
	 * @param EndDate
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws ParseException 
	 */
	@Transactional
	public LigthingActiveStatistic_Month_Year GetLightningAreaStatisticByYear(int AreaIndex,Date Year) throws NumberFormatException, IOException, SQLException, ParseException
	{
	    
		Date FirstDay=Year; //获取Year的第一天
		Calendar   cal=Calendar.getInstance();//获取当前日期 
		cal.setTime(Year);
		//为了获得Year的最后一天，首先将年加1，之后将天数减1，便能得到Year的最后一天
		cal.add(Calendar.YEAR, +1);
		Date NextMonthFirstDay=cal.getTime();//下一个Year的第一天
		cal.add(Calendar.DATE, -1);
		Date LastDay=cal.getTime(); //获取Year的最后一天
		GetLightningAreaStatisticByDate(AreaIndex,FirstDay,LastDay);
		int Months=cal.getActualMaximum(Calendar.MONTH);
		
		
		int areaid=AreaIndex;//区域索引
		String name="无数据";//区域名称
		String StatisticDate=Yearformat.format(Year);//日期
		float AreaMaxIntensity=0;//雷电幅值
		float AreaPositiveMaxIntensity=0;//雷电正幅值
		float AreaNegativeMaxIntensity=0;//雷电负幅值
		int AreaLightningCount=0;//雷电次数
		int AreaPositiveCount=0;//正闪电次数
		int AreaNegativeCount=0;//负闪电次数
		int LightingDays=0;//雷电天数
		String LightingStartTime="2099-12-31 00:00:00";//闪电开始时间
		String LightingEndTime="1970-01-01 00:00:00";//闪电结束时间
		List<TimePeriod> PeriodInfo=new ArrayList<TimePeriod>();
		//遍历该年的所有月
		for(int month=0;month<=Months;month++)
		{
			Calendar   calendar=Calendar.getInstance();//获取当前日期 
			calendar.setTime(Year);
			calendar.set(Calendar.MONTH, month);
			Date date=calendar.getTime();//设置取数据的查询日期
			
			
			Calendar CurDate=Calendar.getInstance();//获取当前日期 
			CurDate.set(Calendar.HOUR_OF_DAY, 0);//设置小时
			CurDate.set(Calendar.MINUTE, 0);//设置分钟
			CurDate.set(Calendar.SECOND, 0);//设置秒
			CurDate.set(Calendar.MILLISECOND, 0);//设置毫秒
			//如果查询日期大于当前日期，则不查询，直接跳过
			if(date.getTime()>=CurDate.getTime().getTime())
			{
				continue;
			}

			LigthingActiveStatistic_Month_Year MonthStatistic=GetLightningAreaStatisticByMonth(AreaIndex, date);//获取一个月的统计信息
			
			TimePeriod temp=new TimePeriod();
			name=MonthStatistic.getName();
			//取出单独一个月的雷电统计数据
			temp.setDate(Monthformat.format(date));//日期
			
			temp.setLightningCount(MonthStatistic.getAreaLightningCount());//闪电次数
			temp.setPositiveCount(MonthStatistic.getAreaPositiveCount());//正闪电次数
			temp.setNegativeCount(MonthStatistic.getAreaNegativeCount());//负闪电次数
			
			temp.setMaxIntensity(MonthStatistic.getAreaMaxIntensity());//幅值
			temp.setPositiveMaxIntensity(MonthStatistic.getAreaPositiveMaxIntensity());//正幅值
			temp.setNegativeMaxIntensity(MonthStatistic.getAreaNegativeMaxIntensity());//负幅值
			temp.setLightingDays(MonthStatistic.getLightingDays());
			
			
			AreaLightningCount += MonthStatistic.getAreaLightningCount();//累加雷电次数
			AreaPositiveCount += MonthStatistic.getAreaPositiveCount();//累加正闪电次数
			AreaNegativeCount += MonthStatistic.getAreaNegativeCount();//累加负闪电次数
			LightingDays+=MonthStatistic.getLightingDays();//雷电天数//累加雷电天数
			
			if(MonthStatistic.getAreaLightningCount()>0)//雷电数大于0时才有计算雷电初终时的必要
			{
				
				temp.setLightingStartTime(MonthStatistic.getLightingStartTime());//雷电初时
				temp.setLightingEndTime(MonthStatistic.getLightingEndTime());//雷电终时
			}
			
			PeriodInfo.add(temp);//添加到数组中
			
			if(MonthStatistic.getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingStartTime).getTime()>=DateConvert.StrToDate(MonthStatistic.getLightingStartTime()).getTime())//确定当月雷电初时
			{
				LightingStartTime=MonthStatistic.getLightingStartTime();
			}
			
			if(MonthStatistic.getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingEndTime).getTime()<=DateConvert.StrToDate(MonthStatistic.getLightingEndTime()).getTime())//确定当月雷电终时
			{
				LightingEndTime=MonthStatistic.getLightingEndTime();
			}
			
			//找出section和Area里雷电幅值，雷电正幅值，雷电负幅值
    		AreaMaxIntensity=Math.max(AreaMaxIntensity, Math.abs( MonthStatistic.getAreaMaxIntensity()));
    		AreaPositiveMaxIntensity=Math.max(AreaPositiveMaxIntensity,  MonthStatistic.getAreaPositiveMaxIntensity());
    		AreaNegativeMaxIntensity=Math.min(AreaNegativeMaxIntensity,  MonthStatistic.getAreaNegativeMaxIntensity());
		}
		//生成返回的对象
		LigthingActiveStatistic_Month_Year YearStatistic=new  LigthingActiveStatistic_Month_Year();
		YearStatistic.setAreaid(areaid);
		YearStatistic.setName(name);
		YearStatistic.setDate(StatisticDate);
		YearStatistic.setAreaMaxIntensity(AreaMaxIntensity);
		YearStatistic.setAreaPositiveMaxIntensity(AreaPositiveMaxIntensity);
		YearStatistic.setAreaNegativeMaxIntensity(AreaNegativeMaxIntensity);
		YearStatistic.setAreaLightningCount(AreaLightningCount);
		YearStatistic.setAreaPositiveCount(AreaPositiveCount);
		YearStatistic.setAreaNegativeCount(AreaNegativeCount);
		YearStatistic.setLightingStartTime(LightingStartTime);
		YearStatistic.setLightingEndTime(LightingEndTime);
		YearStatistic.setTime(PeriodInfo);
		YearStatistic.setLightingDays(LightingDays);
		
		return YearStatistic;
		
	}
	/**
	 * 按天生成指定圆形区域里的雷电活动统计数据
	 * @param CricleLatitude
	 * @param CricleLongitude
	 * @param Radium
	 * @param Date
	 */
	@Transactional
	public String GetLightingCricleStatisticByDate(double CricleLatitude,double CricleLongitude,float Radium,Date Date)
	{
		List<ADTD> ADTD=new ArrayList<ADTD>();
		Radium=Radium*1000;
		double a[]=MapTools.getAround(CricleLatitude, CricleLongitude, Radium);
		float latitudeLower= (float)a[0];
		float latitudeUpper= (float)a[2];
		float longitudeLeft= (float)a[1];
		float longitudeRight= (float)a[3];
		List<ADTD> TempADTD =adtdService.listbyDateAndArea(Date, Date,latitudeLower,latitudeUpper,longitudeLeft,longitudeRight);
		
		String JsonString;
		
	    
		for(int i=0;i<TempADTD.size();i++)
		{
			if(MapTools.GetDistance(CricleLongitude, CricleLatitude, TempADTD.get(i).getLongitude(), TempADTD.get(i).getLatitude())<=Radium)
			{
				ADTD.add(TempADTD.get(i));
			}
		}
		
		//开始写JSON
		JsonString="["+"";
		JsonString+="{"+"\"CricleLatitude\":"+CricleLatitude+",\"CricleLongitude\":"+CricleLongitude+",\"Radium\":"+Radium/1000+","+"\"date\":\""+Dateformatter.format(Date)+"\","+"\"time\":"+"[";                   
			
		JsonString=GenJsonForLigRect_CricleActiveStatisticByDate(JsonString, ADTD);
		return JsonString;
	}
	/**
	 * 按月生成指定圆形区域里的雷电活动统计数据
	 * @param CricleLatitude
	 * @param CricleLongitude
	 * @param Radium
	 * @param Date
	 * @return
	 */
	public LigthingCricleActiveStatistic_Month_Year GetLightningCricleStatisticByMonth(double CricleLatitude,double CricleLongitude,float Radium,Date Month)
	{
		Date FirstDay=Month; //获取Month的第一天
		Calendar   cal=Calendar.getInstance();//获取当前日期 
		cal.setTime(Month);
		//为了获得Month的最后一天，首先将月份加1，之后将天数减1，便能得到Month的最后一天
		cal.add(Calendar.MONTH, +1);
		Date NextMonthFirstDay=cal.getTime();//Month下个月的第一天
		cal.add(Calendar.DATE, -1);
		Date LastDay=cal.getTime(); //获取Month的最后一天
		
		int days=cal.getActualMaximum(Calendar.DATE);
		
		
		
		String StatisticDate=Monthformat.format(Month);//日期
		float AreaMaxIntensity=0;//雷电幅值
		float AreaPositiveMaxIntensity=0;//雷电正幅值
		float AreaNegativeMaxIntensity=0;//雷电负幅值
		int AreaLightningCount=0;//雷电次数
		int AreaPositiveCount=0;//正闪电次数
		int AreaNegativeCount=0;//负闪电次数
		int LightingDays=0;//雷电天数
		String LightingStartTime="2099-12-31 00:00:00";//闪电开始时间
		String LightingEndTime="1970-01-01 00:00:00";//闪电结束时间
		List<TimePeriod> PeriodInfo=new ArrayList<TimePeriod>();
		//遍历该月的所有天
		for(long day=0;day<days;day++)
		{
			Date date=new Date(FirstDay.getTime()+(1000*60*60*24)*day);//设置取数据的查询日期
			Calendar CurDate=Calendar.getInstance();//获取当前日期 
			CurDate.set(Calendar.HOUR_OF_DAY, 0);//设置小时
			CurDate.set(Calendar.MINUTE, 0);//设置分钟
			CurDate.set(Calendar.SECOND, 0);//设置秒
			CurDate.set(Calendar.MILLISECOND, 0);//设置毫秒
			//如果查询日期大于当前日期，则不查询该天，直接跳过
			if(date.getTime()>=CurDate.getTime().getTime())
			{
				continue;
			}

			String jsonData = GetLightingCricleStatisticByDate(CricleLatitude, CricleLongitude, Radium, date);//取出单独一天的文件数据
			LigthingCricleActiveStatistic_Day foo[] = new Gson().fromJson(jsonData, LigthingCricleActiveStatistic_Day[].class);//将json文本信息转换为对象
			TimePeriod temp=new TimePeriod();
			
			//取出单独一天的雷电统计文件中所需的数据
			temp.setDate(Dateformatter.format(date));//日期
			
			temp.setLightningCount(foo[0].getAreaLightningCount());//闪电次数
			temp.setPositiveCount(foo[0].getAreaPositiveCount());//正闪电次数
			temp.setNegativeCount(foo[0].getAreaNegativeCount());//负闪电次数
			
			temp.setMaxIntensity(foo[0].getAreaMaxIntensity());//幅值
			temp.setPositiveMaxIntensity(foo[0].getAreaPositiveMaxIntensity());//正幅值
			temp.setNegativeMaxIntensity(foo[0].getAreaNegativeMaxIntensity());//负幅值
			
			if(foo[0].getAreaLightningCount()>0)//设置雷电天数，由于是单日数据，所以雷电数大于0时设置雷电天数为1
			{
				temp.setLightingDays(1);
			}
			else 
			{
				temp.setLightingDays(0);
			}
			
			
			AreaLightningCount += foo[0].getAreaLightningCount();//累加雷电次数
			AreaPositiveCount += foo[0].getAreaPositiveCount();//累加正闪电次数
			AreaNegativeCount += foo[0].getAreaNegativeCount();//累加负闪电次数
			
			if(foo[0].getAreaLightningCount()>0)//雷电数大于0时才有计算雷电初终时的必要
			{
				
				temp.setLightingStartTime(foo[0].getLightingStartTime());//雷电初时
				temp.setLightingEndTime(foo[0].getLightingEndTime());//雷电终时
				LightingDays++;
			}
			
			PeriodInfo.add(temp);//添加到数组中
			
			if(foo[0].getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingStartTime).getTime()>=DateConvert.StrToDate(foo[0].getLightingStartTime()).getTime())//确定当月雷电初时
			{
				LightingStartTime=foo[0].getLightingStartTime();
			}
			
			if(foo[0].getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingEndTime).getTime()<=DateConvert.StrToDate(foo[0].getLightingEndTime()).getTime())//确定当月雷电终时
			{
				LightingEndTime=foo[0].getLightingEndTime();
			}
			
			//找出section和Area里雷电幅值，雷电正幅值，雷电负幅值
    		AreaMaxIntensity=Math.max(AreaMaxIntensity, Math.abs( foo[0].getAreaMaxIntensity()));
    		AreaPositiveMaxIntensity=Math.max(AreaPositiveMaxIntensity,  foo[0].getAreaPositiveMaxIntensity());
    		AreaNegativeMaxIntensity=Math.min(AreaNegativeMaxIntensity,  foo[0].getAreaNegativeMaxIntensity());
		}
		//生成返回的对象
		LigthingCricleActiveStatistic_Month_Year MonthStatistic=new  LigthingCricleActiveStatistic_Month_Year();
		MonthStatistic.setCricleLatitude(CricleLatitude);
		MonthStatistic.setCricleLongitude(CricleLongitude);
		MonthStatistic.setRadium(Radium);
		MonthStatistic.setDate(StatisticDate);
		MonthStatistic.setAreaMaxIntensity(AreaMaxIntensity);
		MonthStatistic.setAreaPositiveMaxIntensity(AreaPositiveMaxIntensity);
		MonthStatistic.setAreaNegativeMaxIntensity(AreaNegativeMaxIntensity);
		MonthStatistic.setAreaLightningCount(AreaLightningCount);
		MonthStatistic.setAreaPositiveCount(AreaPositiveCount);
		MonthStatistic.setAreaNegativeCount(AreaNegativeCount);
		MonthStatistic.setLightingStartTime(LightingStartTime);
		MonthStatistic.setLightingEndTime(LightingEndTime);
		MonthStatistic.setTime(PeriodInfo);
		MonthStatistic.setLightingDays(LightingDays);
		
		return MonthStatistic;
	}
	/**
	 *  按年生成圆形区域雷电活动统计数据
	 * @param CricleLatitude
	 * @param CricleLongitude
	 * @param Radium
	 * @param Year
	 * @return
	 */
	@Transactional
	public LigthingCricleActiveStatistic_Month_Year GetLightningCricleStatisticByYear(double CricleLatitude,double CricleLongitude,float Radium,Date Year)
	{
	    
		Date FirstDay=Year; //获取Year的第一天
		Calendar   cal=Calendar.getInstance();//获取当前日期 
		cal.setTime(Year);
		//为了获得Year的最后一天，首先将年加1，之后将天数减1，便能得到Year的最后一天
		cal.add(Calendar.YEAR, +1);
		Date NextMonthFirstDay=cal.getTime();//下一个Year的第一天
		cal.add(Calendar.DATE, -1);
		Date LastDay=cal.getTime(); //获取Year的最后一天
		
		int Months=cal.getActualMaximum(Calendar.MONTH);
		
		String StatisticDate=Yearformat.format(Year);//日期
		float AreaMaxIntensity=0;//雷电幅值
		float AreaPositiveMaxIntensity=0;//雷电正幅值
		float AreaNegativeMaxIntensity=0;//雷电负幅值
		int AreaLightningCount=0;//雷电次数
		int AreaPositiveCount=0;//正闪电次数
		int AreaNegativeCount=0;//负闪电次数
		int LightingDays=0;//雷电天数
		String LightingStartTime="2099-12-31 00:00:00";//闪电开始时间
		String LightingEndTime="1970-01-01 00:00:00";//闪电结束时间
		List<TimePeriod> PeriodInfo=new ArrayList<TimePeriod>();
		//遍历该年的所有月
		for(int month=0;month<=Months;month++)
		{
			Calendar   calendar=Calendar.getInstance();//获取当前日期 
			calendar.setTime(Year);
			calendar.set(Calendar.MONTH, month);
			Date date=calendar.getTime();//设置取数据的查询日期
			
			
			Calendar CurDate=Calendar.getInstance();//获取当前日期 
			CurDate.set(Calendar.HOUR_OF_DAY, 0);//设置小时
			CurDate.set(Calendar.MINUTE, 0);//设置分钟
			CurDate.set(Calendar.SECOND, 0);//设置秒
			CurDate.set(Calendar.MILLISECOND, 0);//设置毫秒
			//如果查询日期大于当前日期，则不查询，直接跳过
			if(date.getTime()>=CurDate.getTime().getTime())
			{
				continue;
			}

			LigthingCricleActiveStatistic_Month_Year MonthStatistic=GetLightningCricleStatisticByMonth(CricleLatitude, CricleLongitude, Radium, date);//获取一个月的统计信息
			
			TimePeriod temp=new TimePeriod();
			
			//取出单独一个月的雷电统计数据
			temp.setDate(Monthformat.format(date));//日期
			
			temp.setLightningCount(MonthStatistic.getAreaLightningCount());//闪电次数
			temp.setPositiveCount(MonthStatistic.getAreaPositiveCount());//正闪电次数
			temp.setNegativeCount(MonthStatistic.getAreaNegativeCount());//负闪电次数
			
			temp.setMaxIntensity(MonthStatistic.getAreaMaxIntensity());//幅值
			temp.setPositiveMaxIntensity(MonthStatistic.getAreaPositiveMaxIntensity());//正幅值
			temp.setNegativeMaxIntensity(MonthStatistic.getAreaNegativeMaxIntensity());//负幅值
			temp.setLightingDays(MonthStatistic.getLightingDays());
			
			
			AreaLightningCount += MonthStatistic.getAreaLightningCount();//累加雷电次数
			AreaPositiveCount += MonthStatistic.getAreaPositiveCount();//累加正闪电次数
			AreaNegativeCount += MonthStatistic.getAreaNegativeCount();//累加负闪电次数
			LightingDays+=MonthStatistic.getLightingDays();//雷电天数//累加雷电天数
			
			if(MonthStatistic.getAreaLightningCount()>0)//雷电数大于0时才有计算雷电初终时的必要
			{
				temp.setLightingStartTime(MonthStatistic.getLightingStartTime());//雷电初时
				temp.setLightingEndTime(MonthStatistic.getLightingEndTime());//雷电终时
			}
			
			PeriodInfo.add(temp);//添加到数组中
			
			if(MonthStatistic.getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingStartTime).getTime()>=DateConvert.StrToDate(MonthStatistic.getLightingStartTime()).getTime())//确定当月雷电初时
			{
				LightingStartTime=MonthStatistic.getLightingStartTime();
			}
			
			if(MonthStatistic.getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingEndTime).getTime()<=DateConvert.StrToDate(MonthStatistic.getLightingEndTime()).getTime())//确定当月雷电终时
			{
				LightingEndTime=MonthStatistic.getLightingEndTime();
			}
			
			//找出section和Area里雷电幅值，雷电正幅值，雷电负幅值
    		AreaMaxIntensity=Math.max(AreaMaxIntensity, Math.abs( MonthStatistic.getAreaMaxIntensity()));
    		AreaPositiveMaxIntensity=Math.max(AreaPositiveMaxIntensity,  MonthStatistic.getAreaPositiveMaxIntensity());
    		AreaNegativeMaxIntensity=Math.min(AreaNegativeMaxIntensity,  MonthStatistic.getAreaNegativeMaxIntensity());
		}
		//生成返回的对象
		LigthingCricleActiveStatistic_Month_Year YearStatistic=new  LigthingCricleActiveStatistic_Month_Year();
	
		YearStatistic.setCricleLatitude(CricleLatitude);
		YearStatistic.setCricleLongitude(CricleLongitude);
		YearStatistic.setRadium(Radium);
		YearStatistic.setDate(StatisticDate);
		YearStatistic.setAreaMaxIntensity(AreaMaxIntensity);
		YearStatistic.setAreaPositiveMaxIntensity(AreaPositiveMaxIntensity);
		YearStatistic.setAreaNegativeMaxIntensity(AreaNegativeMaxIntensity);
		YearStatistic.setAreaLightningCount(AreaLightningCount);
		YearStatistic.setAreaPositiveCount(AreaPositiveCount);
		YearStatistic.setAreaNegativeCount(AreaNegativeCount);
		YearStatistic.setLightingStartTime(LightingStartTime);
		YearStatistic.setLightingEndTime(LightingEndTime);
		YearStatistic.setTime(PeriodInfo);
		YearStatistic.setLightingDays(LightingDays);
		
		return YearStatistic;
		
	}
	/**
	 * 按天生成指定矩形区域里的雷电活动统计数据
	 * @param CricleLatitude
	 * @param CricleLongitude
	 * @param Radium
	 * @param Date
	 */
	@Transactional
	public String GetLightingRectangleStatisticByDate(double latitudeLower,double latitudeUpper,double longitudeLeft,double longitudeRight,Date Date)
	{
		List<ADTD> ADTD =adtdService.listbyDateAndArea(Date, Date,(float)latitudeLower,(float)latitudeUpper,(float)longitudeLeft,(float)longitudeRight);
		
		//开始写JSON
		String JsonString="["+"";
		JsonString+="{"+"\"latitudeLower\":"+latitudeLower+",\"latitudeUpper\":"+latitudeUpper+",\"longitudeLeft\":"+longitudeLeft+",\"longitudeRight\":"+longitudeRight+","+"\"date\":\""+Dateformatter.format(Date)+"\","+"\"time\":"+"[";                   
		JsonString=GenJsonForLigRect_CricleActiveStatisticByDate(JsonString, ADTD);
		return JsonString;
	}
	/**
	 * 按月生成指定矩形区域里的雷电活动统计数据
	 * @param CricleLatitude
	 * @param CricleLongitude
	 * @param Radium
	 * @param Month
	 * @return
	 */
	public LigthingRectangleActiveStatistic_Month_Year GetLightningRectangleStatisticByMonth(double latitudeLower,double latitudeUpper,double longitudeLeft,double longitudeRight,Date Month)
	{
		Date FirstDay=Month; //获取Month的第一天
		Calendar   cal=Calendar.getInstance();//获取当前日期 
		cal.setTime(Month);
		//为了获得Month的最后一天，首先将月份加1，之后将天数减1，便能得到Month的最后一天
		cal.add(Calendar.MONTH, +1);
		Date NextMonthFirstDay=cal.getTime();//Month下个月的第一天
		cal.add(Calendar.DATE, -1);
		Date LastDay=cal.getTime(); //获取Month的最后一天
		
		int days=cal.getActualMaximum(Calendar.DATE);
		
		
		
		String StatisticDate=Monthformat.format(Month);//日期
		float AreaMaxIntensity=0;//雷电幅值
		float AreaPositiveMaxIntensity=0;//雷电正幅值
		float AreaNegativeMaxIntensity=0;//雷电负幅值
		int AreaLightningCount=0;//雷电次数
		int AreaPositiveCount=0;//正闪电次数
		int AreaNegativeCount=0;//负闪电次数
		int LightingDays=0;//雷电天数
		String LightingStartTime="2099-12-31 00:00:00";//闪电开始时间
		String LightingEndTime="1970-01-01 00:00:00";//闪电结束时间
		List<TimePeriod> PeriodInfo=new ArrayList<TimePeriod>();
		//遍历该月的所有天
		for(long day=0;day<days;day++)
		{
			Date date=new Date(FirstDay.getTime()+(1000*60*60*24)*day);//设置取数据的查询日期
			Calendar CurDate=Calendar.getInstance();//获取当前日期 
			CurDate.set(Calendar.HOUR_OF_DAY, 0);//设置小时
			CurDate.set(Calendar.MINUTE, 0);//设置分钟
			CurDate.set(Calendar.SECOND, 0);//设置秒
			CurDate.set(Calendar.MILLISECOND, 0);//设置毫秒
			//如果查询日期大于当前日期，则不查询该天，直接跳过
			if(date.getTime()>=CurDate.getTime().getTime())
			{
				continue;
			}

			String jsonData = GetLightingRectangleStatisticByDate(latitudeLower, latitudeUpper, longitudeLeft, longitudeRight, date);//取出单独一天的文件数据
			LigthingRectangleActiveStatistic_Day foo[] = new Gson().fromJson(jsonData, LigthingRectangleActiveStatistic_Day[].class);//将json文本信息转换为对象
			TimePeriod temp=new TimePeriod();
			
			//取出单独一天的雷电统计文件中所需的数据
			temp.setDate(Dateformatter.format(date));//日期
			
			temp.setLightningCount(foo[0].getAreaLightningCount());//闪电次数
			temp.setPositiveCount(foo[0].getAreaPositiveCount());//正闪电次数
			temp.setNegativeCount(foo[0].getAreaNegativeCount());//负闪电次数
			
			temp.setMaxIntensity(foo[0].getAreaMaxIntensity());//幅值
			temp.setPositiveMaxIntensity(foo[0].getAreaPositiveMaxIntensity());//正幅值
			temp.setNegativeMaxIntensity(foo[0].getAreaNegativeMaxIntensity());//负幅值
			
			if(foo[0].getAreaLightningCount()>0)//设置雷电天数，由于是单日数据，所以雷电数大于0时设置雷电天数为1
			{
				temp.setLightingDays(1);
			}
			else 
			{
				temp.setLightingDays(0);
			}
			
			
			AreaLightningCount += foo[0].getAreaLightningCount();//累加雷电次数
			AreaPositiveCount += foo[0].getAreaPositiveCount();//累加正闪电次数
			AreaNegativeCount += foo[0].getAreaNegativeCount();//累加负闪电次数
			
			if(foo[0].getAreaLightningCount()>0)//雷电数大于0时才有计算雷电初终时的必要
			{
				
				temp.setLightingStartTime(foo[0].getLightingStartTime());//雷电初时
				temp.setLightingEndTime(foo[0].getLightingEndTime());//雷电终时
				LightingDays++;
			}
			
			PeriodInfo.add(temp);//添加到数组中
			
			if(foo[0].getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingStartTime).getTime()>=DateConvert.StrToDate(foo[0].getLightingStartTime()).getTime())//确定当月雷电初时
			{
				LightingStartTime=foo[0].getLightingStartTime();
			}
			
			if(foo[0].getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingEndTime).getTime()<=DateConvert.StrToDate(foo[0].getLightingEndTime()).getTime())//确定当月雷电终时
			{
				LightingEndTime=foo[0].getLightingEndTime();
			}
			
			//找出section和Area里雷电幅值，雷电正幅值，雷电负幅值
    		AreaMaxIntensity=Math.max(AreaMaxIntensity, Math.abs( foo[0].getAreaMaxIntensity()));
    		AreaPositiveMaxIntensity=Math.max(AreaPositiveMaxIntensity,  foo[0].getAreaPositiveMaxIntensity());
    		AreaNegativeMaxIntensity=Math.min(AreaNegativeMaxIntensity,  foo[0].getAreaNegativeMaxIntensity());
		}
		//生成返回的对象
		LigthingRectangleActiveStatistic_Month_Year MonthStatistic=new  LigthingRectangleActiveStatistic_Month_Year();
		MonthStatistic.setLatitudeLower(latitudeLower);
		MonthStatistic.setLatitudeUpper(latitudeUpper);
		MonthStatistic.setLongitudeLeft(longitudeLeft);
		MonthStatistic.setLongitudeRight(longitudeRight);
		MonthStatistic.setDate(StatisticDate);
		MonthStatistic.setAreaMaxIntensity(AreaMaxIntensity);
		MonthStatistic.setAreaPositiveMaxIntensity(AreaPositiveMaxIntensity);
		MonthStatistic.setAreaNegativeMaxIntensity(AreaNegativeMaxIntensity);
		MonthStatistic.setAreaLightningCount(AreaLightningCount);
		MonthStatistic.setAreaPositiveCount(AreaPositiveCount);
		MonthStatistic.setAreaNegativeCount(AreaNegativeCount);
		MonthStatistic.setLightingStartTime(LightingStartTime);
		MonthStatistic.setLightingEndTime(LightingEndTime);
		MonthStatistic.setTime(PeriodInfo);
		MonthStatistic.setLightingDays(LightingDays);
		
		return MonthStatistic;
	}
	/**
	 *  按年生成矩形区域雷电活动统计数据
	 * @param CricleLatitude
	 * @param CricleLongitude
	 * @param Radium
	 * @param Year
	 * @return json数据
	 */
	@Transactional
	public LigthingRectangleActiveStatistic_Month_Year GetLightningRectangleStatisticByYear(double latitudeLower,double latitudeUpper,double longitudeLeft,double longitudeRight,Date Year)
	{
	    
		Date FirstDay=Year; //获取Year的第一天
		Calendar   cal=Calendar.getInstance();//获取当前日期 
		cal.setTime(Year);
		//为了获得Year的最后一天，首先将年加1，之后将天数减1，便能得到Year的最后一天
		cal.add(Calendar.YEAR, +1);
		Date NextMonthFirstDay=cal.getTime();//下一个Year的第一天
		cal.add(Calendar.DATE, -1);
		Date LastDay=cal.getTime(); //获取Year的最后一天
		
		int Months=cal.getActualMaximum(Calendar.MONTH);
		
		String StatisticDate=Yearformat.format(Year);//日期
		float AreaMaxIntensity=0;//雷电幅值
		float AreaPositiveMaxIntensity=0;//雷电正幅值
		float AreaNegativeMaxIntensity=0;//雷电负幅值
		int AreaLightningCount=0;//雷电次数
		int AreaPositiveCount=0;//正闪电次数
		int AreaNegativeCount=0;//负闪电次数
		int LightingDays=0;//雷电天数
		String LightingStartTime="2099-12-31 00:00:00";//闪电开始时间
		String LightingEndTime="1970-01-01 00:00:00";//闪电结束时间
		List<TimePeriod> PeriodInfo=new ArrayList<TimePeriod>();
		//遍历该年的所有月
		for(int month=0;month<=Months;month++)
		{
			Calendar   calendar=Calendar.getInstance();//获取当前日期 
			calendar.setTime(Year);
			calendar.set(Calendar.MONTH, month);
			Date date=calendar.getTime();//设置取数据的查询日期
			
			
			Calendar CurDate=Calendar.getInstance();//获取当前日期 
			CurDate.set(Calendar.HOUR_OF_DAY, 0);//设置小时
			CurDate.set(Calendar.MINUTE, 0);//设置分钟
			CurDate.set(Calendar.SECOND, 0);//设置秒
			CurDate.set(Calendar.MILLISECOND, 0);//设置毫秒
			//如果查询日期大于当前日期，则不查询，直接跳过
			if(date.getTime()>=CurDate.getTime().getTime())
			{
				continue;
			}

			LigthingRectangleActiveStatistic_Month_Year MonthStatistic=GetLightningRectangleStatisticByMonth(latitudeLower, latitudeUpper, longitudeLeft, longitudeRight, date);//获取一个月的统计信息
			
			TimePeriod temp=new TimePeriod();
			
			//取出单独一个月的雷电统计数据
			temp.setDate(Monthformat.format(date));//日期
			
			temp.setLightningCount(MonthStatistic.getAreaLightningCount());//闪电次数
			temp.setPositiveCount(MonthStatistic.getAreaPositiveCount());//正闪电次数
			temp.setNegativeCount(MonthStatistic.getAreaNegativeCount());//负闪电次数
			
			temp.setMaxIntensity(MonthStatistic.getAreaMaxIntensity());//幅值
			temp.setPositiveMaxIntensity(MonthStatistic.getAreaPositiveMaxIntensity());//正幅值
			temp.setNegativeMaxIntensity(MonthStatistic.getAreaNegativeMaxIntensity());//负幅值
			temp.setLightingDays(MonthStatistic.getLightingDays());
			
			
			AreaLightningCount += MonthStatistic.getAreaLightningCount();//累加雷电次数
			AreaPositiveCount += MonthStatistic.getAreaPositiveCount();//累加正闪电次数
			AreaNegativeCount += MonthStatistic.getAreaNegativeCount();//累加负闪电次数
			LightingDays+=MonthStatistic.getLightingDays();//雷电天数//累加雷电天数
			
			if(MonthStatistic.getAreaLightningCount()>0)//雷电数大于0时才有计算雷电初终时的必要
			{
				
				temp.setLightingStartTime(MonthStatistic.getLightingStartTime());//雷电初时
				temp.setLightingEndTime(MonthStatistic.getLightingEndTime());//雷电终时
			}
			
			PeriodInfo.add(temp);//添加到数组中
			
			if(MonthStatistic.getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingStartTime).getTime()>=DateConvert.StrToDate(MonthStatistic.getLightingStartTime()).getTime())//确定当月雷电初时
			{
				LightingStartTime=MonthStatistic.getLightingStartTime();
			}
			
			if(MonthStatistic.getAreaLightningCount()>0
				&&DateConvert.StrToDate(LightingEndTime).getTime()<=DateConvert.StrToDate(MonthStatistic.getLightingEndTime()).getTime())//确定当月雷电终时
			{
				LightingEndTime=MonthStatistic.getLightingEndTime();
			}
			
			//找出section和Area里雷电幅值，雷电正幅值，雷电负幅值
    		AreaMaxIntensity=Math.max(AreaMaxIntensity, Math.abs( MonthStatistic.getAreaMaxIntensity()));
    		AreaPositiveMaxIntensity=Math.max(AreaPositiveMaxIntensity,  MonthStatistic.getAreaPositiveMaxIntensity());
    		AreaNegativeMaxIntensity=Math.min(AreaNegativeMaxIntensity,  MonthStatistic.getAreaNegativeMaxIntensity());
		}
		//生成返回的对象
		LigthingRectangleActiveStatistic_Month_Year YearStatistic=new  LigthingRectangleActiveStatistic_Month_Year();
	
		YearStatistic.setLatitudeLower(latitudeLower);
		YearStatistic.setLatitudeUpper(latitudeUpper);
		YearStatistic.setLongitudeLeft(longitudeLeft);
		YearStatistic.setLongitudeRight(longitudeRight);
		YearStatistic.setDate(StatisticDate);
		YearStatistic.setAreaMaxIntensity(AreaMaxIntensity);
		YearStatistic.setAreaPositiveMaxIntensity(AreaPositiveMaxIntensity);
		YearStatistic.setAreaNegativeMaxIntensity(AreaNegativeMaxIntensity);
		YearStatistic.setAreaLightningCount(AreaLightningCount);
		YearStatistic.setAreaPositiveCount(AreaPositiveCount);
		YearStatistic.setAreaNegativeCount(AreaNegativeCount);
		YearStatistic.setLightingStartTime(LightingStartTime);
		YearStatistic.setLightingEndTime(LightingEndTime);
		YearStatistic.setTime(PeriodInfo);
		YearStatistic.setLightingDays(LightingDays);
		
		return YearStatistic;
		
	}
	/**
	 * //一次性生成该年半径1-20公里的雷电密度数据
	 * @param year
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	@Transactional
	private void GenerateOneYearData(int year)throws IOException, NumberFormatException, SQLException
	{
		
		List<ADTD> ADTD; 
		AreaInfo tempArea; 
		ADTD=adtdService.listbyYear(year);
		
		String aString2=new FileTools().ReadFileAsString((new FileTools().GetProjectRootPath()+"resources"+separator+"area.json"));//获取本地文件里的区域信息
		AreaInfo allArea[] = new Gson().fromJson(aString2, AreaInfo[].class);//将json文本信息转换为对象
		

		//生成该年半径1-20公里的数据
		for(int radium=1;radium<=20;radium++ )
		{
				//String uploadFilePath = request.getSession().getServletContext().getRealPath("/data/");
				//判断该文件是否生成，若未生成则开始生成，否则返回
				
				String path=BasePath+String.valueOf(year)+"-radium-"+radium+".json";
				File file=new File(path);    	
				if(file.exists())    
				{    
					System.out.println("file exists");//若已经生成则返回
					continue;
				} 
				else
				{
					 file.createNewFile();//生成文件
				}
				
				
				FileWriter fos=new FileWriter(path);
				//开始写文件
				String title="{"+"\"data\":["+"";
			   fos.write(title);
			   fos.write("\r\n");
			   //为每个区域生成一个文件
				for(int i=0;i<allArea.length-1;i++)//除开allArea的最后一个区域：整个重庆市
				{
					tempArea=allArea[i];
					//确定该区域需要的方格数
					int allOverallRow=(int)(((tempArea.getLatitudeUpper()-tempArea.getLatitudeLower())*111.0/radium)+1);//默认认为1°的纬度为111公里
					int allOverallCol=(int)(((tempArea.getLongitudeRight()-tempArea.getLongitudeLeft())*96.0/radium)+1);//默认认为1°的经度为96公里
					int location[][]=new int[allOverallRow][allOverallCol];
					//计算该区域的面积（矩形面积）
					double size=(tempArea.getLatitudeUpper()-tempArea.getLatitudeLower())*111.0*(tempArea.getLongitudeRight()-tempArea.getLongitudeLeft())*96.0;
					//确定每个方格里的闪电次数
					for(int LightningIndex=0;LightningIndex<ADTD.size();LightningIndex++)
					{
						//判断闪电的经纬度是否在区域
						if(ADTD.get(LightningIndex).getLongitude()<=tempArea.getLongitudeRight()
						&&ADTD.get(LightningIndex).getLongitude()>=tempArea.getLongitudeLeft()
						&&ADTD.get(LightningIndex).getLatitude()<=tempArea.getLatitudeUpper()
						&&ADTD.get(LightningIndex).getLatitude()>=tempArea.getLatitudeLower())
						 {
								//判断该闪电在哪个方格里
							 	int disX=(int)((Math.abs(ADTD.get(LightningIndex).getLatitude()*111-tempArea.getLatitudeUpper()*111))/radium);
				   	            int disY=(int)((Math.abs(ADTD.get(LightningIndex).getLongitude()*96-tempArea.getLongitudeLeft()*96))/radium);
				   	            location[disX][disY]++; //若在该方格里，该方格里的闪电数加1
				   	       }
					  }
					//开始写区域信息
					String title2="{"+"\"areaid\":"+tempArea.getfId()+",\"name\":\""+tempArea.getName()+"\","+"\"radium\":"+radium+","+"\"matrixs\":"+"[";                   
					fos.write(title2);
					fos.write("\r\n"); 
					int index=0;
					int CountLighting=0;
				    for(int d=0;d<allOverallRow;d++)
				    {
				    	  for(int b=0;b<allOverallCol;b++)
				    	  {
				    		  //计算每一个方格的经纬度
				    		  double LongitudeLeft=tempArea.getLongitudeLeft()+(1.0/96.0)*radium*b;
				    		  double LongitudeRight=tempArea.getLongitudeLeft()+(1.0/96.0)*radium*(b+1);
				    		  double LatitudeUpper=tempArea.getLatitudeUpper()-(1.0/111.0)*radium*d;
				    		  double LatitudeLower=tempArea.getLatitudeUpper()-(1.0/111.0)*radium*(d+1);
				    		  //统计每一个方格里的闪电数
				    		  CountLighting=CountLighting+location[d][b];//累加该区域里的闪电数
				    		  String str;
				    		  if((b+1)*(d+1)==allOverallRow*allOverallCol)//如果到该区域数据的最后一行，行尾不加逗号
				    			  str ="{\"serial\":"+index+","+"\"LongitudeLeft\":"+LongitudeLeft+","+"\"LatitudeUpper\":"+LatitudeUpper+","+"\"LongitudeRight\":"+LongitudeRight+","+"\"LatitudeLower\":"+LatitudeLower+",\"count\":"+ location[d][b]+",\"LightningDensity\":"+ location[d][b]*1.0f/(radium*radium*1.0f)+"}";
				    		  else//数据的中间行，行尾加逗号
				    			  str ="{\"serial\":"+index+","+"\"LongitudeLeft\":"+LongitudeLeft+","+"\"LatitudeUpper\":"+LatitudeUpper+","+"\"LongitudeRight\":"+LongitudeRight+","+"\"LatitudeLower\":"+LatitudeLower+",\"count\":"+ location[d][b]+",\"LightningDensity\":"+ location[d][b]*1.0f/(radium*radium*1.0f)+"},";
				    		  fos.write(str);
				              fos.write("\r\n"); 
				              index++;
				    	  }
				    	 
				      }
				      if(i==allArea.length-2)//最后一个区域，行尾不加逗号，并且除开allArea的最后一个区域：整个重庆市
				    	  fos.write("],\"CountLighting\":"+CountLighting+",\"AreaLightningDensity\":"+CountLighting*1.0f/(size)+"}");//统计总闪电次数和闪电密度
				      else//中间区域，行尾加逗号
				    	  fos.write("],\"CountLighting\":"+CountLighting+",\"AreaLightningDensity\":"+CountLighting*1.0f/(size)+"},");
				      fos.write("\r\n");
				}
				fos.write("]}");
			    fos.close();
		}
	}
	/**
	 * 生成圆形或矩形区域内按天的雷电统计数据
	 * 由于圆形和矩形的区域表示方法不同故json头部的圆形和矩形信息需要由JsonString传入
	 * @param JsonString 
	 * @param ADTD
	 * @return
	 */
	private String GenJsonForLigRect_CricleActiveStatisticByDate(String JsonString,List<ADTD> ADTD)
	{
		float AreaMaxIntensity=0;//区域最大雷电值（不区分正负）
	    float AreaPositiveMaxIntensity=0;//区域最大正雷电值
	    float AreaNegativeMaxIntensity=0;//区域最大负雷电值
	    int AreaLightningCount=0;//区域雷电次数
	    int AreaPositiveCount=0;//区域正雷电次数
	    int AreaNegativeCount=0;//区域负雷电次数
	    Date LightingStartTime=new Date();//雷电初时
	    Date LightingEndTime=new Date();//雷电终时
	    
	    if(ADTD.size()>0)//当天有雷电数据时,初始化雷电初时和雷电终时
		{
        	Calendar Tempcalendar =Calendar.getInstance();//使用Calendar处理时间数据
			Tempcalendar.setTime(ADTD.get(0).getDate());//设置日期
			Tempcalendar.set(Calendar.HOUR_OF_DAY, ADTD.get(0).getHour());//设置小时
			Tempcalendar.set(Calendar.MINUTE, ADTD.get(0).getMinute());//设置分钟
			Tempcalendar.set(Calendar.SECOND, (int)ADTD.get(0).getSecond());//设置秒
			LightingStartTime=Tempcalendar.getTime();//初始化雷电初时
			LightingEndTime=Tempcalendar.getTime();//初始化雷电终时
		}
		    //遍历所有时间段，一天共24个时间段每1小时一个时间段
		for(int section_index=0;section_index<24;section_index++)
		{
			//确定是否是section的第一行数据（确保json格式的正确）
			boolean bFirstTimeInThisSection=true;
			  //定义一系列统计值
			int count=0;
			int PositiveCount=0;
			int NegativeCount=0;
			float MaxIntensity=0;
			float PositiveMaxIntensity=0;
			float NegativeMaxIntensity=0;
			
			String StartTime=section_index+":"+"00";
			String EndTime=section_index+1+":"+"00";
			JsonString+="{\"section\":"+section_index+","+"\"starttime\":\""+StartTime+"\","+"\"endtime\":\""+EndTime+"\","+"\"positions\":[";
			
			
			
			//遍历ADTD数据
			for(int data_index=0;data_index<ADTD.size();data_index++)
			{
				//ADTD数据是否在section_index的小时时间段内
				if((int)(section_index)==ADTD.get(data_index).getHour())
				{
					//获取当前ADTD数据的时间
					Calendar Datecalendar =Calendar.getInstance();//使用Calendar处理时间数据
			    	Datecalendar.setTime(ADTD.get(data_index).getDate());//设置日期
			    	Datecalendar.set(Calendar.HOUR_OF_DAY, ADTD.get(data_index).getHour());//设置小时
			    	Datecalendar.set(Calendar.MINUTE, ADTD.get(data_index).getMinute());//设置分钟
			    	Datecalendar.set(Calendar.SECOND, (int)ADTD.get(data_index).getSecond());//设置秒
			    	
			    	if(LightingStartTime.getTime()>=Datecalendar.getTime().getTime())//计算雷电初时
			    	{
			    		LightingStartTime=Datecalendar.getTime();
			    	}
			    	if(LightingEndTime.getTime()<=Datecalendar.getTime().getTime())//计算雷电终时
			    	{
			    		LightingEndTime=Datecalendar.getTime();
			    	}
			    	
					//若生成的不是第一行数据（确保json格式的正确）
					if(!bFirstTimeInThisSection)
					{
						JsonString+=",";
					}
					JsonString+="{\"latitude\":"+ADTD.get(data_index).getLatitude()+","+"\"longitude\":"+ADTD.get(data_index).getLongitude()+","+"\"intensity\":"+ADTD.get(data_index).getIntensity()+",\"datetime\":"+Datecalendar.getTime().getTime()+"}";
		    		
		    		bFirstTimeInThisSection=false;
		    		
		    		//计算统计数据，雷电次数
		    		count++;
		    		AreaLightningCount++;
		    		if(ADTD.get(data_index).getIntensity().floatValue()>=0)
		    		{
		    			PositiveCount++;
		    			AreaPositiveCount++;
		    		}
		    		else
		    		{
		    			NegativeCount++;
		    			AreaNegativeCount++;
		    		}
		    		//找出section和Area里雷电幅值，雷电正幅值，雷电负幅值
		    		AreaMaxIntensity=Math.max(AreaMaxIntensity, Math.abs(ADTD.get(data_index).getIntensity().floatValue()));
		    		AreaPositiveMaxIntensity=Math.max(AreaPositiveMaxIntensity, ADTD.get(data_index).getIntensity().floatValue());
		    		AreaNegativeMaxIntensity=Math.min(AreaNegativeMaxIntensity, ADTD.get(data_index).getIntensity().floatValue());
		    		
		    		MaxIntensity=Math.max(MaxIntensity, Math.abs(ADTD.get(data_index).getIntensity().floatValue()));
		    		PositiveMaxIntensity=Math.max(PositiveMaxIntensity, ADTD.get(data_index).getIntensity().floatValue());
		    		NegativeMaxIntensity=Math.min(NegativeMaxIntensity, ADTD.get(data_index).getIntensity().floatValue());
					
				}
			}
			JsonString+=("],");
			//写入时间段统计信息
			if(section_index==23)//最后一个时间段，行尾加反方括号和逗号
				JsonString+="\"LightningCount\":"+count+",\"PositiveCount\":\""+PositiveCount+"\","+"\"NegativeCount\":"+NegativeCount+","+"\"MaxIntensity\":"+MaxIntensity+","+"\"PositiveMaxIntensity\":"+PositiveMaxIntensity+","+"\"NegativeMaxIntensity\":"+NegativeMaxIntensity+"}],";                   
			else//中间时间段，行尾加逗号
				JsonString+="\"LightningCount\":"+count+",\"PositiveCount\":\""+PositiveCount+"\","+"\"NegativeCount\":"+NegativeCount+","+"\"MaxIntensity\":"+MaxIntensity+","+"\"PositiveMaxIntensity\":"+PositiveMaxIntensity+","+"\"NegativeMaxIntensity\":"+NegativeMaxIntensity+"},";                   
		}
		//写入区域统计信息
		if(ADTD.size()>0)//当天有雷电数据时
		{
			JsonString+="\"AreaMaxIntensity\":"+AreaMaxIntensity+","+"\"AreaPositiveMaxIntensity\":"+AreaPositiveMaxIntensity+","+"\"AreaNegativeMaxIntensity\":"+AreaNegativeMaxIntensity+",\"AreaLightningCount\":\""+AreaLightningCount+"\","+"\"AreaPositiveCount\":"+AreaPositiveCount+","+"\"AreaNegativeCount\":"+AreaNegativeCount+",\"LightingStartTime\":\""+DateConvert.DateToStr(LightingStartTime)+"\","+"\"LightingEndTime\":\""+DateConvert.DateToStr(LightingEndTime)+"\"";                   
		}
		else//当天无雷电数据时,雷电初时和雷电终时设置为0
		{
			JsonString+="\"AreaMaxIntensity\":"+AreaMaxIntensity+","+"\"AreaPositiveMaxIntensity\":"+AreaPositiveMaxIntensity+","+"\"AreaNegativeMaxIntensity\":"+AreaNegativeMaxIntensity+",\"AreaLightningCount\":\""+AreaLightningCount+"\","+"\"AreaPositiveCount\":"+AreaPositiveCount+","+"\"AreaNegativeCount\":"+AreaNegativeCount+",\"LightingStartTime\":\""+0+"\","+"\"LightingEndTime\":\""+0+"\"";                   
		}
		
		//结束写json
		JsonString+=("}]");
		return JsonString;
	}
	//创建目录，作为工具使用
	private static boolean makeDirs(String filePath) {
        String folderName = filePath;
        if (folderName == null || folderName.isEmpty()) {
            return false;
        }
 
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
}
