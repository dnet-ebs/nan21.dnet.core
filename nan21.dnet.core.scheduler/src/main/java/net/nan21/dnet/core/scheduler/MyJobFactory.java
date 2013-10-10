package net.nan21.dnet.core.scheduler;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyJobFactory extends SimpleJobFactory implements
		ApplicationContextAware {

	private final Logger logger = LoggerFactory.getLogger(MyJobFactory.class);

	private ApplicationContext applicationContext;

	protected ServiceLocator serviceLocator;

	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler)
			throws SchedulerException {

		IDnetQuartzJobDetail job = null;

		JobDetail jobDetail = bundle.getJobDetail();//clientId = jobDetail.group
		Class<?> jobClass = jobDetail.getJobClass();
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Producing instance of Job '" + jobDetail.getKey()
						+ "', class=" + jobClass.getName());
			}
			jobClass = this.getClass().getClassLoader()
					.loadClass(jobClass.getCanonicalName());
			job = (IDnetQuartzJobDetail) jobClass.newInstance();
			job.setServiceLocator(this.serviceLocator);
			return job;
		} catch (Exception e) {
			SchedulerException se = new SchedulerException(
					"Problem instantiating class '"
							+ jobDetail.getJobClass().getName() + "'", e);
			throw se;
		}

	}

	public ServiceLocator getServiceLocator() {
		if (this.serviceLocator == null) {
			this.serviceLocator = this.applicationContext
					.getBean(ServiceLocator.class);
		}
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}
}
