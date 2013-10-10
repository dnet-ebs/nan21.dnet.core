/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.domain.impl;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import net.nan21.dnet.core.domain.impl.AbstractEntity;

@MappedSuperclass
public abstract class AbstractBaseNT extends AbstractEntity {

	private static final long serialVersionUID = -8865917134914502125L;

	@PrePersist
	public void prePersist() {
		super.prePersist();
	}
}
