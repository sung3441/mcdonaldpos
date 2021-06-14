package com.mcdonaldspos.customer.order;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mcdonaldspos.customer.login.Member;
import com.mcdonaldspos.customer.product.ProductMain;

import util.JsonManager;

public class PayPage extends JFrame{
	ProductMain productMain;
	Member member;
	
	JLabel lb_user_id;
	JLabel t_user_id;
	JTextArea area;
	JScrollPane scroll;
	JLabel lb_price;
	JLabel t_price;
	JLabel lb_addr;
	JLabel t_addr;	
	JLabel lb_memo;
	JTextField t_memo;
	JButton bt_order;
	JButton bt_cancle;	

	int totalPrice;
	
	public PayPage(ProductMain productMain, Member member) {
		this.productMain = productMain;
		this.member = member;
		lb_user_id = new JLabel("주문자 ID");
		t_user_id = new JLabel("주문자 ID 올 곳");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		lb_price = new JLabel("가격");
		t_price = new JLabel("총 가격 올 곳");
		lb_addr = new JLabel("주소지");
		t_addr = new JLabel("주소지 올 곳");
		lb_memo = new JLabel("메모");
		t_memo = new JTextField(13);
		bt_order = new JButton("주문하기");
		bt_cancle = new JButton("다음에");
		
		setLayout(new FlowLayout());
		
		Dimension lbSize = new Dimension(50, 30);
		Dimension TextSize = new Dimension(130, 30);		
		
		lb_user_id.setPreferredSize(new Dimension(80, 30));
		lb_price.setPreferredSize(lbSize);
		lb_addr.setPreferredSize(lbSize);
		lb_memo.setPreferredSize(new Dimension(40, 30));
		
		t_user_id.setPreferredSize(TextSize);
		t_price.setPreferredSize(TextSize);
		t_addr.setPreferredSize(TextSize);
		
		scroll.setPreferredSize(new Dimension(200, 200));
		
		add(lb_user_id);
		add(t_user_id);
		add(scroll);
		add(lb_price);
		add(t_price);
		add(lb_addr);
		add(t_addr);
		add(lb_memo);
		add(t_memo);
		add(bt_order);
		add(bt_cancle);
		
		bt_order.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createOrder();
			}
		});
		
		bt_cancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PayPage.this.dispose();
			}
		});
		
		setTitle("주문");
		setBounds(700, 150, 250, 420);
		setVisible(true);
		getBasket();
	}
	
	//장바구니 정보를 조회해서 정보 꺼내오기
	public void getBasket() {
		String sql = "select p.product_name, p.price, count(p.product_id) from basket b";
		sql += " inner join product p on b.product_id=p.product_id group by p.product_id";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			totalPrice = 0;
			while(rs.next()) {
				area.append(rs.getString("product_name")+"\t"+rs.getInt("count(p.product_id)")+"EA\n");
				totalPrice += rs.getInt("price")*rs.getInt("count(p.product_id)");
			}
			t_price.setText(totalPrice+" 원");
			t_user_id.setText(member.getUser_id());
			t_addr.setText(member.getAddr());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	finally {
			productMain.getAppMain().release(pstmt, rs);
		}
	}
	
	//데이터베이스에 주문 정보 넣기
	public void createOrder() {
		String sql = "insert into ord(member_id, price, menu, memo) values(?, ?, ?, ?)";
		PreparedStatement pstmt = null;
		
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, member.getMember_id());
			pstmt.setInt(2, totalPrice);
			pstmt.setString(3, area.getText());
			pstmt.setString(4, t_memo.getText());
			
			int result = pstmt.executeUpdate();
			if(result > 0) {
				sendOrd();
			}else {
				System.out.println("오더 정보 삽입 실패");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			productMain.getAppMain().release(pstmt);
		}
	}
	
	
	//상점으로 주문 정보를 보내줘야함
	public void sendOrd() {
		//주문 정보보내기===========================
		String sql = "select ord_id from ord order by ord_id desc limit 1";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String ord_id = null;
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				ord_id = rs.getString("ord_id");
			}
			
			String msg = JsonManager.getOrderJson(productMain.getMember(), ord_id);	
			System.out.println(msg);
			productMain.getClientSocket().send(msg);
			basketDel();
			productMain.updateOrdTable();
			JOptionPane.showMessageDialog(this, "주문 완료!");
			this.dispose();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
		}
	}
	
	//주문 완료 시 장바구니에 들어있는 상품들 지우기
	public void basketDel() {
		String sql  = "delete from basket where member_id="+member.getMember_id();
		PreparedStatement pstmt = null;
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if(result < 1) {
				System.out.println("장바구니 삭제 실패");
			}else {
				//장바구니 새로고침
				productMain.getProductModel().getList();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}















