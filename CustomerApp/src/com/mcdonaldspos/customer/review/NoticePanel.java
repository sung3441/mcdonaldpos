package com.mcdonaldspos.customer.review;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mcdonaldspos.customer.product.ProductMain;

import util.CustomButton;

public class NoticePanel extends JPanel{
	ProductMain productMain;
	int totalRecord; //총 게시물 수
	int pageSize = 6; //한 화면에 보일 게시물의 수
	int currentPage = 0; //현재 내가 보고 있는 페이지의 번호
	int blockSize = 5; //한 화면에 보일 블럭의 사이즈
	Image image;
	Vector<JPanel> panelAr = new Vector<JPanel>();
	Vector<JPanel> pageAr = new Vector<JPanel>();
	Vector<Review> reviewAr  = new Vector<Review>();
	Vector<CustomButton> buttonAr = new Vector<CustomButton>();
	String imgPath;
	
	public NoticePanel(ProductMain productMain) {
		this.productMain = productMain;
		JLabel lb_img = new JLabel("사진");
		JLabel lb_writer = new JLabel("작성자");
		JLabel lb_title = new JLabel("제목");
		JLabel lb_content = new JLabel("평가");
		JLabel lb_score = new JLabel("평점");
		
		JPanel p_img = new JPanel();
		JPanel p_writer = new JPanel();
		JPanel p_title = new JPanel();
		JPanel p_content = new JPanel();
		JPanel p_score = new JPanel();

		p_img.add(lb_img);
		p_writer.add(lb_writer);
		p_title.add(lb_title);
		p_content.add(lb_content);
		p_score.add(lb_score);
		
		lb_img.setPreferredSize(new Dimension(80, 25));
		lb_writer.setPreferredSize(new Dimension(100, 25));
		lb_title.setPreferredSize(new Dimension(180, 25));
		lb_content.setPreferredSize(new Dimension(620, 25));
		lb_score.setPreferredSize(new Dimension(100, 25));
		
		p_img.setBackground(Color.white);
		p_writer.setBackground(Color.white);
		p_title.setBackground(Color.white);
		p_content.setBackground(Color.white);
		p_score.setBackground(Color.white);
		
		Font font = new Font("맑은 고딕", Font.BOLD, 20);
		lb_img.setFont(font);
		lb_writer.setFont(font);
		lb_title.setFont(font);
		lb_content.setFont(font);
		lb_score.setFont(font);
		
		add(p_img);
		add(p_writer);
		add(p_title);
		add(p_content);
		add(p_score);
		
		getReview();
		while(currentPage < (totalRecord/pageSize)+1) {
			JPanel p = new JPanel();
			p.setPreferredSize(new Dimension(1200, 430));
			for(int i = currentPage*pageSize; i < pageSize*(currentPage+1); i++) {
				if(i < totalRecord) {
					if(currentPage == 0) {
						p.setVisible(true);
					}else {
						p.setVisible(false);
					}
					p.add(panelAr.get(i));
				}
			}
			pageAr.add(p);
			currentPage++;
		}
		
		for(int i = 0; i < pageAr.size(); i++) {
			add(pageAr.get(i));
		}
		
		for(int i = 0; i < (totalRecord/pageSize)+1; i++) {
			CustomButton bt = new CustomButton((i+1)+"");
			bt.setId(i);
			bt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showHide(e);
				}
			});
			buttonAr.add(bt);
			add(bt);
		}
		setPreferredSize(new Dimension(1200, 520));
	}
	
	//현재 번호에 따라 보여지는 페이지 다르게 하기
	public void getReview() {
		panelAr = new Vector<JPanel>();
		String sql  = "select r.review_id, m.user_id, r.filename, r.title, r.content, r.score from review r inner join member m on r.member_id=m.member_id order by review_id desc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.last();
			totalRecord = rs.getRow(); //총 레코드 갯수
			System.out.println(totalRecord);
			rs.beforeFirst();
			while(rs.next()) {
				System.out.println("게시물 넣기");
				JPanel panel = new JPanel();
				panel.setPreferredSize(new Dimension(1180, 65));
				
				Review review = new Review();
				review.setFilename(rs.getString("filename"));
				review.setUser_id(rs.getString("user_id"));
				review.setReview_id(rs.getInt("review_id"));
				review.setScore(rs.getInt("score"));
				review.setTitle(rs.getString("title"));
				review.setContent(rs.getString("content"));
				
				//==========이미지 출력 안됨===================================
				//=======페이징 처리하기
				
				ImagePanel p_image = new ImagePanel(review.getFilename(), rs.getRow());
				JLabel lb_writer = new JLabel(review.getUser_id());
				JLabel lb_title = new JLabel(review.getTitle());
				JLabel lb_content = new JLabel(review.getContent());
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < 5; i++) {
					if(review.getScore() >= i) {
						sb.append("★");
					}else {
						sb.append("☆");
					}
				}
				JLabel lb_score = new JLabel(sb.toString());
				
				p_image.setPreferredSize(new Dimension(80, 65));
				p_image.setBackground(Color.yellow);
				lb_writer.setPreferredSize(new Dimension(100, 65));
				lb_title.setPreferredSize(new Dimension(180, 65));
				lb_content.setPreferredSize(new Dimension(620, 65));
				lb_score.setPreferredSize(new Dimension(100, 65));
				
				Font font = new Font("맑은 고딕", Font.BOLD, 20);
				lb_writer.setFont(font);
				lb_title.setFont(font);
				lb_content.setFont(font);
				lb_score.setFont(font);
				
				panel.add(p_image);
				panel.add(lb_writer);
				panel.add(lb_title);
				panel.add(lb_content);
				panel.add(lb_score);
				
				
				panelAr.add(panel);
				reviewAr.add(review);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			productMain.getAppMain().release(pstmt, rs);
		}
	}
	
	public void showHide(ActionEvent e) {
		System.out.println(buttonAr.size()+"버튼의 길이");
		System.out.println(pageAr.size()+"페이지의 길이");
		
		for(int i = 0; i < buttonAr.size(); i++) {
			if(e.getSource() == buttonAr.get(i)) {
				System.out.println(i+"내가 선택한 인덱스");
				pageAr.get(i).setVisible(true);
			}else {
				pageAr.get(i).setVisible(false);
			}
		}
	}
}












