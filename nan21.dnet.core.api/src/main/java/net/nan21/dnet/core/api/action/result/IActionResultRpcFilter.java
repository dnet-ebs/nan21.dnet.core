/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.action.result;

public interface IActionResultRpcFilter extends IActionResult {

	public Object getData();

	public void setData(Object data);

	public Object getParams();

	public void setParams(Object params);

}
