package chat;

import java.io.IOException;
import java.util.Calendar;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws", decoders = ChatDecoder.class, encoders = ChatEncoder.class)
public class ChatEndpoint {

	@OnMessage
	public void onMessage(final Session pSession, final ChatClientMessage pRequest) {
        final ChatServerMessage ret = new ChatServerMessage();
        ret.setDate(Calendar.getInstance().getTime());
        ret.setMessage(pRequest.getMessage());
        ret.setSenderSessionId(pSession.getId());

        for(Session other : pSession.getOpenSessions()) {
        	try {
				other.getBasicRemote().sendObject(ret);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
        }
    }
}
