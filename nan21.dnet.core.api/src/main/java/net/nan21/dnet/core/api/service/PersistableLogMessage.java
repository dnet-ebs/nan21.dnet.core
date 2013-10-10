package net.nan21.dnet.core.api.service;

public class PersistableLogMessage implements IPersistableLogMessage {

	private String type;
	private String message;

	public PersistableLogMessage(String type, String message) {
		super();
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
