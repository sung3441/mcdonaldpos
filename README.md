

# mcdonaldpos

## [서상철](https://github.com/Leviadna)
## [조성일](https://github.com/sung3441)
## [오상화](https://github.com/sean-oh-7)
## [](https://github.com/leejihyeon1)

<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121894979-b73e5b80-cd5a-11eb-9901-c397f8d879f0.PNG">
<br>
<br>
<br>
# 개요<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121895969-c70a6f80-cd5b-11eb-88d7-46d27da23154.PNG">
<br>
<br>
<br>
# 초기 구상 목표<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896144-ef926980-cd5b-11eb-81ba-ad797895a3e8.PNG">
<br>
<br>
<br>
# Library<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896256-105abf00-cd5c-11eb-9949-66ee06910148.PNG">
<br>
<br>
<br>
# 디렉터리 구조<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896330-22d4f880-cd5c-11eb-9861-84416771d252.PNG">
<br>
<br>
<br>
# DB 구조<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896832-b3abd400-cd5c-11eb-89d8-cd92aee68acf.PNG">
<br>
<br>
<br>
# 소켓을 이용한 통신<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896981-dd64fb00-cd5c-11eb-825b-c613af3dba8b.PNG">
<br>
<br>
<br>
# 회원가입<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121897130-01c0d780-cd5d-11eb-8450-e8c9669a597e.PNG">
<br>
<br>
<br>
# 아이디 유효성 검사<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121897506-6a0fb900-cd5d-11eb-8af4-abe132541bb7.PNG">
<br>
<br>
<br>
# 비밀번호 유효성 검사<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121897599-827fd380-cd5d-11eb-9bb9-65dd87fd9e1d.PNG">
<br>
<br>
<br>
# 이메일 인증 확인<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121897727-a3e0bf80-cd5d-11eb-81b5-767a1a842301.PNG">
<br>
'''java 

	import java.util.Properties;
	import javax.mail.Authenticator;
	import javax.mail.Message;
	import javax.mail.MessagingException;
	import javax.mail.Session;
	import javax.mail.Transport;
	import javax.mail.internet.AddressException;
	import javax.mail.internet.InternetAddress;

	import javax.mail.internet.MimeMessage;

	public class MailManager {
		public static void sendMail(String receiver, String title, String content) {
			Properties props = new Properties(); //Map 유형 중 하나
			props.put("mail.smtp.host", "smtp.gmail.com"); //smtp 서버 주소 작성
			props.put("mail.smtp.port", 465); //smtp 서버 포트 번호
			props.put("mail.smtp.auth", "true"); //권한 트루
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); //ssl 사용시	
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new javax.mail.PasswordAuthentication("전송 이메일", "앱 비밀번호");
				}
			});		
			MimeMessage message = new MimeMessage(session);
			try {
				message.setFrom(new InternetAddress("전송자 메일 주소"));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
				message.setSubject(title); //메일 제목
				message.setContent(content, "text/html;charset=utf8"); //html을 지원하는 내용일 경우 인코딩 지정
				Transport.send(message);
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

'''
<br>
<br>
<br>
# 로그인<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121898082-f9b56780-cd5d-11eb-82e7-f8b8cbe92248.PNG">
<br>
''' java

	public void login() {
		String sql = "select * from member where user_id=? and user_pass=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = this.getAppMain().getCon().prepareStatement(sql);
			pstmt.setString(1, t_id.getText());
			pstmt.setString(2, new String(t_password.getPassword()));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				//로그인 성공이므로 메뉴 페이지로 이동한다.
				member = new Member();
				member.setMember_id(rs.getInt("member_id"));
				member.setUser_id(rs.getString("user_id"));
				member.setUser_pass(rs.getString("user_pass"));
				member.setName(rs.getString("user_name"));
				member.setEmail(rs.getString("email"));
				member.setAddr(rs.getString("addr"));
				JOptionPane.showMessageDialog(this, member.getUser_id()+"님 로그인 완료");
				ProductMain productMain = new ProductMain(getAppMain(), member);
				this.dispose();
			}else {
				JOptionPane.showMessageDialog(this, "회원 정보가 다르거나, 존재하지 않는 아이디 입니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
'''
<br>
<br>
<br>
# 상품 목록<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121898887-d63eec80-cd5e-11eb-8313-be8775f99299.PNG">
<br>
''' java 주문 정보 보내기 

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

'''
<br>
''' java 주문 요청 받았을 때<br>

	data=buffr.readLine();
		if(data != null) {
			JSONParser parser=new JSONParser();
			JSONObject obj=(JSONObject) parser.parse(data);

			String cmd=(String) obj.get("cmd");
			if(cmd.equals("order")){
				int ord_id=Integer.parseInt((String)obj.get("ord_id"));
				JSONObject member = (JSONObject)obj.get("member");
				int member_id=Integer.parseInt((String)member.get("member_id"));			
				//새 주문이 왔다는 알림창 띄우기
				new newOrder(mainClient.main.getStoreMain(), ord_id);
'''
<br>
<br>
<br>
# 채팅<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121900091-ffac4800-cd5f-11eb-916f-6c2b5e674591.PNG">
<br>
'''java 채팅 보낼 때

	//메시지를 매개변수로 받고 json화 시킨 데이터를 반환해줌
	//채팅용  //나중에 매개변수로 member도 받아서 처리하기
	public static String getMsgJson(String msg, Member member) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"cmd\" : \"chat\",");
		sb.append("\"msg\" : \""+msg+"\",");
		sb.append("\"member_id\":\""+member.getMember_id()+"\"");
		sb.append("}");
		return sb.toString();
	}
'''
<br>
'''java 채팅 수신(Store)
	
	else if(cmd.equals("chat")) {
		String msg = (String) obj.get("msg");
		int member_id = Integer.parseInt((String)obj.get("member_id"));
		if(Integer.parseInt((String)obj.get("member_id")) == mainClient.main.getMember_id()) { //내가 선택한 테이블 로우가 전송 받은 멤버 아이디와 같다면..
			mainClient.main.getChatTextArea().append(msg+"\n");
		}
	}
'''
<br>
'''java 채팅 수신(Customer)
	
	if(cmd.equals("chat")) {
		if( Integer.parseInt((String)json.get("member_id")) == productMain.getMember().getMember_id()) {
			String msg = (String)json.get("msg");
			productMain.getChatArea().append(msg+"\n");					
		}
'''
<br>
<br>
<br>
# 고객 on/off 시
<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121900805-ca542a00-cd60-11eb-88d9-863175184a02.PNG">
<br>
'''java
	
	//멤버의 로그인 상태를 바꿔줄 메서드
	public void logOnOff(String onoff) {
		String sql  = "update member set onoff='"+onoff+"' where member_id="+member.getMember_id();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = getAppMain().getCon().prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if(result < 1) {
				System.out.println("고객 "+onoff+"으로 상태 변경 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAppMain().release(pstmt);
		}
	}
'''
<br>
<br>
<br>
 
