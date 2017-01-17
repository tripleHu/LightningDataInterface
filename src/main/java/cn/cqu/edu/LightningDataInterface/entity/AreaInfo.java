package cn.cqu.edu.LightningDataInterface.entity;



public class AreaInfo {
	private String name;//区县简称
	private String fullname;//区县全称
	private int fId;//区县编号
	private double longitudeLeft;//"左边界"
	private double longitudeRight;//"右边界"
	private double latitudeUpper;//"上边界"
	private double latitudeLower;//"下边界"
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public double getLongitudeLeft() {
		return longitudeLeft;
	}
	public void setLongitudeLeft(Float longitudeLeft) {
		this.longitudeLeft = longitudeLeft;
	}
	public double getLongitudeRight() {
		return longitudeRight;
	}
	public void setLongitudeRight(Float longitudeRight) {
		this.longitudeRight = longitudeRight;
	}
	public double getLatitudeUpper() {
		return latitudeUpper;
	}
	public void setLatitudeUpper(Float latitudeUpper) {
		this.latitudeUpper = latitudeUpper;
	}
	public double getLatitudeLower() {
		return latitudeLower;
	}
	public void setLatitudeLower(Float latitudeLower) {
		this.latitudeLower = latitudeLower;
	}
	public int getfId() {
		return fId;
	}
	public void setfId(int fId) {
		this.fId = fId;
	}
	
}
