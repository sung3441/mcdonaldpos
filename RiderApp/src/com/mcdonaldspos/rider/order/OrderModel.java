package com.mcdonaldspos.rider.order;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

public class OrderModel extends AbstractTableModel{
	Vector<Order> data=new Vector<Order>();
	Vector<String> column=new Vector<String>();
	
	
	public OrderModel() {
		column.add("주문 번호");
		column.add("고객 이름");
		column.add("가격");
		column.add("주소");
		column.add("담당 라이더");
		column.add("배달 상태");		
	}
	
	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return column.size();
	}

	public String getColumnName(int col) {
		return column.get(col);
	}
	
	public Object getValueAt(int row, int col) {
		
		String value=null;
		Order order=data.get(row);
		if(col==0) {
			value=Integer.toString(order.getOrd_id());
		}else if(col==1) {
			value=order.getUser_name();
		}else if(col==2) {
			value=Integer.toString(order.getPrice());
		}else if(col==3) {
			value=order.getAddr();
		}else if(col==4) {
			if(order.getRider_name() != null) {
				value = order.getRider_name();
			}else {
				value = "라이더 선택";
			}
		}else if(col==5) {
			value = order.getInfo();
		}
		return value;
	}
}


