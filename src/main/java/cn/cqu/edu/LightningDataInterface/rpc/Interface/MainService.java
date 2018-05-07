package cn.cqu.edu.LightningDataInterface.rpc.Interface;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public interface MainService {
	/**
	 * 获取雷电密度数据
	 * 
	 * @param year
	 * @param radium
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String GetLightningDensityForWholeProvice(int year, int radium)
			throws NumberFormatException, IOException, SQLException;
	/**
	 * 选定行政区域雷电活动情况 Date 可以为三种类型xxxx代表年xxxx-xx代表年月xxxx-xx-xx代表年月日 AreaIndex
	 * 0-城口县 1-巫溪县 2- 巫山县 3-开县 4-云阳县 5-奉节县 6-万州区 7-梁平县 8-忠县 9-石柱县 10-垫江县 11-丰都县
	 * 12-彭水县 13-黔江区 14-酉阳县 15-秀山县 16-大渡口区 17-武隆县 18-涪陵区 19-长寿区 20-南川区 21-万盛经开区
	 * 22-綦江区 23-江津区 24-璧山区 25-北碚区 26-渝北区 27-巴南区 28-南岸区 29-江北区 30-合川区 31-沙坪坝区
	 * 32-九龙坡区 33-荣昌区 34-永川区 35-大足区 36-铜梁区 37-潼南县 38-渝中区 39-双桥区 40-重庆全市
	 * 
	 * @param AreaIndex
	 * @param Date
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public String GetLightningAreaStatistic(int AreaIndex, String Date)
			throws NumberFormatException, IOException, ParseException, SQLException;
	/**
	 * 生成圆形区域雷电活动统计数据 * Date 可以为三种类型xxxx代表年xxxx-xx代表年月xxxx-xx-xx代表年月日
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
	public String GetLightingCricleStatisticByDate(double CricleLatitude, double CricleLongitude, float Radium,
			String Date) throws NumberFormatException, IOException, ParseException, SQLException;
	/**
	 * 生成矩形区域雷电活动统计数据 * Date 可以为三种类型xxxx代表年xxxx-xx代表年月xxxx-xx-xx代表年月日
	 * 
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
	public String GetLightingRectangleStatisticByDate(double latitudeLower, double latitudeUpper, double longitudeLeft,
			double longitudeRight, String Date) throws NumberFormatException, IOException, ParseException, SQLException;
	/**
	 * 返回给定时间段闪电活动数据
	 * 
	 * @param StartTime
	 * @param EndTime
	 * @return
	 */
	public String GetLightningActiveByDatetime(long StartTime, long EndTime);
}