/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.security;

import java.util.ArrayList;
import java.util.List;

public class SystemAdministratorUsers {

	private List<SystemAdministratorUser> users;

	public List<SystemAdministratorUser> getUsers() {
		return users;
	}

	public void setUsers(List<SystemAdministratorUser> users) {
		this.users = users;
	}

	public void addUser(SystemAdministratorUser user) {
		if (this.users == null) {
			this.users = new ArrayList<SystemAdministratorUser>();
		}
		this.users.add(user);
	}

	public SystemAdministratorUser findByUserName(String username) {
		for (SystemAdministratorUser u : this.users) {
			if (u.getLoginName().equals(username)) {
				return u;
			}
		}
		return null;
	}
}
