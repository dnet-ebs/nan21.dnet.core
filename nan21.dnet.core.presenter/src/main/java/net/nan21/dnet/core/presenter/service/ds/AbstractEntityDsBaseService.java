/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.service.ds;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.action.result.IDsConverter;
import net.nan21.dnet.core.api.action.result.IDsMarshaller;
import net.nan21.dnet.core.api.service.business.IEntityService;
import net.nan21.dnet.core.presenter.action.marshaller.JsonMarshaller;
import net.nan21.dnet.core.presenter.action.marshaller.XmlMarshaller;
import net.nan21.dnet.core.presenter.converter.AbstractDsConverter;
import net.nan21.dnet.core.presenter.converter.DefaultDsConverter;
import net.nan21.dnet.core.presenter.descriptor.DsDescriptor;
import net.nan21.dnet.core.presenter.descriptor.ViewModelDescriptorManager;
import net.nan21.dnet.core.presenter.model.AbstractDsModel;
import net.nan21.dnet.core.presenter.service.AbstractPresenterReadService;

/**
 * Base abstract class for entity based data-source service hierarchy. An
 * entity-data-source(referred to as entity-ds) is a specialized data-source
 * which provides view-model perspective(M) from a given persistence
 * perspective(E). <br>
 * Subclasses implement standard functionality for standard read actions (query,
 * export ), write actions (insert, update, delete, import) and remote procedure
 * call like method invocation (rpc).
 * 
 * Adds to its super-class an entity-type information.
 * 
 * @author amathe
 * 
 * @param <M>
 * @param <F>
 * @param <P>
 * @param <E>
 */
public abstract class AbstractEntityDsBaseService<M extends AbstractDsModel<E>, F, P, E>
		extends AbstractPresenterReadService<M, F, P> {

	/**
	 * Source entity type it works with.
	 */
	private Class<E> entityClass;

	/**
	 * Converter class to be used for entity-to-ds and ds-to-entity conversions.
	 */
	private Class<? extends AbstractDsConverter<M, E>> converterClass;

	/**
	 * DS descriptor.
	 */
	private DsDescriptor<M> descriptor;

	/**
	 * DS <-> Entity converter
	 */
	private AbstractDsConverter<M, E> converter;

	public DsDescriptor<M> getDescriptor() throws Exception {
		if (this.descriptor == null) {
			boolean useCache = this.getSettings()
					.get(Constants.PROP_WORKING_MODE)
					.equals(Constants.PROP_WORKING_MODE_PROD);
			this.descriptor = ViewModelDescriptorManager.getDsDescriptor(
					this.getModelClass(), useCache);
		}
		return descriptor;
	}

	public void setDescriptor(DsDescriptor<M> descriptor) {
		this.descriptor = descriptor;
	}

	public IDsMarshaller<M, F, P> createMarshaller(String dataFormat)
			throws Exception {
		IDsMarshaller<M, F, P> marshaller = null;
		if (dataFormat.equals(IDsMarshaller.JSON)) {
			marshaller = new JsonMarshaller<M, F, P>(this.getModelClass(),
					this.getFilterClass(), this.getParamClass());
		} else if (dataFormat.equals(IDsMarshaller.XML)) {
			marshaller = new XmlMarshaller<M, F, P>(this.getModelClass(),
					this.getFilterClass(), this.getParamClass());
		}
		return marshaller;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<? extends AbstractDsConverter<M, E>> getConverterClass() {
		return converterClass;
	}

	public void setConverterClass(
			Class<? extends AbstractDsConverter<M, E>> converterClass) {
		this.converterClass = converterClass;
	}

	@SuppressWarnings("unchecked")
	protected IDsConverter<M, E> getConverter() throws Exception {

		if (this.converter != null) {
			return this.converter;
		}

		if (this.converterClass != null) {
			this.converter = this.converterClass.newInstance();
		} else {
			this.converter = DefaultDsConverter.class.newInstance();
		}

		this.converter.setApplicationContext(this.getApplicationContext());
		this.converter.setDescriptor(this.getDescriptor());
		this.converter.setEntityClass(this.getEntityClass());
		this.converter.setModelClass(this.getModelClass());
		this.converter.setServiceLocator(this.getServiceLocator());

		return this.converter;
	}

	public IEntityService<E> getEntityService() throws Exception {
		// if (this.entityService == null) {
		return this.findEntityService(this.getEntityClass());
		// }
		// return this.entityService;
	}

}
