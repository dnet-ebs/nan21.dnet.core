/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.descriptor;

import java.util.Collection;

public interface IDsDefinitions {

	/**
	 * Check if this context contains the given data-source definition.
	 * 
	 * @param name
	 * @return
	 */
	public boolean containsDs(String name);

	/**
	 * Returns the definition for the given data-source name.
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public IDsDefinition getDsDefinition(String name) throws Exception;

	/**
	 * Returns a collection with all the data-source definitions from this
	 * context.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<IDsDefinition> getDsDefinitions() throws Exception;
}
