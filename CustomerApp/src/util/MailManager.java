package util;

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
				return new javax.mail.PasswordAuthentication("mcd202012@gmail.com", "avmzrijduohyvtff");
			}
		});
		
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress("mcd202012@gmail.com"));
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
