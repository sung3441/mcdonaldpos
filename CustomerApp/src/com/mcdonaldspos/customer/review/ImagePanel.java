package com.mcdonaldspos.customer.review;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import util.ImageManager;

public class ImagePanel extends JPanel{
	String filename;
	int id;
	String imgDir;
	Image image;
	
	public ImagePanel(String filename, int id) {
		this.filename = filename;
		this.id = id;
		imgDir = "images/review/";
		if(filename != null) {
			System.out.println(imgDir+filename);
			image = new ImageManager().getScaledImage(imgDir+filename, 65, 65);
		}
	}
	
	public void paint(Graphics g) {
		if(filename != null) {
			g.drawImage(image, 0, 0, this);			
		}else {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 65, 65);
		}
	}
	
	public int getId() {
		return id;
	}
}
