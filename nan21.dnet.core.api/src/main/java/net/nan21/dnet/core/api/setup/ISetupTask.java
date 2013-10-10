/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.setup;
 
import java.util.List;
import java.util.Map;

public interface ISetupTask {

	public String getId();	 
	public String getTitle();
	public String getDescription();
	public List<ISetupTaskParam> getParams();
	
	public void setParamValues(Map<String, Object> values);
} 
