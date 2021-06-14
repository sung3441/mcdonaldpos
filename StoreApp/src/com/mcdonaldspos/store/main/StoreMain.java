package com.mcdonaldspos.store.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import com.mcdonaldspos.store.chat.ChatModel;
import com.mcdonaldspos.store.client.ClientMainThread;
import com.mcdonaldspos.store.client.MainClient;

import util.ImageManager;

public class StoreMain extends JFrame{
	String[] menu= {"주문 조회","완료 조회", "고객 건의"};
	StoreButton[] bt_menu=new StoreButton[menu.length];
	JPanel p_north;
	Canvas can;
	JPanel p_center;
	JPanel p_ex; //공백 처리용
	//채팅관련 패널======================================================
	JPanel p_chat;
	JTextArea area_chat;
	JTextField t_chat;
	JScrollPane scroll_chat;
	JTable table_chat; //고객 명단을 확인할 테이블
	ChatModel chatModel;
	JScrollPane scroll_chatTable;
	int member_id = -1; //지금 대화 중인 대상의 테이블 번호
	int memberIndex = -1; //내가 고른 대화상대의 테이블 번호
	
	Toolkit kit=java.awt.Toolkit.getDefaultToolkit();
	String path="mcdonalds.png";
	Image img=new ImageManager().getScaledImage(path, 100, 50);
	
	
	JTable table;
	JScrollPane scroll;
	String[] columns= {"ord_id","member_id","rider_id","price","menu","memo","regdate","info"};
	Vector<Object> records;
	//Object[][] records= {};
	OrdModel ordModel;
	Ord ord;
	
	ClientMainThread mainThread;
	MainClient mainClient;
	Socket socket;

	Connection con;
	String driver="com.mysql.jdbc.Driver";
	String url="jdbc:mysql://localhost:3306/mcdonaldspos?characterEncoding=UTF-8";
	String user="root";
	String password="1234";
	
