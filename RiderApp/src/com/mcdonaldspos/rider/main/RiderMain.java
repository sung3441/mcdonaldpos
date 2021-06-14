package com.mcdonaldspos.rider.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mcdonaldspos.rider.chat.RiderSocket;
import com.mcdonaldspos.rider.order.RiderOrder;
import com.mcdonaldspos.rider.search.RiderSearch;

public class RiderMain extends JFrame implements ActionListener{
	
	//라이더 메인메뉴
	JPanel page_north; //라이더 메뉴의 메인 타이틀을 걸어놓을 자리
	JPanel page_center;
	String[] rider_title= {"주문조회","라이더조회"};
	CustomButton[] rider_menu=new CustomButton[rider_title.length];
	String[] weather= {"할증", "심야", "우천", "폭설"};
	JComboBox<String> combo=new JComboBox<String>(weather);
	
	RiderPage[] pages=new RiderPage[2];
	//최초 데이터 베이스 연결을 한 연결 객
	Connection con;
	JPanel p_banner;
	
	//데이터베이스
	String driver="com.mysql.jdbc.Driver";
	String url="jdbc:mysql://localhost:3306/mcdonaldspos?characterEncoding=UTF-8";
	String user="root";
	String password="1234";
	
	RiderOrder riderOrder;
	RiderSearch riderSearch;

	private Socket socket;
	private RiderSocket riderSocket;
	private Thread clientThread; //대화용 쓰레드
	Image image; //banner
	public RiderMain() {
		connect();
		runClient(); //서버 가동
		image = java.awt.Toolkit.getDefaultToolkit().getImage("D:\\workspace\\korea_javaworkspace\\RiderApp\\res\\banner.PNG");
		image = image.getScaledInstance(200, 50, Image.SCALE_SMOOTH);
		p_banner = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 200, 50,  this);
			}
		};
		p_banner.setPreferredSize(new Dimension(200,  50));
		
		page_north = new JPanel();
		
		for(int i=0;i<rider_title.length;i++) {
			rider_menu[i] = new CustomButton(rider_title[i]);
			rider_menu[i].setId(i);
		}
		
		page_north.setPreferredSize(new Dimension(1200, 70));
		combo.setPreferredSize(new Dimension(100, 26));
		
		//페이지 생성
		page_center = new JPanel();
		pages[0]= riderOrder = new RiderOrder(this);
		pages[1]= riderSearch = new RiderSearch(this);
		riderOrder.getOrderList(); //최초에 화면에 라이더 목록 붙이기 
		pages[0].setVisible(true);
		
		for(JButton bt : rider_menu) {
			page_north.add(bt);
		}
		add(page_north, BorderLayout.NORTH);
		for(RiderPage p: pages) {
			page_center.add(p);
		}
		add(page_center);
		
		page_north.add(combo);
		
		//리스너
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		for(int i=0; i<rider_menu.length;i++) {
			rider_menu[i].addActionListener(this);
		}
		page_north.add(p_banner);
		//화면 띄우기
		setTitle("주문 및 라이더 조회");
		setBounds(200, 100, 1200, 700);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		//System.out.println(e.getSource());
		CustomButton bt = (CustomButton)e.getSource();
		for(int i=0;i<pages.length;i++) {
			if(bt.getId()==i) {
				if(i == 0) {
					riderOrder.getOrderList();
				}else if(i == 1) {
					riderSearch.getRiderList();
				}
				pages[i].setVisible(true);
			}else {
				pages[i].setVisible(false);
			}
		}
	}
	
	//소켓 생성 및 가동
	public void runClient() {
		socket = RiderSocket.getSocket(this);
		if(socket == null) {
			System.exit(0);
		}
		riderSocket = RiderSocket.getClientSocket();
		Runnable runnable = riderSocket;
		clientThread = new Thread(runnable);
		clientThread.start();
	}
	
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(url, user, password);
			if(con!=null) {
				this.setTitle("Mcdonalds Rider");
			}else {
				JOptionPane.showMessageDialog(this, "접속 실패");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public RiderSocket getRiderSocket() {
		return riderSocket;
	}
	
	public Connection getCon() {
		return con;
	}
	
	public RiderOrder getRiderOrder() {
		return riderOrder;
	}
	
	public RiderSearch getRiderSearch() {
		return riderSearch;
	}
	
	public static void main(String[] args) {
		new RiderMain();
	}

}
