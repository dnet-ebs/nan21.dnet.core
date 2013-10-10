/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.domain.impl;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.model.IModelWithId;
import org.eclipse.persistence.annotations.ReadOnly;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = TempAsgn.TABLE_NAME)
@ReadOnly
public class TempAsgn implements IModelWithId, Serializable {

	public static final String TABLE_NAME = "TEMP_ASGN";

	private static final long serialVersionUID = -8865917134914502125L;

	@Id
	@GeneratedValue(generator = Constants.UUID_GENERATOR_NAME)
	@NotBlank
	@Column(name = "ID", nullable = false, length = 64)
	private String id;

	@NotBlank
	@Column(name = "ASGN", nullable = false, length = 255)
	private String asgn;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAsgn() {
		return this.asgn;
	}

	public void setAsgn(String asgn) {
		this.asgn = asgn;
	}

	@PrePersist
	public void prePersist() {
	}
}
