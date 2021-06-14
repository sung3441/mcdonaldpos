package com.mcdonaldspos.rider.search;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class SearchModel extends AbstractTableModel{
	Vector<Search> data=new Vector<Search>();
	Vector<String> column=new Vector<String>();

	
	public SearchModel() {
		column.add("No");
		column.add("이름");
		column.add("총 배달");
		column.add("배달 현황");
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
		Search search=data.get(row);
		
		if(col==0) {
			value=Integer.toString(search.getRider_id());
		}else if(col==1) {
			value=search.getRider_name();
		}else if(col==2) {
			value=Integer.toString(search.getHit());
		}else if(col==3) {
			value=Integer.toString(search.getHave());
		}
		
		return value;
	}
	
}














