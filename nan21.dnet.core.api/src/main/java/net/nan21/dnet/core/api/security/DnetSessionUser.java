package net.nan21.dnet.core.api.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class DnetSessionUser implements ISessionUser, Serializable {

	private static final long serialVersionUID = 690259452784508198L;

	private final IUser user;
	private final String userAgent;
	private final Date loginDate;
	private final String remoteHost;
	private final String remoteIp;
	private boolean sessionLocked;

	private List<GrantedAuthority> authorities;

	public DnetSessionUser(IUser user, String userAgent, Date loginDate,
			String remoteHost, String remoteIp) {
		super();
		this.user = user;
		this.userAgent = userAgent;
		this.loginDate = loginDate;
		this.remoteHost = remoteHost;
		this.remoteIp = remoteIp;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.authorities == null) {
			this.authorities = new ArrayList<GrantedAuthority>();
			for (String role : this.user.getProfile().getRoles()) {
				this.authorities.add(new SimpleGrantedAuthority(role));
			}
		}
		return this.authorities;
	}

	public String getPassword() {
		return new String(user.getPassword());
	}

	public String getUsername() {
		return user.getLoginName();
	}

	public boolean isAccountNonExpired() {
		return !this.user.getProfile().isAccountExpired();
	}

	public boolean isAccountNonLocked() {
		return !this.user.getProfile().isAccountLocked();
	}

	public boolean isCredentialsNonExpired() {
		return !this.user.getProfile().isCredentialsExpired();
	}

	public boolean isEnabled() {
		IUserProfile p = this.user.getProfile();
		return !p.isAccountExpired() && !p.isAccountLocked()
				&& !p.isCredentialsExpired();
	}

	public IUser getUser() {
		return this.user;
	}

	public String getUserAgent() {
		return this.userAgent;
	}

	public Date getLoginDate() {
		return this.loginDate;
	}

	public String getRemoteHost() {
		return this.remoteHost;
	}

	public String getRemoteIp() {
		return this.remoteIp;
	}

	public boolean isSessionLocked() {
		return sessionLocked;
	}

	public void setSessionLocked(boolean sessionLocked) {
		this.sessionLocked = sessionLocked;
	}

}
