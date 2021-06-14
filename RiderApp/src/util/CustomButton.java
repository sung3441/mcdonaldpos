package util;

import javax.swing.JButton;

public class CustomButton extends JButton{
	private int id;
	public CustomButton(String title) {
		super(title);
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
