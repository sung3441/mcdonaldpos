package com.mcdonaldspos.rider.search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.mcdonaldspos.rider.main.*;


public class RiderSearch extends RiderPage{
	
	String[] columns=new String[] {"No", "이름", "배달 건수", "상태"};
	Object[][] datas=new Object[][] {

	};
	
	JTable table;
	JScrollPane scroll;
	
	SearchModel model;
	RiderMain riderMain;
	
	//라이더 조회하기
	public RiderSearch(RiderMain riderMain) {
		super(riderMain);
		
		this.riderMain=riderMain;
		table=new JTable();
		scroll=new JScrollPane(table);
		
		scroll.setPreferredSize(new Dimension(1150, 400));
		add(scroll, BorderLayout.NORTH);
		getRiderList();
		
		table.addMouseListener(new MouseAdapter() {	
			public void mouseReleased(MouseEvent e) {
				int row=table.getSelectedRow();
				int rider_id=Integer.parseInt((String)table.getValueAt(row, 0));
				new work(riderMain, rider_id);
			}
		});
	}
	
	//라이더 목록 데이터베이스에서 조회
	public void getRiderList() {
		String sql="select r.rider_id, r.rider_name, r.hit, count(if(o.info='배달 중', o.info, null)) as 'have' from rider r left outer join"
				+ " ord o on r.rider_id=o.rider_id group by r.rider_id order by r.rider_id asc";
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		model = new SearchModel();
		try {
			pstmt=this.getRiderMain().getCon().prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				Search search=new Search();
				search.setRider_id(rs.getInt("rider_id"));
				search.setRider_name(rs.getString("rider_name"));
				search.setHit(rs.getInt("hit"));
				search.setHave(rs.getInt("have"));
				
				model.data.add(search);
			}
			table.setModel(model);	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			release(pstmt, rs);
		}	
	}
	
	public void release(PreparedStatement pstmt, ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

class work extends JFrame{
	//JLabel label;
	JTable table;
	JScrollPane scroll;
	SearchModel2 model;
	RiderMain riderMain;
	int rider_id;
	
	work(RiderMain riderMain, int rider_id){
		
		this.riderMain=riderMain;
		this.rider_id=rider_id;
		//label=new JLabel();
		table=new JTable();
		scroll=new JScrollPane(table);
		
		scroll.setPreferredSize(new Dimension(600, 400));
		setLayout(new FlowLayout());
		
		add(scroll, BorderLayout.SOUTH);
		setSize(800, 450);
		setVisible(true);
		
		deliveryList(rider_id);
	}
	
	//라이더 상세보기
	public void deliveryList(int rider_id) {
		String sql="select o.ord_id, o.price, m.addr, o.regdate, o.info from ord o inner join member m on m.member_id=o.member_id";
		sql+=" inner join rider r on r.rider_id=o.rider_id where r.rider_id="+rider_id;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSetMetaData meta;
		model=new SearchModel2();
		
		try {
			pstmt=riderMain.getCon().prepareStatement(sql);
			//pstmt.setInt(1, ord_);
			rs=pstmt.executeQuery();
			meta=rs.getMetaData();

			while(rs.next()) {
				Search2 search2=new Search2();
				search2.setOrd_id(rs.getInt("ord_id"));
				search2.setPrice(rs.getInt("price"));
				search2.setAddr(rs.getString("addr"));
				search2.setRegdate(rs.getString("regdate"));
				search2.setInfo(rs.getString("info"));
				
				model.data.add(search2);
			}
			table.setModel(model);
			table.updateUI();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			release(pstmt, rs);
		}
	}
	
	public void release(PreparedStatement pstmt, ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}












