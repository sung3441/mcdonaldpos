package com.mcdonaldspos.rider.order;

public class Order {
	private int ord_id;
	private String user_name;
	private int price;
	private String addr;
	private String rider_name;
	private String info;
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getOrd_id() {
		
		return ord_id;
	}
	public void setOrd_id(int ord_id) {
		this.ord_id = ord_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getRider_name() {
		return rider_name;
	}
	public void setRider_name(String rider_name) {
		this.rider_name = rider_name;
	}
	
	
}


