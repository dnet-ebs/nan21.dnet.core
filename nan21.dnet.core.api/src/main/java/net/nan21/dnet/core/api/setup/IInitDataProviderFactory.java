/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.setup;

public interface IInitDataProviderFactory {

	public IInitDataProvider createProvider();

	public String getName();

	public void setName(String name);
}
