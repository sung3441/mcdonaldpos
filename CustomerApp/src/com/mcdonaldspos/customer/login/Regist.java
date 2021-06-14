package com.mcdonaldspos.customer.login;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


import com.mcdonaldspos.main.AppMain;

import util.MailManager;
import util.Page;
import util.RandomManager;

//회원 가입 클래스
public class Regist extends Page{
	//아이디
	//패스워드
	//패스워드 확인
	//닉네임 || 이름
	//이메일 인증 버튼 추가
	//인증버튼 //랜덤으로 6자리 코드 생성 후 메일로 전송
	MailManager mailManager;
	String key = "여기는인증키";
	JPanel p_west;
	JPanel p_east;
	JPanel p_south;
	JPanel p_idCheck;
	JPanel p_pw;
	JPanel p_pw2;
	JPanel p_email;
	JPanel p_lb_id;
	JPanel p_lb_pw;
	JPanel p_lb_pw2;
	JPanel p_lb_name;
	JPanel p_radio;
	JPanel p_randomCheck;
	JPanel p_addr;
	
	JLabel lb_id;
	JTextField t_id;
	JButton bt_id;
	
	JLabel lb_id_msg;
	
	JLabel lb_pw;	
	JPasswordField t_pw;
	JLabel lb_pw_msg;
	
	JLabel lb_pw2;
	JPasswordField t_pw2;
	JLabel lb_pw2_msg;
	
	JLabel lb_name;
	JTextField t_name;
	
	JLabel lb_email;
	JTextField t_email;
	JLabel lb_emailImg;
	Choice ch_site;
	JButton bt_send;
	
	JTextField t_random;
	JButton bt_check;
	JLabel lb_email_msg;
	
	JButton bt_regist;
	
	JRadioButton radio1;
	JRadioButton radio2;
	ButtonGroup group;
	
	JLabel lb_addr;
	JTextField t_addr;
	JTextField t_addr2;
	
	//회원가입 유효성 체크
	boolean isIdCheck = false;
	boolean isPwCheck = false;
	boolean isPwCheck2 = false;
	boolean isKeyCheck = false;
	
