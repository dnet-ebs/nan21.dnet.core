/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

import java.io.Serializable;

public class DnetUser implements IUser, Serializable {

	private static final long serialVersionUID = -9131543374115237340L;
	private final String code;
	private final String name;

	private final String loginName;
	private char[] password;

	private final String employeeId;
	private final String employeeCode;

	private final IClient client;
	private final IUserSettings settings;
	private final IUserProfile profile;
	private final IWorkspace workspace;

	private final boolean systemUser;

	public DnetUser(String code, String name, String loginName,
			String password, String employeeId, String employeeCode,
			IClient client, IUserSettings settings, IUserProfile profile,
			IWorkspace workspace, boolean systemUser) {

		super();

		this.code = code;
		this.name = name;

		this.loginName = loginName;
		if (password != null) {
			this.password = password.toCharArray();
		}

		this.employeeId = employeeId;
		this.employeeCode = employeeCode;
		this.client = client;
		this.settings = settings;
		this.profile = profile;
		this.workspace = workspace;
		this.systemUser = systemUser;

	}

	public String getClientId() {
		return this.client.getId();
	}

	public String getClientCode() {
		return this.client.getCode();
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getLoginName() {
		return loginName;
	}

	public IClient getClient() {
		return client;
	}

	public IUserSettings getSettings() {
		return settings;
	}

	public IUserProfile getProfile() {
		return profile;
	}

	public IWorkspace getWorkspace() {
		return workspace;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public boolean isSystemUser() {
		return systemUser;
	}

}
