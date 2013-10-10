package net.nan21.dnet.core.presenter.model;

import java.util.Date;

import net.nan21.dnet.core.api.annotation.DsField;
import net.nan21.dnet.core.api.model.IModelWithId;
import net.nan21.dnet.core.presenter.model.AbstractDsModel;

public class AbstractTypeNTDs<E> extends AbstractDsModel<E> implements
		IModelWithId {

	public static final String f_id = "id";
	public static final String f_name = "name";
	public static final String f_description = "description";
	public static final String f_active = "active";
	public static final String f_notes = "notes";

	public static final String f_createdAt = "createdAt";
	public static final String f_modifiedAt = "modifiedAt";
	public static final String f_createdBy = "createdBy";
	public static final String f_modifiedBy = "modifiedBy";
	public static final String f_version = "version";
	public static final String f_refid = "refid";
	public static final String f_entityFqn = "entityFqn";
	public static final String f_entityAlias = "entityAlias";

	@DsField
	protected String name;

	@DsField
	protected Boolean active;

	@DsField
	protected String description;

	@DsField
	protected String notes;

	@DsField(noUpdate = true)
	protected String id;

	@DsField(noUpdate = true)
	protected Date createdAt;

	@DsField(noUpdate = true)
	protected Date modifiedAt;

	@DsField(noUpdate = true)
	protected String createdBy;

	@DsField(noUpdate = true)
	protected String modifiedBy;

	@DsField(noUpdate = true)
	protected String refid;

	@DsField
	protected Long version;

	@DsField(noUpdate = true, fetch = false)
	protected String entityAlias;

	@DsField(noUpdate = true, fetch = false)
	protected String entityFqn;

	public AbstractTypeNTDs() {
		super();
	}

	public AbstractTypeNTDs(E e) {
		super(e);
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
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;

	}

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

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getEntityAlias() {
		return entityAlias;
	}

	public void setEntityAlias(String entityAlias) {
		this.entityAlias = entityAlias;
	}

	public String getEntityFqn() {
		return entityFqn;
	}

	public void setEntityFqn(String entityFqn) {
		this.entityFqn = entityFqn;
	}

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}
}
