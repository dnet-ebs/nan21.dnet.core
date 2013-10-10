/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller.data;

import net.nan21.dnet.core.api.Constants;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = Constants.CTXPATH_ASGN + "/{resourceName}.{dataFormat}")
public class DefaultAsgnController<M, F, P> extends
		AbstractAsgnController<M, F, P> {

}
