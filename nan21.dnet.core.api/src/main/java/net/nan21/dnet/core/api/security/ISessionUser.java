/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Session user object which contains intrinsic user data and session related
 * information (connection time, remote address, etc)
 * 
 * @author amathe
 * 
 */
public interface ISessionUser extends UserDetails {

	public IUser getUser();

	public String getUserAgent();

	public Date getLoginDate();

	public String getRemoteHost();

	public String getRemoteIp();

	public boolean isSessionLocked();

}
