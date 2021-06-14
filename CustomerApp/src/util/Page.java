package util;

import java.sql.Connection;

import javax.swing.JFrame;

import com.mcdonaldspos.main.AppMain;

public class Page extends JFrame{
	private AppMain appMain;
	
	//모든 페이지가 드라이버와 다비 연동이된 객체에 접근 할 수 있도록!
	public Page(AppMain appMain) {
		this.appMain = appMain;
	}
	
	public AppMain getAppMain() {
		return appMain;
	}
}
