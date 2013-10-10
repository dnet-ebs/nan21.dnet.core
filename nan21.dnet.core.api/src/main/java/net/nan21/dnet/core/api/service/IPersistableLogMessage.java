package net.nan21.dnet.core.api.service;

public interface IPersistableLogMessage {

	public static final String INFO = "info";
	public static final String ERROR = "error";
	public static final String WARNING = "warning";

	public String getType();

	public void setType(String type);

	public String getMessage();

	public void setMessage(String message);

}
