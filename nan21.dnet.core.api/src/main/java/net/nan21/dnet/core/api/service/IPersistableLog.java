package net.nan21.dnet.core.api.service;

import java.util.List;
import java.util.Map;

public interface IPersistableLog {

	public void info(String message);

	public void error(String message);

	public void warning(String message);

	public void setProperty(String key, Object value);

	public Object getProperty(String key);

	public Map<String, Object> getProperties();

	public List<IPersistableLogMessage> getMessages();

}
