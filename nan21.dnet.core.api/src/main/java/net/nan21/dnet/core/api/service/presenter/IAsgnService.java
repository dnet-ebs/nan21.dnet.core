/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.presenter;

import java.util.List;

import net.nan21.dnet.core.api.ISettings;
import net.nan21.dnet.core.api.action.query.IQueryBuilder;
import net.nan21.dnet.core.api.action.result.IDsMarshaller;
import net.nan21.dnet.core.api.service.business.IAsgnTxServiceFactory;

public interface IAsgnService<M, F, P> {

	/**
	 * Saves the selection(s).
	 * 
	 * @throws Exception
	 */
	public void save(String selectionId, String objectId) throws Exception;

	/**
	 * Restores all the changes made by the user to the initial state.
	 * 
	 * @throws Exception
	 */
	public void reset(String selectionId, String objectId) throws Exception;

	/**
	 * Add the specified list of IDs to the selected ones.
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void moveRight(String selectionId, List<String> ids)
			throws Exception;

	/**
	 * Add all the available values to the selected ones.
	 * 
	 * @throws Exception
	 */
	public void moveRightAll(String selectionId, F filter, P params)
			throws Exception;

	/**
	 * Remove the specified list of IDs from the selected ones.
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void moveLeft(String selectionId, List<String> ids) throws Exception;

	/**
	 * Remove all the selected values.
	 * 
	 * @throws Exception
	 */
	public void moveLeftAll(String selectionId, F filter, P params)
			throws Exception;

	/**
	 * Initialize the temporary table with the existing selection. Creates a
	 * record in the TEMP_ASGN table and the existing selections in
	 * TEMP_ASGN_LINE.
	 * 
	 * @return the UUID of the selection
	 * @throws Exception
	 */
	public String setup(String asgnName, String objectId) throws Exception;

	/**
	 * Clean-up the temporary tables.
	 * 
	 * @throws Exception
	 */
	public void cleanup(String selectionId) throws Exception;

	public List<M> findLeft(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception;

	public List<M> findRight(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception;

	public Long countLeft(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception;

	public Long countRight(String selectionId, F filter, P params,
			IQueryBuilder<M, F, P> builder) throws Exception;

	public IQueryBuilder<M, F, P> createQueryBuilder() throws Exception;

	public IDsMarshaller<M, F, P> createMarshaller(String dataFormat)
			throws Exception;

	public List<IAsgnTxServiceFactory> getAsgnTxServiceFactories();

	public void setAsgnTxServiceFactories(
			List<IAsgnTxServiceFactory> asgnTxServiceFactories);

	public ISettings getSettings();

	public void setSettings(ISettings settings);

}
