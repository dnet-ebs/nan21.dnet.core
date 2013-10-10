/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.service;

import java.util.ArrayList;
import java.util.List;

import net.nan21.dnet.core.api.model.IModelWithId;
import net.nan21.dnet.core.api.service.business.IEntityService;
import net.nan21.dnet.core.api.service.presenter.IDsService;
import net.nan21.dnet.core.api.wf.IActivitiProcessEngineHolder;
import net.nan21.dnet.core.presenter.AbstractPresenterBase;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

/**
 * Root abstract class for presenter service hierarchy. It provides support for
 * the sub-classes with the generally needed elements like an
 * applicationContext, system configuration parameters, workflow services etc.
 * 
 * Usually an application developer shouldn't sub-class directly this class but
 * choose one of the more specialized sub-class based on the particular need.
 * 
 * @author amathe
 * 
 */
public abstract class AbstractPresenterBaseService extends
		AbstractPresenterBase {

	private ProcessEngine workflowEngine;

	/**
	 * Lookup a data-source service.
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	public <M, F, P> IDsService<M, F, P> findDsService(String dsName)
			throws Exception {
		return this.getServiceLocator().findDsService(dsName);
	}

	/**
	 * Lookup an entity service.
	 * 
	 * @param <E>
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <E> IEntityService<E> findEntityService(Class<E> entityClass)
			throws Exception {
		return this.getServiceLocator().findEntityService(entityClass);
	}

	/**
	 * Prepare a data-source delegate. Inject all required dependencies marked
	 * with <code>@Autowired</code> for which there is no attempt to auto-load
	 * on-demand from the spring-context.
	 */
	protected <M, F, P> void prepareDelegate(
			AbstractPresenterBaseService delegate) {
		delegate.setApplicationContext(this.getApplicationContext());
	}

	/**
	 * Get a presenter delegate
	 * 
	 * @param claz
	 * @return
	 * @throws Exception
	 */
	protected <T extends AbstractPresenterBaseService> T getDelegate(
			Class<T> claz) throws Exception {
		T delegate;
		try {
			delegate = claz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new Exception("Cannot instantiate class"
					+ claz.getCanonicalName(), e);
		}
		this.prepareDelegate(delegate);
		return delegate;
	}

	/**
	 * Get the workflow engine
	 * 
	 * @return
	 * @throws Exception
	 */
	public ProcessEngine getWorkflowEngine() throws Exception {
		if (this.workflowEngine == null) {
			this.workflowEngine = (ProcessEngine) this.getApplicationContext()
					.getBean(IActivitiProcessEngineHolder.class)
					.getProcessEngine();
		}
		return this.workflowEngine;
	}

	public RuntimeService getWorkflowRuntimeService() throws Exception {
		return this.getWorkflowEngine().getRuntimeService();
	}

	public TaskService getWorkflowTaskService() throws Exception {
		return this.getWorkflowEngine().getTaskService();
	}

	public RepositoryService getWorkflowRepositoryService() throws Exception {
		return this.getWorkflowEngine().getRepositoryService();
	}

	public HistoryService getWorkflowHistoryService() throws Exception {
		return this.getWorkflowEngine().getHistoryService();
	}

	public FormService getWorkflowFormService() throws Exception {
		return this.getWorkflowEngine().getFormService();
	}

	protected List<Object> collectIds(List<?> list) {
		List<Object> result = new ArrayList<Object>();
		for (Object e : list) {
			try {
				result.add(((IModelWithId) e).getId());
			} catch (ClassCastException exc) {
				// ignore it , go to next in list
			}
		}
		return result;
	}

}
