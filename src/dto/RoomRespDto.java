package dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoomRespDto {
	private String createRoomMessage;
	private List<String> createdRoomList;
	private List<String> kingUsersList;

}