package com.mcdonaldspos.customer.product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class ProductModel extends AbstractTableModel{
	Vector<Basket> basketList;
	Vector<String> column;
	ProductMain productMain;
	int member_id;
	Basket basket;
	
	public ProductModel(ProductMain productMain) {
		this.productMain = productMain;
		this.member_id = productMain.getMember().getMember_id();
		getList();
	}
	
	public int getRowCount() {
		return basketList.size();
	}

	public int getColumnCount() {
		//product_id컬럼은 보여주기 위함이 아니라
		//VO에 정보를 심기 위한 용도이므로 size-1을 해서
		//테이블에는 보이지 않게 한다.
		return column.size()-1;
	}

	public Object getValueAt(int row, int col) {
		String data = null;
		Basket basket = basketList.get(row);
		switch(col) {
			case 0:
				data = basket.getProduct_name(); break;
			case 1:
				data = Integer.toString(basket.getPrice()); break;
			case 2:
				data = Integer.toString(basket.getKcal()); break;
			case 3:
				data = Integer.toString(basket.getCount()); break;
		}
		return data;
	}
	
	public String getColumnName(int col) {
		return column.get(col);
	}

	public void getList()	{
		int totalPrice = 0;
		column = new Vector<String>();
		String sql = "select p.product_name as '제품명', sum(p.price) as '가격', sum(p.kcal) as 'kcal', count(b.product_id) as '수량', p.product_id "
				+ " from basket b"
				+ " inner join product p"
				+ " on b.product_id=p.product_id"
				+ " inner join member m"
				+ " on b.member_id=m.member_id"
				+ " where b.member_id = ?"
				+ " group by b.product_id";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			//스크롤을 사용할 수 있게 옵션 주기
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setInt(1, member_id);
			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int colLen = rsmd.getColumnCount(); //조회하는 테이블의 컬럼 수
			for(int i = 1; i <= colLen; i++) {
				column.add(rsmd.getColumnLabel(i));
			}

			basketList = new Vector<Basket>();
			while(rs.next()) {
				basket = new Basket();
				
				basket.setProduct_name(rs.getString(column.get(0)));
				basket.setPrice(rs.getInt(column.get(1)));
				basket.setKcal(rs.getInt(column.get(2)));
				basket.setCount(rs.getInt(column.get(3)));
				basket.setProduct_id(rs.getInt("product_id"));
				
				totalPrice += rs.getInt(column.get(1));
				basketList.add(basket);
			}
			productMain.basketTotal(totalPrice);
			productMain.table_basket.updateUI();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			productMain.getAppMain().release(pstmt, rs);
		}
	}
}
