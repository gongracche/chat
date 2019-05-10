package chat;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/rooms")
public class RoomChatApi {

	@Inject
	RoomManager roomManager;

	@Path("/descriptors")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String>getRoomDescriptors() {
		return roomManager.getRoomDescriptors();
	}

	@Path("/descriptor")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getRoomDescriptor() {
		return UUID.randomUUID().toString();
	}

}
