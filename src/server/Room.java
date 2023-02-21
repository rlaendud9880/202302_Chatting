package server;

import java.util.List;

import lombok.Data;

@Data
public class Room {
	private String kingUser;
	private String roomName;
	private List<String> connecteUsers;
}
