/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter;

import net.nan21.dnet.core.api.ISettings;
import net.nan21.dnet.core.api.service.business.IEntityService;
import net.nan21.dnet.core.presenter.service.ServiceLocator;

public abstract class AbstractPresenterBase extends
		AbstractApplicationContextAware {

	/**
	 * System configuration. May be null, use the getter.
	 */
	private ISettings settings;

	/**
	 * Presenter level service locator
	 */
	private ServiceLocator serviceLocator;

	/**
	 * Get presenter service locator. If it is null attempts to retrieve it from
	 * Spring context.
	 * 
	 * @return
	 */
	public ServiceLocator getServiceLocator() {
		if (this.serviceLocator == null) {
			this.serviceLocator = this.getApplicationContext().getBean(
					ServiceLocator.class);
		}
		return serviceLocator;
	}

	/**
	 * Set presenter service locator.
	 * 
	 * @param serviceLocator
	 */
	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	/**
	 * Settings getter. If null, attempts to resolve it from spring context.
	 * 
	 * @return
	 */
	public ISettings getSettings() {
		if (this.settings == null) {
			this.settings = this.getApplicationContext().getBean(
					ISettings.class);
		}
		return settings;
	}

	/**
	 * Settings setter
	 * 
	 * @param settings
	 */
	public void setSettings(ISettings settings) {
		this.settings = settings;
	}

	/**
	 * Lookup an entity service.
	 * 
	 * @param <E>
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> IEntityService<T> findEntityService(Class<T> entityClass)
			throws Exception {
		return this.getServiceLocator().findEntityService(entityClass);
	}
}
