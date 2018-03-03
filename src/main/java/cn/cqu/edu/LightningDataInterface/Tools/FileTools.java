package cn.cqu.edu.LightningDataInterface.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.hibernate.loader.custom.Return;

public  class FileTools {
	/**
	 *  获取web项目的根目录的绝对地址，能适应跨平台应用
	 * @return web项目的根目录，该路径最后带一个斜线
	 *  如 D:\Program Files\apache-tomcat-8.0.36\wtpwebapps\LightningDensity\
	 * @throws UnsupportedEncodingException
	 */
	 public String GetProjectRootPath() throws UnsupportedEncodingException
	{
		 char separator=System.getProperty("file.separator").charAt(0);//文件路径间隔符，为了windows和linux兼容
		 
		 String configPath = java.net.URLDecoder.decode(this.getClass().getResource("/").getPath(),"utf-8"); //获取到WEB-INF/classes/路径
		 configPath=configPath.substring(0, configPath.length()-16);//删除 “WEB-INF/classes/” 所占用的16个字符，便能得到项目的根目录的绝对地址
		 configPath=configPath.substring(1);//删除获取到路径里的第一个字符，该字符为一个斜线：‘/’
		 
		String rootpath=configPath.replace('/', separator);//使文件路径的斜线跨平台兼容
		rootpath=rootpath.replace('\\', separator);//使文件路径的斜线跨平台兼容
		
		String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win"))//是windows系统路径前不用加斜线
		{  
			 return rootpath;
		}  
		else //是linux系统路径前加斜线，如/home/java/
		{
			 return "/"+rootpath;
		}
		
	}
	 
	 public String ReadFileAsString(String FilePath)
	 {
		 File file = new File(FilePath);
		 Scanner scanner = null;
	     StringBuilder buffer = new StringBuilder();
	     try 
	     {
	            scanner = new Scanner(file, "utf-8");
	            while (scanner.hasNextLine()) 
	            {
	                buffer.append(scanner.nextLine());
	            }
	 
	      } 
	     catch (FileNotFoundException e)
	     {
	            // TODO Auto-generated catch block  
	      }
	     finally 
	     {
	            if (scanner != null) 
	            {
	                scanner.close();
	            }
	     }
	         
	     return buffer.toString();
	 }
	
}
