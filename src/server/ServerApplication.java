package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {
	
	public static void main(String[] args) {
		
		
		ServerSocket serverSocket = null; 
		try {
			serverSocket = new ServerSocket(9090);
			System.out.println("=====<< 서버 실행 >>=====");
						
			
			while(true){
				Socket socket =	serverSocket.accept(); // 클라이언트의 접속을 기다리는 것. #1에 반응
				ConnectedSocket connectedSocket = new ConnectedSocket(socket);				
				connectedSocket.start();				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("=====<< 서버 종료 >>=====");
		}
	}
}
