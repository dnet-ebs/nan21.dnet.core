/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.service.ds;

import net.nan21.dnet.core.api.service.presenter.IDsService;
import net.nan21.dnet.core.presenter.model.AbstractDsModel;

/**
 * Default base class for an entity-ds service. It can be exposed as an
 * entity-ds presenter service in case the standard functionality is
 * appropriate.
 * 
 * Consider implementing your own custom service which extends
 * {@link AbstractEntityDsService} to customize standard behavior through the
 * provided template methods or necessary overrides.
 * 
 * @author amathe
 * 
 * @param <M>
 * @param <F>
 * @param <P>
 * @param <E>
 */
public class DefaultEntityDsService<M extends AbstractDsModel<E>, F, P, E>
		extends AbstractEntityDsService<M, F, P, E> implements
		IDsService<M, F, P> {

}
