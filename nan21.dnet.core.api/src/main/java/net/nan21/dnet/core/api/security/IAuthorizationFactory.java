/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

public interface IAuthorizationFactory {

	/**
	 * Return an assignment authorization service.
	 * 
	 * @return
	 */
	public IAuthorization getAsgnAuthorizationProvider();

	/**
	 * Return a data-source authorization service.
	 * 
	 * @return
	 */
	public IAuthorization getDsAuthorizationProvider();

	/**
	 * Return a job authorization service.
	 * 
	 * @return
	 */
	public IAuthorization getJobAuthorizationProvider();
}
