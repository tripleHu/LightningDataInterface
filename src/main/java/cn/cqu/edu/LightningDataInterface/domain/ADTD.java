package cn.cqu.edu.LightningDataInterface.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "adtdsReal")
public class ADTD {
	@Id
	@GeneratedValue
	private long MAPINFO_ID;


	@Column(nullable = true)
	private Integer id;
	/**
	 * 
	 */

	@Column(nullable = false)
	private Date date;
	/*
	 * 日期
	 */

	@Column(nullable = false)
	private Integer hour;
	/*
	 *时 
	 */
	
	@Column(nullable = false)
	private Integer minute;
	/*
	 * 分
	 */
	
	@Column(nullable = false)
	private float second;
	/*
	 * 秒
	 */

	@Column(nullable = false)
	private Float latitude;
	/*
	 * 纬度
	 */

	@Column(nullable = false)
	private Float longitude;
	/*
	 * 经度
	 */
	
	@Column(nullable = false)
	private Float intensity;
	/*
	 * 闪电强度
	 */
	@Column(nullable = false)
	private Float steepness;
	/*
	 * 陡度
	 */
	@Column(nullable = false)
	private Float deviation;
	/*
	 * 误差
	 */
	@Column(nullable = true, length = 8)
	private String positionType;
	/*
	 * 定位方式
	 */
	@Column(nullable = true, length = 20)
	private String county;
	/*
	 * 区县
	 */
	@Column(nullable = true, length = 10)
	private String city;
	/*
	 * 市
	 */
	public long getMAPINFO_ID() {
		return MAPINFO_ID;
	}
	public void setMAPINFO_ID(long mAPINFO_ID) {
		MAPINFO_ID = mAPINFO_ID;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	public Integer getMinute() {
		return minute;
	}
	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	public float getSecond() {
		return second;
	}
	public void setSecond(float second) {
		this.second = second;
	}
	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public Float getIntensity() {
		return intensity;
	}
	public void setIntensity(Float intensity) {
		this.intensity = intensity;
	}
	public Float getSteepness() {
		return steepness;
	}
	public void setSteepness(Float steepness) {
		this.steepness = steepness;
	}
	public Float getDeviation() {
		return deviation;
	}
	public void setDeviation(Float deviation) {
		this.deviation = deviation;
	}
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
