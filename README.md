# mcdonaldpos

<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121894979-b73e5b80-cd5a-11eb-9901-c397f8d879f0.PNG">
<br>
<br>
<br>
#개요
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121895969-c70a6f80-cd5b-11eb-88d7-46d27da23154.PNG">
<br>
<br>
<br>
#초기 구상 목표
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896144-ef926980-cd5b-11eb-81ba-ad797895a3e8.PNG">
<br>
<br>
<br>
#Library
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896256-105abf00-cd5c-11eb-9949-66ee06910148.PNG">
<br>
<br>
<br>
#디렉터리 구조
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896330-22d4f880-cd5c-11eb-9861-84416771d252.PNG">
<br>
<br>
<br>
#DB 구조
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896832-b3abd400-cd5c-11eb-89d8-cd92aee68acf.PNG">
<br>
<br>
<br>
#소켓을 이용한 통신
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896981-dd64fb00-cd5c-11eb-825b-c613af3dba8b.PNG">
<br>
<br>
<br>
#회원가입
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121897130-01c0d780-cd5d-11eb-8450-e8c9669a597e.PNG">
<br>
<br>
<br>
#아이디 유효성 검사
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121897506-6a0fb900-cd5d-11eb-8af4-abe132541bb7.PNG">
<br>
<br>
<br>
#비밀번호 유효성 검사
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121897599-827fd380-cd5d-11eb-9bb9-65dd87fd9e1d.PNG">
<br>
<br>
<br>
#이메일 인증 확인
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121897727-a3e0bf80-cd5d-11eb-81b5-767a1a842301.PNG">
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
#로그인
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121898082-f9b56780-cd5d-11eb-82e7-f8b8cbe92248.PNG">
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
#상품 목록
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121898887-d63eec80-cd5e-11eb-8313-be8775f99299.PNG">
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
''' java 주문 요청 받았을 때

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
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896981-dd64fb00-cd5c-11eb-825b-c613af3dba8b.PNG">
<br>
<br>
<br>
