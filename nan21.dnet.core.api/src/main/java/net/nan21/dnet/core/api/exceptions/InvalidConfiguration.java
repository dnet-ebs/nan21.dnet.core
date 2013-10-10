/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.exceptions;

public class InvalidConfiguration extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidConfiguration(String message) {
		super(message);
	}

	public InvalidConfiguration(String message, Exception e) {
		super(message, e);
	}
}
