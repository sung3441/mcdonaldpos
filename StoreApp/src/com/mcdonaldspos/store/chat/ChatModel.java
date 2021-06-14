package com.mcdonaldspos.store.chat;

import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import com.mcdonaldspos.store.main.Ord;
import com.mcdonaldspos.store.main.StoreMain;

public class ChatModel extends AbstractTableModel implements TableCellRenderer{
	Vector<ChatVo> records;
	Vector<String> column = new Vector<String>();
	StoreMain storeMain;
	ChatVo chatVo; //채팅에 사용할 고객정보 및 아이디  

	
	public ChatModel(StoreMain storeMain) {
		this.storeMain = storeMain;
		chatVo = new ChatVo();
		column.add("고객 번호");
		column.add("고객 ID");
		column.add("상태");
		memberList();
	}
	
	public int getRowCount() {
		return records.size();
	}

	public int getColumnCount() {
		return column.size();
	}

	public Object getValueAt(int row, int col) {
		String value=null;
		ChatVo chatVo = records.get(row);
		if(col == 0) {
			value = Integer.toString(chatVo.getMember_id());
		}else if(col == 1) {
			value = chatVo.getUser_id();
		}else if(col == 2) {
			value = chatVo.getOnoff();
		}
		return value;
	}
	
	public String getColumnName(int col) {
		return column.get(col);
	}
	
	public void memberList() {
		records = new Vector<ChatVo>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSetMetaData rsmd = null;
		
		String sql="select member_id, user_id, onoff from member order by onoff desc";
		try {
			pstmt=storeMain.getCon().prepareStatement(sql);
			rs=pstmt.executeQuery();
			rsmd=rs.getMetaData();

			records=new Vector<ChatVo>();
			
			while(rs.next()) {
				chatVo = new ChatVo();
				chatVo.setMember_id(rs.getInt("member_id"));
				chatVo.setUser_id(rs.getString("user_id"));
				chatVo.setOnoff(rs.getString("onoff"));
				
				records.add(chatVo);
			}
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			storeMain.release(pstmt, rs);
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}
}
