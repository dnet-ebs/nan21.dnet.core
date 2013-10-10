package net.nan21.dnet.core.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistableLog implements IPersistableLog {

	private Map<String, Object> properties = new HashMap<String, Object>();

	private List<IPersistableLogMessage> messages = new ArrayList<IPersistableLogMessage>();

	@Override
	public void info(String message) {
		this.addMessage(IPersistableLogMessage.INFO, message);
	}

	@Override
	public void error(String message) {
		this.addMessage(IPersistableLogMessage.ERROR, message);
	}

	@Override
	public void warning(String message) {
		this.addMessage(IPersistableLogMessage.WARNING, message);
	}

	private void addMessage(String type, String message) {
		this.messages.add(new PersistableLogMessage(type, message));
	}

	@Override
	public Object getProperty(String key) {
		return this.properties.get(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		this.properties.put(key, value);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public List<IPersistableLogMessage> getMessages() {
		return messages;
	}

}
