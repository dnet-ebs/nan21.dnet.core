/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.business.service.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.Assert;

import net.nan21.dnet.core.api.exceptions.BusinessException;
import net.nan21.dnet.core.api.model.IModelWithClientId;
import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.business.service.AbstractBusinessBaseService;

/**
 * Implements the read actions as various finders for an entity-service. See the
 * super-classes for more details.
 * 
 * @author amathe
 * 
 * @param <E>
 */
public abstract class AbstractEntityReadService<E> extends
		AbstractBusinessBaseService {

	public abstract Class<E> getEntityClass();

	/**
	 * Create a new entity instance
	 */
	public E create() throws BusinessException {
		try {
			return this.getEntityClass().newInstance();
		} catch (Exception e) {
			throw new BusinessException("Cannot create a new instance of "
					+ this.getEntityClass().getCanonicalName(), e);
		}
	}

	/**
	 * Find entity by id.
	 */
	public E findById(Object object) {
		return this.getEntityManager().find(getEntityClass(), object);
	}

	/**
	 * Find an entity of the specified class by its ID
	 */
	public <T> T findById(Object object, Class<T> claz) {
		return this.getEntityManager().find(claz, object);
	}

	/**
	 * Find entities by the list of ids..
	 */
	public List<E> findByIds(List<Object> ids) {
		return this.findByIds(ids, this.getEntityClass());
	}

	/**
	 * Find entities of the specified class by the list of ids.
	 * 
	 * @param ids
	 * @param claz
	 * @return
	 */
	public <T> List<T> findByIds(List<Object> ids, Class<T> claz) {
		if (IModelWithClientId.class.isAssignableFrom(claz)) {
			return (List<T>) this
					.getEntityManager()
					.createQuery(
							"select e from "
									+ claz.getSimpleName()
									+ " e where  e.clientId = :clientId and e.id in :ids ",
							claz)
					.setParameter("ids", ids)
					.setParameter("clientId",
							Session.user.get().getClient().getId())
					.getResultList();
		} else {
			return (List<T>) this
					.getEntityManager()
					.createQuery(
							"select e from " + claz.getSimpleName()
									+ " e where e.id in :ids ", claz)
					.setParameter("ids", ids).getResultList();
		}
	}

	/**
	 * Find entities by the list of reference ids
	 */
	public List<E> findByRefids(List<Object> refids) {
		return this.findByRefids(refids, getEntityClass());

	}

	/**
	 * Find entities of the specified class by the list of reference ids
	 * 
	 * @param refids
	 * @param claz
	 * @return
	 */
	public <T> List<T> findByRefids(List<Object> refids, Class<T> claz) {
		if (IModelWithClientId.class.isAssignableFrom(claz)) {
			return (List<T>) this
					.getEntityManager()
					.createQuery(
							"select e from "
									+ claz.getSimpleName()
									+ " e where  e.clientId = :clientId and e.refid in :refids ",
							claz)
					.setParameter("clientId",
							Session.user.get().getClient().getId())
					.setParameter("refids", refids).getResultList();
		} else {
			return (List<T>) this
					.getEntityManager()
					.createQuery(
							"select e from " + claz.getSimpleName()
									+ " e where e.refid in :refids ", claz)
					.setParameter("refids", refids).getResultList();
		}
	}

	/**
	 * Find an entity by its reference-id
	 * 
	 * @param refid
	 * @return
	 */
	public E findByRefid(Object refid) {
		return this.findByRefid(refid, this.getEntityClass());
	}

	/**
	 * Find an entity of the specified class by its reference-id
	 * 
	 * @param refid
	 * @param claz
	 * @return
	 */
	public <T> T findByRefid(Object refid, Class<T> claz) {
		if (IModelWithClientId.class.isAssignableFrom(claz)) {
			return (T) this
					.getEntityManager()
					.createQuery(
							"select e from "
									+ getEntityClass().getSimpleName()
									+ " e where  e.clientId = :clientId and e.refid = :refid ",
							claz)
					.setParameter("clientId",
							Session.user.get().getClient().getId())
					.setParameter("refid", refid).getResultList().get(0);
		} else {
			return (T) this
					.getEntityManager()
					.createQuery(
							"select e from " + getEntityClass().getSimpleName()
									+ " e where e.refid = :refid ", claz)
					.setParameter("refid", refid).getResultList().get(0);
		}
	}

	/**
	 * Find an entity by unique-key name.
	 * 
	 * @param namedQueryName
	 * @param params
	 * @return
	 */
	public E findByUk(String namedQueryName, Map<String, Object> params) {
		return this.findByUk(namedQueryName, params, this.getEntityClass());
	}

	/**
	 * Find an entity of the specified class by its unique-key name.
	 */
	public <T> T findByUk(String namedQueryName, Map<String, Object> params,
			Class<T> claz) {
		TypedQuery<T> q = this.getEntityManager().createNamedQuery(
				namedQueryName, claz);
		Set<String> keys = params.keySet();
		if (IModelWithClientId.class.isAssignableFrom(claz)) {
			q.setParameter("clientId", Session.user.get().getClient().getId());
		}
		for (String key : keys) {
			q.setParameter(key, params.get(key));
		}
		return (T) q.getSingleResult();
	}

	/**
	 * Find an <E> entity using as filter criteria the specified list of
	 * attribute values. The filter criteria must uniquely identify an entity.
	 * 
	 * @param params
	 * @return
	 * @throws BusinessException
	 */
	public E findEntityByAttributes(Map<String, Object> params) {
		return findEntityByAttributes(this.getEntityClass(), params);
	}

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
			Map<String, Object> params) {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		Root<T> root = cq.from(entityClass);
		cq.select(root);
		Assert.notNull(params);
		Predicate p = null;
		if (entityClass.isAssignableFrom(IModelWithClientId.class)) {
			p = cb.equal(root.get("clientId"), Session.user.get().getClient()
					.getId());
		}
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			p = cb.and(cb.equal(root.get(entry.getKey()), entry.getValue()));
		}
		cq.where(p);
		TypedQuery<T> query = this.getEntityManager().createQuery(cq);
		return (T) query.getSingleResult();
	}

	/**
	 * Find a list of <E> entities using as filter criteria the specified list
	 * of attribute values.
	 * 
	 * @param params
	 * @return
	 * @throws BusinessException
	 */
	public List<E> findEntitiesByAttributes(Map<String, Object> params) {
		return findEntitiesByAttributes(this.getEntityClass(), params);
	}

	/**
	 * Find a list of entities of the given type, using as filter criteria the
	 * specified list of attribute values.
	 * 
	 * @param entityClass
	 * @param params
	 * @return
	 * @throws BusinessException
	 */
	public <T> List<T> findEntitiesByAttributes(Class<T> entityClass,
			Map<String, Object> params) {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		Root<T> root = cq.from(entityClass);
		cq.select(root);
		Assert.notNull(params);
		Predicate p = null;
		if (entityClass.isAssignableFrom(IModelWithClientId.class)) {
			p = cb.equal(root.get("clientId"), Session.user.get().getClient()
					.getId());
		}
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			p = cb.and(cb.equal(root.get(entry.getKey()), entry.getValue()));
		}
		cq.where(p);
		TypedQuery<T> query = this.getEntityManager().createQuery(cq);
		return (List<T>) query.getResultList();
	}

}
