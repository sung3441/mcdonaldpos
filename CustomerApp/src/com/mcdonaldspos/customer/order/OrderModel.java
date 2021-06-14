package com.mcdonaldspos.customer.order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.mcdonaldspos.customer.product.ProductMain;

public class OrderModel extends AbstractTableModel{
	Vector<Ord> data;
	Vector<String> columns = new Vector<String>();
	ProductMain productMain;
	
	public OrderModel(ProductMain productMain) {
		this.productMain = productMain;
		
		columns.add("주문 번호");
		columns.add("rider");
		columns.add("가격");
		columns.add("메모");
		columns.add("주문 일시");
		columns.add("상태");
		columns.add("리뷰");
		getOrder();
	}
	
	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return columns.size();
	}

	public Object getValueAt(int row, int col) {
		String value = null;
		Ord ord = data.get(row);
		switch(col) {
			case 0:
				value = Integer.toString(ord.getOrd_id());
				break;
			case 1:
				value = Integer.toString(ord.getRider_id());
				break;
			case 2:
				value = Integer.toString(ord.getPrice());
				break;
			case 3:
				value = ord.getMemo();
				break;
			case 4:
				value = ord.getRegdate();
				break;
			case 5:
				value = ord.getInfo();
				break;
			case 6:
				value = ord.getReview();
				break;
		}
		return value;
	}

	public String getColumnName(int col) {
		return columns.get(col);
	}
	
	public void getOrder() {
		String sql = "select ord_id, rider_id, price, memo, regdate, info, review from ord where member_id="+productMain.getMember().getMember_id();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		data = new Vector<Ord>();
		
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Ord ord = new Ord();
				ord.setOrd_id(rs.getInt("ord_id"));
				ord.setRider_id(rs.getInt("rider_id"));
				ord.setPrice(rs.getInt("price"));
				ord.setMemo(rs.getString("memo"));
				ord.setRegdate(rs.getString("regdate"));
				ord.setInfo(rs.getString("info"));
				ord.setReview(rs.getString("review"));
				
				data.add(ord);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}












