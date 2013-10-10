/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.business.service.job;

import net.nan21.dnet.core.api.service.job.IJob;
import net.nan21.dnet.core.api.service.job.IJobFactory;
import net.nan21.dnet.core.business.AbstractApplicationContextAware;

public class BusinessJobFactory extends AbstractApplicationContextAware
		implements IJobFactory {

	@Override
	public IJob create(String key) {
		IJob s = this.getApplicationContext().getBean(key, IJob.class);
		return s;
	}

}
