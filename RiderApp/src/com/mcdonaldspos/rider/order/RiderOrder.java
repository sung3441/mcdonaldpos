package com.mcdonaldspos.rider.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.mcdonaldspos.rider.main.RiderMain;
import com.mcdonaldspos.rider.main.RiderPage;

public class RiderOrder extends RiderPage{

	//센터
	String[] columns=new String[] {"No", "고객명", "가격", "주소", "라이더"};
	Object[][] datas=new Object[][] {};
	
	JTable table;
	JScrollPane scroll;
	JComboBox<String> riderList;
	OrderModel model;
	
	public RiderOrder(RiderMain riderMain) {
		super(riderMain);
		
		table=new JTable(); //표 만들기
		scroll=new JScrollPane(table); //스트롤 틀에 테이블 넣기
		
		scroll.setPreferredSize(new Dimension(1150, 400));
		
		add(scroll, BorderLayout.NORTH); //스크롤 넣기
		
		//셀 넓이
//		table.getColumn("No").setPreferredWidth(100);
//		table.getColumn("고객명").setPreferredWidth(100);
//		table.getColumn("가격").setPreferredWidth(100);
//		table.getColumn("주소").setPreferredWidth(650);
//		table.getColumn("라이더").setPreferredWidth(100);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//테이블의 라이더 선택란이 '라이더 선택' 일 때만 새 창 띄우기
				if(table.getValueAt(table.getSelectedRow(), 4).equals("라이더 선택") || table.getValueAt(table.getSelectedRow(), 5).equals("배차 중")) {
					new who(riderMain, table.getSelectedRow(), model);
				}else if(table.getValueAt(table.getSelectedRow(), 5).equals("배달 중")) {
					int data = JOptionPane.showConfirmDialog(RiderOrder.this, "해당 주문의 배달이 완료됐습니까?");
					if(data == JOptionPane.OK_OPTION) {
						completeOrder(); //주문 최종 완료 처리
						sendMsg("배달 완료"); //서버로 메세지 보내기
						getOrderList();
					}
				}
			}
		});
		
//		//콤보박스
//		riderList=new JComboBox<String>();
//		riderList.addItem("조사장");
//		riderList.addItem("이지현");
//		riderList.addItem("오상화");
//		riderList.addItem("서상철");
		
		//테이블에서 하나의 column 관리자 얻어오기
		//라이더를 콤보박스로 전환
