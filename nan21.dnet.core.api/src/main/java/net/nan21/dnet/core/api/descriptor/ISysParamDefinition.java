/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.descriptor;

public interface ISysParamDefinition {

	public static final String TYPE_STRING = "string";
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_NUMBER = "number";

	public String getName();

	public String getTitle();

	public String getDescription();

	public String getDataType();

	public String getDefaultValue();

	public String getListOfValues();

}