# 배차 요청하기<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121901187-23bc5900-cd61-11eb-8621-d72b6837cbdc.PNG">
<br>
'''java 배차 상태 업로드(Store)
	
	//배차 중으로 정보 업데이트
	public void infoUpdate() {
		int ord_id=(int)table.getValueAt(table.getSelectedRow(), 0);
		
		PreparedStatement pstmt=null;
		String sql="update ord set info='배차 중' where ord_id="+ord_id;
		
		try {
			pstmt=con.prepareStatement(sql);
			int result=pstmt.executeUpdate();
			if(result<1) {
				System.out.println("배차중 업데이트 실패");
			}else {
				ordModel.ordList();		
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
'''
<br>
''' java 변경된 상태 전송(Store)<br>

	/라이더 페이지에 배차를 요청!
	public void sendState() {
		StringBuffer sb=new StringBuffer();
		Object ord_id = table.getValueAt(table.getSelectedRow(), 0) ;
		sb.append("{");
		sb.append("\"cmd\" : \"state\",");
		sb.append("\"state\" : \"배차 중\"");
		sb.append("\"ord_id\" : \""+ord_id+"\"");
		sb.append("}");
		mainThread.send(sb.toString());
	}
'''
<br>
''' java 변경된 상태 수신(Rider)

	String cmd = (String)json.get("cmd");
		if(cmd.equals("state")) {
			String state = (String)json.get("state");
			if(state.equals("배차 중")) {
				JOptionPane.showMessageDialog(riderMain, "새 주문이 들어왔습니다. 배차를 진행해주세요.");
				riderMain.getRiderOrder().getOrderList();
			}
		}

'''
<br>
<br>
<br>

# 라이더 배정<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121901942-d5f42080-cd61-11eb-9cd1-76e6f81696c0.PNG">
<br>
'''java 라이더 배정하기

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
'''
<br>
<br>
<br>

