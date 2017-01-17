package cn.cqu.edu.LightningDataInterface.entity;

import java.util.List;

public class LigthingCricleActiveStatistic_Month_Year {
	/*
	 * 中心纬度
	 */
	private double CricleLatitude;
	/*
	 * 中心经度
	 */
	private double CricleLongitude;
	/*
	 * 半径
	 */
	private float Radium;
	/*
	 * 日期
	 */
	private String date;
	/*
	 * 雷电幅值
	 */
	private float AreaMaxIntensity;
	/*
	 * 雷电正幅值
	 */
	private float AreaPositiveMaxIntensity;
	/*
	 * 雷电负幅值
	 */
	private float AreaNegativeMaxIntensity;
	/*
	 * 雷电次数
	 */
	private int AreaLightningCount;
	/*
	 * 正闪电次数
	 */
	private int AreaPositiveCount;
	/*
	 * 负闪电次数
	 */
	private int AreaNegativeCount;
	/*
	 * 闪电开始时间
	 */
	private String LightingStartTime;
	/*
	 * 闪电结束时间
	 */
	private String LightingEndTime;
	/*
	 * 雷电日数
	 */
	private int LightingDays;
	/*
	 * 所有时间区间的数据
	 */
	private List<TimePeriod> time;
	public double getCricleLatitude() {
		return CricleLatitude;
	}
	public void setCricleLatitude(double cricleLatitude) {
		CricleLatitude = cricleLatitude;
	}
	public double getCricleLongitude() {
		return CricleLongitude;
	}
	public void setCricleLongitude(double cricleLongitude) {
		CricleLongitude = cricleLongitude;
	}
	public float getRadium() {
		return Radium;
	}
	public void setRadium(float radium) {
		Radium = radium;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public float getAreaMaxIntensity() {
		return AreaMaxIntensity;
	}
	public void setAreaMaxIntensity(float areaMaxIntensity) {
		AreaMaxIntensity = areaMaxIntensity;
	}
	public float getAreaPositiveMaxIntensity() {
		return AreaPositiveMaxIntensity;
	}
	public void setAreaPositiveMaxIntensity(float areaPositiveMaxIntensity) {
		AreaPositiveMaxIntensity = areaPositiveMaxIntensity;
	}
	public float getAreaNegativeMaxIntensity() {
		return AreaNegativeMaxIntensity;
	}
	public void setAreaNegativeMaxIntensity(float areaNegativeMaxIntensity) {
		AreaNegativeMaxIntensity = areaNegativeMaxIntensity;
	}
	public int getAreaLightningCount() {
		return AreaLightningCount;
	}
	public void setAreaLightningCount(int areaLightningCount) {
		AreaLightningCount = areaLightningCount;
	}
	public int getAreaPositiveCount() {
		return AreaPositiveCount;
	}
	public void setAreaPositiveCount(int areaPositiveCount) {
		AreaPositiveCount = areaPositiveCount;
	}
	public int getAreaNegativeCount() {
		return AreaNegativeCount;
	}
	public void setAreaNegativeCount(int areaNegativeCount) {
		AreaNegativeCount = areaNegativeCount;
	}
	public String getLightingStartTime() {
		return LightingStartTime;
	}
	public void setLightingStartTime(String lightingStartTime) {
		LightingStartTime = lightingStartTime;
	}
	public String getLightingEndTime() {
		return LightingEndTime;
	}
	public void setLightingEndTime(String lightingEndTime) {
		LightingEndTime = lightingEndTime;
	}
	public int getLightingDays() {
		return LightingDays;
	}
	public void setLightingDays(int lightingDays) {
		LightingDays = lightingDays;
	}
	public List<TimePeriod> getTime() {
		return time;
	}
	public void setTime(List<TimePeriod> time) {
		this.time = time;
	}
	
}
