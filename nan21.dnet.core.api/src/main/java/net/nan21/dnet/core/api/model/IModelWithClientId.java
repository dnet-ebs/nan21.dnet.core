/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.model;

/**
 * Interface to implement by all domain entities except the Client domain entity.
 * A client is an isolated self-contained environment.
 * @author AMATHE
 *
 */
public interface IModelWithClientId {
	public String getClientId();
	public void setClientId(String clientId);
} 
