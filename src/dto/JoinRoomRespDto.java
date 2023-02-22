package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JoinRoomRespDto {
	private String welcomeRoomMessage;
	private String roomname;

}
