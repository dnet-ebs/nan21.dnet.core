package net.nan21.dnet.core.api.service.job;

public interface IJob {

	/**
	 * persistable log key: job
	 */
	public static final String PLK_JOB = "job";
	/**
	 * persistable log key: job-context
	 */
	public static final String PLK_JOB_CONTEXT = "job-context";
	/**
	 * persistable log key: job-timer
	 */
	public static final String PLK_JOB_TIMER = "job-timer";

	/**
	 * persistable log key: start-time
	 */
	public static final String PLK_START_TIME = "start-time";

	/**
	 * persistable log key: end-time
	 */
	public static final String PLK_END_TIME = "end-time";

	/**
	 * Set job execution context, probably a Quartz execution context
	 * 
	 * @param executionContext
	 */
	public void setExecutionContext(Object executionContext);

	/**
	 * Main job execution entry point
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception;
}
