package net.nan21.dnet.core.api.descriptor;

import java.util.Collection;

public interface IJobDefinitions {

	/**
	 * Check if this context contains the given job definition.
	 * 
	 * @param name
	 * @return
	 */
	public boolean containsJob(String name);

	/**
	 * Returns the definition for the given job name.
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public IJobDefinition getJobDefinition(String name) throws Exception;

	/**
	 * Returns a collection with all the job definitions from this context.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<IJobDefinition> getJobDefinitions() throws Exception;

}
