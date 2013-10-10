/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.presenter;

import java.io.InputStream;
import java.util.List;

public interface IDsRpcService<M, F, P> extends IDsBaseService<M, F, P> {

	public void rpcFilter(String procedureName, F filter, P params)
			throws Exception;

	public void rpcData(String procedureName, M ds, P params) throws Exception;

	public void rpcData(String procedureName, List<M> list, P params)
			throws Exception;

	public InputStream rpcFilterStream(String procedureName, F filter, P params)
			throws Exception;

	public InputStream rpcDataStream(String procedureName, M ds, P params)
			throws Exception;

	public InputStream rpcDataStream(String procedureName, List<M> list,
			P params) throws Exception;
}