	public Regist(AppMain appMain) {
		super(appMain);
		p_west = new JPanel();
		p_east = new JPanel();
		p_south = new JPanel();
		p_idCheck = new JPanel();
		p_pw = new JPanel();
		p_pw2 = new JPanel();
		p_email = new JPanel();
		p_radio = new JPanel()	;
		
		p_lb_id = new JPanel();
		p_lb_pw = new JPanel();
		p_lb_pw2 = new JPanel();
		p_lb_name = new JPanel();
		p_randomCheck = new JPanel();
		p_addr = new JPanel();
		
		
		lb_id = new JLabel("ID");
		t_id = new JTextField();
		bt_id = new JButton("중복검사");
		
		lb_id_msg = new JLabel("아이디 중복 검사 필수!");
		
		lb_pw = new JLabel("pass");
		t_pw = new JPasswordField();
		lb_pw_msg = new JLabel("보안 매우 취약");
		
		lb_pw2 = new JLabel("pass재확인");
		t_pw2 = new JPasswordField();
		lb_pw2_msg = new JLabel("비밀번호 재입력");
		
		lb_name = new JLabel("이름");
		t_name = new JTextField();
		
		lb_email = new JLabel("E-mail");
		t_email = new JTextField();
		lb_emailImg = new JLabel("@");
		ch_site = new Choice();
		bt_send = new JButton("인증하기");
		
		t_random = new JTextField();
		bt_check = new JButton("인증");
		lb_email_msg = new JLabel("이메일 인증을 진행해주세요.");
		
		bt_regist = new JButton("회원가입");
		
		radio1 = new JRadioButton("개인정보 이용 동의");
		radio2 = new JRadioButton("개인정보 이용 거부");
		group = new ButtonGroup();
		
		lb_addr = new JLabel("주소");
		t_addr = new JTextField();
		t_addr2 = new JTextField();
		
		
		Font font = new Font("맑은 고딕", Font.BOLD, 20);
		
		p_west.setPreferredSize(new Dimension(300, 200));
		p_west.setLayout(new GridLayout(4, 2));
		
		p_east.setPreferredSize(new Dimension(400, 200));
		p_east.setLayout(new GridLayout(4, 1));
		
		p_south.setPreferredSize(new Dimension(700, 350));
		
		p_idCheck.setPreferredSize(new Dimension(400, 50));
		
		p_pw.setPreferredSize(new Dimension(400, 50));
		p_pw2.setPreferredSize(new Dimension(400, 50));
	
		p_email.setPreferredSize(new Dimension(600, 50));
		
		lb_email.setPreferredSize(new Dimension(50, 50));
		t_email.setPreferredSize(new Dimension(200, 25));
		ch_site.setPreferredSize(new Dimension(180, 50));
		
		p_lb_id.setPreferredSize(new Dimension(150, 50));
		p_lb_pw.setPreferredSize(new Dimension(150, 50));
		p_lb_pw2.setPreferredSize(new Dimension(150, 50));
		p_lb_name.setPreferredSize(new Dimension(150, 50));
		
		t_random.setPreferredSize(new Dimension(100, 25));
		p_randomCheck.setPreferredSize(new Dimension(600, 50));
		p_radio.setPreferredSize(new Dimension(600, 50));
				
		p_addr.setPreferredSize(new Dimension(600, 50));
		lb_addr.setPreferredSize(new Dimension(50, 50));
		t_addr.setPreferredSize(new Dimension(210, 25));
		t_addr2.setPreferredSize(new Dimension(280, 25));
		
		
		p_lb_id.add(lb_id);
		p_lb_pw.add(lb_pw);
		p_lb_pw2.add(lb_pw2);
		p_lb_name.add(lb_name);

		p_west.add(p_lb_id);
		p_west.add(t_id);
		p_west.add(p_lb_pw);
		p_west.add(t_pw);
		p_west.add(p_lb_pw2);
		p_west.add(t_pw2);
		p_west.add(p_lb_name);
		p_west.add(t_name);
		
		p_idCheck.add(bt_id);
		p_idCheck.add(lb_id_msg);
		
		p_pw.add(lb_pw_msg);
		p_pw2.add(lb_pw2_msg);
		
		p_east.add(p_idCheck);
		p_east.add(p_pw);
		p_east.add(p_pw2);
				
		p_email.add(lb_email);
		p_email.add(t_email);
		p_email.add(lb_emailImg);
		ch_site.add("naver.com");
		ch_site.add("gmail.com");
		ch_site.add("nate.com");
		ch_site.add("daum.net");
		
		p_email.add(ch_site);
		p_email.add(bt_send);
		
		p_randomCheck.add(t_random);
		p_randomCheck.add(bt_check);
		p_randomCheck.add(lb_email_msg);
		
		radio2.setSelected(true);
		p_radio.add(radio1);
		p_radio.add(radio2);
		
		group.add(radio1);
		group.add(radio2);
		
		p_addr.add(lb_addr);
		p_addr.add(t_addr);
		p_addr.add(t_addr2);
		
		p_south.add(p_addr);
		p_south.add(p_email);
		p_south.add(p_randomCheck);
		p_south.add(p_radio);
		p_south.add(bt_regist);
		
		//아이디 중복 검사
		bt_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idCheck();
			}
		});
		
		//아이디입력 창에 변화 있을 때마다 아이디 중복검사 해제
		t_id.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				isIdCheck = false;
				lb_id_msg.setText("아이디 중복 검사 필수!!");
				lb_id_msg.updateUI();
			}
		});
		
		//비밀번호 입력 시
		t_pw.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				pwCheck();
				pwCheck2();
			}
		});
		
		//비밀번호 재확인 입력 시
		t_pw2.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				pwCheck2();
			}
		});
		
		bt_regist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//회원가입 전송 하기 전에 공백은 없는지 유효성 체크
				registCheck();
			}
		});
		
		bt_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendKey(); //인증번호를 메일로 보내기
			}
		});
		
		bt_check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkKey();//인증키와 입력한 값을 비교
			}
		});
		

		add(p_west, BorderLayout.WEST);
		add(p_east, BorderLayout.EAST);
		add(p_south, BorderLayout.SOUTH);
		
		setVisible(true);
		setBounds(10, 10, 700, 550);
		setTitle("회원가입 창");
	}
	
	public void idCheck() {
		String sql = "select user_id from member where user_id=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = Regist.this.getAppMain().getCon().prepareCall(sql);
			pstmt.setString(1, t_id.getText());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				JOptionPane.showMessageDialog(this.getAppMain(), "사용할 수 없는 ID입니다.");
				isIdCheck = false;
				lb_id_msg.setText("사용 불가");
				lb_id_msg.updateUI();
			}else {
				JOptionPane.showMessageDialog(this.getAppMain(), "사용할 수 있는 ID입니다.");
				isIdCheck = true;
				lb_id_msg.setText("중복검사 완료");
				lb_id_msg.updateUI();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.getAppMain().release(pstmt, rs);
		}
	}
	
	//비밀번호 유효성 검사
	public void pwCheck()	{
		for(char c : t_pw.getPassword()) {
			if(Character.toString(c).equals(" ")) {
				JOptionPane.showMessageDialog(this, "공백을 두지 마세요.");
				t_pw.setText("");
				t_pw.updateUI();
				isPwCheck = false;
				lb_pw_msg.setText("보안 매우 취약");
				lb_pw_msg.updateUI();
				return;
			}
		}
		int len = t_pw.getPassword().length;
		if(len < 5) {
			isPwCheck = false;
			lb_pw_msg.setText("보안 매우 취약");
			lb_pw_msg.updateUI();
		}else if(len >= 5 && len <= 8){
			isPwCheck = true;
			lb_pw_msg.setText("적당한 보안");
			lb_pw_msg.updateUI();
		}else {
			isPwCheck = true;
			lb_pw_msg.setText("강력한 보안 수준");
			lb_pw_msg.updateUI();
		}
	}
	
	//비밀번호 재확인 유효성 검사
	public void pwCheck2() {
		String pw1 = new String(t_pw.getPassword());
		String pw2 = new String(t_pw2.getPassword());
		if(pw1.equals(pw2)) {
			isPwCheck2 = true;
			lb_pw2_msg.setText("일치");
			lb_pw2_msg.updateUI();
		}else {
			isPwCheck2 = false;
			lb_pw2_msg.setText("불일치");
			lb_pw2_msg.updateUI();
		}
	}
	
	//회원가입 전 유효성 체크
	public void registCheck() {
		if(t_id.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "아이디를 입력하세요.");
		}else if(!isIdCheck) {
			JOptionPane.showMessageDialog(this, "아이디 중복 검사를 진행하세요.");
		}else if(t_pw.getPassword().length == 0) {
			JOptionPane.showMessageDialog(this, "패스워드를 입력하세요.");
		}else if(!isPwCheck) {
			JOptionPane.showMessageDialog(this, "패스워드가 보안에 취약합니다.");
		}else if(!isPwCheck2) {
			JOptionPane.showMessageDialog(this, "재입력 패스워드가 일치하지 않습니다.");
		}else if(t_name.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "별명을 입력하세요.");			
		}else if(t_addr.getText().equals("") || t_addr2.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "주소를 입력하세요.");
		}else if(t_email.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "email을 입력하세요.");
		}else if(radio1.isSelected() == false) {
			JOptionPane.showMessageDialog(this, "개인정보 이용을 동의해주세요!");
		}else if(!isKeyCheck){
			JOptionPane.showMessageDialog(this, "이메일 인증을 진행해주세요.");
		}else {
			//이제 회원가입 가능
			//이메일 통한 인증번호 추가하기!!!!!
			regist();
		}
	}
	
	//회원가입 전송
	public void regist() {
		PreparedStatement pstmt = null;
		String sql = "insert into member(user_id, user_pass, user_name, email, addr) values(?, ?, ?, ?, ?)";
		try {
			pstmt = this.getAppMain().getCon().prepareStatement(sql);
			pstmt.setString(1, t_id.getText());
			pstmt.setString(2, new String(t_pw.getPassword()));
			pstmt.setString(3, t_name.getText());
			pstmt.setString(4, t_email.getText()+"@"+ch_site.getSelectedItem());
			pstmt.setString(5, t_addr.getText()+t_addr2.getText());
			
			int result = pstmt.executeUpdate();
			if(result < 1) {
				JOptionPane.showMessageDialog(this, "회원가입에 실패 했습니다.");
			}else {
				JOptionPane.showMessageDialog(this, "회원가입에 성공 했습니다.");
				this.dispose();
				//회원가입 성공 시 창 종료 하기!!!
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			this.getAppMain().release(pstmt);
		}
	}
	
	//랜덤한 6자리 키 발송
	public void sendKey() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String title = "@@@맥도날드 앱 회원가입 인증 번호@@@";
		key = RandomManager.getRandomNumber(6); //6자리 인증번호 반환 받기
		String email = t_email.getText()+"@"+ch_site.getSelectedItem();
		System.out.println(email);
		String sql = "select email from member where email='"+email+"'";
		
		try {
			pstmt = getAppMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				JOptionPane.showMessageDialog(this, "이미 사용 중인 이메일 계정입니다.");
			}else {
				Thread thread = new Thread() {
					public void run() {
						mailManager.sendMail(email, title, key);
						JOptionPane.showMessageDialog(Regist.this, "인증번호를 전송했습니다.");
					}
				};
				thread.start();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			getAppMain().release(pstmt);
		}
	}
	
	//인증 키와 입력한 값 비교
	public void checkKey() {
		if(key.equals(t_random.getText())) {
			isKeyCheck = true;
			JOptionPane.showMessageDialog(this, "인증 되었습니다.");
			lb_email_msg.setText("인증이 완료됐습니다.");
		}else {
			JOptionPane.showMessageDialog(this, "인증번호가 일치하지 않습니다.");
		}
	}
}



