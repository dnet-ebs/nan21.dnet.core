/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.action.result;

/**
 * Root interface of the result types
 * 
 * @author amathe
 * 
 */
public interface IActionResult {
	/**
	 * Get the total execution time in milliseconds.
	 * 
	 * @return
	 */
	public long getExecutionTime();

	/**
	 * Set the total execution time in milliseconds.
	 * 
	 * @param executionTime
	 */
	public void setExecutionTime(long executionTime);
}
