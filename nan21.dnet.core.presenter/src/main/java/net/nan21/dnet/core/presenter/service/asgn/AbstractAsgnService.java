/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.service.asgn;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.action.query.IQueryBuilder;
import net.nan21.dnet.core.api.action.result.IDsMarshaller;
import net.nan21.dnet.core.api.descriptor.IAsgnContext;
import net.nan21.dnet.core.api.service.business.IAsgnTxService;
import net.nan21.dnet.core.api.service.business.IAsgnTxServiceFactory;
import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.presenter.action.marshaller.JsonMarshaller;
import net.nan21.dnet.core.presenter.action.query.QueryBuilderWithJpql;
import net.nan21.dnet.core.presenter.descriptor.AsgnDescriptor;
import net.nan21.dnet.core.presenter.descriptor.ViewModelDescriptorManager;
import net.nan21.dnet.core.presenter.model.AbstractAsgnModel;
import net.nan21.dnet.core.presenter.service.AbstractPresenterReadService;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

/**
 * Base abstract class for assignment service. An assignment component is used
 * to manage the many-to-many associations between entities.
 * 
 * @author amathe
 * 
 * @param <M>
 * @param <F>
 * @param <P>
 * @param <E>
 */
public abstract class AbstractAsgnService<M extends AbstractAsgnModel<E>, F, P, E>
		extends AbstractPresenterReadService<M, F, P> {

	/**
	 * Source entity type it works with.
	 */
	private Class<E> entityClass;

	private AsgnDescriptor<M> descriptor;

	private List<IAsgnTxServiceFactory> asgnTxServiceFactories;

	private IAsgnContext ctx;

	private String asgnTxFactoryName;

	private IAsgnTxService<E> txService;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private IAsgnTxService<E> findAsgnTxService() throws Exception {
		IAsgnTxService<E> s = this.getServiceLocator().findAsgnTxService(
				Constants.SPRING_DEFAULT_ASGN_TX_SERVICE,
				this.asgnTxFactoryName);
		s.setEntityClass(entityClass);
		return s;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public IAsgnTxService<E> getTxService() throws Exception {
		if (this.txService == null) {
			this.txService = findAsgnTxService();
		}
		return this.txService;
	}

	/**
	 * Add the specified list of IDs to the selected ones.
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void moveRight(String selectionId, List<String> ids)
			throws Exception {
		this.getTxService().doMoveRight(this.ctx, selectionId, ids);
	}

	/**
	 * Add all the available values to the selected ones.
	 * 
	 * @throws Exception
	 */
	public void moveRightAll(String selectionId, F filter, P params)
			throws Exception {
		// TODO: send the filter also to move all according to filter
		this.getTxService().doMoveRightAll(this.ctx, selectionId);
	}

	/**
	 * Remove the specified list of IDs from the selected ones.
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void moveLeft(String selectionId, List<String> ids) throws Exception {
		this.getTxService().doMoveLeft(this.ctx, selectionId, ids);
	}

	/**
	 * Remove all the selected values.
	 * 
	 * @throws Exception
	 */
	public void moveLeftAll(String selectionId, F filter, P params)
			throws Exception {
		// TODO: send the filter also to move all according to filter
		this.getTxService().doMoveLeftAll(this.ctx, selectionId);
	}

	/**
	 * 
	 * @param selectionId
	 * @param objectId
	 * @throws Exception
	 */
	public void save(String selectionId, String objectId) throws Exception {
		this.getTxService().doSave(this.ctx, selectionId, objectId);
	}

	/**
	 * Initialize the component's temporary workspace.
	 * 
	 * @return the UUID of the selection
	 * @throws Exception
	 */
	public String setup(String asgnName, String objectId) throws Exception {
		return this.getTxService().doSetup(this.ctx, asgnName, objectId);
	}

	/**
	 * Clean-up the temporary selections.
	 * 
	 * @throws Exception
	 */
	public void cleanup(String selectionId) throws Exception {
		this.getTxService().doCleanup(this.ctx, selectionId);
	}

	/**
	 * Restores all the changes made by the user to the initial state.
	 * 
	 * @throws Exception
	 */
	public void reset(String selectionId, String objectId) throws Exception {
		this.getTxService().doReset(this.ctx, selectionId, objectId);
	}

	/**
	 * 
	 * @param selectionId
	 * @param filter
	 * @param params
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	public List<M> findLeft(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception {
		QueryBuilderWithJpql<M, F, P> bld = (QueryBuilderWithJpql<M, F, P>) builder;

		bld.addFilterCondition(" e.clientId = :clientId and e."
				+ this.ctx.getLeftPkField()
				+ " not in (select x.itemId from TempAsgnLine x where x.selectionId = :selectionId)");

		bld.setFilter(filter);
		bld.setParams(params);

		List<M> result = new ArrayList<M>();
		TypedQuery<E> q = bld.createQuery(this.getEntityClass());
		q.setParameter("clientId", Session.user.get().getClient().getId());
		q.setParameter("selectionId", selectionId);
		List<E> list = q.setFirstResult(bld.getResultStart())
				.setMaxResults(bld.getResultSize()).getResultList();
		for (E e : list) {
			M m = this.getModelClass().newInstance();
			entityToModel(e, m);
			result.add(m);
		}
		return result;
	}

	/**
	 * 
	 * @param selectionId
	 * @param filter
	 * @param params
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	public List<M> findRight(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception {
		QueryBuilderWithJpql<M, F, P> bld = (QueryBuilderWithJpql<M, F, P>) builder;

		bld.addFilterCondition(" e.clientId = :clientId and e."
				+ this.ctx.getLeftPkField()
				+ " in (select x.itemId from TempAsgnLine x where x.selectionId = :selectionId)");
		bld.setFilter(filter);
		bld.setParams(params);

		List<M> result = new ArrayList<M>();

		TypedQuery<E> q = bld.createQuery(this.getEntityClass());
		q.setParameter("clientId", Session.user.get().getClient().getId());
		q.setParameter("selectionId", selectionId);
		List<E> list = q.setFirstResult(bld.getResultStart())
				.setMaxResults(bld.getResultSize()).getResultList();
		for (E e : list) {
			M m = this.getModelClass().newInstance();
			entityToModel(e, m);
			result.add(m);
		}
		return result;
	}

	/**
	 * 
	 * @param e
	 * @param m
	 * @throws Exception
	 */
	public void entityToModel(E e, M m) throws Exception {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext(e);
		Map<String, String> refpaths = this.getDescriptor().getE2mConv();
		Method[] methods = this.getModelClass().getMethods();
		for (Method method : methods) {
			if (!method.getName().equals("set__clientRecordId__")
					&& method.getName().startsWith("set")) {
				String fn = StringUtils.uncapitalize(method.getName()
						.substring(3));
				try {
					method.invoke(m, parser.parseExpression(refpaths.get(fn))
							.getValue(context));
				} catch (Exception exc) {

				}
			}
		}
	}

	/**
	 * 
	 * @param selectionId
	 * @param filter
	 * @param params
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	public Long countLeft(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception {
		return this.count_(selectionId, filter, params, builder);
	}

	/**
	 * 
	 * @param selectionId
	 * @param filter
	 * @param params
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	public Long countRight(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception {
		return this.count_(selectionId, filter, params, builder);
	}

	/**
	 * 
	 * @param selectionId
	 * @param filter
	 * @param params
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	protected Long count_(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception {
		QueryBuilderWithJpql<M, F, P> bld = (QueryBuilderWithJpql<M, F, P>) builder;
		Query q = bld.createQueryCount();
		q.setParameter("clientId", Session.user.get().getClient().getId());
		q.setParameter("selectionId", selectionId);
		Object count = q.getSingleResult();
		if (count instanceof Integer) {
			return ((Integer) count).longValue();
		} else {
			return (Long) count;
		}
	}

	/**
	 * 
	 * @param dataFormat
	 * @return
	 * @throws Exception
	 */
	public IDsMarshaller<M, F, P> createMarshaller(String dataFormat)
			throws Exception {
		IDsMarshaller<M, F, P> marshaller = null;
		if (dataFormat.equals(IDsMarshaller.JSON)) {
			marshaller = new JsonMarshaller<M, F, P>(this.getModelClass(),
					this.getFilterClass(), this.getParamClass());
		}
		return marshaller;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public IQueryBuilder<M, F, P> createQueryBuilder() throws Exception {
		QueryBuilderWithJpql<M, F, P> qb = new QueryBuilderWithJpql<M, F, P>();
		qb.setFilterClass(this.getFilterClass());
		qb.setParamClass(this.getParamClass());
		qb.setDescriptor(this.getDescriptor());
		qb.setEntityManager(this.getTxService().getEntityManager());
		qb.setSettings(this.getSettings());
		if (qb instanceof QueryBuilderWithJpql) {
			QueryBuilderWithJpql<M, F, P> jqb = (QueryBuilderWithJpql<M, F, P>) qb;
			jqb.setBaseEql("select e from "
					+ this.getEntityClass().getSimpleName() + " e");
			jqb.setBaseEqlCount("select count(1) from "
					+ this.getEntityClass().getSimpleName() + " e");

			if (this.getDescriptor().isWorksWithJpql()) {
				jqb.setDefaultWhere(this.getDescriptor().getJpqlDefaultWhere());
				jqb.setDefaultSort(this.getDescriptor().getJpqlDefaultSort());
			}
		}

		return qb;
	}

	// ==================== getters- setters =====================

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public List<IAsgnTxServiceFactory> getAsgnTxServiceFactories() {
		return asgnTxServiceFactories;
	}

	public void setAsgnTxServiceFactories(
			List<IAsgnTxServiceFactory> asgnTxServiceFactories) {
		this.asgnTxServiceFactories = asgnTxServiceFactories;
	}

	public IAsgnContext getCtx() {
		return ctx;
	}

	public void setCtx(IAsgnContext ctx) {
		this.ctx = ctx;
	}

	public String getAsgnTxFactoryName() {
		return asgnTxFactoryName;
	}

	public void setAsgnTxFactoryName(String asgnTxFactoryName) {
		this.asgnTxFactoryName = asgnTxFactoryName;
	}

	public AsgnDescriptor<M> getDescriptor() throws Exception {
		if (this.descriptor == null) {
			boolean useCache = this.getSettings()
					.get(Constants.PROP_WORKING_MODE)
					.equals(Constants.PROP_WORKING_MODE_PROD);
			this.descriptor = ViewModelDescriptorManager.getAsgnDescriptor(
					this.getModelClass(), useCache);
		}
		return descriptor;
	}

	public void setDescriptor(AsgnDescriptor<M> descriptor) {
		this.descriptor = descriptor;
	}

}
