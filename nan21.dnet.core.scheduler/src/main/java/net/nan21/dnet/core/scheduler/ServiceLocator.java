package net.nan21.dnet.core.scheduler;

import java.util.List;

import net.nan21.dnet.core.api.service.job.IJob;
import net.nan21.dnet.core.api.service.job.IJobFactory;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceLocator implements ApplicationContextAware {

	/**
	 * Spring application context
	 */
	protected ApplicationContext applicationContext;

	/**
	 * Job factories exported by bundles which declare jobs.
	 */
	private List<IJobFactory> jobFactories;

	public IJob findJob(String name) throws Exception {
		IJob srv = null;
		for (IJobFactory f : jobFactories) {
			try {
				srv = f.create(name);
				if (srv != null) {
					// srv.setDsServiceFactories(factories);
					// srv.setSystemConfig(this.systemConfig);
					return srv;
				}
			} catch (NoSuchBeanDefinitionException e) {
				// service not found in this factory, ignore
			}
		}
		return null;
	}

	/**
	 * Get job factories. If it is null attempts to retrieve it from Spring
	 * context by <code>osgiEntityJobFactories</code> alias.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IJobFactory> getEntityJobFactories() {
		if (this.jobFactories == null) {
			this.jobFactories = (List<IJobFactory>) this.applicationContext
					.getBean("osgiEntityJobFactories");
		}
		return this.jobFactories;
	}

	/**
	 * Set job factories
	 * 
	 * @param entityServiceFactories
	 */
	public void setJobFactories(List<IJobFactory> jobFactories) {
		this.jobFactories = jobFactories;
	}

	/**
	 * Get applicationContext
	 * 
	 * @return
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * Set applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
