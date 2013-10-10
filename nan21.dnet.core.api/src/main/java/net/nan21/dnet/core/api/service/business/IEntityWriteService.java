/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.business;

import java.util.List;
import java.util.Map;

import net.nan21.dnet.core.api.exceptions.BusinessException;

public interface IEntityWriteService<E> extends IEntityReadService<E> {

	/**
	 * Insert a list of new entities. This should be a transactional method.
	 * 
	 * @param list
	 * @throws BusinessException
	 */
	public void insert(List<E> list) throws BusinessException;

	/**
	 * Helper insert method for one single entity. It creates a list with this
	 * single entity and delegates to the <code> insert(List<E> list)</code>
	 * method
	 */
	public void insert(E e) throws BusinessException;

	/**
	 * Update a list of existing entities. This should be a transactional
	 * method.
	 * 
	 * @param list
	 * @throws BusinessException
	 */
	public void update(List<E> list) throws BusinessException;

	/**
	 * Helper update method for one single entity. It creates a list with this
	 * single entity and delegates to the <code> update(List<E> list)</code>
	 * method
	 */
	public void update(E e) throws BusinessException;

	/**
	 * Execute an update JPQL statement. This should be a transactional method.
	 * 
	 * @param jpqlStatement
	 * @param parameters
	 * @return
	 * @throws BusinessException
	 */
	public int update(String jpqlStatement, Map<String, Object> parameters)
			throws BusinessException;

	public void deleteById(Object id) throws BusinessException;

	public void deleteByIds(List<Object> ids) throws BusinessException;

	public void delete(List<E> list) throws BusinessException;

}
