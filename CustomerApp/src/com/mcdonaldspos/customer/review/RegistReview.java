package com.mcdonaldspos.customer.review;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mcdonaldspos.customer.product.ProductMain;
import com.mcdonaldspos.main.AppMain;

import util.FileManager;

//리뷰를 등록하자!
public class RegistReview extends JFrame{
	int ord_id;
	Image image;
	JFileChooser chooser = new JFileChooser();
	FileInputStream fis;
	FileOutputStream fos;
	String fileName; //이미지의 파일 네임을 담을 곳
	
	ProductMain productMain;
	JPanel p_north; //이미지 리뷰제목 area가 들어갈 곳
	JPanel p_center; //별점 등록 버튼 들어갈 곳
	
	JPanel p_image; //리뷰 사진 붙일 곳
	JPanel p_write; //리뷰제목, area 붙이기
	JTextField t_title; //리뷰 제목
	JTextArea area; //리뷰 내용
	JScrollPane scroll;
	JPanel p_score; //별점 보여줄 곳
	JLabel[] lb_scoreAr = new JLabel[5]; //별점 출력할 곳
	int[] lbNumberAr = new int[5];
	JButton bt_regist; //등록 버튼
	
	int score; //별점의 갯수
	
	public RegistReview(int ord_id, ProductMain productMain) {
		this.ord_id = ord_id;
		this.productMain = productMain;
		
		p_north = new JPanel();
		p_center = new JPanel();
		p_image = new JPanel() {
			public void paint(Graphics g) {
				if(image == null) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(0, 0, 300, 350);
					g.setColor(Color.YELLOW);
					g.drawString("여기를 눌러 업로드할 이미지를 선택하세요.", 30, 200);	
				}else {
					g.drawImage(image, 0, 0, 300, 350, this);
				}
			}
		};
		p_write = new JPanel();
		t_title = new JTextField();
		area = new JTextArea();
		scroll = new JScrollPane(area);
		p_score = new JPanel();
		
		//별점 조작 이벤트
		for(int i = 0; i < lb_scoreAr.length; i++) {
			JLabel lb = new JLabel("☆");
			lb.setPreferredSize(new Dimension(100, 100));
			lb.setFont(new Font("맑은 고딕", Font.BOLD, 50));
			lb.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					boolean flag = true;
					for(int i = 0; i < lb_scoreAr.length; i++) {
						if(!lb_scoreAr[i].equals(e.getSource()) && flag) {
							lb_scoreAr[i].setText("★");
						}else if(lb_scoreAr[i].equals(e.getSource())) {
							lb_scoreAr[i].setText("★");
							score = i;
							flag = false;
						}else {
							lb_scoreAr[i].setText("☆");
						}
					}
				}
			});
			lb_scoreAr[i] = lb;
		}
		bt_regist = new JButton("등록");
		
		p_north.setPreferredSize(new Dimension(600, 350));
		p_center.setPreferredSize(new Dimension(600, 150));
		p_image.setPreferredSize(new Dimension(300, 350));
		p_write.setPreferredSize(new Dimension(300, 350));
		t_title.setPreferredSize(new Dimension(280, 50));
		scroll.setPreferredSize(new Dimension(280, 300));
		p_score.setPreferredSize(new Dimension(600, 100));
		
		p_north.setLayout(new BorderLayout());
		
		p_write.add(t_title);
		p_write.add(scroll);		
		p_north.add(p_image, BorderLayout.WEST);
		p_north.add(p_write);
		for(int i = 0; i < lb_scoreAr.length; i++) {			
			p_score.add(lb_scoreAr[i]);
		}
		p_center.add(p_score);
		p_center.add(bt_regist);
		
		add(p_north, BorderLayout.NORTH);
		add(p_center);
		
		p_image.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				updateImage();
			}
		});
		
		bt_regist.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});
		
		setTitle("리뷰작성");
		setBounds(250, 250, 600, 550);
		setVisible(true);
	}
	
	//이미지 선택 후 업로드 하기
	public void updateImage() {
		image = selectImage();
		p_image.repaint();
	}
	
	public Image selectImage() {
		int result = chooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			String ext = FileManager.getExtend(file.getAbsolutePath());
			String savePath = "D:\\workspace\\korea_javaworkspace\\CustomerApp\\res\\images\\review\\";
			try {
				fis = new FileInputStream(file);
				fileName =Integer.toString((int)System.currentTimeMillis())+"."+ext;
				fos = new FileOutputStream(savePath+fileName);
				int data = -1;
				while(true) {
					data = fis.read();
					if(data == -1) break;
					fos.write(data);
				}
				System.out.println("이미지 업로드 완료");
				image = Toolkit.getDefaultToolkit().getImage(savePath+fileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return image;
	}
	
	//게시물 등록
	public void regist() {
		PreparedStatement pstmt = null;
		String sql = "insert into review(member_id, ord_id, title, content, filename, score)";
		sql += " values(?, ?, ?, ?, ?, ?)";
		
		try {
			System.out.println("멤버 id : "+productMain.getMember().getMember_id());
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, productMain.getMember().getMember_id());
			pstmt.setInt(2, ord_id);
			pstmt.setString(3, t_title.getText());
			pstmt.setString(4, area.getText());
			pstmt.setString(5, fileName);
			pstmt.setInt(6, score);
			
			int result = pstmt.executeUpdate();
			if(result > 0) {
				setReview();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			productMain.getAppMain().release(pstmt);
		}
	}
	
	//리뷰 작성 완료 시 작성 완료로 변경
	public void setReview(){
		String sql = "update ord set review='리뷰 작성 완료' where ord_id="+ord_id;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = productMain.getAppMain().getCon().prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if(result > 0) {
				JOptionPane.showMessageDialog(this, "리뷰 등록 완료");
				productMain.updateOrdTable();
				productMain.updateReview();
				this.dispose();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {	
			productMain.getAppMain().release(pstmt);
		}
	}
}












