package com.mcdonaldspos.store.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mcdonaldspos.store.main.StoreMain;

public class MainClient {
	Socket socket;
	StoreMain main;
	ClientMainThread clientMainThread;
	public MainClient(StoreMain main) {
		this.main = main;
		connect();

	}
	
	//방법2) 채팅서버에 회원의 id만 보내서, 이 회원의 정보는 서버측에서 참조할수 있게 하는 방법
	public void sendId() {
		
	}
	
	public void connect() {
		//String ip=t_ip.getText();
		//int port=Integer.parseInt(t_port.getText());
		String ip="";
		int port=9999;
		
		try {
			socket = new Socket(ip, port); //접속!!!!!!!
			//클라이언트 측의 대화용 쓰레드 생성 
			clientMainThread = new ClientMainThread(socket, this);
			clientMainThread.start(); //서버의 메시지 실시간 청취 시작
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ClientMainThread getClientMainThread() {
		return clientMainThread;
	}
}
