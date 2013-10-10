/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

import java.util.List;

public interface IUserProfile {

	public boolean isAdministrator();

	public List<String> getRoles();

	public boolean isCredentialsExpired();

	public boolean isAccountExpired();

	public boolean isAccountLocked();

}