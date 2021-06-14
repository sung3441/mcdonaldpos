package com.mcdonaldspos.store.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mcdonaldspos.store.main.Ord;
import com.mcdonaldspos.store.neworder.newOrder;

public class ClientMainThread extends Thread{
	MainClient mainClient;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;	
	boolean flag=true;
	Ord ord;
	newOrder newOrder;
	
	public ClientMainThread(Socket socket,MainClient mainClient) {
		System.out.println("메인 쓰레드 동작 중");
		this.socket=socket;
		this.mainClient=mainClient;
		
		try {
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	//고객 채팅 듣기
	public void listen() {
		String data=null;
		try {
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
					
				}else if(cmd.equals("chat")) {
					String msg = (String) obj.get("msg");
					int member_id = Integer.parseInt((String)obj.get("member_id"));
					if(Integer.parseInt((String)obj.get("member_id")) == mainClient.main.getMember_id()) { //내가 선택한 테이블 로우가 전송 받은 멤버 아이디와 같다면..
						mainClient.main.getChatTextArea().append(msg+"\n");
					}
				}else if(cmd.equals("state")) {
					mainClient.main.getOrdList();
					System.out.println(obj);
					String ord_id = (String)(obj.get("ord_id"));
					if(((String)obj.get("state")).equals("배달 완료")) {
						createBill(ord_id);
					}
				}
			}
			//state면 model.ordlist
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag=false;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	//주문 정보 가져오기
	public String getOrder(String ord_id) {
		String sql = "select * from ord where ord_id="+ord_id;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			pstmt = mainClient.main.getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int ord = rs.getInt("ord_id");
				int price  = rs.getInt("price");
				String menu = rs.getString("menu");
				String memo = rs.getString("memo");
				
				sb.append("================\n");
				sb.append(" 주문 번호 : ["+ord_id+"]\n");
				sb.append(" 가격 : ["+price+"원]\n");
				sb.append(" 주문 메뉴 : "+menu+"\n");
				sb.append(" 메모 : "+memo+"\n");
				sb.append("================\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			mainClient.main.release(pstmt, rs);
		}
		return sb.toString();
	}
	
	//매장 채팅 말하기
	public void send(String msg) {
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//쓰레드 멈추게하기
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public void run() {
		while(flag) {
			listen();
		}
		if(buffw!=null) {
			try {
				buffw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(buffr!=null) {
			try {
				buffr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
