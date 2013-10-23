/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

import java.io.Serializable;
import java.util.List;

public class DnetUserProfile implements IUserProfile, Serializable {

	private static final long serialVersionUID = -9131543374115237340L;
	private final boolean administrator;
	private final List<String> roles;

	private final boolean credentialsExpired;
	private final boolean accountExpired;
	private final boolean accountLocked;

	public DnetUserProfile(boolean administrator, List<String> roles,
			boolean credentialsExpired, boolean accountExpired,
			boolean accountLocked) {
		super();
		this.administrator = administrator;
		this.roles = roles;
		this.credentialsExpired = credentialsExpired;
		this.accountExpired = accountExpired;
		this.accountLocked = accountLocked;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public List<String> getRoles() {
		return roles;
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

}
