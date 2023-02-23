package server;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Room {
	
	private String kingname;
	private String roomname;
	private List<ConnectedSocket> users;
	
	public Room(String kingname, String roomname) {
		this.kingname = kingname;
		this.roomname = roomname;
		this.users = new ArrayList<>();
	}
//	
//	public void addRoomList(Socket socket) {
//		roomList.add(socket);
//    }
//     
//    public void removeRoomList(Socket socket) {
//    	roomList.remove(socket);
//    }
//    
//    public void removeAllRoomList() {
//    	roomList.removeAll(roomList);
//    }
//     
//    public void broadcast(ResponseDto responseDto) throws IOException {
//        
//    	for(Socket socket : roomList) {
//    			
//                OutputStream outputStream = socket.getOutputStream();
//                PrintWriter out = new PrintWriter(outputStream, true);
//                out.println(gson.toJson(responseDto));
//                System.out.println(responseDto);
//        }
//    }

}
