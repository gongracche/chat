package chat;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class ChatApi {

	@Path("/id")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getRoomDescriptor() {
		return UUID.randomUUID().toString();
	}

}
