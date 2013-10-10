/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

public interface IChangePasswordService {

	public void doChangePassword(String userCode, String newPassword,
			String oldPassword, String clientId, String clientCode)
			throws Exception;
}
