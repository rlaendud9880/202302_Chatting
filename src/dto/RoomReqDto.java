package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoomReqDto {
	private String roomName;
	private String kingUser;
}
