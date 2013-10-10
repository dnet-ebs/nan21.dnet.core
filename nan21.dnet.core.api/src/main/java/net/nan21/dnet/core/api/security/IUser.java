/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

public interface IUser {

	public String getCode();

	public String getName();

	public String getLoginName();

	public char[] getPassword();

	public void setPassword(char[] password);

	public String getClientId();

	public String getClientCode();

	public String getEmployeeId();

	public String getEmployeeCode();

	public IClient getClient();

	public IUserSettings getSettings();

	public IUserProfile getProfile();

	public IWorkspace getWorkspace();

	public boolean isSystemUser();

}
