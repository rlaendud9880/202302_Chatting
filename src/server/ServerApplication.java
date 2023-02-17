package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import dto.ExitReqDto;
import dto.ExitRespDto;
import dto.JoinReqDto;
import dto.JoinRespDto;
import dto.MessageReqDto;
import dto.MessageRespDto;
import dto.RequestDto;
import dto.ResponseDto;
import dto.RoomReqDto;
import dto.RoomRespDto;
import lombok.Data;


@Data
class ConnectedSocket extends Thread {
	private static List<ConnectedSocket> socketList = new ArrayList<>();
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Gson gson;
	
	private String username;
	private String roomtitle;
	
	public ConnectedSocket(Socket socket) {
		this.socket = socket;
		gson = new Gson();
		socketList.add(this);
		
	}
	
	public void run() {
		try {
			inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream)); 
			
			while(true) {
				String request = in.readLine();	// requestDto(Json형태로 받음)
				RequestDto requestDto = gson.fromJson(request, RequestDto.class);
				
				switch (requestDto.getResource()) {
				case "join": 
					JoinReqDto joinReqDto =gson.fromJson(requestDto.getBody(), JoinReqDto.class);
					username = joinReqDto.getUsername(); // 항상 Json형태로 날라옴
					List<String> connectedUsers = new ArrayList<>();
					
					for(ConnectedSocket connectedSocket : socketList) {
						connectedUsers.add(connectedSocket.getUsername());							
					}
					
					JoinRespDto joinRespDto = new JoinRespDto(username + "님이 접속하였습니다.", connectedUsers); 
					sendToAll(requestDto.getResource(),"ok", gson.toJson(joinRespDto));
					break;
				case "sendMessage":
					MessageReqDto messageReqDto = gson.fromJson(requestDto.getBody(), MessageReqDto.class);
					
					if(messageReqDto.getToUser().equalsIgnoreCase("all")) {
						String message = messageReqDto.getFromUser() + "[전체]: " + messageReqDto.getMessageValue();
						MessageRespDto messageRespDto = new MessageRespDto(message);
						sendToAll(requestDto.getResource(),"ok",gson.toJson(messageRespDto));
					}else {
						String message = messageReqDto.getFromUser() + "[" + messageReqDto.getToUser() + "]: " + messageReqDto.getMessageValue();
						MessageRespDto messageRespDto = new MessageRespDto(message);
						sendToUser(requestDto.getResource(),"ok",gson.toJson(messageRespDto), messageReqDto.getToUser());
					}
					break;
					
				case "plus":
					RoomReqDto roomReqDto = gson.fromJson(requestDto.getBody(), RoomReqDto.class);
					roomtitle = roomReqDto.getRoomtitle();
					List<String> createdRoomList = new ArrayList<>();
					
					for(ConnectedSocket connectedSocket : socketList) {
						createdRoomList.add(connectedSocket.getRoomtitle());							
					}
					
					RoomRespDto roomRespDto = new RoomRespDto(createdRoomList); 
					sendToAll(requestDto.getResource(),"ok", gson.toJson(roomRespDto));
					break;
					
				case "exit":
					ExitReqDto exitReqDto = gson.fromJson(requestDto.getBody(), ExitReqDto.class);
					username = exitReqDto.getUsername(); // 항상 Json형태로 날라옴
					List<String> unconnectedUsers = new ArrayList<>();
					
					for(ConnectedSocket connectedSocket : socketList) {
						unconnectedUsers.add(connectedSocket.getUsername());							
					}
					
					ExitRespDto exitRespDto = new ExitRespDto(username + "님이 퇴장하였습니다.", unconnectedUsers); 
					sendToAll(requestDto.getResource(),"ok", gson.toJson(exitRespDto));
					break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void sendToAll(String resource, String status, String body) throws IOException {
		ResponseDto responseDto = new ResponseDto(resource, status, body);
		for(ConnectedSocket connectedSocket : socketList) {
			OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			
			out.println(gson.toJson(responseDto));
		
		}
		
	}
	
	private void sendToUser(String resource, String status, String body, String toUser) throws IOException {
		ResponseDto responseDto = new ResponseDto(resource, status, body);
		for(ConnectedSocket connectedSocket : socketList) {
			if(connectedSocket.getUsername().equals(toUser) || connectedSocket.getUsername().equals(username)) {
				OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
				PrintWriter out = new PrintWriter(outputStream, true);
				
				out.println(gson.toJson(responseDto));
			}
		}
	}
	
}



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
