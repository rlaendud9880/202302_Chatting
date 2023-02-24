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
	private String roomName;
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
					case "reflashRoomList":
						
						List<String> roomNameList = gson.fromJson(responseDto.getBody(), List.class);
						
						Client.getInstance().getRoomListModel().clear();
						Client.getInstance().getRoomListModel().addElement("---<< RoomList>>---");
						Client.getInstance().getRoomListModel().addAll(roomNameList);
						Client.getInstance().getUserList().setSelectedIndex(0);
						break;
					case "plusSuccess":
						roomName = responseDto.getBody();
						Client.getInstance().getMainCard().show(Client.getInstance().getMainPanel(), "chatRoomPanel");
						Client.getInstance().getRoomTitleLabel().setText(roomName);
						break;
					
					case "joinSuccess":
						JoinRoomRespDto joinRoomRespDto = gson.fromJson(responseDto.getBody(), JoinRoomRespDto.class);
						Client.getInstance().getMainCard().show(Client.getInstance().getMainPanel(), "chatRoomPanel");
						Client.getInstance().getRoomTitleLabel().setText(joinRoomRespDto.getRoomname());
						
						break;
						
					case "sendMessage":
						MessageRespDto messageRespDto = gson.fromJson(responseDto.getBody(), MessageRespDto.class);
						Client.getInstance().getMainCard().show(Client.getInstance().getMainPanel(), "chatRoomPanel");
						Client.getInstance().getChatArea().append(messageRespDto.getMessageValue() + "\n");
						System.out.println(messageRespDto);
						break;
					case "exitSuccess":
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
