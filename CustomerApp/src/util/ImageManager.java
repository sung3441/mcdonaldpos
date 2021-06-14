package util;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class ImageManager {

	public ImageIcon getScaledIcon(String filename, int width, int height) {
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource(filename));
		icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		return icon;
	}
	
	public Image getScaledImage(String filename, int width, int height) {
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource(filename));
		icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		return icon.getImage();
	}
	
	
	
	
	
}
