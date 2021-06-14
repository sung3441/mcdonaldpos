package com.mcdonaldspos.store.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class OrdModel extends AbstractTableModel{
	Vector<Vector> records=new Vector<Vector>();
	Vector<String> column=new Vector<String>();
	StoreMain storeMain;
	Ord ord;
	String key;
	
	public OrdModel(StoreMain storeMain, String key) {
		this.storeMain = storeMain;
		this.key = key;
		ord = new Ord();
		// TODO Auto-generated constructor stub
		column.add("주문번호");
		column.add("고객명");
		column.add("라이더");
		column.add("가격");
		column.add("메뉴");
		column.add("메모");
		column.add("주문일시");
		column.add("상태");
		ordList();
	}
	
	public int getRowCount() {
		return records.size();
	}

	public int getColumnCount() {
		return column.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		Object value=null;
		Vector<Object> ord=records.get(row);
		if(col==0) {
			value = ord.get(col);
		}else if(col==1) {
			value = ord.get(col);
		}else if(col==2) {
			value = ord.get(col);
		}else if(col==3) {
			value = ord.get(col);
		}else if(col==4) {
			value = ord.get(col);
		}else if(col==5) {
			value = ord.get(col);
		}else if(col==6) {
			value = ord.get(col);
		}else if(col==7) {
			value = ord.get(col);
		}
		return value;
	}

	public String getColumnName(int col) {
		return column.get(col);
	}
	public void ordList() {
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSetMetaData rsmd = null;
		String sql;
		if(key.equals("완료")) {
			sql="select o.ord_id,m.user_id,o.rider_id,o.price,o.menu,o.memo,o.regdate,o.info";
			sql += " from ord o inner join member m on o.member_id=m.member_id where info='배달 완료'";
		}else {
			sql="select o.ord_id,m.user_id,o.rider_id,o.price,o.menu,o.memo,o.regdate,o.info";
			sql += " from ord o inner join member m on o.member_id=m.member_id where info like '%배차%' or info='배달 중'";
		}
		try {
			pstmt=storeMain.con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rsmd=rs.getMetaData();

			records=new Vector<Vector>();
			
			while(rs.next()) {
				Vector<Object> data=new Vector<Object>();
				
				ord.setOrd_id(rs.getInt("ord_id"));
				ord.setUser_id(rs.getString("user_id"));
				ord.setRider_id(rs.getInt("rider_id"));
				ord.setPrice(rs.getInt("price"));
				ord.setMenu(rs.getString("menu"));
				ord.setMemo(rs.getString("memo"));
				ord.setRegdate(rs.getString("regdate"));
				ord.setInfo(rs.getString("info"));
				
				data.add(ord.getOrd_id());
				data.add(ord.getUser_id());
				data.add(Integer.toString(rs.getInt("rider_id")));
				data.add(Integer.toString(rs.getInt("price"))) ;
				data.add(rs.getString("menu")) ;
				data.add(rs.getString("memo")) ;
				data.add(rs.getString("regdate"));
				data.add(rs.getString("info"));
				
				records.add(data);
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			storeMain.release(pstmt, rs);
		}
	}
}
