/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.domain.impl;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import net.nan21.dnet.core.api.model.IModelWithClientId;
import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.domain.impl.AbstractEntity;

import org.hibernate.validator.constraints.NotBlank;

@MappedSuperclass
public abstract class AbstractBase extends AbstractEntity implements
		IModelWithClientId {

	private static final long serialVersionUID = -8865917134914502125L;

	@NotBlank
	@Column(name = "CLIENTID", nullable = false, length = 64)
	private String clientId;

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
		this.clientId = Session.user.get().getClient().getId();
	}

	@PreUpdate
	public void preUpdate() {
		this.__validate_client_context__(this.clientId);
	}

	protected void __validate_client_context__(String clientId) {
		if (clientId != null
				&& !Session.user.get().getClient().getId().equals(clientId)) {
			throw new RuntimeException(
					"Client conflict detected. You are trying to work with an entity which belongs to client with id=`"
							+ clientId
							+ "` but the current session is connected to client with id=`"
							+ Session.user.get().getClient().getId() + "` ");
		}
	}
}
