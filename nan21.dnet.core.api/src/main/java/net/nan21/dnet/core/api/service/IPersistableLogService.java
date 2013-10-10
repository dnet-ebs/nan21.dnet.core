package net.nan21.dnet.core.api.service;

import net.nan21.dnet.core.api.exceptions.BusinessException;

public interface IPersistableLogService {

	public static final String PL_TYPE_JOB = "job";

	public String getType();

	public String insert(IPersistableLog log) throws BusinessException;
}
