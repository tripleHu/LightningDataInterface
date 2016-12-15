package cn.cqu.edu;




import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.cqu.edu.LightningDataInterface.Tools.MapPoint;
import cn.cqu.edu.LightningDataInterface.Tools.MapTools;
import cn.cqu.edu.LightningDataInterface.services.hibernate.ADTDServiseForFljsjg2;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})  
public class MapToolTest {
	@Autowired
	private ADTDServiseForFljsjg2 adtdServiseForFljsjg2;
	
	@Test
	public void testMapTool() throws NumberFormatException, IOException
	{
		 MapPoint point=new MapPoint();
		 point.setLatitude(30.258659362793);
		 point.setLongitude(107.405479431152);
		 
		 
		 
		 System.out.println(MapTools.getAreaPointsArray(40).length);
		 /*for(int j=0; j<MapTools.getAreaPointsArray(40).length; j++)
		 {
			 System.out.println(MapTools.getAreaPointsArray(40)[j]);
		 }*/
		 
		
		 
		boolean a=MapTools.isInPolygon(point,MapTools.getAreaPointsArray(40));
		System.out.println(a);
	}
	/*@Test
	public void GetLightningActiveByDate() throws NumberFormatException, IOException, ParseException, SQLException
	{
		 String dateString = "2014-08-01 ";  
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");  
		 Date date = sdf.parse(dateString);  
		 
		 String dateString1 = "2014-08-10 ";  
		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");  
		 Date date1 = sdf1.parse(dateString1);  
		 adtdServiseForFljsjg2.GetLightningActiveByDate(0, date, date1);
	}*/
}
