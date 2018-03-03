package cn.cqu.edu.LightningDataInterface.Tools;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.cqu.edu.LightningDataInterface.domain.ADTD;
import cn.cqu.edu.LightningDataInterface.entity.LigthingActiveStatistic_Month_Year;
import cn.cqu.edu.LightningDataInterface.services.hibernate.ADTDService;
import cn.cqu.edu.LightningDataInterface.services.hibernate.ADTDServiseForFljsjg2;
import hmsas.HmsasService;
import hmsas.HmsasServiceLocator;
import hmsas.HmsasserverPortType;
import cn.cqu.edu.LightningDataInterface.controllers.MainController;

@Component
public class SyncAdtdTask {
	@Autowired
	private ADTDService adtdService;
	private ADTDServiseForFljsjg2 adtdServiseForFljsjg2;
	
	SimpleDateFormat Yearformat = new SimpleDateFormat("yyyy");
	boolean busy=false;
	 String BaseHttpUrl=null;  
	// 每小时执行一次
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void TaskJob() 
    {
    	if(!busy)
    	{
    	  //UpdateAdtd(); 
    	}
    }
    //每天晚上1点执行统计分析ADTD数据
    @Scheduled(cron = "0 0 1 * * ?")
    public void ADTDTaskJob() throws ParseException, NumberFormatException, IOException, SQLException 
    {
    	for(int year=2008;year<=Calendar.getInstance().get(Calendar.YEAR);year++)
    	{
    		 Date Year = Yearformat.parse(String.valueOf(year));//转换为Date类型
    		 for(int AreaIndex=0;AreaIndex<41;AreaIndex++)
    		 {
    			 System.out.println("Begin LightningStatistic Area : "+AreaIndex+" ; "+"Year : "+Year);
    			 String url=GetBaseHttpUrl()+"/AreaStatistic?AreaIndex="+AreaIndex+"&Date="+Year;
    			 String returnstring=HttpConnectUtil.getURLContent(url);//通过http请求返回字符串
    			 System.out.println("End LightningStatistic Area : "+AreaIndex+" ; "+"Year : "+Year);
    		 }
    		
    	}
    }
    /**
     * 更新ADTD数据
     */
    public void UpdateAdtd()
    {
    	busy=true;
    	ADTD adtd=adtdService.GetLastestData();//获取最后一条ADTD数据
    	Calendar StartDatecalendar =Calendar.getInstance();//使用Calendar处理时间数据
    	StartDatecalendar.setTime(adtd.getDate());//设置日期
    	StartDatecalendar.set(Calendar.HOUR_OF_DAY, adtd.getHour());//设置小时
    	StartDatecalendar.set(Calendar.MINUTE, adtd.getMinute());//设置分钟
    	StartDatecalendar.set(Calendar.SECOND, (int)adtd.getSecond());//设置秒
    	StartDatecalendar.set(Calendar.MILLISECOND, 0);//设置毫秒
    	
    	StartDatecalendar.add(Calendar.SECOND, +1);//增加秒,在最后一条数据时间上加1秒，防止查询重复数据
    	
    	Calendar Nowcalendar =Calendar.getInstance();//获取现在时间
    	//从最后一条ADTD数据的时间开始，每次循环增加一小时，一直到和当前时间的小时相同
    	while(!(StartDatecalendar.get(Calendar.YEAR)==Nowcalendar.get(Calendar.YEAR)
    			&&StartDatecalendar.get(Calendar.MONTH)==Nowcalendar.get(Calendar.MONTH)
    			&&StartDatecalendar.get(Calendar.DATE)==Nowcalendar.get(Calendar.DATE)
    			&&StartDatecalendar.get(Calendar.HOUR_OF_DAY)==Nowcalendar.get(Calendar.HOUR_OF_DAY)))
    	{
    		//处理查询时间的开始时间
    		Date StartDate=StartDatecalendar.getTime();
    		String StartSection=DateConvert.DateToStr1(StartDate);//将Date转换为字符串
    		
    		//处理查询时间的结束时间
    		StartDatecalendar.add(Calendar.HOUR_OF_DAY, +1);//StartDatecalendar增加一小时
    		Date EndDate=StartDatecalendar.getTime();
    		String EndSection=DateConvert.DateToStr1(EndDate);//将Date转换为字符串
    		System.out.println(StartSection+"------------"+EndSection);
    		//开始读取接口
    		try
    		{
    			HmsasService hmsasService = new HmsasServiceLocator();
    			HmsasserverPortType _hmsasserverPort = hmsasService.gethmsasserverPort();
    			// 最长1小时 
				String[] result = (String[]) _hmsasserverPort.getflashdata("FLASH_LIGHT_DATA",StartSection,EndSection,"*", "*","cqu", "123456");
				if(result.length>1)
				{     
		    	     
		    	     for (int i = result.length-1; i >=1 ; i--) 
		    	     {
		        	     System.out.println(result[i]);
		                 boolean InsertResult=insertDate(result[i]);
		                 if(InsertResult==false)
		                 {
		                	 break;
		                 }
		    	     }
				}
			} 
    		catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				busy=false;
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				busy=false;
			}
    	}
    	busy=false;
    }
    private boolean insertDate(String result)
    {
    	 String[] lines=result.split("\t");
    	 if(lines.length==10)
    	 { 
    		 ADTD adtd=new ADTD();
    		 String strDate = lines[0];
    		 String tsStr =strDate.substring(0, 4)+"-"+strDate.substring(4, 6)+"-"+strDate.substring(6, 8)+" "+"00:00:00";
    		 Date date=DateConvert.StrToDate(tsStr);
    		 adtd.setDate(date);
    		 
    		 Date date1 =DateConvert.StrToDate1(strDate);
 			 Calendar   calendar1   = Calendar.getInstance();  
 		     calendar1.setTime(date1);
 		     adtd.setHour(calendar1.get(Calendar.HOUR));
 		     adtd.setMinute(calendar1.get(Calendar.MINUTE));
 		     adtd.setSecond((float)(calendar1.get(Calendar.SECOND)+(Float.parseFloat(lines[1])/10000000)));
 		     adtd.setLatitude(Float.parseFloat(lines[2]));
 		     adtd.setLongitude(Float.parseFloat(lines[3]));
 		     adtd.setIntensity(Float.parseFloat(lines[4]));
 		     adtd.setSteepness(Float.parseFloat(lines[5]));
 		     adtd.setDeviation(Float.parseFloat(lines[8]));
 		     adtd.setPositionType(lines[9]);
 		    adtd.setMAPINFO_ID(adtdService.GetLastestData().getMAPINFO_ID()+1);//获取最后一条ADTD数据ID并加1
 		    
 		     boolean Saveresult=adtdService.add(adtd);
 		     if(Saveresult==false)
 		     {
 		    	 return false;
 		     }
 		     else
 		     {
 		    	 return true;
 		     }
    	 }
    	 else
    	 {
    		 return false;
    	 }
    	 		   
    	 		  
    }
	//获取adtd接口地址
	private String GetBaseHttpUrl()
	{
		if(BaseHttpUrl==null)
		{
			BaseHttpUrl=new SystemSetting().get_ADTD_INTERFACE_ADDRESS();
		}
		return BaseHttpUrl;
	}
}
