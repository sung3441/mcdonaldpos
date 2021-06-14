package com.mcdonaldspos.rider.search;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class SearchModel2 extends AbstractTableModel{
	Vector<Search2> data=new Vector<Search2>();
	Vector<String> column=new Vector<String>();

	
	public SearchModel2() {
		column.add("No");
		column.add("가격");
		column.add("배달 주소");
		column.add("배달 시간");
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
		Search2 search2=data.get(row);
		
		if(col==0) {
			value=Integer.toString(search2.getOrd_id());
		}else if(col==1) {
			value=Integer.toString(search2.getPrice());
		}else if(col==2) {
			value=search2.getAddr();
		}else if(col==3) {
			value=search2.getRegdate();
		}else if(col == 4) {
			value = search2.getInfo();
		}
		return value;
	}
	
}














