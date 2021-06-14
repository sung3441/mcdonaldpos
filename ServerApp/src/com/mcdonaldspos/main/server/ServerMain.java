package com.mcdonaldspos.main.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ServerMain extends JFrame{
	ServerThread serverThread;
	JTextArea area;
	JScrollPane scroll;
	
	ServerSocket server;
	Thread ServerThread; //접속자 감지용 스레드
	private Connection con;
	private String url = "jdbc:mysql://localhost:3306/mcdonaldspos?characterEncoding=utf-8";
	private String user = "root";
	private String password = "1234";
	
	public ServerMain() {
		connect();
		area = new JTextArea();
		scroll = new JScrollPane(area);

		add(scroll);
		
		setVisible(true);
		setBounds(100, 100, 300, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//ServerFlag = false;
				disconnect();
			}
		});
		serverThread = new ServerThread(this);
		serverThread.start();
	}
	
	//mysql 연결하기
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			if(con != null) {
				setTitle("MCD 서버 가동 중");
			}else if(con == null){
				setTitle("서버 가동 실패");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getCon() {
		return con;
	}
	
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
		new ServerMain();
	}
}

