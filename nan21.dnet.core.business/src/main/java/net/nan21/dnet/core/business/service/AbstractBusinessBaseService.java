/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.nan21.dnet.core.api.ISettings;
import net.nan21.dnet.core.api.exceptions.BusinessException;
import net.nan21.dnet.core.api.model.IModelWithId;
import net.nan21.dnet.core.api.service.business.IEntityService;
import net.nan21.dnet.core.api.wf.IActivitiProcessEngineHolder;
import net.nan21.dnet.core.business.AbstractApplicationContextAware;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

/**
 * Root abstract class for business service hierarchy. It provides support for
 * the sub-classes with the generally needed elements like an
 * applicationContext, system configuration parameters, workflow services etc.
 * 
 * 
 * @author amathe
 * 
 */
public abstract class AbstractBusinessBaseService extends
		AbstractApplicationContextAware {

	private ISettings settings;

	private ServiceLocatorBusiness serviceLocator;

	private ProcessEngine workflowEngine;

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	/**
	 * Lookup an entity service.
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 * @throws BusinessException
	 */
	public <T> IEntityService<T> findEntityService(Class<T> entityClass)
			throws BusinessException {
		return this.getServiceLocator().findEntityService(entityClass);
	}

	/**
	 * Return a new instance of a business delegate by the given class.
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws BusinessException
	 */
	public <T extends AbstractBusinessDelegate> T getBusinessDelegate(
			Class<T> clazz) throws BusinessException {
		T delegate;
		try {
			delegate = clazz.newInstance();
		} catch (Exception e) {
			throw new BusinessException("Cannot create a new instance of  "
					+ clazz.getCanonicalName(), e);
		}
		delegate.setApplicationContext(this.getApplicationContext());
		delegate.setEntityManager(this.getEntityManager());
		return delegate;
	}

	public ISettings getSettings() {
		if (this.settings == null) {
			this.settings = this.getApplicationContext().getBean(
					ISettings.class);
		}
		return settings;
	}

	public void setSettings(ISettings settings) {
		this.settings = settings;
	}

	/**
	 * @return the entity manager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * @param entityManager
	 *            the entity manager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Get business service locator. If it is null attempts to retrieve it
	 * 
	 * @return
	 */
	public ServiceLocatorBusiness getServiceLocator() {
		if (this.serviceLocator == null) {
			this.serviceLocator = this.getApplicationContext().getBean(
					ServiceLocatorBusiness.class);
		}
		return serviceLocator;
	}

	/**
	 * Set business service locator.
	 * 
	 * @param serviceLocator
	 */
	public void setServiceLocator(ServiceLocatorBusiness serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public ProcessEngine getWorkflowEngine() throws BusinessException {
		if (this.workflowEngine == null) {
			try {
				this.workflowEngine = (ProcessEngine) this
						.getApplicationContext()
						.getBean(IActivitiProcessEngineHolder.class)
						.getProcessEngine();
			} catch (Exception e) {
				throw new BusinessException(
						"Cannot get the Activiti workflow engine.", e);
			}
		}
		return this.workflowEngine;
	}

	public RuntimeService getWorkflowRuntimeService() throws BusinessException {
		return this.getWorkflowEngine().getRuntimeService();
	}

	public TaskService getWorkflowTaskService() throws BusinessException {
		return this.getWorkflowEngine().getTaskService();
	}

	public RepositoryService getWorkflowRepositoryService()
			throws BusinessException {
		return this.getWorkflowEngine().getRepositoryService();
	}

	public HistoryService getWorkflowHistoryService() throws BusinessException {
		return this.getWorkflowEngine().getHistoryService();
	}

	public FormService getWorkflowFormService() throws BusinessException {
		return this.getWorkflowEngine().getFormService();
	}

	public void doStartWfProcessInstanceByKey(String processDefinitionKey,
			String businessKey, Map<String, Object> variables)
			throws BusinessException {
		this.getWorkflowRuntimeService().startProcessInstanceByKey(
				processDefinitionKey, businessKey, variables);
	}

	public void doStartWfProcessInstanceById(String processDefinitionId,
			String businessKey, Map<String, Object> variables)
			throws BusinessException {
		this.getWorkflowRuntimeService().startProcessInstanceById(
				processDefinitionId, businessKey, variables);
	}

	public void doStartWfProcessInstanceByMessage(String messageName,
			String businessKey, Map<String, Object> processVariables)
			throws BusinessException {
		this.getWorkflowRuntimeService().startProcessInstanceByMessage(
				messageName, businessKey, processVariables);
	}

	protected void sendMessage(String to, Object content) {
		Message<Object> message = MessageBuilder.withPayload(content).build();
		this.getApplicationContext().getBean(to, MessageChannel.class)
				.send(message);
	}

	protected List<Object> collectIds(List<? extends IModelWithId> entities) {
		List<Object> result = new ArrayList<Object>();
		for (IModelWithId e : entities) {
			result.add(e.getId());
		}
		return result;
	}
}
