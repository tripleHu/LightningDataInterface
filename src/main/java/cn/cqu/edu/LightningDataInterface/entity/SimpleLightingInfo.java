package cn.cqu.edu.LightningDataInterface.entity;

public class SimpleLightingInfo {
	/**
	 * 纬度
	 */
	private double latitude;
	/**
	 * 纬度
	 */
	private double longitude;
	/**
	 * 强度
	 */
	private float intensity;
	/**
	 * long型表示的时间，从1970年1月1日起经过的时间
	 */
	private long datetime;
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public float getIntensity() {
		return intensity;
	}
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	public long getDatetime() {
		return datetime;
	}
	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}
	
}
