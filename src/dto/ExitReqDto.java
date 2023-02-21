package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExitReqDto {
	private String username;
	private String roomname;
}
