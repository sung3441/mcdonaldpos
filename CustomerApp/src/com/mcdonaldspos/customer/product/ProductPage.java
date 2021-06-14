package com.mcdonaldspos.customer.product;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mcdonaldspos.customer.login.Member;
import com.mcdonaldspos.main.AppMain;

import util.CustomButton;
import util.ImageManager;

public class ProductPage extends JPanel{
	int category_id;
	Member member;
	ProductMain productMain;
	String folderName;
	
	JPanel p_search;
	JLabel lb_category;
	JPanel p_center;
	JScrollPane scroll;
	JPanel[] productAr;
	
	//상품을 보여주게 될 페이지
	public ProductPage(int category_id, ProductMain productMain, String folderName, Member member) {
		//System.out.println(category_id);
		//반복문에서 넘어오는 값은 0~7이지만 실제
		//데이터베이스에 담겨진 category_id는 1~8이다 그러므로 +1
		this.category_id = category_id+1; 
		this.productMain = productMain;
		this.folderName = folderName; //각 카테고리마다 폴더명을 넘겨받음
		this.member = member;
		setLayout(new BorderLayout());
		
		//System.out.println(folderName);
		createTop();
		
		p_center = new JPanel();
		p_center.setLayout(new FlowLayout());
		p_center.setPreferredSize(new Dimension(550, 1200));
		scroll = new JScrollPane(p_center);
		createProduct();
		for(int i = 0; i < productAr.length; i++) {
			p_center.add(productAr[i]);
		}
		add(p_search, BorderLayout.NORTH);
		add(scroll);
	}
	
	//알레르기 정보를 선택할 수 있는 상단 판넬 제작
	public void createTop() {
		p_search = new JPanel();
		p_search.setPreferredSize(new Dimension(600, 35));
		lb_category = new JLabel(folderName.substring(0, folderName.length()-1));
		lb_category.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		p_search.add(lb_category);
	}
	
	//내가 지정한 카테고리의 상품 하나 하나를 생성, 판넬에 붙이기
	public void createProduct() {
		String sql = "select product_id, product_name, price, kcal, filename from product where category_id=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = productMain.getAppMain().getCon().prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setInt(1, category_id);
			rs = pstmt.executeQuery();
			rs.last();
			int last = rs.getRow();
			productAr = new JPanel[last];
			rs.beforeFirst();
			int data = 0;
			while(rs.next()) {
				int product_id = rs.getInt("product_id");
				String product_name = rs.getString("product_name");
				int price = rs.getInt("price");
				int kcal = rs.getInt("kcal");
				String filename = rs.getString("filename");
				
				JPanel p = new JPanel();
				p.setPreferredSize(new Dimension(130, 200));
				p.setLayout(new FlowLayout());
				CustomButton bt_image = new CustomButton("") {
					public void paint(Graphics g) {
						createBtImage(g , this);
					}
				};
				bt_image.setId(product_id);
				bt_image.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						insertProduct(bt_image.getId());
					}
				});
				bt_image.setPreferredSize(new Dimension(120, 100));
				p.add(bt_image);
				JLabel lb_name = new JLabel(product_name);
				JLabel lb_price = new JLabel(price+"원");
				JLabel lb_kcal = new JLabel(kcal+"kcal");
				lb_name.setPreferredSize(new Dimension(120,  15));
				lb_price.setPreferredSize(new Dimension(120,  15));
				lb_kcal.setPreferredSize(new Dimension(120,  15));
				p.add(lb_name);
				p.add(lb_price);
				p.add(lb_kcal);
				productAr[data] = p;
				data++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			productMain.getAppMain().release(pstmt, rs);
		}
	}
	
	//버튼에 그림 그리기
	public void createBtImage(Graphics g, CustomButton bt) {
		String sql = "select filename from product where product_id=?";
		int product_id = bt.getId();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, product_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String filename = rs.getString("filename");
				//System.out.println("images/"+folderName+filename);
				Image image = new ImageManager().getScaledImage("images/"+folderName+filename, 130, 100);
				g.drawImage(image, 0, 0, bt);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			productMain.getAppMain().release(pstmt, rs);
		}
	}
	
	//내가 조회한 제품 정보 장바구니에 넣기
	public void insertProduct(int product_id) {
		String sql = "insert into basket(member_id, product_id) values(?, ?)";
		PreparedStatement pstmt = null;
		
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, member.getMember_id());
			pstmt.setInt(2, product_id);
			
			int data = pstmt.executeUpdate();
			if(data > 1) {
				JOptionPane.showMessageDialog(this, "장바구니 추가 실패");
			}else {
				JOptionPane.showMessageDialog(this, "장바구니에 추가 완료");
				productMain.productModel.getList();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			productMain.getAppMain().release(pstmt);
		}
	}

	
//	//내가 선택한 제품 조회
//	public void selectProduct(int product_id) {
//		String sql = "select product_name, price, kcal, filename from product where product_id=?";
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int id = product_id;
//		
//		try {
//			pstmt = appMain.getCon().prepareCall(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			String product_name = rs.getString("product_name");
//			int price = rs.getInt("price");
//			int kcal = rs.getInt("kcal");
//			String filename = rs.getString("filename");	
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally {
//			appMain.release(pstmt, rs);
//		}
//	}
}
















