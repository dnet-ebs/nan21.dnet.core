package net.nan21.dnet.core.business.service.job;

import java.util.Map;

import net.nan21.dnet.core.api.service.job.IJob;
import net.nan21.dnet.core.business.service.AbstractBusinessBaseService;

public abstract class AbstractBusinessJob extends AbstractBusinessBaseService
		implements IJob {
	
	private Map<String, Object> contextMap;

	@Override
	public void execute() throws Exception {
		this.onExecute();
	}

	protected abstract void onExecute() throws Exception;

	public Map<String, Object> getContextMap() {
		return contextMap;
	}

	public void setContextMap(Map<String, Object> contextMap) {
		this.contextMap = contextMap;
	}
}
