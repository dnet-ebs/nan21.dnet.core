/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.business;

import java.util.List;

import javax.persistence.EntityManager;

import net.nan21.dnet.core.api.descriptor.IAsgnContext;

public interface IAsgnTxService<E> {

	/**
	 * Saves the selection(s).
	 * 
	 * @throws Exception
	 */
	public void doSave(IAsgnContext ctx, String selectionId, String objectId)
			throws Exception;

	/**
	 * Restores all the changes made by the user to the initial state.
	 * 
	 * @throws Exception
	 */
	public void doReset(IAsgnContext ctx, String selectionId, String objectId)
			throws Exception;

	/**
	 * Add the specified list of IDs to the selected ones.
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void doMoveRight(IAsgnContext ctx, String selectionId,
			List<String> ids) throws Exception;

	/**
	 * Add all the available values to the selected ones.
	 * 
	 * @throws Exception
	 */
	public void doMoveRightAll(IAsgnContext ctx, String selectionId)
			throws Exception;

	/**
	 * Remove the specified list of IDs from the selected ones.
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void doMoveLeft(IAsgnContext ctx, String selectionId,
			List<String> ids) throws Exception;

	/**
	 * Remove all the selected values.
	 * 
	 * @throws Exception
	 */
	public void doMoveLeftAll(IAsgnContext ctx, String selectionId)
			throws Exception;

	/**
	 * Initialize the temporary table with the existing selection. Creates a
	 * record in the TEMP_ASGN table and the existing selections in
	 * TEMP_ASGN_LINE.
	 * 
	 * @return the UUID of the selection
	 * @throws Exception
	 */
	public String doSetup(IAsgnContext ctx, String asgnName, String objectId)
			throws Exception;

	/**
	 * Clean-up the temporary tables.
	 * 
	 * @throws Exception
	 */
	public void doCleanup(IAsgnContext ctx, String selectionId)
			throws Exception;

	public EntityManager getEntityManager();

	public void setEntityManager(EntityManager em);

	public Class<E> getEntityClass();

	public void setEntityClass(Class<E> entityClass);

}