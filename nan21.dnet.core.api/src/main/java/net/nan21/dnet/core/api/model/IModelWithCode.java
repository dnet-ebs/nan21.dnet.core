package net.nan21.dnet.core.api.model;

/**
 * Interface implemented by all entities which have code and name fields. Based
 * on this interface, the framework can automatically allocate code based on the
 * code allocation mode specified by the entity.
 * 
 * The possible modes are specified in {@link Constants} with the
 * ENTITY_CODE_xxx options
 * 
 * It is automatically inherited by all entities which extend
 * AbstractTypeWithCode entities.
 * 
 * @author amathe
 * 
 */
public interface IModelWithCode {

	public int _code_allocation_mode();

	public String getCode();

	public void setCode(String code);

	public String getName();

	public void setName(String name);
}
