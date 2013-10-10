/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.domain.impl;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = TempAsgnLine.TABLE_NAME)
@ReadOnly
public class TempAsgnLine implements Serializable {

	public static final String TABLE_NAME = "TEMP_ASGN_LINE";

	private static final long serialVersionUID = -8865917134914502125L;

	@Id
	@NotBlank
	@Column(name = "ITEMID", nullable = false, length = 64)
	private String itemId;

	@Id
	@NotBlank
	@Column(name = "SELECTION_ID", nullable = false, length = 64)
	private String selectionId;

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSelectionId() {
		return this.selectionId;
	}

	public void setSelectionId(String selectionId) {
		this.selectionId = selectionId;
	}

	@PrePersist
	public void prePersist() {
	}
}
