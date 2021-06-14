package com.mcdonaldspos.rider.main;

import javax.swing.JButton;


public class CustomButton extends JButton{
	private int id;
	
	public CustomButton(String title) {
		super(title);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
