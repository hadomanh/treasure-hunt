package ngakinz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ngakinz.enums.MessageHeader;

@Data
@AllArgsConstructor
public class Request {
	
	private MessageHeader header;
	
	private String message;

}
