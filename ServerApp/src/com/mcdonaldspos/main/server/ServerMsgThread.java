package com.mcdonaldspos.main.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ServerMsgThread extends Thread{
	private ServerThread serverThread;
	private Socket socket;
	private boolean flag = true;
	private BufferedReader buffr;
	private BufferedWriter buffw;	
	private JSONParser parser;
	
	//생성자
	public ServerMsgThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket = socket;
		parser = new JSONParser();
		try {
			//데이터를 읽고 쓸 스트림 경로 생성
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//서버에서 신호 감지
	public void listen() {
		try {
			String msg = buffr.readLine();
			if(msg != null) {
				JSONObject json = (JSONObject)parser.parse(msg);
				String cmd = (String)json.get("cmd");
				if(cmd.equals("chat")) {
					serverThread.main.area.append("채팅시도\n");
					chat(json);
				}else if(cmd.equals("order")) {
					order(json);
				}else if(cmd.equals("state")) {
					state(json);
				}
				serverThread.main.area.append("여기는 온거냐?\n");
				for(int i = 0; i < serverThread.clientAr.size(); i++) {
					serverThread.clientAr.get(i).send(msg);
				}
			}
		} catch (IOException e) {
			flag = false;
			deleteThread();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//서버에서 클라이언트의 listen으로 메세지 전송
	public void send(String msg) {
		try {
			System.out.println("서버에서 클라이언트로 보내는 메시지 : "+msg);
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//넘어오느 메세지가 널이거나 -1, 혹은 io익셉션 발생 시 쓰레드 종료 및 배열에서 제거
	public void deleteThread(){
		serverThread.main.area.append("[사용자 접속 종료]\n");
		serverThread.clientAr.remove(this);
	}
	
	//넘겨받은 요청이 채팅이라면 DB에 값 넣기
	public void chat(JSONObject json) {
		PreparedStatement pstmt = null;
		String sql = "insert into chat(member_id, msg) values(?, ?)";
		try {
			pstmt = serverThread.main.getCon().prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt((String)json.get("member_id")));
			pstmt.setString(2, (String)json.get("msg"));
			int result = pstmt.executeUpdate();
			if(result == -1) {
				serverThread.main.area.append("[DB에 채팅 넣기 실패]\n");
			}else {
				serverThread.main.area.append("[DB에 채팅 넣기 성공]\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			serverThread.main.release(pstmt);
		}
	}
	
	//넘겨받은 요청이 주문이라면
	public void order(JSONObject json) {
		
	}
	
	//넘겨받은 요청이 상태변경이라면
	public void state(JSONObject json) {
		String sql = "";
	}
	
	
	public void run() {
		while(flag) {
			listen();
		}
		//반복문이 종료되면 쓰레드를 제거하고 삭제한다.
		if(buffr != null) {
			try {
				buffr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(buffw != null) {
			try {
				buffw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
