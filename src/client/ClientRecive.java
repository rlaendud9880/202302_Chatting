package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;

import dto.JoinRespDto;
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
						// equalsIgnoreCase("ok") 대소문자 구분을 안함.
						JoinRespDto joinRespDto = gson.fromJson(responseDto.getBody(), JoinRespDto.class);
						
//						Client.getInstance().getChatArea().append(joinRespDto.getWelcomeMessage() + "\n");
						Client.getInstance().getRoomListModel().clear();
						Client.getInstance().getRoomListModel().addElement("[제목]:");
						Client.getInstance().getRoomListModel().addAll(joinRespDto.getConnectedUsers());
						Client.getInstance().getUserList().setSelectedIndex(0);
						
						
						break;
					case "sendMessage":
						MessageRespDto messageRespDto = gson.fromJson(responseDto.getBody(), MessageRespDto.class);
						Client.getInstance().getChatArea().append(messageRespDto.getMessageValue() + "\n");
						break;
					case "plus":
						RoomRespDto roomRespDto = gson.fromJson(responseDto.getBody(), RoomRespDto.class);
						
						Client.getInstance().getChatArea().append(requset);
						
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
