package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JoinRoomReqDto {
	private String roomname;
	private String username;
}
