/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.business.service.entity;

import net.nan21.dnet.core.api.service.business.IEntityService;
import net.nan21.dnet.core.api.service.business.IEntityServiceFactory;
import net.nan21.dnet.core.business.AbstractApplicationContextAware;

public class EntityServiceFactory extends AbstractApplicationContextAware
		implements IEntityServiceFactory {

	@Override
	public <E> IEntityService<E> create(String key) {
		@SuppressWarnings("unchecked")
		IEntityService<E> s = (IEntityService<E>) this.getApplicationContext()
				.getBean(key);
		return s;
	}

	public <E> IEntityService<E> create(Class<E> type) {
		@SuppressWarnings("unchecked")
		IEntityService<E> s = (IEntityService<E>) this.getApplicationContext()
				.getBean(type);
		return s;
	}
}
