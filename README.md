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
'''이메일 전송 java
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
<br>
<br>
<br>


<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896981-dd64fb00-cd5c-11eb-825b-c613af3dba8b.PNG">
<br>
<br>
<br>
<img width = "500" alt = "슬라이드1" src = "https://user-images.githubusercontent.com/67699933/121896981-dd64fb00-cd5c-11eb-825b-c613af3dba8b.PNG">
<br>
<br>
<br>
