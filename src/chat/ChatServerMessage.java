package chat;

import java.util.Date;

public class ChatServerMessage {
	private Date date;
	private String message;
	private String senderSessionId;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenderSessionId() {
		return senderSessionId;
	}

	public void setSenderSessionId(String senderSessionId) {
		this.senderSessionId = senderSessionId;
	}
}
