package net.nan21.dnet.core.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.nan21.dnet.core.api.security.IAuthenticationClientUserService;

/**
 * Dummy class to provide a default client-user authentication service. It
 * simply refuses authentication as in this stage doesn't know how to do it.
 * This class is necessary however to be exported as a service only at a bare
 * core framework runtime when there is no real client-user authentication
 * service provided.
 * 
 * The security configuration in core.web expects a system-user authentication
 * as well as an client-user authentication.
 * 
 * The real client-user authentication is implemented in the administration
 * module.
 * 
 * @author amathe
 * 
 */
public class DefaultAuthenticationClientUserService implements
		IAuthenticationClientUserService {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		throw new UsernameNotFoundException(
				"The default client-user authentication service is used. Replace it with a real implementation.");
	}

}
