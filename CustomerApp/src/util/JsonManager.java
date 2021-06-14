package util;

import com.mcdonaldspos.customer.login.Member;

//json 처리를 도와줄 클래스
public class JsonManager {

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
	
	//주문용 json을 반환
	public static String getOrderJson(Member member, String order_id) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"cmd\" : \"order\",");
		sb.append("\"member\" : {");
		sb.append("\"member_id\" : \""+member.getMember_id()+"\",");
		sb.append("\"user_id\" : \""+member.getUser_id()+"\"");
		sb.append("},");
		sb.append("\"ord_id\" : \""+order_id+"\"");
		sb.append("}");

		return sb.toString();
	}
	
	//상태 변경용 json을 반환
	public static void getStateJson() {
		
	}
}



















