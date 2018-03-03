package cn.cqu.edu.LightningDataInterface.Tools;

import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;


/**
 * 系统配置信息
 */
public class SystemSetting {
	
	private String ADTD_INTERFACE_ADDRESS;//雷电数据接口地址
	private String UPLOAD_PATH;//报告上传地址

	
	/**
	 * 获取雷电数据接口地址
	 * @return
	 */
	public String get_ADTD_INTERFACE_ADDRESS()
	{
		try //获取系统设置
		{
			String aString2=new FileTools().ReadFileAsString((new FileTools().GetProjectRootPath()+"resources"+System.getProperty("file.separator")+"SystemSetting.json"));
			SystemSetting foo[] = new Gson().fromJson(aString2, SystemSetting[].class);//将json文本信息转换为对象
			ADTD_INTERFACE_ADDRESS=foo[0].ADTD_INTERFACE_ADDRESS;
		} 
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return ADTD_INTERFACE_ADDRESS;
	}
	/**
	 * 获取报告上传地址
	 * @return
	 */
	public String get_UPLOAD_PATH()
	{
		try //获取系统设置
		{
			String aString2=new FileTools().ReadFileAsString((new FileTools().GetProjectRootPath()+"resources"+System.getProperty("file.separator")+"SystemSetting.json"));
			SystemSetting foo[] = new Gson().fromJson(aString2, SystemSetting[].class);//将json文本信息转换为对象
			UPLOAD_PATH=foo[0].UPLOAD_PATH;
		} 
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return UPLOAD_PATH;
	}
}