//		TableColumn column=table.getColumnModel().getColumn(4);
//		column.setCellEditor(new DefaultCellEditor(riderList));
		
	}
	
	//서버로 메세지 보내기
	public void sendMsg(String msg) {
		StringBuffer sb=new StringBuffer();
		String ord_id = (String)table.getValueAt(table.getSelectedRow(), 0);
		System.out.println("오더 주문번호"+ord_id);
		sb.append("{");
		sb.append("\"cmd\" : \"state\",");
		sb.append("\"state\" : \""+msg+"\",");
		sb.append("\"ord_id\" : \""+ord_id+"\"");
		sb.append("}");
		getRiderMain().getRiderSocket().send(sb.toString());
	}
	//배달을 최종 완료 처리하는 곳
	//1)서버로 전송
	//2)라이더 건수 올리기
	//3)고객영역 테이블 업데이트
	//4)스토어 영역 테이블 업데이트
	
	//주문 완료 처리하기
	public void completeOrder() {
		//내가 선택한 행 고르기
		int ord_id = Integer.parseInt((String)table.getValueAt(table.getSelectedRow(), 0));
		String sql  = "update ord set info='배달 완료' where ord_id="+ord_id;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = getRiderMain().getCon().prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if(result < 1) {
				System.out.println("배달 완료 처리 실패");
			}else {
				updateRiderHit(ord_id); //라이더 건수 올리기
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			release(pstmt, null);
		}
	}
	
	//라이더 건수 올리기
	public void updateRiderHit(int ord_id) {
		String sql = "update rider set hit=hit+1 where rider_id=(select rider_id from ord where ord_id=?)";
		PreparedStatement pstmt = null;
		
		try {
			pstmt = getRiderMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, ord_id);
			int result = pstmt.executeUpdate();
			if(result < 1) {
				System.out.println("배달 건수 올리기 실패");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			release(pstmt, null);
		}
	}

	
	//라이더 조회하기
	public void getOrderList() {
		String sql="select ord_id, user_name, price, addr, r.rider_name, o.info";
		sql+=" from ord o inner join member m on o.member_id=m.member_id";
		sql+=" inner join rider r on o.rider_id=r.rider_id where o.info='배차 중' or o.info='배달 중'";
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSetMetaData meta;
		model=new OrderModel();
		
		try {
			pstmt=getRiderMain().getCon().prepareStatement(sql);
			rs=pstmt.executeQuery();
			meta=rs.getMetaData();
			
//			int col_count=meta.getColumnCount();
//			System.out.println("컬럼 길이  :"+col_count);
//			for(int i =1;i<=col_count;i++) {
//				String name=meta.getColumnName(i);
//				//System.out.println("컬럼명 은"+name);
//				model.column.add(name);
//			}
			while(rs.next()) {
				Order order=new Order();
				order.setOrd_id(rs.getInt("ord_id"));
				order.setUser_name(rs.getString("user_name"));
				order.setPrice(rs.getInt("price"));
				order.setAddr(rs.getString("addr"));
				order.setRider_name(rs.getString("rider_name"));
				order.setInfo(rs.getString("info"));
				model.data.add(order);
			}
			table.setModel(model);		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			release(pstmt, rs);
		}
		
	}
	
	public void release(PreparedStatement pstmt, ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
		
class who extends JFrame{

	JButton btn;
	JComboBox<String> riderList;
	JPanel panel;
	JPanel panel2;
	RiderMain riderMain;
	OrderModel model;
	int tableRow;
	
	who(RiderMain riderMain, int tableRow, OrderModel model){
		this.riderMain=riderMain;
		this.tableRow=tableRow;
		this.model=model;
		
		btn = new JButton("배달 수락");
		riderList=new JComboBox<String>();
		panel=new JPanel();
		panel2=new JPanel();
		riderList.setPreferredSize(new Dimension(200, 25));
		panel.setPreferredSize(new Dimension(280, 45));
		panel2.setPreferredSize(new Dimension(280, 45));
		
		//콤보박스
		riderList.addItem("조성일");
		riderList.addItem("이지현");
		riderList.addItem("오상화");
		riderList.addItem("서상철");
		
		setTitle("담당 라이더");
		setLayout(new FlowLayout());
		
		add(panel);
		panel.add(riderList); //콤보박스 들어갈 곳
		add(panel2);
		panel2.add(btn); //버튼 이벤트
		
		//주문 접수 버튼
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//내가 선택한 라이더 이름 구해오기
				String rider_name=(String)riderList.getSelectedItem();
				int deliveryNum = getDelivery(rider_name);
				if(deliveryNum < 3) { //구해오기
					Order order=model.data.get(tableRow);
					order.setRider_name(rider_name);
					int ord_id = order.getOrd_id();
					//데이터 베이스에 라이더 값 넣기
					updateInfo(rider_name, ord_id, "배달 중");
					sendMsg("배달 중", ord_id);
					who.this.model = new OrderModel();
					who.this.riderMain.getRiderOrder().getOrderList();
					who.this.dispose();
				}else {
					JOptionPane.showMessageDialog(riderMain, "해당 라이더는 이미 "+deliveryNum+" 개의 배달을 진행 중입니다.\n안전을 위해 다른 라이더를 배차 해주세요.");
				}
			}
		});
			
		setBounds(200, 200, 300, 130);
		setVisible(true);
	}
	
	//해당 라이더가 진행 중인 배달의 유무 파악
	public int getDelivery(String rider_name) {
		String sql = "select count(*) as '수량', r.rider_name from ord o, rider r where o.rider_id=r.rider_id and r.rider_name='"+rider_name+"' and o.info='배달 중' group by o.rider_id;";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = -1;
		try {
			pstmt = riderMain.getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("수량");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			release(pstmt, rs);
		}
		return result;
	}
	
	//서버로 메세지 보내기
	public void sendMsg(String msg, int ord_id) {
		StringBuffer sb=new StringBuffer();
		sb.append("{");
		sb.append("\"cmd\" : \"state\",");
		sb.append("\"state\" : \""+msg+"\"");
		sb.append("\"ord_id\" : \""+ord_id+"\"");
		sb.append("}");
		who.this.riderMain.getRiderSocket().send(sb.toString());
	}
	
	//라이더 선택 후 데이터 베이스에 값 넣기
	public void updateInfo(String rider_name, int ord_id, String info) {
		String sql="update ord set rider_id = (select rider_id from rider";
		sql+=" where rider_name='"+rider_name+"'), info='"+info+"' where ord_id="+ord_id;
		
		PreparedStatement pstmt=null;
		try {
			pstmt=riderMain.getCon().prepareStatement(sql);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			release(pstmt);
		}
	}
	
	public void release(PreparedStatement pstmt, ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void release(PreparedStatement pstmt) {
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}









