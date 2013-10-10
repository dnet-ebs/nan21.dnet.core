/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.business;

public interface IEntityServiceFactory {
	public <E> IEntityService<E> create(String key);
	public <E> IEntityService<E> create(Class<E> type);
}
