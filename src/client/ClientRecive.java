package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;

import dto.JoinRespDto;
import dto.JoinRoomRespDto;
import dto.MessageRespDto;
import dto.ResponseDto;
import dto.RoomRespDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientRecive extends Thread{
	
	private final Socket socket;
	private InputStream inputStream;
	
	private Gson gson;
	
	@Override
	public void run() {
		try {
			inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			gson = new Gson();
			
			while(true) {
				String requset = in.readLine();
				ResponseDto responseDto = gson.fromJson(requset, ResponseDto.class);
				switch(responseDto.getResource()) {
					case "join":
						JoinRespDto joinRespDto = gson.fromJson(responseDto.getBody(), JoinRespDto.class);
						Client.getInstance().getRoomListModel().addElement("---<< RoomList>>---");
						Client.getInstance().getRoomListModel().addAll(joinRespDto.getConnectedRooms());
						Client.getInstance().getUserList().setSelectedIndex(0);
						System.out.println(joinRespDto);
						System.out.println();
						break;
						
					case "joinRoom":
						// equalsIgnoreCase("ok") 대소문자 구분을 안함.
						JoinRoomRespDto joinRoomRespDto= gson.fromJson(responseDto.getBody(), JoinRoomRespDto.class);
						System.out.println(joinRoomRespDto+"\n");
						Client.getInstance().getChatArea().append(joinRoomRespDto.getWelcomeRoomMessage() + "\n");
						Client.getInstance().getRoomTitleLabel().setText(joinRoomRespDto.getRoomname());
						Client.getInstance().getUserList().setSelectedIndex(MIN_PRIORITY);
						System.out.println(joinRoomRespDto);
						System.out.println();
						break;
						
					case "sendMessage":
						MessageRespDto messageRespDto = gson.fromJson(responseDto.getBody(), MessageRespDto.class);
//						Client.getInstance().getChatArea().append(responseDto.getBody() + "\n");
						
						System.out.println(Client.getInstance().getChatArea());
						
						Client.getInstance().getChatArea().append(messageRespDto.getMessageValue() + "\n");
						System.out.println(messageRespDto);
						break;
						
					case "plus":
						RoomRespDto roomRespDto = gson.fromJson(responseDto.getBody(), RoomRespDto.class);
						
						Client.getInstance().getRoomListModel().clear();
						Client.getInstance().getRoomListModel().addElement("---<< RoomList>>---");
						Client.getInstance().getRoomListModel().addAll(roomRespDto.getCreatedRoomList());
//						Client.getInstance().getUserList().setSelectedIndex(0);
						System.out.println(roomRespDto.getCreatedRoomList());
						System.out.println(roomRespDto);
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
