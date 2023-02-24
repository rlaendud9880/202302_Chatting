package dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JoinRoomRespDto {
	private String roomname;
	private String username;
}
