package net.nan21.dnet.core.presenter.model;

import net.nan21.dnet.core.api.annotation.DsField;
import net.nan21.dnet.core.api.model.IModelWithId;
import net.nan21.dnet.core.presenter.model.AbstractDsModel;

public class AbstractTypeWithCodeNTLov<E> extends AbstractDsModel<E> implements
		IModelWithId {

	public static final String f_id = "id";
	public static final String f_code = "code";
	public static final String f_name = "name";
	public static final String f_active = "active";

	@DsField
	protected String id;

	@DsField
	protected String code;

	@DsField
	protected String name;

	@DsField
	protected Boolean active;

	public AbstractTypeWithCodeNTLov() {
		super();
	}

	public AbstractTypeWithCodeNTLov(E e) {
		super(e);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;

	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

}
