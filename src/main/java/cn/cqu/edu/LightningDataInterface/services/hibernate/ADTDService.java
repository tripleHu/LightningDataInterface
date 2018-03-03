package cn.cqu.edu.LightningDataInterface.services.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cqu.edu.LightningDataInterface.Tools.DateConvert;
import cn.cqu.edu.LightningDataInterface.domain.ADTD;



@Service
@Transactional
public class ADTDService extends BaseService {

	private static final String MAPINFO_ID = "MAPINFO_ID";
	private static final String id = "id";
	private static final String date = "日期";
	private static final String hour = "时";
	private static final String minute = "分";
	private static final String second = "秒";
	private static final String latitude = "闪电纬度";
	private static final String longitude = "闪电经度";
	private static final String intensity = "闪电强度";
	private static final String steepness = "闪电陡度";
	private static final String deviation = "误差";
	private static final String positionType = "定位方式";
	private static final String county = "县";
	private static final String city = "市";
	
	
	
	
	
	
	//@SuppressWarnings("unchecked")
	//public List<ADTD> list(QueryCondition queryCondition) {
		/*
		 * 查询闪电定位仪数据
		 * */
	//	List<ADTD> list = hibernateTemplate.find("from ADTD as adtd where "+queryCondition.getCondition()
				//,queryCondition.getParamters().toArray());

	//	return list;
	//}
	/**
	 * 返回数据库中最后一条ADTD
	 * @return ADTD
	 */
	@SuppressWarnings("unchecked")
	public ADTD GetLastestData() {
		List<ADTD> LastestData = (List<ADTD>) hibernateTemplate.find("from ADTD where MAPINFO_ID=(select max(MAPINFO_ID) from ADTD)");
		return LastestData.get(0);
	}
	/**
	 *  按开始和结束时间查询ADTD数据
	 * @param StartDate
	 * @param EndDate
	 *  如StartTime表示2015-01-01 12:00:00 EndTime表示2015-01-02 12:00:00 总计24小时
	 *  但返回的是2015-01-01 至 2015-01-02  总计48小时数据
	 * @return 开始和结束时间之间的ADTD数据
	 */
	@SuppressWarnings("unchecked")
	public List<ADTD> listbyDate(Date StartDate,Date EndDate) 
	{
		Calendar StartDatecalendar =Calendar.getInstance();//使用Calendar处理时间数据
    	StartDatecalendar.setTime(StartDate);//设置日期
    	StartDatecalendar.set(Calendar.HOUR_OF_DAY, 0);//设置小时
    	StartDatecalendar.set(Calendar.MINUTE, 0);//设置分钟
    	StartDatecalendar.set(Calendar.SECOND, 0);//设置秒
    	StartDatecalendar.set(Calendar.MILLISECOND, 0);//设置毫秒
    	
    	Calendar EndDatecalendar =Calendar.getInstance();//使用Calendar处理时间数据
    	EndDatecalendar.setTime(EndDate);//设置日期
    	EndDatecalendar.set(Calendar.HOUR_OF_DAY, 0);//设置小时
    	EndDatecalendar.set(Calendar.MINUTE, 0);//设置分钟
    	EndDatecalendar.set(Calendar.SECOND, 0);//设置秒
    	EndDatecalendar.set(Calendar.MILLISECOND, 0);//设置毫秒
    	
		List<ADTD> list = (List<ADTD>) hibernateTemplate.find("from ADTD where date BETWEEN ? and ?",StartDatecalendar.getTime(),EndDatecalendar.getTime());
		return list;
	}
	/**
	 *  按开始和结束时间以及经纬度范围查询ADTD数据
	 * @param StartDate
	 * @param EndDate
	 *  如StartTime表示2015-01-01 12:00:00 EndTime表示2015-01-02 12:00:00 总计24小时
	 *  但返回的是2015-01-01 至 2015-01-02  总计48小时数据
	 * @return 开始和结束时间之间的ADTD数据
	 */
	@SuppressWarnings("unchecked")
	public List<ADTD> listbyDateAndArea(Date StartDate,Date EndDate,float latitudeLower,float latitudeUpper,float longitudeLeft,float longitudeRight) 
	{
		Calendar StartDatecalendar =Calendar.getInstance();//使用Calendar处理时间数据
    	StartDatecalendar.setTime(StartDate);//设置日期
    	StartDatecalendar.set(Calendar.HOUR_OF_DAY, 0);//设置小时
    	StartDatecalendar.set(Calendar.MINUTE, 0);//设置分钟
    	StartDatecalendar.set(Calendar.SECOND, 0);//设置秒
    	StartDatecalendar.set(Calendar.MILLISECOND, 0);//设置毫秒
    	
    	Calendar EndDatecalendar =Calendar.getInstance();//使用Calendar处理时间数据
    	EndDatecalendar.setTime(EndDate);//设置日期
    	EndDatecalendar.set(Calendar.HOUR_OF_DAY, 0);//设置小时
    	EndDatecalendar.set(Calendar.MINUTE, 0);//设置分钟
    	EndDatecalendar.set(Calendar.SECOND, 0);//设置秒
    	EndDatecalendar.set(Calendar.MILLISECOND, 0);//设置毫秒
    	
    	String hql="from ADTD where date BETWEEN ? and ?  and latitude BETWEEN ? and ? and longitude BETWEEN ? and ?";
    	Object[] param=new Object[]{StartDatecalendar.getTime(),EndDatecalendar.getTime(),latitudeLower,latitudeUpper,longitudeLeft,longitudeRight};
    	List<ADTD> list = (List<ADTD>) hibernateTemplate.find(hql,param);
    	return list;
	}
	@SuppressWarnings("unchecked")
	public List<ADTD> listbyYear(int year) {
		/*
		 * 查询闪电定位仪数据
		 * */
		String stim=year+"-01-01 0:0:0";
		String etim=(year+1)+"-01-01 0:0:0";
		Date StartDate=DateConvert.StrToDate(stim);
		Date EndDate=DateConvert.StrToDate(etim);
		List<ADTD> list = (List<ADTD>) hibernateTemplate.find("from ADTD where date BETWEEN ? and ?",StartDate,EndDate);

		return list;
	}
	public ADTD getAdtd(Long id) {
		/*
		 * 获取一个闪电数据
		 * */
		return hibernateTemplate.get(ADTD.class, id);
	}
	
	@Transactional
	public void delete(Long id) {
		/*
		 * 删除一个闪电数据
		 * */
		ADTD adtd = hibernateTemplate.get(ADTD.class, id);
		hibernateTemplate.delete(adtd);
	}
	/**
	 * 保存adtd数据到数据库
	 * @param id
	 */
	@Transactional
	public boolean add(ADTD adtd)
	{
		try
		{
			hibernateTemplate.save(adtd);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}

	
}