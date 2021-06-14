package com.mcdonaldspos.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.mcdonaldspos.customer.login.Login;
import com.mcdonaldspos.customer.product.ProductMain;

//앱의 메인 즉, 실행부임
public class AppMain extends JFrame{
	
	private final String driverPath = "com.mysql.jdbc.Driver"; //오라클 드라이버 경로
	private final String url = "jdbc:mysql://localhost/mcdonaldspos?characterEncoding=UTF-8";
	private final String user = "root"; //추후 우리 데이터 베이스 연결
	private final String password = "1234"; //추후 우리 데이터 베이스 연결
	
	private Connection con; //디비 연동을 위한 객체
	
	private JButton bt_close; //프로그램을 완전히 종료할 버튼
	
	private Dimension screenSize; //모니터 화면의 해_상도 얻기
	private final int frameX = 125;
	private final int frameY = 80;
	private Login login;
	
	public AppMain() {
		connect(); //드라이버, 디비 연결
		//생성
		bt_close = new JButton("프로그램 종료");
		
		//스타일
		setLayout(new FlowLayout());
		
		//조립
		add(bt_close);
		
		//보여주기
		setVisible(true);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //모니터 해상도 얻기
		int centerX = (screenSize.width / 2) - (frameX / 2); //화면의 중앙 구하기
		setBounds(2500, 20, frameX, frameY);
		
		openPage(); //customer, store, rider 세 개의 페이지를 동시에 띄움
	}
	
	//customer, rider, store
	public void openPage() {
		login = new Login(this);
		this.dispose();
		//테스트를 위해 잠시 생성함!!!!!
		//new ProductMain(this);
		//디자인 테스트 후 삭제!!!!!!!!
	}
	
	//드라이버 로드 및 데이터 베이스 연동 함수
	public void connect() {
		try {//오라클 연동, 데이터 베이스와의 연동이 여러 번 있으면 안되므로 메인에서 처리
			Class.forName(driverPath);
			System.out.println("드라이버 로드 완료");
			con = DriverManager.getConnection(url, user, password); //오라클 연동
			if(con == null) { //오라클 연동 실패
				System.out.println("오라클 연동 xx");				
			}else { //오라클 연동이 됐다면..
				setTitle("프로그램이 동작 중입니다.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("드라이버 로드 실패");
		} catch (SQLException e) {
			setTitle("mysql 연결 실패");
			e.printStackTrace();
		}
	}
	
	
	//디비 연결 정보를 가지고 있는 커넥션 객체 반환
	public Connection getCon() {
		return con;
	}
	
	//드라이버 연결 및 데이터 베이스 연결 끊는 함수
	public void disconnect() {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	public void release(PreparedStatement pstmt, ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void release(PreparedStatement pstmt) {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
		AppMain appMain = new AppMain();
	}
}
