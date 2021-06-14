package util;

import javax.swing.JPanel;

import com.mcdonaldspos.main.AppMain;

public class CustomPannel extends JPanel{
	AppMain appMain;
	
	public CustomPannel(AppMain appMain) {
		this.appMain = appMain;
	}

	AppMain getAppMain() {
		return appMain;
	}

	void setAppMain(AppMain appMain) {
		this.appMain = appMain;
	}
}
