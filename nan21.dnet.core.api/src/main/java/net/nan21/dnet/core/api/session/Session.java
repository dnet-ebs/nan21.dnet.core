/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.session;

import net.nan21.dnet.core.api.security.IUser;

public class Session {

	public static ThreadLocal<IUser> user = new ThreadLocal<IUser>();

}
