package cn.cqu.edu.LightningDataInterface.entity;

public class TimePeriod {
	/*
	 * 日期
	 */
	private String date;
	/*
	 * 雷电幅值
	 */
	private float MaxIntensity;
	/*
	 * 雷电正幅值
	 */
	private float PositiveMaxIntensity;
	/*
	 * 雷电负幅值
	 */
	private float NegativeMaxIntensity;
	/*
	 * 雷电次数
	 */
	private int LightningCount;
	/*
	 * 正闪电次数
	 */
	private int PositiveCount;
	/*
	 * 负闪电次数
	 */
	private int NegativeCount;
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
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public float getMaxIntensity() {
		return MaxIntensity;
	}
	public void setMaxIntensity(float maxIntensity) {
		MaxIntensity = maxIntensity;
	}
	public float getPositiveMaxIntensity() {
		return PositiveMaxIntensity;
	}
	public void setPositiveMaxIntensity(float positiveMaxIntensity) {
		PositiveMaxIntensity = positiveMaxIntensity;
	}
	public float getNegativeMaxIntensity() {
		return NegativeMaxIntensity;
	}
	public void setNegativeMaxIntensity(float negativeMaxIntensity) {
		NegativeMaxIntensity = negativeMaxIntensity;
	}
	public int getLightningCount() {
		return LightningCount;
	}
	public void setLightningCount(int lightningCount) {
		LightningCount = lightningCount;
	}
	public int getPositiveCount() {
		return PositiveCount;
	}
	public void setPositiveCount(int positiveCount) {
		PositiveCount = positiveCount;
	}
	public int getNegativeCount() {
		return NegativeCount;
	}
	public void setNegativeCount(int negativeCount) {
		NegativeCount = negativeCount;
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
}
