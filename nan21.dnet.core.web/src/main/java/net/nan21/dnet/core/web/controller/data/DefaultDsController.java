/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller.data;

import net.nan21.dnet.core.api.Constants;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = Constants.CTXPATH_DS + "/{resourceName}.{dataFormat}")
public class DefaultDsController<M, F, P> extends
		AbstractDsWriteController<M, F, P> {

}
