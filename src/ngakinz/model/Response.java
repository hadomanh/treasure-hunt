package ngakinz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ngakinz.enums.MessageHeader;

@Data
@AllArgsConstructor
public class Response {
	
	private int status;
	
	private MessageHeader header;
	
	private String message;

}
