package net.nan21.dnet.core.presenter.service.job;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.SysParams_Core;
import net.nan21.dnet.core.api.security.DnetClient;
import net.nan21.dnet.core.api.security.DnetUser;
import net.nan21.dnet.core.api.security.DnetUserProfile;
import net.nan21.dnet.core.api.security.DnetUserSettings;
import net.nan21.dnet.core.api.security.IClient;
import net.nan21.dnet.core.api.security.IUser;
import net.nan21.dnet.core.api.security.IUserProfile;
import net.nan21.dnet.core.api.security.IUserSettings;
import net.nan21.dnet.core.api.security.IWorkspace;
import net.nan21.dnet.core.api.service.IClientInfoProvider;
import net.nan21.dnet.core.api.service.IPersistableLog;
import net.nan21.dnet.core.api.service.IPersistableLogService;
import net.nan21.dnet.core.api.service.PersistableLog;
import net.nan21.dnet.core.api.service.job.IJob;
import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.presenter.service.AbstractPresenterBaseService;

public abstract class AbstractPresenterJob extends AbstractPresenterBaseService
		implements IJob {

	private JobExecutionContext executionContext;
	private IPersistableLog persistableLog;

	final static Logger logger = LoggerFactory
			.getLogger(AbstractPresenterJob.class);

	@Override
	public final void execute() throws Exception {
		try {
			this.beforeExecute();
			this.onExecute();
			this.afterExecute();
		} catch (Exception e) {
			e.printStackTrace();
			throw (e);
		} finally {
			Session.user.set(null);
		}
	}

	private void beforeExecute() throws Exception {

		String clientId = executionContext.getJobDetail().getKey().getGroup();
		String userCode = this.getSettings().getParam(
				SysParams_Core.CORE_JOB_USER);

		if (logger.isInfoEnabled()) {
			logger.info("Starting job " + this.getClass().getCanonicalName());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Executing job {}: \n - clientId={} \n - user={} ",
					new String[] { this.getClass().getCanonicalName(),
							clientId, userCode });
		}

		IClient client = new DnetClient(clientId, "", "");
		IUserSettings settings;
		settings = DnetUserSettings.newInstance(this.getSettings());
		IUserProfile profile = new DnetUserProfile(true, null, false, false,
				false);

		// create an incomplete user first
		IUser user = new DnetUser(userCode, userCode, null, userCode, null,
				null, client, settings, profile, null, true);
		Session.user.set(user);

		// get the client workspace info
		IWorkspace ws = this.getApplicationContext()
				.getBean(IClientInfoProvider.class).getClientWorkspace();
		user = new DnetUser(userCode, userCode, null, userCode, null, null,
				client, settings, profile, ws, true);
		Session.user.set(user);

		// ready to proceed ...
		BeanUtils.populate(this, this.executionContext.getJobDetail()
				.getJobDataMap().getWrappedMap());
		this.createPersistableLog();

	}

	protected IPersistableLog getPersistableLog() {
		return this.persistableLog;
	}

	private void createPersistableLog() {
		PersistableLog l = new PersistableLog();

		l.setProperty(PLK_START_TIME, new Date());
		l.setProperty(PLK_JOB_CONTEXT, this.executionContext.getJobDetail()
				.getKey().getName());
		l.setProperty(PLK_JOB_TIMER, this.executionContext.getTrigger()
				.getKey().getName());
		this.persistableLog = l;
	}

	protected abstract void onExecute() throws Exception;

	private void afterExecute() throws Exception {
		try {
			this.persistableLog.setProperty(PLK_END_TIME, new Date());
			for (IPersistableLogService s : getPersistableLogServices()) {
				if (s.getType().equals(IPersistableLogService.PL_TYPE_JOB)) {
					s.insert(persistableLog);
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("Job " + this.getClass().getCanonicalName()
						+ " executed succesfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setExecutionContext(Object executionContext) {
		this.executionContext = (JobExecutionContext) executionContext;
	}

	@SuppressWarnings("unchecked")
	public List<IPersistableLogService> getPersistableLogServices() {
		return (List<IPersistableLogService>) this.getApplicationContext()
				.getBean(Constants.SPRING_OSGI_PERSISTABLE_LOG_SERVICES);
	}

}
