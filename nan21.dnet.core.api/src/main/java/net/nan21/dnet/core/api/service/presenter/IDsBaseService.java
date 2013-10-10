/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.presenter;

import net.nan21.dnet.core.api.ISettings;
import net.nan21.dnet.core.api.action.result.IDsMarshaller;

/**
 * @author amathe
 * 
 */
public interface IDsBaseService<M, F, P> {

	public Class<?> getEntityClass();

	public Class<M> getModelClass();

	public Class<F> getFilterClass();

	public Class<P> getParamClass();

	public IDsMarshaller<M, F, P> createMarshaller(String dataFormat)
			throws Exception;

	public ISettings getSettings();

	public void setSettings(ISettings settings);

}
