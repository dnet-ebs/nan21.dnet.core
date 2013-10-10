/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.security;

import java.util.Date;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.ISettings;
import net.nan21.dnet.core.api.exceptions.InvalidConfiguration;
import net.nan21.dnet.core.api.security.DnetClient;
import net.nan21.dnet.core.api.security.DnetWorkspace;
import net.nan21.dnet.core.api.security.IAuthenticationSystemUserService;
import net.nan21.dnet.core.api.security.IClient;
import net.nan21.dnet.core.api.security.ISessionUser;
import net.nan21.dnet.core.api.security.IUser;
import net.nan21.dnet.core.api.security.IUserProfile;
import net.nan21.dnet.core.api.security.IUserSettings;
import net.nan21.dnet.core.api.security.IWorkspace;
import net.nan21.dnet.core.api.security.LoginParams;
import net.nan21.dnet.core.api.security.LoginParamsHolder;
import net.nan21.dnet.core.api.security.DnetSessionUser;
import net.nan21.dnet.core.api.security.DnetUser;
import net.nan21.dnet.core.api.security.DnetUserProfile;
import net.nan21.dnet.core.api.security.DnetUserSettings;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Authenticates a system user from the {@link SystemAdministratorUsers}
 * 
 * @author amathe
 * 
 */
public class DefaultAuthenticationSystemUserService implements
		IAuthenticationSystemUserService {

	private SystemAdministratorUsers repository;
	private ISettings settings;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		SystemAdministratorUser u = this.repository.findByUserName(username);
		LoginParams lp = LoginParamsHolder.params.get();

		IClient client = new DnetClient(null, null, null);
		IUserSettings settings;
		try {
			settings = DnetUserSettings.newInstance(this.settings);
		} catch (InvalidConfiguration e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
		IUserProfile profile = new DnetUserProfile(true, u.getRoles(), false,
				false, false);

		String workspacePath = this.getSettings().get(Constants.PROP_WORKSPACE);

		IWorkspace ws = new DnetWorkspace(workspacePath);

		IUser user = new DnetUser(u.getCode(), u.getName(), u.getLoginName(),
				u.getPassword(), null, null, client, settings, profile, ws,
				true);

		ISessionUser su = new DnetSessionUser(user, lp.getUserAgent(),
				new Date(), lp.getRemoteHost(), lp.getRemoteIp());
		return su;
	}

	public SystemAdministratorUsers getRepository() {
		return repository;
	}

	public void setRepository(SystemAdministratorUsers repository) {
		this.repository = repository;
	}

	public ISettings getSettings() {
		return settings;
	}

	public void setSettings(ISettings settings) {
		this.settings = settings;
	}

}