	public StoreMain() {
		connect();
		//records = new Vector<Object>();
		mainClient = new MainClient(this);
		mainThread = mainClient.getClientMainThread();
		//생성
		p_north=new JPanel();
		p_ex=new JPanel();
		can=new Canvas() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, can);
			}
		};
		p_north.add(can);
		for(int i=0; i<menu.length;i++) {
			bt_menu[i]=new StoreButton(menu[i]);
			p_north.add(bt_menu[i]);
		}
		
		p_north.add(p_ex);
		p_center=new JPanel();
		table=new JTable();
		scroll=new JScrollPane(table);
		
		//채팅 관련 패널 생성
		p_chat = new JPanel();
		area_chat = new JTextArea();
		scroll_chat = new JScrollPane(area_chat);
		t_chat = new JTextField();
		table_chat = new JTable();
		scroll_chatTable = new JScrollPane(table_chat);
		chatModel = new ChatModel(getStoreMain());
		
		//스타일
		area_chat.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		p_north.setPreferredSize(new Dimension(200,70));
		can.setPreferredSize(new Dimension(200,60));
		//can.setBackground(Color.yellow);
		p_ex.setPreferredSize(new Dimension(200, 60));
		scroll.setPreferredSize(new Dimension(1000,600));
		
		//채팅 관련 패널 스타일
		p_chat.setPreferredSize(new Dimension(800, 650));
		p_chat.setLayout(new BorderLayout());
		t_chat.setPreferredSize(new Dimension(800, 60));
		scroll_chatTable.setPreferredSize(new Dimension(200, 600));
		
		//조립
		p_center.add(scroll);
		add(p_north,BorderLayout.NORTH);
		add(p_center);
		ordModel = new OrdModel(StoreMain.this, "중");
		table.setModel(ordModel);
		
		//채팅패널 조립
		p_chat.add(scroll_chat);
		p_chat.add(t_chat, BorderLayout.SOUTH);
		p_chat.add(scroll_chatTable, BorderLayout.EAST);
		p_chat.setVisible(false);
		p_center.add(p_chat);
		table_chat.setModel(chatModel);
		
		//메세지 전송하기
		t_chat.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(member_id != -1 && isOnOff(member_id)) {
						sendChat();
						t_chat.setText("");
					}else {
						t_chat.setText("");
						JOptionPane.showMessageDialog(StoreMain.this, "["+table_chat.getValueAt(table_chat.getSelectedRow(), 1)+"] 고객님은 현재 접속 중이 아닙니다.");
					}
				}
			}
		});
		
		//리스너연결
		table_chat.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				getChatList(); //채팅 내용 불러오기
			}
		});
		
		//아직 배차가 되지 않은 주문의 목록 보기
		bt_menu[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				getOrdList();
			}
		});
		
		//배달이 완료된 주문의 목록 보기
		bt_menu[1].addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {	
				ordModel = new OrdModel(StoreMain.this, "완료");
				table.setModel(ordModel);	
				p_chat.setVisible(false);
				scroll.setVisible(true);
			}
		});
		
		bt_menu[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatModel.memberList();
				table_chat.updateUI();
				p_chat.setVisible(true);
				scroll.setVisible(false);
			}
		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(table.getValueAt(table.getSelectedRow(), 7).equals("배차 대기")) {
					int result=JOptionPane.showConfirmDialog(StoreMain.this, "배차 하시겠습니까?");
					if(result==JOptionPane.YES_OPTION) {
						//해당 member_id의 info를 배차중으로 업데이트
						infoUpdate();
						sendState();
					}	
				}
			}
		});
		
		//윈도우 창 닫히면
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainThread.setFlag(false);
				System.exit(0);
			}
		});
		
		//보여주기
		setVisible(true);
		setBounds(500,  100,  1100, 800);
		this.setTitle("매장");
		
	}
	
	//대화를 하려는 고객이 현재 접속 중인지를 판단
	public boolean isOnOff(int member_id) {
		boolean b = true;
		String sql  = "select onoff from member where member_id="+member_id;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String onoff = rs.getString("onoff");
				if(onoff.equals("off")) {
					b = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	
	//배차 중으로 정보 업데이트
	public void infoUpdate() {
		int ord_id=(int)table.getValueAt(table.getSelectedRow(), 0);
		
		PreparedStatement pstmt=null;
		String sql="update ord set info='배차 중' where ord_id="+ord_id;
		
		try {
			pstmt=con.prepareStatement(sql);
			int result=pstmt.executeUpdate();
			if(result<1) {
				System.out.println("배차중 업데이트 실패");
			}else {
				ordModel.ordList();		
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendChat() {
		int member_id = Integer.parseInt((String)table_chat.getValueAt(table_chat.getSelectedRow(), 0));
		System.out.println("메세지 보낼 때 멤버 아이디"+member_id);
		StringBuffer sb=new StringBuffer();
		sb.append("{");
		sb.append("\"cmd\" : \"chat\",");
		sb.append("\"msg\" : \""+"맥도날드 : "+t_chat.getText()+"\",");
		sb.append("\"member_id\" : \""+member_id+"\"");
		sb.append("}");
		mainThread.send(sb.toString());
	}
	
	//라이더 페이지에 배차를 요청!
	public void sendState() {
		StringBuffer sb=new StringBuffer();
		Object ord_id = table.getValueAt(table.getSelectedRow(), 0) ;
		sb.append("{");
		sb.append("\"cmd\" : \"state\",");
		sb.append("\"state\" : \"배차 중\"");
		sb.append("\"ord_id\" : \""+ord_id+"\"");
		sb.append("}");
		mainThread.send(sb.toString());
	}
	//=-------------조회 목록 desc 처리하기
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//드라이버 로드
			con=DriverManager.getConnection(url,user,password);
			if(con!=null) {
				this.setTitle("접속 성공");
			}else {
				this.setTitle("접속 실패");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//주문목록 조회
	public void getOrdList() {
		table.setModel(ordModel = new OrdModel(StoreMain.this, "배달"));
		p_chat.setVisible(false);
		scroll.setVisible(true);
	}
	
	//채팅 내용을 불러와서 area에 붙여 넣기
	public void getChatList() {
		area_chat.setText("");
		memberIndex = table_chat.getSelectedRow();
		member_id = Integer.parseInt((String)table_chat.getValueAt(memberIndex, 0));
		String sql = "select msg from chat where member_id="+member_id;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			StringBuffer sb = new StringBuffer();
			while(rs.next()) {
				String msg = rs.getString("msg");
				sb.append(msg+"\n");
			}
			area_chat.setText(sb.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			release(pstmt, rs);
		}
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
	public Connection getCon() {
		return con;
	}
	
	public StoreMain getStoreMain() {
		return this;
	}
	
	public JTextArea getChatTextArea() {
		return area_chat;
	}
	
	public int getMember_id() {
		return member_id;
	}
	
	public int getMemberIndex() {
		return memberIndex;
	}
	
	public JTable getTable_chat() {
		return table_chat;
	}
	
	public ClientMainThread getClientMainThread() {
		return mainThread;
	}
	
	public static void main(String[] args) {
		new StoreMain();
	}
}

