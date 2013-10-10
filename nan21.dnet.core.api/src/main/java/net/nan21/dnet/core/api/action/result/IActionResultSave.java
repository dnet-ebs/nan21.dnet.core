/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.action.result;

import java.util.List;

public interface IActionResultSave extends IActionResult {

	public List<?> getData();

	public void setData(List<?> data);

	public Object getParams();

	public void setParams(Object params);

}
