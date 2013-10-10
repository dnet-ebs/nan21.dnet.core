/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.domain.impl;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import net.nan21.dnet.core.domain.impl.AbstractAuditable;
import org.hibernate.validator.constraints.NotBlank;

@MappedSuperclass
public abstract class AbstractType extends AbstractAuditable {

	private static final long serialVersionUID = -8865917134914502125L;

	@NotBlank
	@Column(name = "NAME", nullable = false, length = 255)
	private String name;

	@Column(name = "DESCRIPTION", length = 400)
	private String description;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
