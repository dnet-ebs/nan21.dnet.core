/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.business;

import java.util.List;
import java.util.Map;

import net.nan21.dnet.core.api.exceptions.BusinessException;

/**
 * @author amathe
 * 
 */
public interface IEntityReadService<E> extends IEntityBaseService<E> {

	/**
	 * Retrieve an entity by its ID
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public E findById(Object id);

	/**
	 * Retrieve an entity of the specified class by its ID
	 * 
	 * @param object
	 * @param claz
	 * @return
	 */
	public <T> T findById(Object object, Class<T> claz);

	/**
	 * Retrieve entities which match the given list of IDs.
	 * 
	 * @param ids
	 * @return
	 * @throws BusinessException
	 */
	public List<E> findByIds(List<Object> ids);

	/**
	 * Retrieve entities of the specified class which match the given list of
	 * IDs.
	 * 
	 * @param ids
	 * @param claz
	 * @return
	 */
	public <T> List<T> findByIds(List<Object> ids, Class<T> claz);

	/**
	 * Find entities which match the given list of reference-ids and belong to
	 * the current client.
	 * 
	 * @param ids
	 * @return
	 * @throws BusinessException
	 */
	public List<E> findByRefids(List<Object> refids);

	/**
	 * Find entities of the specified class which match the given list of
	 * reference-ids and belong to the current client.
	 * 
	 * @param refids
	 * @param claz
	 * @return
	 */
	public <T> List<T> findByRefids(List<Object> refids, Class<T> claz);

	/**
	 * Find entity which matches the given reference-id and belongs to the
	 * current client.
	 */
	public E findByRefid(Object refid);

	/**
	 * Find entity of the specified class which matches the given reference-id
	 * and belongs to the current client.
	 * 
	 * @param refid
	 * @param claz
	 * @return
	 * @throws BusinessException
	 */
	public <T> T findByRefid(Object refid, Class<T> claz);

	/**
	 * Find an entity by unique-key.
	 * 
	 * @param namedQueryName
	 *            the associated named query
	 * @param params
	 *            Parameters with values for the unique-key fields.
	 * @return
	 * @throws BusinessException
	 */
	public E findByUk(String namedQueryName, Map<String, Object> params)
			throws BusinessException;

	/**
	 * Find an entity of the specified class by unique-key.
	 * 
	 * @param namedQueryName
	 * @param params
	 * @param claz
	 * @return
	 */
	public <T> T findByUk(String namedQueryName, Map<String, Object> params,
			Class<T> claz);

	/**
	 * Find a list of entities using as filter criteria the specified list of
	 * attribute values.
	 * 
	 * @param params
	 * @return
	 * @throws BusinessException
	 */
	public List<E> findEntitiesByAttributes(Map<String, Object> params);

	/**
	 * Find a list of entities of the specified class using as filter criteria
	 * the specified list of attribute values.
	 * 
	 * @param entityClass
	 * @param params
	 * @return
	 * @throws BusinessException
	 */
	public <T> List<T> findEntitiesByAttributes(Class<T> entityClass,
			Map<String, Object> params);

	/**
	 * Find an <E> entity using as filter criteria the specified list of
	 * attribute values. The filter criteria must uniquely identify an entity.
	 * 
	 * @param params
	 * @return
	 */
	public E findEntityByAttributes(Map<String, Object> params);

	/**
	 * Find an entity of the given type, using as filter criteria the specified
	 * list of attribute values. The filter criteria must uniquely identify an
	 * entity.
	 * 
	 * @param entityClass
	 * @param params
	 * @return
	 * @throws BusinessException
	 */
	public <T> T findEntityByAttributes(Class<T> entityClass,
			Map<String, Object> params);
}
