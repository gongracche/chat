package chat;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatEncoder implements Encoder.Text<ChatServerMessage>{

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig arg0) {
	}

	@Override
	public String encode(ChatServerMessage arg0) throws EncodeException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(arg0);
		} catch (JsonProcessingException e) {
			throw new EncodeException(arg0, e.getLocalizedMessage(), e);
		}
	}

}
