/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.presenter;

import java.util.List;

import net.nan21.dnet.core.api.action.impex.IDsExport;
import net.nan21.dnet.core.api.action.query.IFilterRule;
import net.nan21.dnet.core.api.action.query.IQueryBuilder;
import net.nan21.dnet.core.api.action.query.ISortToken;

public interface IDsReadService<M, F, P> extends IDsRpcService<M, F, P> {

	public M findById(Object id) throws Exception;

	public M findById(Object id, P params) throws Exception;

	public List<M> findByIds(List<Object> ids) throws Exception;

	public List<M> find(F filter) throws Exception;

	public List<M> find(F filter, int resultStart, int resultSize)
			throws Exception;

	public List<M> find(F filter, P params) throws Exception;

	public List<M> find(F filter, P params, int resultStart, int resultSize)
			throws Exception;

	public List<M> find(F filter, List<IFilterRule> filterRules)
			throws Exception;

	public List<M> find(F filter, List<IFilterRule> filterRules,
			int resultStart, int resultSize) throws Exception;

	public List<M> find(F filter, P params, List<IFilterRule> filterRules)
			throws Exception;

	public List<M> find(F filter, P params, List<IFilterRule> filterRules,
			int resultStart, int resultSize) throws Exception;

	public List<M> find(F filter, P params, List<IFilterRule> filterRules,
			List<ISortToken> sortTokens) throws Exception;

	public List<M> find(F filter, P params, List<IFilterRule> filterRules,
			int resultStart, int resultSize, List<ISortToken> sortTokens)
			throws Exception;

	public List<M> find(IQueryBuilder<M, F, P> builder) throws Exception;

	public List<M> find(IQueryBuilder<M, F, P> builder, List<String> fieldNames)
			throws Exception;

	public Long count(IQueryBuilder<M, F, P> builder) throws Exception;

	public void doExport(IQueryBuilder<M, F, P> builder, IDsExport<M> writer)
			throws Exception;

	public IQueryBuilder<M, F, P> createQueryBuilder() throws Exception;

}
