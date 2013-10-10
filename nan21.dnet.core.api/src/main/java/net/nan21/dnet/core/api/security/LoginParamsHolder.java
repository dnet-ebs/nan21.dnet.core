/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

/**
 * ThreadLocal variable holder used to send extra login parameters to the
 * authentication service.
 */
public class LoginParamsHolder {
	public static ThreadLocal<LoginParams> params = new ThreadLocal<LoginParams>();
}
