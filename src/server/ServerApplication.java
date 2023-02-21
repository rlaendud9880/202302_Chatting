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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import dto.ExitReqDto;
import dto.ExitRespDto;
import dto.JoinReqDto;
import dto.JoinRespDto;
import dto.JoinRoomReqDto;
import dto.JoinRoomRespDto;
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
	private String roomname;
	private String kinguser;
	private String joineduser;
	private static List<String> roomList = new ArrayList<>();
	private static List<String> kingUsers = new ArrayList<>();
	private static List<String> connectedUsers = new ArrayList<>();
	
	public ConnectedSocket(Socket socket) {
		this.socket = socket;
		gson = new Gson();
		socketList.add(this);
		
	}
	
	public void run() {
		try {
			inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream)); 
			List<String> createdRooms = new ArrayList<>();
			Set<String> createdRoomsSetName;
			
			while(true) {
				String request = in.readLine();	// requestDto(Json형태로 받음)
				RequestDto requestDto = gson.fromJson(request, RequestDto.class);
				System.out.println(request);
				switch (requestDto.getResource()) {
					case "join": 
						JoinReqDto joinReqDto =gson.fromJson(requestDto.getBody(), JoinReqDto.class);
						username = joinReqDto.getUsername(); // 항상 Json형태로 날라옴
						
						for(ConnectedSocket connectedSocket : socketList) {
							connectedUsers.add(connectedSocket.getUsername());
							createdRooms.add(connectedSocket.getRoomname());
						}
						
						createdRoomsSetName = new LinkedHashSet<>(createdRooms);
						createdRooms.clear();
						roomList.addAll(createdRoomsSetName);
						
						System.out.println(roomList);
						
						JoinRespDto joinRespDto = new JoinRespDto(username + "님이 접속하였습니다.", connectedUsers); 
						sendToAll(requestDto.getResource(),"ok", gson.toJson(joinRespDto));
						System.out.println("joinRespDto: " + joinRespDto);
						
						break;
						
					case "sendMessage":
						MessageReqDto messageReqDto = gson.fromJson(requestDto.getBody(), MessageReqDto.class);
						String message = messageReqDto.getFromUser() + ": " + messageReqDto.getMessageValue();
						MessageRespDto messageRespDto = new MessageRespDto(message);
						messageToRoom(requestDto.getResource(),"ok",gson.toJson(messageRespDto), messageReqDto.getRoomName());
						System.out.println("messageReqDto: "+message);
						break;
						
					case "plus":
						RoomReqDto roomReqDto = gson.fromJson(requestDto.getBody(), RoomReqDto.class);
						roomname = roomReqDto.getRoomName();
						
						for(ConnectedSocket connectedSocket : socketList) {
							createdRooms.add(connectedSocket.getUsername());
							System.out.println(createdRooms);
						}
						createdRoomsSetName = new LinkedHashSet<>(createdRooms);
						createdRooms.clear();
						
						roomList.addAll(createdRoomsSetName);
						System.out.println(roomList);
						
						kinguser = roomReqDto.getKingUser();
						System.out.println(kinguser);
						
						for(ConnectedSocket connectedSocket : socketList) {
							kingUsers.add(connectedSocket.getUsername());
						}
						RoomRespDto roomRespDto = new RoomRespDto(roomname + "생성됨", createdRooms, kingUsers); 
						sendToAll(requestDto.getResource(),"ok", gson.toJson(roomRespDto));
						System.out.println("roomRespDto:" + roomRespDto);
						break;
						
					case "joinRoom":
						JoinRoomReqDto joinRoomReqDto = gson.fromJson(requestDto.getBody(), JoinRoomReqDto.class);
						
						roomname = joinRoomReqDto.getRoomname();
						joineduser = joinRoomReqDto.getUsername();
						
						List<String> roomConnectedUsers = new ArrayList<>();
						for(ConnectedSocket connectedSocket : socketList) {
							roomConnectedUsers.add(connectedSocket.joineduser);
						}
						JoinRoomRespDto joinRoomRespDto = new JoinRoomRespDto(joineduser + "님이 접속하였습니다.", roomConnectedUsers);
						messageToRoom(requestDto.getResource(), "ok", gson.toJson(joinRoomRespDto), roomname);
						System.out.println("joinRoomRespDto:" + joinRoomRespDto);
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
						System.out.println("exitRespDto:" + exitRespDto);
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
	
	private void messageToRoom(String resource, String status, String body, String roomname) throws IOException {
		ResponseDto responseDto = new ResponseDto(resource, status, body);
		
//		connectedUsers.add(roomname);
		for(ConnectedSocket connectedSocket : socketList) {
			if(connectedSocket.getRoomname().equals(roomname)) {
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
