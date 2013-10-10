/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.security;

import net.nan21.dnet.core.api.security.IAuthorization;

public class DummyAuthorizationForAsgn implements IAuthorization {

	public void authorize(String dsName, String action, String rpcMethod)
			throws Exception {
		// If it doesn't throw exception is authorized
	}

}
