package net.nan21.dnet.core.api.service;

import java.util.Date;
import java.util.Map;

import net.nan21.dnet.core.api.exceptions.BusinessException;
import net.nan21.dnet.core.api.exceptions.InvalidDatabase;

public interface ISysParamValueProvider {

	public Map<String, String> getParamValues(Date validAt)
			throws BusinessException, InvalidDatabase;
}
