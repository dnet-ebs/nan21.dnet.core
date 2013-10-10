/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

public interface IAuthorization {

	public static final String DS_ACTION_QUERY = "query";
	public static final String DS_ACTION_INSERT = "insert";
	public static final String DS_ACTION_UPDATE = "update";
	public static final String DS_ACTION_DELETE = "delete";
	public static final String DS_ACTION_IMPORT = "import";
	public static final String DS_ACTION_EXPORT = "export";
	public static final String DS_ACTION_SERVICE = "service";

	public static final String ASGN_ACTION_READ = "read";
	public static final String ASGN_ACTION_WRITE = "write";

	public static final String JOB_ACTION_EXECUTE = "execute";

	/**
	 * Action authorization. The third parameter applies only for data-source
	 * RPC calls, in other situations send null.
	 * 
	 * @param resourceName
	 * @param action
	 * @param rpcMethod
	 * @throws Exception
	 */
	public void authorize(String resourceName, String action, String rpcMethod)
			throws Exception;

}
