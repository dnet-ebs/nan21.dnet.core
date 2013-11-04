/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.service.ds;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import net.nan21.dnet.core.api.model.IModelWithId;
import net.nan21.dnet.core.presenter.descriptor.RpcDefinition;
import net.nan21.dnet.core.presenter.model.AbstractDsModel;
import net.nan21.dnet.core.presenter.service.AbstractPresenterBaseService;

/**
 * Implements the rpc(remote-procedure call) actions for an entity-ds. See the
 * super-classes for more details.
 * 
 * @author amathe
 * 
 * @param <M>
 * @param <F>
 * @param <P>
 * @param <E>
 */
public abstract class AbstractEntityDsRpcService<M extends AbstractDsModel<E>, F, P, E>
		extends AbstractEntityDsWriteService<M, F, P, E> {

	private Map<String, RpcDefinition> rpcData = new HashMap<String, RpcDefinition>();
	private Map<String, RpcDefinition> rpcFilter = new HashMap<String, RpcDefinition>();

	// ======================== RPC ===========================

	/**
	 * Execute an arbitrary service method with the data object.
	 */
	public void rpcData(String procedureName, M ds, P params) throws Exception {

		if (!rpcData.containsKey(procedureName)) {
			throw new Exception("No such procedure defined: `" + procedureName
					+ "`");
		}

		RpcDefinition def = rpcData.get(procedureName);
		AbstractPresenterBaseService delegate = this.getDelegate(def
				.getDelegateClass());
		RpcMethod rpcMethod = this.getRpcMethod(def.getDelegateClass(),
				def.getMethodName(), getModelClass());
		if (rpcMethod.isWithParams()) {
			rpcMethod.getMethod().invoke(delegate, ds, params);
		} else {
			rpcMethod.getMethod().invoke(delegate, ds);
		}

		if (def.getReloadFromEntity()) {
			if (ds instanceof IModelWithId) {
				EntityManager em = this.getEntityService().getEntityManager();
				if (((IModelWithId) ds).getId() != null) {
					E e = (E) em.find(this.getEntityClass(),
							((IModelWithId) ds).getId());
					this.getConverter().entityToModel(e, ds, em, null);
				}
			}
		}
		// delegate.execute(ds);
	}

	/**
	 * Execute an arbitrary service method with the data object returning a
	 * stream as result.
	 * 
	 * @param procedureName
	 * @param ds
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public InputStream rpcDataStream(String procedureName, M ds, P params)
			throws Exception {

		if (!rpcData.containsKey(procedureName)) {
			throw new Exception("No such procedure defined: " + procedureName);
		}

		RpcDefinition def = rpcData.get(procedureName);
		AbstractPresenterBaseService delegate = this.getDelegate(def
				.getDelegateClass());
		RpcMethod rpcMethod = this.getRpcMethod(def.getDelegateClass(),
				def.getMethodName(), getModelClass());

		InputStream result = null;
		if (rpcMethod.isWithParams()) {
			result = (InputStream) rpcMethod.getMethod().invoke(delegate, ds,
					params);
		} else {
			result = (InputStream) rpcMethod.getMethod().invoke(delegate, ds);
		}
		return result;
	}

	/**
	 * Execute an arbitrary service method with the filter object.
	 * 
	 * @param procedureName
	 * @param filter
	 * @param params
	 * @throws Exception
	 */
	public void rpcFilter(String procedureName, F filter, P params)
			throws Exception {

		if (!rpcFilter.containsKey(procedureName)) {
			throw new Exception("No such procedure defined: " + procedureName);
		}

		RpcDefinition def = rpcFilter.get(procedureName);
		AbstractPresenterBaseService delegate = this.getDelegate(def
				.getDelegateClass());
		RpcMethod rpcMethod = this.getRpcMethod(def.getDelegateClass(),
				def.getMethodName(), getFilterClass());

		if (rpcMethod.isWithParams()) {
			rpcMethod.getMethod().invoke(delegate, filter, params);
		} else {
			rpcMethod.getMethod().invoke(delegate, filter);
		}
	}

	/**
	 * Execute an arbitrary service method with the filter object returning a
	 * stream as result.
	 * 
	 * @param procedureName
	 * @param filter
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public InputStream rpcFilterStream(String procedureName, F filter, P params)
			throws Exception {

		if (!rpcFilter.containsKey(procedureName)) {
			throw new Exception("No such procedure defined: " + procedureName);
		}

		RpcDefinition def = rpcFilter.get(procedureName);
		AbstractPresenterBaseService delegate = this.getDelegate(def
				.getDelegateClass());
		RpcMethod rpcMethod = this.getRpcMethod(def.getDelegateClass(),
				def.getMethodName(), getFilterClass());

		InputStream result = null;
		if (rpcMethod.isWithParams()) {
			result = (InputStream) rpcMethod.getMethod().invoke(delegate,
					filter, params);
		} else {
			result = (InputStream) rpcMethod.getMethod().invoke(delegate,
					filter);
		}

		return result;
	}

	/**
	 * Execute an arbitrary service method with a list of data objects.
	 * Contributed by Jan Fockaert
	 * 
	 * @param procedureName
	 * @param list
	 * @param params
	 * @throws Exception
	 */
	public void rpcData(String procedureName, List<M> list, P params)
			throws Exception {

		if (!rpcData.containsKey(procedureName)) {
			throw new Exception("No such procedure defined: " + procedureName);
		}

		RpcDefinition def = rpcData.get(procedureName);
		AbstractPresenterBaseService delegate = this.getDelegate(def
				.getDelegateClass());
		RpcMethod rpcMethod = this.getRpcMethod(def.getDelegateClass(),
				def.getMethodName(), List.class);
		if (rpcMethod.isWithParams()) {
			rpcMethod.getMethod().invoke(delegate, list, params);
		} else {
			rpcMethod.getMethod().invoke(delegate, list);
		}

		if (def.getReloadFromEntity()) {
			for (M ds : list) {
				if (ds instanceof IModelWithId) {
					String _id = ((IModelWithId) ds).getId();
					if (_id != null && !"".equals(_id)) {
						EntityManager em = this.getEntityService()
								.getEntityManager();
						E e = (E) em.find(this.getEntityClass(), _id);
						this.getConverter().entityToModel(e, ds, em, null);
					}
				}
			}
		}
	}

	/**
	 * Execute an arbitrary service method with a list of data objects returning
	 * a stream as result.
	 * 
	 * Contributed by Jan Fockaert
	 * 
	 * @param procedureName
	 * @param list
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public InputStream rpcDataStream(String procedureName, List<M> list,
			P params) throws Exception {

		if (!rpcData.containsKey(procedureName)) {
			throw new Exception("No such procedure defined: " + procedureName);
		}

		RpcDefinition def = rpcData.get(procedureName);
		AbstractPresenterBaseService delegate = this.getDelegate(def
				.getDelegateClass());
		RpcMethod rpcMethod = this.getRpcMethod(def.getDelegateClass(),
				def.getMethodName(), List.class);

		InputStream result = null;
		if (rpcMethod.isWithParams()) {
			result = (InputStream) rpcMethod.getMethod().invoke(delegate, list,
					params);
		} else {
			result = (InputStream) rpcMethod.getMethod().invoke(delegate, list);
		}

		return result;
	}

	/**
	 * Helper method to find the rpc method to be invoked.
	 * 
	 * @param claz
	 * @param methodName
	 * @param dataClass
	 * @return
	 * @throws Exception
	 */
	protected RpcMethod getRpcMethod(
			Class<? extends AbstractPresenterBaseService> claz,
			String methodName, Class<?> dataClass) throws Exception {
		Method m = null;
		boolean withParams = false;
		try {
			m = claz.getMethod(methodName, dataClass, getParamClass());
			withParams = true;
		} catch (NoSuchMethodException e) {
			try {
				m = claz.getMethod(methodName, dataClass);
			} catch (NoSuchMethodException ex) {
				throw new Exception("Delegate class " + claz.getCanonicalName()
						+ " does not implement a `" + methodName + "("
						+ dataClass.getSimpleName() + ")` method.");
			}
		}

		return new RpcMethod(m, withParams);
	}

	public Map<String, RpcDefinition> getRpcData() {
		return rpcData;
	}

	public void setRpcData(Map<String, RpcDefinition> rpcData) {
		this.rpcData = rpcData;
	}

	public Map<String, RpcDefinition> getRpcFilter() {
		return rpcFilter;
	}

	public void setRpcFilter(Map<String, RpcDefinition> rpcFilter) {
		this.rpcFilter = rpcFilter;
	}

	private class RpcMethod {
		private Method method;
		private boolean withParams;

		public RpcMethod(Method method, boolean withParams) {
			super();
			this.method = method;
			this.withParams = withParams;
		}

		public Method getMethod() {
			return method;
		}

		public boolean isWithParams() {
			return withParams;
		}

	}
}
