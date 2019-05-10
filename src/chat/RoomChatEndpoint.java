package chat;

import java.io.IOException;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws/rooms/{room-descriptor}")
public class RoomChatEndpoint {

	@Inject
	RoomManager roomManager;

	@OnOpen
	public void onOpen(@PathParam("room-descriptor") final String pRoomDescriptor, final Session pSession) {
		this.roomManager.addSession(pRoomDescriptor, pSession);
	}

	@OnMessage
    public void onMessage(@PathParam("room-descriptor") final String pRoomDescriptor, final String pText) {
		for (final Session session : this.roomManager.getSessions(pRoomDescriptor)) {
            try {
                session.getBasicRemote().sendText("Re: " + pText);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

	@OnError
    public void onError(Throwable pError) {
		pError.printStackTrace();
    }

	@OnClose
    public void onClose(@PathParam("room-descriptor") final String pRoomDescriptor, final Session pSession) {
		this.roomManager.removeSession(pRoomDescriptor, pSession);
    }

}
