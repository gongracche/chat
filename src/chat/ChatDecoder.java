package chat;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatDecoder implements Decoder.Text<ChatClientMessage> {

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig arg0) {
	}

	@Override
	public ChatClientMessage decode(String arg0) throws DecodeException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(arg0, ChatClientMessage.class);
		} catch (IOException e) {
			throw new DecodeException(arg0, e.getMessage(), e);
		}
	}

	@Override
	public boolean willDecode(String arg0) {
		return true;
	}

}
