package com.mcdonaldspos.customer.product;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import com.mcdonaldspos.customer.client.ClientSocket;
import com.mcdonaldspos.customer.login.Member;
import com.mcdonaldspos.customer.order.OrderModel;
import com.mcdonaldspos.customer.order.PayPage;
import com.mcdonaldspos.customer.review.NoticePanel;
import com.mcdonaldspos.customer.review.RegistReview;
import com.mcdonaldspos.main.AppMain;
import com.sun.net.httpserver.Authenticator.Result;

import util.CustomButton;
import util.CustomPannel;
import util.ImageManager;
import util.JsonManager;
import util.Page;

public class ProductMain extends Page{
	//크게 상 중 하 세개의 판넬로 나누며
	//south는 배너, north는 메뉴버튼으로 고정 시킨다.
	//center에는 각각의 페이지들이 미리 존재시킨다.
	//로그인한 유저의 정보를 담아야함
	private Member member;
	private Socket socket; //대화용 소켓
	private ClientSocket clientSocket; //대화용 소켓을 가지고 있는 클래스
	private Thread clientThread; //대화용 쓰레드
	
	//상단 버튼이 붙을 곳==============================
	JPanel p_north;
	String[] topNavi = {"메인 메뉴", "주문내역", "1:1상담", "Review"};
	//버튼을 담을 배열
	CustomButton[] naviAr = new CustomButton[topNavi.length];
	
	
	//페이지 붙을 가운데 영역 정의=========================
	JPanel p_center;
	
	CustomPannel[] mainPannelAr = new CustomPannel[naviAr.length];
	CustomPannel p_product; //메인메뉴 버튼 눌렀을 때
	CustomPannel p_orderList; //주문내역 버튼 눌렀을 때
	CustomPannel p_chat; //1:1상담 버튼 눌렀을 때
	CustomPannel p_review; //리뷰버튼 눌렀을 때
	
	//가운데 영역중 왼쪽을 담당하는 패널
	//메뉴의 카테고리가 나온다.
	//좌측 카테고리에 나올 이미지 경로
	String pathCategory = "images/category/";
	//좌측 카테고리에 나올 이미지 파일명
	String[] categoryName = {"빅맥.png", "빅맥 세트.png", "맥윙.png", "아이스크림콘.png", "코카콜라.png", "아이스아메리카노.png", "에그 맥머핀.png", "에그 맥머핀 세트.png"};
	JPanel p_product_west;
	CustomButton[] categoryAr = new CustomButton[categoryName.length];
	JScrollPane scroll_category;
	
	
	//가운데 영역의 가운데를 담당하는 패널 메뉴가 나오게 되는 곳
	String[] folderPath = {"햄버거단품/", "햄버거세트/", "사이드/", "디저트/", "음료/", "맥카페/", "맥모닝단품/", "맥모닝세트/"};
	JPanel p_product_center;
	ProductPage[] productPageAr = new ProductPage[folderPath.length];
	//가운데 영역의 우측을 담당하는 패널
	//장바구니목록이 나오게 될 곳.
	//장바구니 중에서 최상단 판넬
	JPanel p_product_east;
	JPanel p_basket_top;
	JButton bt_basket_del;
	
	//장바구니 중 목록을 보여줄 리스트 판넬
	JPanel p_basket_list;
	
	//장바구니목록 조회결과를 담을 테이블과 장바구니 조회 전용 모델
	JTable table_basket = new JTable();
	ProductModel productModel;
	JScrollPane scroll_basket;
	
	//장바구니 중 종합 가격과 결재 버튼
	JPanel p_basket_total;
	JLabel lb_totalPrice;
	JButton bt_pay;
	int totalPrice = 0;
	
	//화면 가운데에 붙여질 주문 정보 확인 페이지
	JTable table_ord = new JTable();
	OrderModel ordModel;
	JScrollPane scroll_ord;
	
