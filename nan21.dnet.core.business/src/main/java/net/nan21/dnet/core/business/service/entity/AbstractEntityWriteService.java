/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.business.service.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.exceptions.BusinessException;
import net.nan21.dnet.core.api.model.IMessageData;
import net.nan21.dnet.core.api.model.IModelWithClientId;
import net.nan21.dnet.core.api.model.IModelWithCode;
import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.business.message.MessageData;

/**
 * Implements the write actions for an entity-service. See the super-classes for
 * more details.
 * 
 * @author amathe
 * 
 * @param <E>
 */
public abstract class AbstractEntityWriteService<E> extends
		AbstractEntityReadService<E> {

	private boolean noInsert = false;
	private boolean noUpdate = false;
	private boolean noDelete = false;
	private boolean noDeleteById = false;

	/* ========================== INSERT =========================== */

	/**
	 * Pre-insert template method for one entity.
	 * 
	 * @param e
	 */
	protected void preInsert(E e) throws BusinessException {
		if (IModelWithCode.class.isAssignableFrom(e.getClass())) {
			this.code_allocation((IModelWithCode) e, true);
		}
	}

	/**
	 * On-insert template method for one entity, it actually does the work.
	 * 
	 * @param e
	 */
	protected void onInsert(E e) throws BusinessException {
		if (IModelWithClientId.class.isAssignableFrom(e.getClass())) {
			IModelWithClientId x = (IModelWithClientId) e;
			if (x.getClientId() == null
					|| x.getClientId().equals(
							Session.user.get().getClient().getId())) {
				this.getEntityManager().persist(e);
			} else {
				throw new BusinessException(
						"You are trying to insert an object into a different client as your current client.");
			}
		} else {
			this.getEntityManager().persist(e);
		}
	}

	/**
	 * Post-insert template method for one entity.
	 * 
	 * @param list
	 */
	protected void postInsert(E e) throws BusinessException {
	}

	/**
	 * Pre-insert template method for a collection of entities.
	 * 
	 * @param list
	 */
	protected void preInsert(List<E> list) throws BusinessException {
	}

	/**
	 * On-insert template method for a collection of entities, it actually does
	 * the work.
	 * 
	 * @param list
	 */
	protected void onInsert(List<E> list) throws BusinessException {
		for (E e : list) {
			this.preInsert(e);
			this.onInsert(e);
			this.postInsert(e);
		}
	}

	/**
	 * Post-insert template method for a collection of entities.
	 * 
	 * @param list
	 */
	protected void postInsert(List<E> list) throws BusinessException {
	}

	/**
	 * Insert (persist) a list of entities.
	 */
	public void insert(List<E> list) throws BusinessException {
		if (this.noInsert) {
			throw new BusinessException("Insert not allowed for type "
					+ this.getEntityClass().getCanonicalName());
		}
		this.preInsert(list);
		this.onInsert(list);
		this.postInsert(list);
	}

	/**
	 * Helper insert method for one single entity. It creates a list with this
	 * single entity and delegates to the <code> insert(List<E> list)</code>
	 * method
	 */
	public void insert(E e) throws BusinessException {
		List<E> list = new ArrayList<E>();
		list.add(e);
		this.insert(list);
	}

	/* ========================== UPDATE =========================== */

	/**
	 * Pre-update template method for one entity.
	 * 
	 * @param e
	 */
	protected void preUpdate(E e) throws BusinessException {
		if (IModelWithCode.class.isAssignableFrom(e.getClass())) {
			this.code_allocation((IModelWithCode) e, false);
		}
	}

	/**
	 * On-update template method for one entity, it actually does the work.
	 * 
	 * @param e
	 */
	protected void onUpdate(E e) throws BusinessException {
		if (IModelWithClientId.class.isAssignableFrom(e.getClass())) {
			IModelWithClientId x = (IModelWithClientId) e;
			if (x.getClientId() == null
					|| x.getClientId().equals(
							Session.user.get().getClient().getId())) {
				this.getEntityManager().merge(e);
			} else {
				throw new BusinessException(
						"You are trying to update an object which doesn't belong to your current client.");
			}
		} else {
			this.getEntityManager().merge(e);
		}
	}

	/**
	 * Post-update template method for one entity.
	 * 
	 * @param e
	 */
	protected void postUpdate(E e) throws BusinessException {
	}

	/**
	 * Pre-update template method for a collection of entities.
	 * 
	 * @param list
	 */
	protected void preUpdate(List<E> list) throws BusinessException {
	}

	/**
	 * On-update template method for a collection of entities, it actually does
	 * the work.
	 * 
	 * @param list
	 */
	protected void onUpdate(List<E> list) throws BusinessException {
		for (E e : list) {
			this.preUpdate(e);
			this.onUpdate(e);
			this.postUpdate(e);
		}
	}

	/**
	 * Post-update template method for a collection of entities.
	 * 
	 * @param list
	 */
	protected void postUpdate(List<E> list) throws BusinessException {
	}

	/**
	 * Update (merge) a list of entities.
	 */

	public void update(List<E> list) throws BusinessException {
		if (this.noUpdate) {
			throw new BusinessException("Update not allowed for type "
					+ this.getEntityClass().getCanonicalName());
		}
		this.preUpdate(list);
		this.onUpdate(list);
		this.postUpdate(list);
	}

	/**
	 * Helper update method for one single entity. It creates a list with this
	 * single entity and delegates to the <code> update(List<E> list)</code>
	 * method
	 */
	public void update(E e) throws BusinessException {
		List<E> list = new ArrayList<E>();
		list.add(e);
		this.update(list);
	}

	/**
	 * Execute a JPQL update statement.
	 * 
	 * @param jpqlStatement
	 * @param parameters
	 * @return
	 * @throws BusinessException
	 */
	public int update(String jpqlStatement, Map<String, Object> parameters)
			throws BusinessException {
		Query q = this.getEntityManager().createQuery(jpqlStatement);

		if (parameters != null) {
			for (Map.Entry<String, Object> p : parameters.entrySet()) {
				q.setParameter(p.getKey(), p.getValue());
			}
		}
		return q.executeUpdate();
	}

	/* ========================== DELETE BY ENTITY =========================== */

	/**
	 * Pre-delete template method for one entity.
	 * 
	 * @param e
	 */
	protected void preDelete(E e) throws BusinessException {
	}

	/**
	 * On-delete template method for one entity, it actually does the work.
	 * 
	 * @param e
	 */
	protected void onDelete(E e) throws BusinessException {
		if (IModelWithClientId.class.isAssignableFrom(e.getClass())) {
			IModelWithClientId x = (IModelWithClientId) e;
			if (x.getClientId() == null
					|| x.getClientId().equals(
							Session.user.get().getClient().getId())) {
				this.getEntityManager().remove(e);
			} else {
				throw new BusinessException(
						"You are trying to delete an object which doesn't belong to your current client.");
			}
		}
	}

	/**
	 * Post-delete template method for one entity.
	 * 
	 * @param e
	 */
	protected void postDelete(E e) throws BusinessException {
	}

	/**
	 * Pre-delete template method for a collection of entities.
	 * 
	 * @param list
	 */
	protected void preDelete(List<E> list) throws BusinessException {
	}

	/**
	 * On-delete template method for a collection of entities, it actually does
	 * the work.
	 * 
	 * @param list
	 */
	protected void onDelete(List<E> list) throws BusinessException {
		for (E e : list) {
			this.preDelete(e);
			this.onDelete(e);
			this.postDelete(e);
		}
	}

	/**
	 * Post-delete template method for a collection of entities.
	 * 
	 * @param list
	 */
	protected void postDelete(List<E> list) throws BusinessException {
	}

	/**
	 * Delete (remove) a list of entities.
	 */
	public void delete(List<E> list) throws BusinessException {
		if (this.noDelete) {
			throw new BusinessException("Delete not allowed for type "
					+ this.getEntityClass().getCanonicalName());
		}
		this.preDelete(list);
		this.onDelete(list);
		this.postDelete(list);
	}

	/**
	 * Helper delete method for one single entity. It creates a list with this
	 * single entity and delegates to the <code> delete(List<E> list)</code>
	 * method
	 */
	public void delete(E e) throws BusinessException {
		List<E> list = new ArrayList<E>();
		list.add(e);
		this.delete(list);
	}

	/* ========================== DELETE BY ID =========================== */

	/**
	 * Pre-delete template method for a list of IDs.
	 * 
	 * @param list
	 */
	protected void preDeleteByIds(List<Object> ids, Map<String, Object> context)
			throws BusinessException {
	}

	/**
	 * On-delete template method for a list of IDs, it actually does the work.
	 * 
	 * @param list
	 */
	protected void onDeleteByIds(List<Object> ids, Map<String, Object> context)
			throws BusinessException {
		if (ids == null || ids.size() == 0) {
			return;
		}
		if (IModelWithClientId.class.isAssignableFrom(this.getEntityClass())) {
			this.getEntityManager()
					.createQuery(
							"delete from "
									+ getEntityClass().getSimpleName()
									+ " e where e.clientId = :clientId and e.id in :ids")
					.setParameter("ids", ids)
					.setParameter("clientId",
							Session.user.get().getClient().getId())
					.executeUpdate();

		} else {
			this.getEntityManager()
					.createQuery(
							"delete from " + getEntityClass().getSimpleName()
									+ " e where  e.id in :ids")
					.setParameter("ids", ids).executeUpdate();
		}

	}

	/**
	 * Post-delete template method for a list of IDs.
	 * 
	 * @param list
	 */
	protected void postDeleteByIds(List<Object> ids, Map<String, Object> context)
			throws BusinessException {
	}

	/**
	 * Delete entities by a list of IDs.If delete-by-id is not allowed it
	 * redirects to delete-by-entity.
	 */
	public void deleteByIds(List<Object> ids) throws BusinessException {
		if (this.noDeleteById) {
			List<E> list = this.findByIds(ids);
			this.delete(list);
		} else {
			Map<String, Object> context = new HashMap<String, Object>();
			this.preDeleteByIds(ids, context);
			this.onDeleteByIds(ids, context);
			this.postDeleteByIds(ids, context);
		}
	}

	/**
	 * Helper delete method for one ID. It creates a list with this single ID
	 * and delegates to the <code> delete(List&lt;Object&gt; ids)</code> method
	 */
	public void deleteById(Object id) throws BusinessException {
		List<Object> list = new ArrayList<Object>();
		list.add(id);
		this.deleteByIds(list);
	}

	/* ========================== UTILITIES =========================== */

	/**
	 * Implement code allocation for {@link AbstractTypeWithCode} and
	 * {@link AbstractTypeWithCodeNT} type entities
	 * 
	 * @param e
	 * @throws BusinessException
	 */
	protected void code_allocation(IModelWithCode e, boolean isInsert)
			throws BusinessException {
		if (e.getName() == null || "".equals(e.getName())) {
			throw new BusinessException("Specify the name, it cannot be empty.");
		}
		if (e.getCode() == null || "".equals(e.getCode())) {
			if (e._code_allocation_mode() == Constants.ENTITY_CODE_DERIVED) {
				// TODO: allow a customizable transformer.
				e.setCode(e.getName().replaceAll(" ", "_").toUpperCase());
			} else if (e._code_allocation_mode() == Constants.ENTITY_CODE_MANUAL) {
				throw new BusinessException(
						"Specify a code for record with name `" + e.getName()
								+ "`");
			}
			// TODO: handle the sequence allocation
		}
	}

	/**
	 * Fire an entity specific event
	 * 
	 * @param eventData
	 */
	protected void fireEvent(IMessageData eventData) {
		Message<IMessageData> message = MessageBuilder.withPayload(eventData)
				.build();
		this.getApplicationContext()
				.getBean(
						this.getEntityClass().getSimpleName() + "EventChannel",
						MessageChannel.class).send(message);
	}

	/**
	 * Fire an event with the specified action and data-map.
	 * 
	 * @param action
	 * @param data
	 */
	protected void fireEvent(String action, Map<String, Object> data) {
		IMessageData eventData = new MessageData(this.getEntityClass()
				.getCanonicalName(), action, data);
		Message<IMessageData> message = MessageBuilder.withPayload(eventData)
				.build();

		this.getApplicationContext()
				.getBean(
						this.getEntityClass().getSimpleName() + "EventChannel",
						MessageChannel.class).send(message);
	}

	public boolean isNoInsert() {
		return noInsert;
	}

	public void setNoInsert(boolean noInsert) {
		this.noInsert = noInsert;
	}

	public boolean isNoUpdate() {
		return noUpdate;
	}

	public void setNoUpdate(boolean noUpdate) {
		this.noUpdate = noUpdate;
	}

	public boolean isNoDelete() {
		return noDelete;
	}

	public void setNoDelete(boolean noDelete) {
		this.noDelete = noDelete;
	}

	public boolean isNoDeleteById() {
		return noDeleteById;
	}

	public void setNoDeleteById(boolean noDeleteById) {
		this.noDeleteById = noDeleteById;
	}

}
