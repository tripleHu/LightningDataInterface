package cn.cqu.edu.LightningDataInterface.entity;

import java.util.List;

public class LigthingRectangleActiveStatistic_Month_Year {
	/*
	 * 左下纬度
	 */
	private double latitudeLower;
	/*
	 * 左下经度
	 */
	private double longitudeLeft;
	/*
	 * 右上纬度
	 */
	private double latitudeUpper;
	/*
	 * 右上经度
	 */
	private double longitudeRight;
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
	public double getLatitudeLower() {
		return latitudeLower;
	}
	public void setLatitudeLower(double latitudeLower) {
		this.latitudeLower = latitudeLower;
	}
	public double getLongitudeLeft() {
		return longitudeLeft;
	}
	public void setLongitudeLeft(double longitudeLeft) {
		this.longitudeLeft = longitudeLeft;
	}
	public double getLatitudeUpper() {
		return latitudeUpper;
	}
	public void setLatitudeUpper(double latitudeUpper) {
		this.latitudeUpper = latitudeUpper;
	}
	public double getLongitudeRight() {
		return longitudeRight;
	}
	public void setLongitudeRight(double longitudeRight) {
		this.longitudeRight = longitudeRight;
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
