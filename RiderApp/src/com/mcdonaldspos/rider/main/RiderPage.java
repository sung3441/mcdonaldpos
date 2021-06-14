package com.mcdonaldspos.rider.main;

import java.awt.Dimension;

import javax.swing.JPanel;

public class RiderPage extends JPanel{
	private RiderMain riderMain;
	
	//다른 패키지에서 riderMain 접근
	public RiderMain getRiderMain() {
		return riderMain;
	}
	
	public RiderPage(RiderMain riderMain) {
		this.riderMain=riderMain;
		setPreferredSize(new Dimension(1150, 500));
		setVisible(false);
	}
}
