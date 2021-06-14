package com.mcdonaldspos.rider.chat;

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

import com.mcdonaldspos.rider.main.RiderMain;



public class  RiderSocket implements Runnable{
	private static RiderMain riderMain;

	private static RiderSocket riderSocket;
	private static String ip = "";
	private static int port = 9999;
	private static boolean flag = true;
	private static BufferedReader buffr;
	private static BufferedWriter buffw;
	private static Socket socket;
	
	private RiderSocket(String ip, int port) throws UnknownHostException, IOException{
		try {
			socket = new Socket(ip, port);			
		}catch(ConnectException e){
			System.exit(0);
			e.printStackTrace();
		}
	}
	
	public static Socket getSocket(RiderMain riderMain) {
		if(riderSocket == null) {
			try {
				riderSocket.riderMain = riderMain;
				riderSocket = new RiderSocket(riderSocket.ip, riderSocket.port);
				buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return socket;
	}
	
	public static RiderSocket getClientSocket() {
		return riderSocket;
	}
	
	//여기가 최종 도착지 여기서 넘어온 cmd를 분석해서 로직 짜야함
	public void listen() {
		try {
			String data = buffr.readLine();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject)parser.parse(data);
			
			String cmd = (String)json.get("cmd");
			if(cmd.equals("state")) {
				String state = (String)json.get("state");
				if(state.equals("배차 중")) {
					JOptionPane.showMessageDialog(riderMain, "새 주문이 들어왔습니다. 배차를 진행해주세요.");
					riderMain.getRiderOrder().getOrderList();
				}
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