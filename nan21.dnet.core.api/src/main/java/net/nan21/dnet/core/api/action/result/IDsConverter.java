/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.action.result;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface to be implemented by a view-object entity converter
 * 
 * @author amathe
 * 
 * @param <M>
 * @param <E>
 */
public interface IDsConverter<M, E> {

	public void modelToEntity(M m, E e, boolean isInsert, EntityManager em)
			throws Exception;

	// public void entityToModel(E e, M m, EntityManager em) throws Exception;

	public void entityToModel(E e, M m, EntityManager em,
			List<String> fieldNames) throws Exception;

	public List<M> entitiesToModels(List<E> entities, EntityManager em,
			List<String> fieldNames) throws Exception;

}
