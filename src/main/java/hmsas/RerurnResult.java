package hmsas;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

//import jdbcp.JdbcAdtd;
//import domains.user.ADTD;

public class RerurnResult {
	   /* JdbcAdtd  jdbcadtd =new JdbcAdtd();
	   public boolean start(HttpServletRequest request){
		   
		    boolean judge=false;
		      jdbcadtd.readIP(request);
		if(jdbcadtd.SetConnection(jdbcadtd.getURL(),jdbcadtd.getUSERNAME(),jdbcadtd.getPASSWORD())){
						 
			     judge=true;
			 }
		return judge;
	   }
	 
	 
	 public void close(){
		 jdbcadtd.close();
	 }*/
	 
	 
	 String startString="";
	 String endString="";
	 //由于查询的时间有时不在区间内，也被查出，因此插入之前先检验
	 public boolean test(String insertString) throws ParseException{
		 
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
		 Date date1=sdf.parse(insertString);
		 Calendar calendarInsert=Calendar.getInstance();
		 calendarInsert.setTime(date1);
		 
		 
		 Date date2=sdf.parse(startString);
		 Calendar calendarStart=Calendar.getInstance();
		 calendarStart.setTime(date2);
		 
		 
		 Date date3=sdf.parse(endString);
		 Calendar calendarEnd=Calendar.getInstance();
		 calendarEnd.setTime(date3);
             if((calendarInsert.compareTo(calendarStart)==1)&&((calendarInsert.compareTo(calendarEnd)==-1)||(calendarInsert.compareTo(calendarEnd)==0)))
		           return true;
		 else  return false;
	 }
	 
	 
	public int resultQuery(String start ,String end,int id){
 // TODO code application logic here
		startString=start;
		endString=end;
            int startID=id;
        try { // This code block invokes the HmsasserverPort:getdatatoarray operation on web service
            HmsasService hmsasService = new HmsasServiceLocator();
            HmsasserverPortType _hmsasserverPort = hmsasService.gethmsasserverPort();
            // 最长72h
           String[] result = (String[]) _hmsasserverPort.getflashdata("FLASH_LIGHT_DATA",start,end,"*",
        		   "*","cqu", "123456");
           
           
      if(result.length>1){     
    	     int j=0;
           for (int i = result.length-1; i >=1 ; i--) {// start from one
        	//   String[] lines=result[i].split("\t");
        	     System.out.println(result[i]);
        	       j=j+1;
                 insertDate(result[i],startID+j);
           }
     
         
          startID=startID+result.length-1;
           System.out.println("result.length -1=    "+(result.length-1));
           }  
        } catch(javax.xml.rpc.ServiceException ex) {
            java.util.logging.Logger.getLogger(HmsasService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch(java.rmi.RemoteException ex) {
            java.util.logging.Logger.getLogger(HmsasService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch(Exception ex) {
            java.util.logging.Logger.getLogger(HmsasService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
	
         return  startID;
	}	 
	
	
	  public void insertDate(String result,int startID1){
		 
			/*使用jdbc   查询数据库，先读取数据库的ip(不能写死)    在链接数据库   ，然后再查询       关闭*/
		  
		  String[] lines=result.split("\t");
if(lines.length==10){		   
		   //ADTD adtd=new ADTD();
		   
		   String strDate = lines[0];
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		 Timestamp ts = new Timestamp(System.currentTimeMillis());   
	       String tsStr =strDate.substring(0, 4)+"-"+strDate.substring(4, 6)+"-"+strDate.substring(6, 8)+" "+"00:00:00";
	   //   System.out.println("tsStr=   "+tsStr);
	       try {   
	           ts = Timestamp.valueOf(tsStr);   
	             //adtd.setDate(ts);
	       } catch (Exception e) {   
	           e.printStackTrace();   
	       } 
		   
//System.out.println("lines[0]=  "+lines[0]+"lines.length=  "+lines.length);	       
	       
		   try {
			Date date1 = sdf.parse(strDate);
			Calendar   calendar1   = Calendar.getInstance();  
		     calendar1.setTime(date1);
		    // adtd.setHour(calendar1.get(Calendar.HOUR));
		     //adtd.setMinute(calendar1.get(Calendar.MINUTE));
		     //adtd.setSecond((float)calendar1.get(Calendar.SECOND));
		    // adtd.setLatitude(Float.parseFloat(lines[2]));
		   //  adtd.setLongitude(Float.parseFloat(lines[3]));
		    // adtd.setIntensity(Float.parseFloat(lines[4]));
		   //  adtd.setSteepness(Float.parseFloat(lines[5]));
		  //   adtd.setDeviation(Float.parseFloat(lines[8]));
		  //   adtd.setPositionType(lines[9]);
		  //   adtd.setMAPINFO_ID(startID1);     //插入一条数据  ，setMAPINFO_ID  自动增加
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		//	jdbcadtd.readIP(request);
		//	if(jdbcadtd.SetConnection(jdbcadtd.getURL(),jdbcadtd.getUSERNAME(),jdbcadtd.getPASSWORD())){
		  //  try {
			//	if(test(strDate))
					  // jdbcadtd.addADTD(adtd);
		//	} catch (ParseException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
		//	}
		//	jdbcadtd.close();
		   
		   
		   
		   
		
	  }
	
  }
}
