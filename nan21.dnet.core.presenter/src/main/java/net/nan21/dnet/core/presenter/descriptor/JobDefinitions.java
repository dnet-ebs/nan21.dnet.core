package net.nan21.dnet.core.presenter.descriptor;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import net.nan21.dnet.core.api.descriptor.IJobDefinition;
import net.nan21.dnet.core.api.descriptor.IJobDefinitions;
import net.nan21.dnet.core.api.service.job.IJob;

public class JobDefinitions implements IJobDefinitions, BeanFactoryAware,
		ApplicationContextAware {

	/**
	 * Bean factory reference to read the bean definition from spring context.
	 */
	private BeanFactory beanFactory;

	private ApplicationContext applicationContext;

	@Override
	public boolean containsJob(String name) {
		return this.applicationContext.containsBean(name);
	}

	@Override
	public IJobDefinition getJobDefinition(String name) throws Exception {
		ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;
		JobDefinition definition = createDefinition(name, factory);
		return definition;
	}

	@Override
	public Collection<IJobDefinition> getJobDefinitions() throws Exception {
		Collection<IJobDefinition> result = new ArrayList<IJobDefinition>();

		ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;

		String[] names = factory.getBeanNamesForType(IJob.class);
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			JobDefinition definition = createDefinition(name, factory);
			result.add(definition);
		}

		return result;
	}

	/**
	 * Helper method to create the definition for a single given job.
	 * 
	 * @param name
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	private JobDefinition createDefinition(String name,
			ConfigurableListableBeanFactory factory) throws Exception {
		BeanDefinition beanDef = factory.getBeanDefinition(name);

		JobDefinition definition = new JobDefinition();
		definition.setName(name);
		String BeanClassName = beanDef.getBeanClassName();
		definition.setJavaClass(this.applicationContext.getClassLoader()
				.loadClass(BeanClassName));
		return definition;
	}

	/**
	 * BeanFactory setter
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * ApplicationContext setter
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
