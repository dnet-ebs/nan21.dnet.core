/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.domain.impl;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.persistence.Version;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.model.IModelWithId;

import org.hibernate.validator.constraints.NotBlank;

@MappedSuperclass
public abstract class AbstractEntity implements IModelWithId, Serializable {

	private static final long serialVersionUID = -8865917134914502125L;

	@Id
	@GeneratedValue(generator = Constants.UUID_GENERATOR_NAME)
	@NotBlank
	@Column(name = "ID", nullable = false, length = 64)
	private String id;

	@NotBlank
	@Column(name = "REFID", nullable = false, length = 64)
	private String refid;

	@Version
	@Column(name = "VERSION")
	private Long version;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Transient
	public String getEntityAlias() {
		return this.getClass().getSimpleName();
	}

	@Transient
	public String getEntityFqn() {
		return this.getClass().getCanonicalName();
	}

	@PrePersist
	public void prePersist() {
		if (this.refid == null || this.refid.equals("")) {
			this.refid = UUID.randomUUID().toString().toUpperCase();
		}
	}
}
