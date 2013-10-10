/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api;

import java.util.List;

import net.nan21.dnet.core.api.descriptor.ISysParamDefinitions;
import net.nan21.dnet.core.api.exceptions.InvalidConfiguration;

public interface ISettings {

	public String get(String key);

	public boolean getAsBoolean(String key);

	public String getParam(String paramName) throws InvalidConfiguration;

	public boolean getParamAsBoolean(String paramName)
			throws InvalidConfiguration;

	/**
	 * Reload the parameter definitions.
	 */
	public void reloadParams() throws Exception;

	/**
	 * Reload the parameter values for the current client.
	 */
	public void reloadParamValues() throws Exception;

	public List<ISysParamDefinitions> getParamDefinitions();

	public String getProductName();

	public String getProductVersion();

}
