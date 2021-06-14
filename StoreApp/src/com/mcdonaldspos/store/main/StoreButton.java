package com.mcdonaldspos.store.main;

import javax.swing.JButton;

public class StoreButton extends JButton{
	private int id;
	
	public StoreButton(String title) {
		super(title);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

}
