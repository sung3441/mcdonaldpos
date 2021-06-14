package com.mcdonaldspos.customer.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.mcdonaldspos.customer.product.ProductMain;
import com.mcdonaldspos.main.AppMain;

import util.ImageManager;
import util.Page;

public class Login extends Page{
	JPanel p_center;
	JPanel p_banner; //광고를 부착할 배너 쓰레드 적용하기
	JPanel p_login; // 로그인 텍스트 필드를 담는 패널
	JLabel lb_id;
	JTextField t_id;
	JLabel lb_password;
	JPasswordField t_password;
	JButton bt_login; //로그인 버튼
	JButton bt_signup; //회원 가입 버튼
	JPanel p_south;
	
	Member member;
	//배너 이미지를 위한 효과
	Thread bannerThread; //배너를 변경할 쓰레드
	JProgressBar bannerBar; //배너 변경 시간을 보여줄 바
	int bannerCnt = 0;
	int bannerIndex = 0;
	String path = "images/banner/login/";
	String[] imgName = {"맥모닝.jpg", "빅맥.PNG", "맥윙.PNG", "bts.PNG"};
	
	//폰트 설정
	Font font = new Font("맑은 고딕", Font.BOLD , 40);
	
	//이미지 주소
	
	//이미지, 아이콘 처리를 위한 클래스
	ImageManager imageManager = new ImageManager();
	
	public Login(AppMain appMain) {
		super(appMain); //부모한테 메인 객체 넘겨주기
		//생성
		p_center = new JPanel();
		p_login = new JPanel();
		lb_id = new JLabel("ID");
		t_id = new JTextField();
		lb_password = new JLabel("PW");
		t_password = new JPasswordField();
		bt_login = new JButton("login");
		bt_signup = new JButton("signup");
		p_south = new JPanel();
		
		//배너에 그림 걸기
		p_banner = new JPanel() {
			public void paint(Graphics g) {
				Toolkit kit = getToolkit();
				Image image =	imageManager.getScaledImage(path+imgName[bannerIndex], 900, 500);
				g.drawImage(image, 0, 0, 900, 500, this);
			}
		};
		
		bannerBar = new JProgressBar();
		bannerThread = new Thread() {
			public void run() {
				bannerThread();
			}
		};
		bannerThread.start();
		
		//스타일
		p_center.setPreferredSize(new Dimension(1000, 750));
		p_banner.setPreferredSize(new Dimension(900, 500));
		bannerBar.setPreferredSize(new Dimension(900, 20));
		p_login.setPreferredSize(new Dimension(950, 150));
		p_south.setPreferredSize(new Dimension(1000, 80));
		
		p_center.setLayout(new FlowLayout());
		p_login.setLayout(new FlowLayout());
		p_south.setLayout(new GridLayout(1, 2));
		p_banner.setLayout(new BorderLayout());
		
		lb_id.setPreferredSize(new Dimension(200, 60));
		t_id.setPreferredSize(new Dimension(550, 60));
		lb_password.setPreferredSize(new Dimension(200, 60));
		t_password.setPreferredSize(new Dimension(550, 60));
		
		//폰트 적용
		bt_login.setFont(font);
		bt_signup.setFont(font);
		lb_id.setFont(font);
		t_id.setFont(font);
		lb_password.setFont(font);
		t_password.setFont(font);
		
		
		//조립
		p_center.add(p_banner);
		p_center.add(bannerBar);
		p_login.add(lb_id);
		p_login.add(t_id);
		p_login.add(lb_password);
		p_login.add(t_password);
		p_center.add(p_login);
		add(p_center);
		
		p_south.add(bt_login);
		p_south.add(bt_signup);
		add(p_south, BorderLayout.SOUTH);
		
		//리스너
		//로그인 버튼 클릭 시
		bt_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginCheck();
			}
		});
		
		//회원가입 버튼 클릭 시
		bt_signup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Regist(getAppMain());
			}
		});
		
		setBounds(100,  100,  1000, 800);
		setVisible(true);
	}
	
	//로그인 버튼 클릭 시 호출되는 로그인 메서드
	public void loginCheck() {
		//패스워드 값 가져오기
		char[] pass = t_password.getPassword();
		String password = "";
		for(char p : pass) {
			password += Character.toString(p);
		}
		//유효성 검사
		if(t_id.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "아이디를 입력하세요!");
		}else if(password.equals("")) {
			JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요!");
		}else {
			//로그인 메서드 만들기
			login();
		}
	}
	
	//로그인 하기
	public void login() {
		String sql = "select * from member where user_id=? and user_pass=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = this.getAppMain().getCon().prepareStatement(sql);
			pstmt.setString(1, t_id.getText());
			pstmt.setString(2, new String(t_password.getPassword()));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				//로그인 성공이므로 메뉴 페이지로 이동한다.
				member = new Member();
				member.setMember_id(rs.getInt("member_id"));
				member.setUser_id(rs.getString("user_id"));
				member.setUser_pass(rs.getString("user_pass"));
				member.setName(rs.getString("user_name"));
				member.setEmail(rs.getString("email"));
				member.setAddr(rs.getString("addr"));
				JOptionPane.showMessageDialog(this, member.getUser_id()+"님 로그인 완료");
				ProductMain productMain = new ProductMain(getAppMain(), member);
				this.dispose();
			}else {
				JOptionPane.showMessageDialog(this, "회원 정보가 다르거나, 존재하지 않는 아이디 입니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//배너에 쓰레드 적용
	public void bannerThread() {
		while(true) {
			try {
				Thread.sleep(40);
				bannerCnt++;
				bannerBar.setValue(bannerCnt);
				if(bannerCnt > 100) {
					bannerCnt = 0; 
					bannerIndex++;
					
					if(bannerIndex == imgName.length) bannerIndex = 0;
					p_banner.repaint();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}






















