package com.mcdonaldspos.customer.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mcdonaldspos.customer.login.Member;
import com.mcdonaldspos.customer.product.ProductMain;

public class  ClientSocket implements Runnable{
	private static ProductMain productMain;

	private static ClientSocket clientSocket;
	private static String ip = "";
	private static int port = 9999;
	private static boolean flag = true;
	private static BufferedReader buffr;
	private static BufferedWriter buffw;
	private static Socket socket;
	
	private ClientSocket(String ip, int port) throws UnknownHostException, IOException{
		try {
			socket = new Socket(ip, port);			
		}catch(ConnectException e){
			JOptionPane.showMessageDialog(productMain, "매장 오픈시간이 아닙니다.");
			System.exit(0);
			e.printStackTrace();
		}
	}
	
	public static Socket getSocket(ProductMain productMain) {
		if(clientSocket == null) {
			try {
				ClientSocket.productMain = productMain;
				clientSocket = new ClientSocket(ClientSocket.ip, ClientSocket.port);
				buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return socket;
	}
	
	public static ClientSocket getClientSocket() {
		return clientSocket;
	}
	
	//여기가 최종 도착지 여기서 넘어온 cmd를 분석해서 로직 짜야함
	public void listen() {
		try {
			String data = buffr.readLine();
			System.out.println("클라이언트가 서버로부터 전달 받은 메시지 : "+data);
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject)parser.parse(data);
			String cmd = (String)json.get("cmd");
			if(cmd.equals("chat")) {
				if( Integer.parseInt((String)json.get("member_id")) == productMain.getMember().getMember_id()) {
					String msg = (String)json.get("msg");
					productMain.getChatArea().append(msg+"\n");					
				}
			}else if(cmd.equals("state")) {
				productMain.updateOrdTable();
			}else if(cmd.equals("cancel")) {
				String ord_id = (String)json.get("ord_id");
				JOptionPane.showMessageDialog(productMain, "매장 상황으로 ["+ord_id+"] 번 주문이 취소 됐습니다.");
				productMain.updateOrdTable();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			System.out.println("클라이언트가 서버로 보내는 메세지 : "+msg);
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void run() {
		while(flag) {
			listen();
		}
	}
}
