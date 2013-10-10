/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.setup;

import java.util.List;

public interface IInitData {

	public abstract String getSequence();

	public abstract void setSequence(String sequence);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract List<IInitDataItem> getItems();

	public abstract void setItems(List<IInitDataItem> items);

	public abstract boolean isMandatory();

	public abstract void setMandatory(boolean mandatory);

}