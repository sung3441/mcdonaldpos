package com.mcdonaldspos.main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerThread extends Thread{
	private boolean serverFlag = true;
	private ServerSocket server;
	private Socket socket;
	private  static int port = 9999;
	private Thread severThread;
	private boolean socketFlag = true;
	Vector<ServerMsgThread> clientAr = new Vector<ServerMsgThread>();
	ServerMain main;
	//서버 가동
	public ServerThread(ServerMain main) {
		this.main = main;
	}
	
	//서버 실행 및 접속자 감지 메서드
	public void runServer() {
		try {
			server = new ServerSocket(port);
			main.area.append("Server is running at "+port+"....\n");
			while(socketFlag) {
				socket = server.accept(); //접속자 감지
				String ip = socket.getInetAddress().getHostAddress(); //접속자 아이피 주소 알아내기
				main.area.append("["+ip+"접속자 발견]\n");
				ServerMsgThread serverMsgThread = new ServerMsgThread(this, socket); //한 사람을 담당한 쓰레드 생성
				serverMsgThread.start(); //쓰레드 동작
				clientAr.add(serverMsgThread);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isServerFlag() {
		return serverFlag;
	}

	public void setServerFlag(boolean serverFlag) {
		this.serverFlag = serverFlag;
	}

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		ServerThread.port = port;
	}

	public Thread getSeverThread() {
		return severThread;
	}

	public void setSeverThread(Thread severThread) {
		this.severThread = severThread;
	}

	public boolean isSocketFlag() {
		return socketFlag;
	}

	public void setSocketFlag(boolean socketFlag) {
		this.socketFlag = socketFlag;
	}

	public Vector<ServerMsgThread> getClientAr() {
		return clientAr;
	}

	public void setClientAr(Vector<ServerMsgThread> clientAr) {
		this.clientAr = clientAr;
	}

	public void run() {
		while(serverFlag) {
			runServer();
		}
	}
}
