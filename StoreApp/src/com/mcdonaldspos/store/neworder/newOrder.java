package com.mcdonaldspos.store.neworder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.mcdonaldspos.store.main.Ord;
import com.mcdonaldspos.store.main.StoreMain;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class newOrder extends JFrame{
	JPanel p_north;
	JLabel label;
	JPanel p_center;
	JTable table;
	JPanel p_south;
	JButton bt_ok;
	JButton bt_cancel;
	JScrollPane scroll;
	
	String[] columnName= {"ord_id","member_id","rider_id","price","menu","memo","regdate","info"};
	String [][] rows= {};
	Ord ord;
	StoreMain storeMain;
	int ord_id;
	Calendar calendar;
	
	FileOutputStream fos;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//mp3파일
	Player player;
	FileInputStream fis;
	boolean flag=true;
	
	public newOrder(StoreMain main,int ord_id) {
		this.storeMain = main;
		this.ord_id=ord_id;
		getList();
		p_north=new JPanel();
		p_center=new JPanel();
		p_south=new JPanel();
		label=new JLabel("새로운 주문!");
		table=new JTable(new AbstractTableModel() {
			
			public Object getValueAt(int row, int col) {
				return rows[row][col];
			}
			
			public int getRowCount() {
				return rows.length;
			}
			
			public String getColumnName(int col) {
				return columnName[col];
			}
			
			@Override
			public int getColumnCount() {
				return columnName.length;
			}
		});
		scroll=new JScrollPane(table);
		bt_ok=new JButton("확인");
		bt_cancel=new JButton("취소");
		
		p_north.setPreferredSize(new Dimension(150,100));
		label.setFont(new Font("휴먼엑스포", Font.BOLD, 30));
		p_center.setPreferredSize(new Dimension(400, 400));
		p_south.setPreferredSize(new Dimension(150, 100));
		
		p_north.add(label);
		p_center.add(scroll);
		p_south.add(bt_ok);
		p_south.add(bt_cancel);
		
		add(p_north,BorderLayout.NORTH);
		add(p_center,BorderLayout.CENTER);
		add(p_south,BorderLayout.SOUTH);
		
		setVisible(true);
		setBounds(500, 350, 500, 650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Thread thread = new Thread() {
			public void run() {
				mp3();
			};
		};
		thread.start();
		
		
		bt_ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ord테이블에 주문 추가하고 창닫기
				dispose();
				//mp3같이 꺼지게
				player.close();
				//asdasfasfafasf
				storeMain.getOrdList();			
			}
		});
		
		bt_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//주문 취소 컨펌메세지 띄우고 확인 - 창닫기, 취소- 컨펌창만 닫기
				int result=JOptionPane.showConfirmDialog(newOrder.this, "주문 취소 하시겠습니까?");
				if(result==JOptionPane.YES_OPTION) {
					delete();
					player.close();
					sendCancelMsg(); //고객에게 취소 메세지 보내기
				}
			}
		});
	}
	
	
	

	
	//음악 재생
	public void mp3() {
		try {
			fis=new FileInputStream("D:\\workspace\\korea_javaworkspace\\StoreApp\\res\\주문접수.mp3");
			player=new Player(fis);
			if(flag) {
				player.play();	
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(fis != null	){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void getList() {
		String sql="select * from ord where ord_id="+ord_id;
		
		try {
			System.out.println("스토어 메인"+storeMain);
			System.out.println("겟 스토어 메인"+storeMain.getStoreMain());
			pstmt = storeMain.getStoreMain().getCon().prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.last();
			int total=rs.getRow();
			
			String[][] data=new String[total][columnName.length];
			rs.beforeFirst();
			
			int index=0;
			while(rs.next()) {
				data[index][0]=Integer.toString(rs.getInt("ord_id"));
				data[index][1]=Integer.toString(rs.getInt("member_id"));
				data[index][2]=Integer.toString(rs.getInt("rider_id"));
				data[index][3]=Integer.toString(rs.getInt("price"));
				data[index][4]=rs.getString("menu");
				data[index][5]=rs.getString("memo");
				data[index][6]=rs.getString("regdate");
				data[index][7]=rs.getString("info");
				index++;
			}
			rows=data;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			storeMain.release(pstmt, rs);
		}
	}
	
	public void delete() {
		String sql="delete from ord where ord_id="+ord_id;
		try {
			pstmt=storeMain.getCon().prepareStatement(sql);
			int result=pstmt.executeUpdate();
			if(result<1) {
				System.out.println("주문취소 실패");
			}else {
				dispose();
				table.updateUI();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			storeMain.release(pstmt);
		}
	}
	
	//취소 메세지 보내기
	public void sendCancelMsg() {
		StringBuffer sb=new StringBuffer();
		sb.append("{");
		sb.append("\"cmd\" : \"cancel\",");
		sb.append("\"ord_id\" : \""+ord_id+"\"");
		sb.append("}");
		storeMain.getClientMainThread().send(sb.toString());
	}
}
