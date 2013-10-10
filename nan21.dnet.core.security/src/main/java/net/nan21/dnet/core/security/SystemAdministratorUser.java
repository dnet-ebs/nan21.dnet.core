/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.security;

import java.util.List;

public class SystemAdministratorUser {

	private final String code;
	private final String name;
	private final String loginName;
	private final String password;

	private List<String> roles;

	public SystemAdministratorUser(String code, String name, String loginName,
			String password) {
		super();
		this.code = code;
		this.name = name;
		this.loginName = loginName;
		this.password = password;

	}

	public String getCode() {
		return code;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
