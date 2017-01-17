package cn.cqu.edu.LightningDataInterface.Tools;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapTools {
	 /** 
     * 判断当前位置是否在围栏内 
     * @param mobilelocationEntity 
     * @param PonitList 该数组偶数位为纬度，奇数位为经度
     * @return 
     */  
    public static boolean isInPolygon(MapPoint Checkingpoint,double[] PonitList){  
        double p_x =Checkingpoint.getLongitude();  
        double p_y =Checkingpoint.getLatitude();  
        Point2D.Double point = new Point2D.Double(p_x, p_y);  
 
        List<Point2D.Double> pointList= new ArrayList<Point2D.Double>();  
          
        for (int index=0;index<PonitList.length;index=index+2)
        {  
            double polygonPoint_x=PonitList[index+1];  
            double polygonPoint_y=PonitList[index];  
            Point2D.Double polygonPoint = new Point2D.Double(polygonPoint_x,polygonPoint_y);  
            pointList.add(polygonPoint);  
        }  
        MapTools test = new MapTools();  
        return test.checkWithJdkGeneralPath(point,pointList);  
    }  
    /** 
     * 返回一个点是否在一个多边形区域内 
     * @param point 
     * @param polygon 
     * @return 
     */  
   private boolean checkWithJdkGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {  
          java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();  
 
          Point2D.Double first = polygon.get(0);  
          p.moveTo(first.x, first.y);  
          polygon.remove(0);  
          for (Point2D.Double d : polygon) {  
             p.lineTo(d.x, d.y);  
          }  
 
          p.lineTo(first.x, first.y);  
 
          p.closePath();  
 
          return p.contains(point);  
 
       }
   /**
    *  获取区域边界数组，该数组偶数位为纬度，奇数位为经度，如边界的第一个点由数组第0个和第1个组成，边界的第二个点由数组第2个和第3个组成.......
    * @param index
    * 0-城口县 1-巫溪县 2- 巫山县 3-开县 4-云阳县 5-奉节县 6-万州区 7-梁平县 8-忠县 9-石柱县 10-垫江县 11-丰都县 12-彭水县 13-黔江区 14-酉阳县 15-秀山县 16-大渡口区 17-武隆县 18-涪陵区 19-长寿区 20-南川区
    * 21-万盛经开区 22-綦江区 23-江津区 24-璧山区 25-北碚区 26-渝北区 27-巴南区 28-南岸区 29-江北区 30-合川区 31-沙坪坝区 32-九龙坡区 33-荣昌区 34-永川区 35-大足区 36-铜梁区 37-潼南县 38-渝中区 39-双桥区 40-重庆全市 
    * @return 区域边界数组
    * @throws NumberFormatException
    * @throws IOException
    */
  public static double[] getAreaPointsArray(int index) throws NumberFormatException, IOException
  {
	  String separator=System.getProperty("file.separator");//文件路径间隔符，为了windows和linux兼容
	 
	  File file = new File(new FileTools().GetProjectRootPath()+"resources"+separator+"AreaPointsArray.txt");//Junit单元测试时使用
	  //File file = new File(new FileTools().GetProjectRootPath()+"resources"+separator+"AreaPointsArray.txt");//部署web运行时使用
	  BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	  String s = null;
	  double []AreaPointsArray = null;
	  int Curindex=0;
	  //若index超界，则直接令index为0
	  if(index<0||index>40)
	  {
		index=0;
	  }
	  //使用readLine方法，一次读一行
	  while((s = br.readLine())!=null)
	  {
		  //是否是要获取的那一行数据
		  if(Curindex==index)
		  {
			  String[] arr = s.split(",");//以逗号分开并组成数组
			  AreaPointsArray=new double[arr.length];
			  for(int i=0;i<arr.length;i++)
			  {
				  AreaPointsArray[i] =Double.parseDouble(arr[i]);//将字符串数组转换为double类型
			  }
			  break;
		  }
		  Curindex++;
	  }
	  
	  br.close();
	  return AreaPointsArray;
  }
  /** 根据圆心经纬度和圆半径计算圆的正方形包围范围
   * @param raidus 单位米
   * return minLat,minLng,maxLat,maxLng
  */
   public static double[] getAround(double lat, double lon, float raidus) 
   {
	   Double latitude = lat;
	   Double longitude = lon;
	
	   Double degree = (24901 * 1609) / 360.0;
	   double raidusMile = raidus;
	
	   Double dpmLat = 1 / degree;
	   Double radiusLat = dpmLat * raidusMile;
	   Double minLat = latitude - radiusLat;
	   Double maxLat = latitude + radiusLat;
	
	   Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
	   Double dpmLng = 1 / mpdLng;
	   Double radiusLng = dpmLng * raidusMile;
	   Double minLng = longitude - radiusLng;
	   Double maxLng = longitude + radiusLng;
	   return new double[]{minLat, minLng, maxLat, maxLng};
  }
   //计算两点距离，单位米
   private static double EARTH_RADIUS = 6378137;
   private static double rad(double d)
   {
      return d * Math.PI / 180.0;
   }
   public static double GetDistance(double lng1, double lat1, double lng2, double lat2)
    {
           double radLat1 = rad(lat1);
           double radLat2 = rad(lat2);
           double a = radLat1 - radLat2;
           double b = rad(lng1) - rad(lng2);
           double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
            Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
           s = s * EARTH_RADIUS;
           s = Math.round(s * 10000) / 10000;
           return s;
   }
}
