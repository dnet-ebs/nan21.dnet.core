package net.nan21.dnet.core.business.message;

import java.util.Map;

import net.nan21.dnet.core.api.model.IMessageData;

public class MessageData implements IMessageData {

	/**
	 * A key identifying the source of the event. Usually it is a class name,
	 * but can be whatever string.
	 * 
	 */
	private String source;

	/**
	 * An optional information regarding the source of the event, specifying the
	 * action which triggered this event. For example insert or update.
	 */
	private String action;

	/**
	 * Any other information which may be useful for the listeners, for example
	 * a list of ID's.
	 */
	private Map<String, Object> data;

	public MessageData(String source) {
		super();
		this.source = source;
	}

	public MessageData(String source, String action) {
		super();
		this.source = source;
		this.action = action;
	}

	public MessageData(String source, String action, Map<String, Object> data) {
		super();
		this.source = source;
		this.action = action;
		this.data = data;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
