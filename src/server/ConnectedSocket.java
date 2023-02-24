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
			Room room;
			while(true) {
				String request = in.readLine();	// requestDto(Json형태로 받음)
				RequestDto requestDto = gson.fromJson(request, RequestDto.class);
				room = new Room(username,requestDto.getBody());
				switch (requestDto.getResource()) {
				
					case "join": 
						username = requestDto.getBody(); // 항상 Json형태로 날라옴
						reflashRoomList();
						
						break;
						
					case "sendMessage":
						MessageReqDto messageReqDto = gson.fromJson(requestDto.getBody(), MessageReqDto.class);
						
						if(!messageReqDto.getToUser().equalsIgnoreCase(room.getRoomname())) {
							String message = messageReqDto.getFromUser() + "> " + messageReqDto.getMessageValue();
							MessageRespDto messageRespDto = new MessageRespDto(message);
							ResponseDto responseDto = new ResponseDto("sendMessage", "ok", gson.toJson(messageRespDto));
							sendToRoom(responseDto, socketList);
						} 
//						System.out.println(messageReqDto);
						
						// roomname을 가져오는것
						// 메시지
						
						break;
						
					case "plus":
						room = new Room(username,requestDto.getBody());
						room.getUsers().add(this);
						roomList.add(room);
						
						ResponseDto responseDto1 = new ResponseDto("plusSuccess", "ok", room.getRoomname());
//						System.out.println(responseDto);
						sendToMe(responseDto1);
						
						reflashRoomList();
						break;
						
					case "joinRoom":
						
						// 방에 참여를 하였을 때, 무엇을 할 것인가?
						// 방의 이름, users를 가져오기
						JoinRoomReqDto joinRoomReqDto = gson.fromJson(requestDto.getBody(), JoinRoomReqDto.class);
						List<String> joinUser = new ArrayList<>();
						room.getUsers().add(this);
						joinUser.add(joinRoomReqDto.getUsername());
						
						String joinMessage = username + "님이 퇴장하셨습니다.";
						
						ResponseDto joinResponseDto = new ResponseDto("joinSuccess", "ok", room.getRoomname());
//						System.out.println(joinResponseDto);
						sendToMe(joinResponseDto);
						
						reflashRoomList();
						
						break;
						
					case "exit":
						
						username = requestDto.getBody(); // 항상 Json형태로 날라옴
						
						ResponseDto exitRsponseDto = new ResponseDto("exitSuccess", "ok", username);
						room.getUsers().remove(this);
						roomList.remove(room);
						String exitMessage = username + "님이 퇴장하셨습니다.";
//						MessageRespDto messageRespDto2 = 
						reflashRoomList();
						
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
	private void sendToRoom(ResponseDto responseDto,List<ConnectedSocket> socketList) throws IOException {
		for(ConnectedSocket connectedSocket : socketList) {
			OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			
			out.println(gson.toJson(responseDto));
		
		}
	}
	private void broadcastMessage(String message1, Room room) {
	    MessageRespDto messageRespDto = new MessageRespDto(message1);
	    ResponseDto responseDto = new ResponseDto("sendMessage", "ok", gson.toJson(messageRespDto));
	    try {
	        for (ConnectedSocket user : room.getUsers()) {
	            user.sendToMe(responseDto);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
