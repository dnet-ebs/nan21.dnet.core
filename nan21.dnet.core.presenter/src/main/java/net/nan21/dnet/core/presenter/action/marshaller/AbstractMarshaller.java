/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.action.marshaller;

public class AbstractMarshaller<M, F, P> {

	protected Class<M> modelClass;
	protected Class<F> filterClass;
	protected Class<P> paramClass;

	protected Class<M> getModelClass() {
		return this.modelClass;
	}

	public Class<F> getFilterClass() {
		return filterClass;
	}

	protected Class<P> getParamClass() {
		return this.paramClass;
	}
}
