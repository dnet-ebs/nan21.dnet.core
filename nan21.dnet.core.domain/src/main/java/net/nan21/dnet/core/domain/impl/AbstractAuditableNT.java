/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.domain.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.domain.impl.AbstractBaseNT;

import org.hibernate.validator.constraints.NotBlank;

@MappedSuperclass
public abstract class AbstractAuditableNT extends AbstractBaseNT {

	private static final long serialVersionUID = -8865917134914502125L;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDAT", nullable = false)
	private Date createdAt;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIEDAT", nullable = false)
	private Date modifiedAt;

	@NotBlank
	@Column(name = "CREATEDBY", nullable = false, length = 32)
	private String createdBy;

	@NotBlank
	@Column(name = "MODIFIEDBY", nullable = false, length = 32)
	private String modifiedBy;

	@NotNull
	@Column(name = "ACTIVE", nullable = false)
	private Boolean active;

	@Column(name = "NOTES", length = 4000)
	private String notes;

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
		this.createdAt = new Date();
		this.modifiedAt = new Date();
		this.createdBy = Session.user.get().getCode();
		this.modifiedBy = Session.user.get().getCode();
		if (this.getActive() == null) {
			this.active = new Boolean(false);
		}
	}

	@PreUpdate
	public void preUpdate() {
		this.modifiedAt = new Date();
		this.modifiedBy = Session.user.get().getCode();
	}
}
