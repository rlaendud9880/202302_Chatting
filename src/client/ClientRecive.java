package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

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
				RoomRespDto roomRespDto;
				switch(responseDto.getResource()) {
					case "reflashRoomList":
						List<String> roomNameList = gson.fromJson(responseDto.getBody(), List.class);
						
						Client.getInstance().getRoomListModel().clear();
						Client.getInstance().getRoomListModel().addElement("---<< RoomList>>---");
						Client.getInstance().getRoomListModel().addAll(roomNameList);
						Client.getInstance().getUserList().setSelectedIndex(0);
						break;
					case "plusSuccess":
						// equalsIgnoreCase("ok") 대소문자 구분을 안함.
						String roomName= responseDto.getBody();
						Client.getInstance().getMainCard().show(Client.getInstance().getMainPanel(), "chatRoomPanel");
						Client.getInstance().getRoomTitleLabel().setText(roomName);
//						Client.getInstance().getChatArea().append(joinRoomRespDto.getWelcomeRoomMessage() + "\n");
						break;
						
//					case "joinRoom":
//						// equalsIgnoreCase("ok") 대소문자 구분을 안함.
//						JoinRoomRespDto joinRoomRespDto= gson.fromJson(responseDto.getBody(), JoinRoomRespDto.class);
//						System.out.println(joinRoomRespDto+"\n");
//						Client.getInstance().getChatArea().append(joinRoomRespDto.getWelcomeRoomMessage() + "\n");
//						Client.getInstance().getRoomTitleLabel().setText(joinRoomRespDto.getRoomname());
//						Client.getInstance().getUserList().setSelectedIndex(0);
//						System.out.println(joinRoomRespDto);
//						System.out.println();
//						break;
						
					case "sendMessage":
						MessageRespDto messageRespDto = gson.fromJson(responseDto.getBody(), MessageRespDto.class);
//						Client.getInstance().getChatArea().append(responseDto.getBody() + "\n");
						System.out.println();
						System.out.println(Client.getInstance().getChatArea());
						Client.getInstance().getChatArea().append(messageRespDto.getMessageValue() + "\n");
						System.out.println();
						System.out.println(messageRespDto);
						break;
						
					case "plus":
						roomRespDto = gson.fromJson(responseDto.getBody(), RoomRespDto.class);
						
//						Client.getInstance().getRoomListModel().clear();
//						Client.getInstance().getRoomListModel().addElement("---<< RoomList>>---");
//						Client.getInstance().getRoomListModel().addAll(roomRespDto.getCreatedRoomList());
//						Client.getInstance().getUserList().setSelectedIndex(0);
//						System.out.println("리시브" + Client.getInstance().getRoomListModel());
//						
//						
//						Client.getInstance().getRoomListModel().addAll(roomRespDto.getCreatedRoomList());
//						Client.getInstance().getUserList().setSelectedIndex(0);
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
