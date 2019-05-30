package chat;

import java.io.IOException;
import java.util.Calendar;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/ws/room/{room-descriptor}", decoders = ChatDecoder.class, encoders = ChatEncoder.class)
public class ChatRoomEndpoint {

	@Inject
	ChatRoomManager roomManager;

	@OnOpen
	public void onOpen(@PathParam("room-descriptor") final String descrptor, final Session session) {
		this.roomManager.getRoom(descrptor).addSession(session);
	}

	@OnMessage
    public void onMessage(@PathParam("room-descriptor") final String descrptor, final ChatClientMessage request, final Session session) {
        final ChatServerMessage ret = new ChatServerMessage();
        ret.setDate(Calendar.getInstance().getTime());
        ret.setMessage(request.getMessage());
        ret.setSenderSessionId(session.getId());

		for (final Session session_ : this.roomManager.getRoom(descrptor).getSessions()) {
            try {
                session_.getBasicRemote().sendObject(ret);
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
				e.printStackTrace();
			}
        }
    }

	@OnError
    public void onError(Throwable pError) {
		pError.printStackTrace();
    }

	@OnClose
    public void onClose(@PathParam("room-descriptor") final String descriptor, final Session session) {
		this.roomManager.getRoom(descriptor).removeSession(session);
    }

}
