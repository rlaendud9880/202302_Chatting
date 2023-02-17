package dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExitRespDto {
	private String exitMessage;
	private List<String> connectedUsers;
}