	//채팅관련 페이지
	JPanel p_chatBox; //채팅창 판넬
	JPanel p_chat_top; //채팅창 윗 부분-공지 나오는 곳
	JLabel lb_chat_top; //공지 적을 라벨
	JTextArea area_chat; //채팅 대화 내용 올라올 곳
	JScrollPane scroll_chat;
	JPanel p_send; //채팅입력과 보내기 버튼
	JTextField t_chat; //채팅 입력하는 창
	JButton bt_chat; //채팅 보내기 버튼
	//리뷰관련 페이지
	NoticePanel p_notice;
	
	//배너 정의===================================
	JPanel p_banner;
	
	//배너 관련된 이미지 주소
	String bannerPath = "images/banner/menu/";
	String[] imgName = {"bts.PNG", "맥런치.PNG", "스트로베리.PNG", "트리플치즈.PNG", "필레오피쉬.PNG", "해피스낵.PNG"};
	Thread bannerThread; //배너를 변경할 쓰레드
	JProgressBar bannerBar; //배너 변경 시간을 보여줄 바
	int bannerCnt = 0;
	int bannerIndex = 0;
	String path = "images/banner/menu/";
	ImageManager imageManager = new ImageManager();
	
	//생성자
	public ProductMain(AppMain appMain, Member member) {
		super(appMain);
		this.member = member;
		setTitle(this.member.getUser_id()+"님 접속 중");
		runClient(); //소켓 생성 및 쓰레드 가동
		//로그인 시 로그인 한 계정의 상태를 바꿔주는 메서드
		logOnOff("on");
		//상단 네비 관련===============================
		p_north = new JPanel();
		p_north.setLayout(new GridLayout(1, 5));
		//네비에 붙을 버튼 생성
		for(int i = 0; i < naviAr.length; i++) {
			//버튼 생성 후에 id부여 및 배열에 담기
			naviAr[i] = new CustomButton(topNavi[i]);
			naviAr[i].setId(i);
			if(i == 0) {
				naviAr[i].setBackground(Color.lightGray);				
			}else {
				naviAr[i].setBackground(Color.WHITE);
			}
			naviAr[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideMenu(e);
				}
			});;
			p_north.add(naviAr[i]);
		}
		p_north.setPreferredSize(new Dimension(1200, 50));
		add(p_north, BorderLayout.NORTH);
		
		//센터 관련================================
		p_center = new JPanel();
		mainPannelAr[0] = p_product = new CustomPannel(getAppMain());
		mainPannelAr[1] = p_orderList = new CustomPannel(getAppMain());
		mainPannelAr[2] = p_chat = new CustomPannel(getAppMain());
		mainPannelAr[3] = p_review = new CustomPannel(getAppMain());
		
		p_product.setLayout(new BorderLayout());
		
		//센터 중 맨 왼쪽---------------------------------------------------
		p_product_west = new JPanel();
		p_product_west.setPreferredSize(new Dimension(150, 910));
		scroll_category = new JScrollPane(p_product_west);
		//좌측카테고리 버튼 생성 및 이미지 부여
		for(int i = 0; i < categoryName.length; i++) {
			categoryAr[i] = new CustomButton("") {
				public void paint(Graphics g) {
					Image image = imageManager.getScaledImage(pathCategory+categoryName[this.getId()], 130, 112);
					g.drawImage(image, 0, 0, this);
				}
			};
			categoryAr[i].setId(i);
			categoryAr[i].setPreferredSize(new Dimension(140, 104));
			categoryAr[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideProduct(e);
				}
			});
			p_product_west.add(categoryAr[i]);
		}
		//해결 스크롤이 담고 있는 컨테이너의 크기만큼만 스크롤이 내려가는 것임
		p_product.add(scroll_category, BorderLayout.WEST);
		
		//가운데 제품 정보 뜨는 페이지 정의--------------------------------------
		p_product_center = new JPanel();
		p_product_center.setPreferredSize(new Dimension(1200, 570));
		for(int i = 0; i < folderPath.length; i++) {
			//System.out.println(folderPath[i]); //폴더 패스 뜸
			productPageAr[i] = new ProductPage(i, this, folderPath[i], member);
			productPageAr[i].setPreferredSize(new Dimension(580, 570));
			p_product_center.add(productPageAr[i]);
		}
		
		p_product.add(p_product_center);
		p_product.setPreferredSize(new Dimension(1200, 570));
		p_product.setBackground(Color.yellow);
		
		//가운데 중 우측 장바구니 정보----------------------------------------
		p_product_east = new JPanel();
		p_product_east.setLayout(new BorderLayout());
		p_product_east.setPreferredSize(new Dimension(450, 570));
		p_product_east.setBackground(Color.yellow);
		//데이터가 입력될 때 마다 테이블을 업그레이드 해야함
		p_basket_top = new JPanel();
		p_basket_top.setPreferredSize(new Dimension(450, 35));
		bt_basket_del = new JButton("장바구니 비우기");
		bt_basket_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int data = JOptionPane.showConfirmDialog(ProductMain.this, "장바구니를 비우시겠습니까?");
				if(data == JOptionPane.OK_OPTION) {					
					basketDel();
				}
			}
		});
		p_basket_top.add(bt_basket_del);
		p_product_east.add(p_basket_top, BorderLayout.NORTH);
		
		//테이블 형태 컬럼 : 상품명, 가격, 버튼
		p_basket_list = new JPanel();
		p_basket_list.setPreferredSize(new Dimension(450, 300));

		//테이블이 업데이트 되면서 lb의 정보도 업데이트 되야 하므로 미리 생성해야 한다.
		lb_totalPrice = new JLabel("총 금액 올 곳");

		//장바구니 테이블을 위한 모델 클래스를 만둘고 적용시킴
		table_basket.setModel(productModel = new ProductModel(this));
		table_basket.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				String product_name = (String) table_basket.getValueAt(table_basket.getSelectedRow(), 0);
				int result = JOptionPane.showConfirmDialog(ProductMain.this, product_name+"을 장바구니에서 제외하시겠습니까?");
				if(result == JOptionPane.OK_OPTION) {					
					productDel();
				}
			}
		});
		scroll_basket = new JScrollPane(table_basket);
		
		p_basket_list.add(scroll_basket);
		p_product_east.add(p_basket_list);
		p_basket_total = new JPanel();
		p_basket_total.setPreferredSize(new Dimension(450, 90));
		lb_totalPrice.setFont(new Font("맑은 고딕", Font.BOLD, 28));
		lb_totalPrice.setPreferredSize(new Dimension(250, 30));
		bt_pay = new JButton("Pay");
		bt_pay.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				if(totalPrice - 10000 >= 0) {
					pay();					
				}else {
					JOptionPane.showMessageDialog(ProductMain.this, "죄송합니다 최소 금액은 10,000원 입니다.\n"+(10000-totalPrice)+"원 추가 구매가 필요합니다.");
				}
			}
		});
		
		//장바구니 총 정보, 결제 버튼 생성
		p_basket_total.add(lb_totalPrice);
		p_basket_total.add(bt_pay);
		p_product_east.add(p_basket_total, BorderLayout.SOUTH);
		p_product.add(p_product_east, BorderLayout.EAST);
		p_center.add(p_product);
		add(p_center);
		
		//주문 정보 화면=============================
		p_orderList.setVisible(false);
		p_orderList.setBackground(Color.CYAN);;		
		p_orderList.setPreferredSize(new Dimension(800, 530));
		p_orderList.setLayout(new BorderLayout());
		//주문목록 테이블	
		ordModel = new OrderModel(this);
		table_ord.setModel(ordModel);
		table_ord.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				//상태가 주문 완료인 제품들만 리뷰 작성 가능
				if(table_ord.getValueAt(table_ord.getSelectedRow(), 5).equals("배달 완료") && table_ord.getValueAt(table_ord.getSelectedRow(), 6).equals("리뷰 작성하기")) {	
					int result = JOptionPane.showConfirmDialog(ProductMain.this, "리뷰를 작성하시겠습니까?");
					if(result == JOptionPane.OK_OPTION) {
						createReview();
					}
				}
			}
		});
		scroll_ord = new JScrollPane(table_ord);
		p_orderList.add(scroll_ord);
		
		p_center.add(p_orderList);
		//채팅 관련 화면=============================
		p_chat.setPreferredSize(new Dimension(1200, 530));
			
		p_chatBox = new JPanel();
		p_chat_top = new JPanel();
		lb_chat_top = new JLabel("욕설 및 폭언을 삼가해주세요.");
		area_chat = new JTextArea();
		getChatList();
		scroll_chat = new JScrollPane(area_chat);
		p_send = new JPanel();
		t_chat = new JTextField();
		bt_chat = new JButton("전송");
		
		area_chat.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		p_chatBox.setLayout(new BorderLayout());
		p_chatBox.setBackground(Color.yellow);
		p_chatBox.setPreferredSize(new Dimension(500, 530));
		p_chat_top.setPreferredSize(new Dimension(500,  50));
		lb_chat_top.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		t_chat.setPreferredSize(new Dimension(430, 30));
		p_send.setPreferredSize(new Dimension(500,  50));
		
		bt_chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		
		t_chat.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMsg();
				}
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					logOnOff("off");
					clientSocket.setFlag(false);
					getAppMain().getCon().close();
					System.exit(0);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		p_chat_top.add(lb_chat_top);
		p_send.add(t_chat);
		p_send.add(bt_chat);
		p_chatBox.add(p_chat_top, BorderLayout.NORTH);
		p_chatBox.add(scroll_chat);
		p_chatBox.add(p_send, BorderLayout.SOUTH);
		
		p_chat.add(p_chatBox);
		p_center.add(p_chat);
		
		//리뷰 관련============================================
		p_review.setPreferredSize(new Dimension(1200, 530));
		p_notice = new NoticePanel(this);
		p_review.add(p_notice);
		
		
		p_center.add(p_review);
		//배너 관련================================
		p_banner = new JPanel() {
			public void paint(Graphics g) {
				printBanner(g);
			}
		};
		bannerThread = new Thread() {
			public void run() {
				bannerThread();
			}
		};
		bannerThread.start();
		//배너 크기 설정
		p_banner.setPreferredSize(new Dimension(1200, 200));
		add(p_banner, BorderLayout.SOUTH);
		
		
		//보여주기
		setBounds(100, 100, 1200, 820);
		setVisible(true);
	}
	
	//대화용 쓰레드 소켓 생성 및 가동
	public void runClient() {
		socket = ClientSocket.getSocket(this);
		if(socket == null) {
			System.exit(0);
		}
		clientSocket = ClientSocket.getClientSocket();
		Runnable runnable = clientSocket;
		clientThread = new Thread(runnable);
		clientThread.start();
	}
	
	//멤버의 로그인 상태를 바꿔줄 메서드
	public void logOnOff(String onoff) {
		String sql  = "update member set onoff='"+onoff+"' where member_id="+member.getMember_id();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = getAppMain().getCon().prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if(result < 1) {
				System.out.println("고객 "+onoff+"으로 상태 변경 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAppMain().release(pstmt);
		}
	}

	//하단 배너에 이미지 출력하기
	public void printBanner(Graphics g) {
		Image image = new ImageManager().getScaledImage(bannerPath+imgName[bannerIndex], 1200, 200);
		g.drawImage(image, 0, 0, p_banner);
	}
	
	//내가 누른 카테고리의 페이지만 보여주고 나머지는 숨기기
	public void hideProduct(ActionEvent e) {
		for(int i = 0; i < categoryAr.length; i++) {
			if(e.getSource() == categoryAr[i]) {
				productPageAr[i].setVisible(true);
			}else {
				productPageAr[i].setVisible(false);
			}
		}
	}
	
	//내가 누른 메뉴의 페이지만 보여주고 나머지는 숨기기
	public void hideMenu(ActionEvent e) {
		for(int i = 0; i < mainPannelAr.length; i++) {
			if(e.getSource() == naviAr[i]) {
				naviAr[i].setBackground(Color.LIGHT_GRAY);
				mainPannelAr[i].setVisible(true);
			}else {
				naviAr[i].setBackground(Color.WHITE);
				mainPannelAr[i].setVisible(false);
			}
		}
	}
	
	//장바구니 목록 전체 삭제(지금 접속한 사용자만)
	public void basketDel() {
		String sql = "delete from basket where member_id="+member.getMember_id();
		PreparedStatement pstmt = null;
		try {
			pstmt = getAppMain().getCon().prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if(result < 1) {
				JOptionPane.showMessageDialog(this, "장바구니 비우기 실패");
			}
			productModel.getList();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAppMain().release(pstmt);
		}
	}
	
	//상품 하나 삭제
	public void productDel() {
		int product_id = productModel.basketList.get(table_basket.getSelectedRow()).getProduct_id();
		String sql = "delete from basket where product_id="+product_id;
		PreparedStatement pstmt = null;
		try {
			pstmt = getAppMain().getCon().prepareCall(sql);
			int result = pstmt.executeUpdate();
			if(result > 1) {
				JOptionPane.showMessageDialog(this, "제품 하나 삭제 실패");
			}else {
				productModel.getList();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAppMain().release(null);
		}
	}
	
	public ProductModel getProductModel() {
		return productModel;
	}
	
	//장바구니목록의 총 정보를 집계 내는 메서드=======================
	public void basketTotal(int totalPrice) {
		this.totalPrice = totalPrice;
		lb_totalPrice.setText("총 금액 : "+totalPrice);
	}
	//==========================================
	
	//결제 메서드
	public void pay() {
		new PayPage(this, member);
	}
	
	//멤버 게터
	public Member getMember() {
		return member;
	}
	
	//채팅창 게터
	public JTextArea getChatArea() {
		return area_chat;
	}
	
	//주문 조회 테이블 업데이트
	public void updateOrdTable() {
		//테이블 updateUI시에는 널포인트 에러발생
//		ordModel.getOrder();
//		table_ord.updateUI();
		
		//모델 객체를 하나 새로 생성 후 적용하는 방법
		table_ord.setModel(ordModel = new OrderModel(this));
	}
	
	//채팅내용 전송하기
	public void sendMsg() {
		//대화 내용을 데이터 베이스에 넣고,
		String jsonData = JsonManager.getMsgJson(member.getUser_id()+" : "+t_chat.getText(), member);
		clientSocket.send(jsonData);
		t_chat.setText("");
	}
	
	public void getChatList() {
		area_chat.setText("");
		String sql = "select msg from chat where member_id="+member.getMember_id();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = getAppMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			StringBuffer sb = new StringBuffer();
			while(rs.next()) {
				String msg = rs.getString("msg");
				sb.append(msg+"\n");
			}
			area_chat.setText(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAppMain().release(pstmt, rs);
		}
	}
	
	public ClientSocket getClientSocket() {
		return clientSocket;
	}
	
	//테이블 선택 시 리뷰 창 띄우기
	public void createReview() {
		new RegistReview(Integer.parseInt((String)table_ord.getValueAt(table_ord.getSelectedRow(), 0)) , this);
	}
	
	//업데이트하기
	public void updateReview() {
		p_review.remove(p_notice);
		p_notice = new NoticePanel(this);
		p_review.add(p_notice);
	}
	
	//배너에 쓰레드 적용
	public void bannerThread() {
		while(true) {
			try {
				Thread.sleep(40);
				bannerCnt++;
				if(bannerCnt > 100) {
					bannerCnt = 0; 
					bannerIndex++;
					
					if(bannerIndex == imgName.length) bannerIndex = 0;
					p_banner.repaint();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}