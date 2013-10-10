package net.nan21.dnet.core.presenter.model;

import net.nan21.dnet.core.api.annotation.DsField;
import net.nan21.dnet.core.api.model.IModelWithClientId;
import net.nan21.dnet.core.api.model.IModelWithId;
import net.nan21.dnet.core.presenter.model.AbstractDsModel;

public class AbstractTypeLov<E> extends AbstractDsModel<E> implements
		IModelWithId, IModelWithClientId {

	public static final String f_id = "id";
	public static final String f_clientId = "clientId";
	public static final String f_name = "name";
	public static final String f_active = "active";

	@DsField
	protected String id;

	@DsField
	protected String clientId;

	@DsField
	protected String name;

	@DsField
	protected String description;

	@DsField
	protected Boolean active;

	public AbstractTypeLov() {
		super();
	}

	public AbstractTypeLov(E e) {
		super(e);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;

	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
