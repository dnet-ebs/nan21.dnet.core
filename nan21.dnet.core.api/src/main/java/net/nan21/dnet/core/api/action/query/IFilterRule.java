/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.action.query;

public interface IFilterRule {
	public String getFieldName();

	public void setFieldName(String fieldName);

	public String getOperation();

	public void setOperation(String operation);

	public String getValue1();

	public void setValue1(String value1);

	public String getValue2();

	public void setValue2(String value2);

}