# 라이더 조회<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121903307-215afe80-cd63-11eb-923d-130d6ef08f09.PNG">
<br>
'''java 라이더 조회
	//라이더 조회하기
	public RiderSearch(RiderMain riderMain) {
		super(riderMain);
		
		this.riderMain=riderMain;
		table=new JTable();
		scroll=new JScrollPane(table);
		
		scroll.setPreferredSize(new Dimension(1150, 400));
		add(scroll, BorderLayout.NORTH);
		getRiderList();
		
		table.addMouseListener(new MouseAdapter() {	
			public void mouseReleased(MouseEvent e) {
				int row=table.getSelectedRow();
				int rider_id=Integer.parseInt((String)table.getValueAt(row, 0));
				new work(riderMain, rider_id);
			}
		});
	}
	
'''
<br>
'''java 라이더 목록 데이터 베이스에서 조회
	
	public void getRiderList() {
		String sql="select r.rider_id, r.rider_name, r.hit, count(if(o.info='배달 중', o.info, null)) as 'have' from rider r left outer join"
				+ " ord o on r.rider_id=o.rider_id group by r.rider_id order by r.rider_id asc";
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		model = new SearchModel();
		try {
			pstmt=this.getRiderMain().getCon().prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				Search search=new Search();
				search.setRider_id(rs.getInt("rider_id"));
				search.setRider_name(rs.getString("rider_name"));
				search.setHit(rs.getInt("hit"));
				search.setHave(rs.getInt("have"));
				
				model.data.add(search);
			}
			table.setModel(model);	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			release(pstmt, rs);
		}	
	}
'''
<br>
'''java 라이더 상세보기

	//라이더 상세보기
	public void deliveryList(int rider_id) {
		String sql="select o.ord_id, o.price, m.addr, o.regdate, o.info from ord o inner join member m on m.member_id=o.member_id";
		sql+=" inner join rider r on r.rider_id=o.rider_id where r.rider_id="+rider_id;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSetMetaData meta;
		model=new SearchModel2();
		
		try {
			pstmt=riderMain.getCon().prepareStatement(sql);
			//pstmt.setInt(1, ord_);
			rs=pstmt.executeQuery();
			meta=rs.getMetaData();

			while(rs.next()) {
				Search2 search2=new Search2();
				search2.setOrd_id(rs.getInt("ord_id"));
				search2.setPrice(rs.getInt("price"));
				search2.setAddr(rs.getString("addr"));
				search2.setRegdate(rs.getString("regdate"));
				search2.setInfo(rs.getString("info"));
				
				model.data.add(search2);
			}
			table.setModel(model);
			table.updateUI();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			release(pstmt, rs);
		}
	}
'''
<br>
<br>
<br>

# 배달 완료<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121903934-bcec6f00-cd63-11eb-8323-ff6d9d682f17.PNG">
<br>
'''java 주문 완료 처리(Rider)
	
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
'''
<br>
''' java 배달 건수 증가
	
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
'''
<br>
<br>
<br>

# 영수증 생성<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121904525-3a17e400-cd64-11eb-9834-f903c506b60b.PNG">
<br>
''' java 배달 완료 시 영수증 생성

	//배달 완료 시 영수증 추가하기
	public void createBill(String ord_id) {
		Calendar calendar = Calendar.getInstance();
		String mm = (calendar.get(Calendar.MONTH)+1)+"MM";
		String dd = calendar.get(Calendar.DATE)+"DD";
		String billname = ord_id+"ord.txt";
		String result = getOrder(ord_id);
		BufferedReader r = null;
		BufferedWriter w = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			String path = "D:\\workspace\\korea_javaworkspace\\StoreApp\\bill\\"+mm+"\\"+dd; //파일을 저장할 디렉터리 경로
			File file = new File(path); //디렉터리 경로를 file로 지정한뒤

			if(!file.exists()) { //존재 여부를 판단한다
				file.mkdirs(); //만약 경로가 없다면 경로생성
			}
			r = new BufferedReader(new InputStreamReader(is = new ByteArrayInputStream(result.getBytes())));
			//생성한 경로를 이용해 파일 생성하기
			w = new BufferedWriter(new FileWriter(path+"\\"+billname));
			int data = -1;

			while(true) {
				data = r.read();
				if(data == -1) break;
				w.write(data);
			}
			JOptionPane.showMessageDialog(mainClient.main, "완료된 주문의 영수증을 보관 했습니다.");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(w != null) {
				try {
					w.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(r != null) {
				try {
					r.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
'''
<br>
<br>
<br>

# 리뷰 작성<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121904810-7cd9bc00-cd64-11eb-8ae4-e64ad54a7b75.PNG">
<br>
'''java 리뷰 등록

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
'''
<br>
''' java 리뷰 작성 완료 시
	
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
'''
<br>
''' java 리뷰 게시글 페이징 처리
	
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
'''
<br>
<br>
<br>
