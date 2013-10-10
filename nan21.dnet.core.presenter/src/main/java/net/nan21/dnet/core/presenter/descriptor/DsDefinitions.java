/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.descriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.nan21.dnet.core.api.descriptor.IDsDefinition;
import net.nan21.dnet.core.api.descriptor.IDsDefinitions;
import net.nan21.dnet.core.api.service.presenter.IAsgnService;
import net.nan21.dnet.core.api.service.presenter.IDsBaseService;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DsDefinitions implements IDsDefinitions, BeanFactoryAware,
		ApplicationContextAware {

	/**
	 * Bean factory reference to read the bean definition from spring context.
	 */
	private BeanFactory beanFactory;

	private ApplicationContext applicationContext;

	@Override
	public boolean containsDs(String name) {
		return this.applicationContext.containsBean(name);
	}

	/**
	 * Get the definition for the specified data-source if it is available in
	 * the current context.
	 */
	@Override
	public IDsDefinition getDsDefinition(String name) throws Exception {
		ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;
		DsDefinition definition = createDefinition(name, factory);
		return definition;
	}

	/**
	 * Get the definitions for all data-sources available in the given context.
	 */
	@Override
	public Collection<IDsDefinition> getDsDefinitions() throws Exception {
		Collection<IDsDefinition> result = new ArrayList<IDsDefinition>();

		ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;

		String[] names = factory.getBeanNamesForType(IDsBaseService.class);
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			DsDefinition definition = createDefinition(name, factory);
			result.add(definition);
		}

		names = factory.getBeanNamesForType(IAsgnService.class);
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			DsDefinition definition = createDefinition(name, factory);
			definition.setAsgn(true);
			result.add(definition);
		}
		return result;
	}

	/**
	 * Helper method to create the definition for a single given data-source.
	 * 
	 * @param name
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	private DsDefinition createDefinition(String name,
			ConfigurableListableBeanFactory factory) throws Exception {

		BeanDefinition beanDef = factory.getBeanDefinition(name);

		DsDefinition definition = new DsDefinition();
		definition.setName(name);

		String modelClass = null;
		String filterClass = null;
		String paramClass = null;

		String readOnly = null;

		MutablePropertyValues mpv = beanDef.getPropertyValues();

		// model class

		modelClass = ((TypedStringValue) mpv.getPropertyValue("modelClass")
				.getValue()).getValue();
		definition.setModelClass(this.applicationContext.getClassLoader()
				.loadClass(modelClass));

		// filter class

		if (mpv.getPropertyValue("filterClass") != null) {
			filterClass = ((TypedStringValue) mpv.getPropertyValue(
					"filterClass").getValue()).getValue();
			definition.setFilterClass(this.applicationContext.getClassLoader()
					.loadClass(filterClass));
		} else {
			definition.setFilterClass(this.applicationContext.getClassLoader()
					.loadClass(modelClass));
		}

		// param-class
		if (mpv.getPropertyValue("paramClass") != null) {
			paramClass = ((TypedStringValue) mpv.getPropertyValue("paramClass")
					.getValue()).getValue();
			definition.setParamClass(this.applicationContext.getClassLoader()
					.loadClass(paramClass));
		}

		// other attributes

		if (mpv.contains("readOnly")) {
			readOnly = ((TypedStringValue) mpv.getPropertyValue("readOnly")
					.getValue()).getValue();
			definition.setReadOnly(Boolean.getBoolean(readOnly));
		}

		// RPC methods

		if (mpv.contains("rpcData")) {
			@SuppressWarnings("unchecked")
			ManagedMap<TypedStringValue, Object> rpcData = (ManagedMap<TypedStringValue, Object>) mpv
					.getPropertyValue("rpcData").getValue();

			List<String> services = new ArrayList<String>();
			for (TypedStringValue tsv : rpcData.keySet()) {
				services.add(tsv.getValue());
			}
			definition.setServiceMethods(services);
		}

		if (mpv.contains("rpcFilter")) {
			@SuppressWarnings("unchecked")
			ManagedMap<TypedStringValue, Object> rpcFilter = (ManagedMap<TypedStringValue, Object>) mpv
					.getPropertyValue("rpcFilter").getValue();
			List<String> services = new ArrayList<String>();
			for (TypedStringValue tsv : rpcFilter.keySet()) {
				services.add(tsv.getValue());
			}
			definition.setServiceMethods(services);
		}
		return definition;
	}

	/**
	 * BeanFactory setter
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * ApplicationContext setter
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
