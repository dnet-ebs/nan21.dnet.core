/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.model;

/**
 * Interface to be implemented by all models(entities and view-objects) 
 * which have an <code>id</code> primary key.   
 * @author amathe
 *
 */
public interface IModelWithId {
	public String getId();	
	public void setId(String id);	
}
