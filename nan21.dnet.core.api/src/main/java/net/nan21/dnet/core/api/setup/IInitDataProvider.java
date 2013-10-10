/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.setup;

import java.util.List;

public interface IInitDataProvider {

	public List<IInitData> getList();

	public void setList(List<IInitData> list);

	public void addToList(IInitData initData);
}
