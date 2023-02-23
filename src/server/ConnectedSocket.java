package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
import lombok.Getter;

@Getter
public class ConnectedSocket extends Thread {
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Gson gson;

	private String username;
	
	private static List<ConnectedSocket> socketList = new ArrayList<>();
	private static List<Room> roomList = new ArrayList<>();

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
			
			while(true) {
				String request = in.readLine();	// requestDto(Json형태로 받음)
				RequestDto requestDto = gson.fromJson(request, RequestDto.class);
				System.out.println(request);
				switch (requestDto.getResource()) {
				
					case "join": 
						username = requestDto.getBody(); // 항상 Json형태로 날라옴
						reflashRoomList();
						
						break;
						
					case "sendMessage":
						MessageReqDto messageReqDto = gson.fromJson(requestDto.getBody(), MessageReqDto.class);
						String message = messageReqDto.getFromUser() + ": " + messageReqDto.getMessageValue();
						MessageRespDto messageRespDto = new MessageRespDto(message);
//						sendToAll(requestDto.getResource(),"ok",gson.toJson(messageRespDto));
						
						break;
						
					case "plus":
						Room room = new Room(username,requestDto.getBody());
						room.getUsers().add(this);
						
						roomList.add(room);
						
//						RoomRespDto roomRespDto = new RoomRespDto("입장", kingUsers, createdRooms); 
						ResponseDto responseDto = new ResponseDto("plusSuccess", "ok", room.getRoomname());
						
						sendToMe(responseDto);
						
						reflashRoomList();
						break;
						
					case "joinRoom":
						JoinRoomReqDto joinRoomReqDto = gson.fromJson(requestDto.getBody(), JoinRoomReqDto.class);
						
//						JoinRoomRespDto joinRoomRespDto = new JoinRoomRespDto(joineduser + "님이 접속하였습니다.", roomname);
//						ResponseDto respDto = new ResponseDto(requestDto.getResource(), "ok", gson.toJson(joinRoomRespDto));
						
//						for(Room rooms : roomList) {
//							if(rooms.getRoomname().equals(roomname)|| rooms.getRoomname().isBlank()) {
//								rooms.addRoomList(this.socket);
//								rooms.broadcast(respDto);
//							}
//						}
						
						
//						sendToRoom();
//						sendToAll(requestDto.getResource(), "ok", gson.toJson(joinRoomRespDto));
						break;
						
					case "exit":
						ExitReqDto exitReqDto = gson.fromJson(requestDto.getBody(), ExitReqDto.class);
						username = exitReqDto.getUsername(); // 항상 Json형태로 날라옴
						List<String> unconnectedUsers = new ArrayList<>();
						
					
						
						ExitRespDto exitRespDto = new ExitRespDto(username + "님이 퇴장하였습니다.", unconnectedUsers); 
//						sendToAll(requestDto.getResource(),"ok", gson.toJson(exitRespDto));
						break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void reflashRoomList() {
		List<String> roomNameList = new ArrayList<>();
		
		for(Room room : roomList) {
			roomNameList.add(room.getRoomname());
		}
		ResponseDto dto = new ResponseDto("reflashRoomList", "ok", gson.toJson(roomNameList));
		
		try {
			sendToAll(dto, socketList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendToMe(ResponseDto responseDto) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		PrintWriter out = new PrintWriter(outputStream, true);
			
		out.println(gson.toJson(responseDto));
	}
	
	private void sendToAll(ResponseDto responseDto, List<ConnectedSocket> socketList) throws IOException {
		for(ConnectedSocket connectedSocket : socketList) {
			OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			
			out.println(gson.toJson(responseDto));
		
		}
	}
	private void sendToRoom(ResponseDto responseDto, List<ConnectedSocket> socketList) throws IOException {
		for(ConnectedSocket connectedSocket : socketList) {
			OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			
			out.println(gson.toJson(responseDto));
		
		}
	}
	
}
