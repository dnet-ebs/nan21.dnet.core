/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.descriptor;

import java.util.Map;

public interface IViewModelDescriptor<M> {

	public Class<M> getModelClass();

	public Map<String, String> getRefPaths();

	public boolean isWorksWithJpql();

	public String getJpqlDefaultWhere();

	public String getJpqlDefaultSort();

	public Map<String, String> getJpqlFieldFilterRules();

	public Map<String, String> getFetchJoins();

	public Map<String, Object> getQueryHints();

	public Map<String, String[]> getOrderBys();
}
