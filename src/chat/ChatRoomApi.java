package chat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/room")
public class ChatRoomApi {

	@Inject
	ChatRoomManager roomManager;

	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ChatRoom_>getRooms() {
		List<ChatRoom_> rooms = new ArrayList<>();
		for(ChatRoom room : roomManager.getRooms()) {
			rooms.add(new ChatRoom_(room));
		}
		return rooms;
	}

	@Path("/{descriptor}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ChatRoom getRoom(@PathParam("descriptor") String descriptor) {
		return roomManager.getRoom(descriptor);
	}

	@Path("/create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ChatRoom_ createRoom(@FormParam("name") final String name) {
		String descriptor = UUID.randomUUID().toString();
		ChatRoom room = new ChatRoom(descriptor, name);
		roomManager.addRoom(room);
		return new ChatRoom_(room);
	}

	private class ChatRoom_ {
		private String descriptor;
		private String name;

		public ChatRoom_(ChatRoom room) {
			this.descriptor = room.getDescriptor();
			this.name = room.getName();
		}

		@SuppressWarnings("unused")
		public String getDescriptor() {
			return descriptor;
		}

		@SuppressWarnings("unused")
		public void setDescriptor(String descriptor) {
			this.descriptor = descriptor;
		}

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}

		@SuppressWarnings("unused")
		public void setName(String name) {
			this.name = name;
		}

	}
}
