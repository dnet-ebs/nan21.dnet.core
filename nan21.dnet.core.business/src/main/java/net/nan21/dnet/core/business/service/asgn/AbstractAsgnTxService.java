package net.nan21.dnet.core.business.service.asgn;

import java.util.List;
import java.util.UUID;

import net.nan21.dnet.core.api.descriptor.IAsgnContext;
import net.nan21.dnet.core.business.service.AbstractBusinessBaseService;
import net.nan21.dnet.core.domain.impl.TempAsgn;
import net.nan21.dnet.core.domain.impl.TempAsgnLine;

public abstract class AbstractAsgnTxService<E> extends
		AbstractBusinessBaseService {

	private Class<E> entityClass;

	protected final String ASGN_TEMP_TABLE = TempAsgn.TABLE_NAME;
	protected final String ASGNLINE_TEMP_TABLE = TempAsgnLine.TABLE_NAME;

	protected boolean saveAsSqlInsert = true;

	/**
	 * Add the specified list of IDs to the selected ones.
	 * 
	 * @param ids
	 * @throws Exception
	 */

	public void doMoveRight(IAsgnContext ctx, String selectionId,
			List<String> ids) throws Exception {
		StringBuffer sb = new StringBuffer("('-1'");
		for (String id : ids) {
			sb.append(",'" + id + "'");
		}
		sb.append(")");

		this.getEntityManager()
				.createNativeQuery(
						"insert into " + this.ASGNLINE_TEMP_TABLE
								+ " (selection_id, itemId)" + " select ?, "
								+ ctx.getLeftPkField() + " from "
								+ ctx.getLeftTable() + " r where r."
								+ ctx.getLeftPkField() + "  in "
								+ sb.toString()).setParameter(1, selectionId)
				.executeUpdate();
		this.getEntityManager().flush();
	}

	/**
	 * Add all the available values to the selected ones.
	 * 
	 * @throws Exception
	 */
	public void doMoveRightAll(IAsgnContext ctx, String selectionId)
			throws Exception {
		doMoveLeftAll(ctx, selectionId);
		this.getEntityManager()
				.createNativeQuery(
						"insert into " + this.ASGNLINE_TEMP_TABLE
								+ " ( selection_id, itemId)" + " select  ?,  "
								+ ctx.getLeftPkField() + "  from "
								+ ctx.getLeftTable() + " ")
				.setParameter(1, selectionId).executeUpdate();
		this.getEntityManager().flush();
	}

	/**
	 * Remove the specified list of IDs from the selected ones.
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void doMoveLeft(IAsgnContext ctx, String selectionId,
			List<String> ids) throws Exception {
		StringBuffer sb = new StringBuffer("('-1'");
		for (String id : ids) {
			sb.append(",'" + id + "'");
		}
		sb.append(")");
		this.getEntityManager()
				.createNativeQuery(
						"delete from " + this.ASGNLINE_TEMP_TABLE
								+ " WHERE  selection_id = ? and itemId in "
								+ sb.toString() + "")
				.setParameter(1, selectionId).executeUpdate();
		this.getEntityManager().flush();

	}

	/**
	 * Remove all the selected values.
	 * 
	 * @throws Exception
	 */
	public void doMoveLeftAll(IAsgnContext ctx, String selectionId)
			throws Exception {
		this.getEntityManager()
				.createNativeQuery(
						"delete from " + this.ASGNLINE_TEMP_TABLE
								+ " WHERE selection_id = ?")
				.setParameter(1, selectionId).executeUpdate();
		this.getEntityManager().flush();
	}

	/**
	 * Initialize the temporary table with the existing selection. Creates a
	 * record in the TEMP_ASGN table and the existing selections in
	 * TEMP_ASGN_LINE.
	 * 
	 * @return the UUID of the selection
	 * @throws Exception
	 */
	public String doSetup(IAsgnContext ctx, String asgnName, String objectId)
			throws Exception {
		String selectionId = UUID.randomUUID().toString();
		this.getEntityManager()
				.createNativeQuery(
						"insert into " + this.ASGN_TEMP_TABLE
								+ " (id, asgn) values( ? ,?  ) ")
				.setParameter(1, selectionId).setParameter(2, asgnName)
				.executeUpdate();
		this.getEntityManager().flush();
		this.doReset(ctx, selectionId, objectId);
		return selectionId;
	}

	/**
	 * Clean-up the temporary tables.
	 * 
	 * @throws Exception
	 */
	public void doCleanup(IAsgnContext ctx, String selectionId)
			throws Exception {
		this.getEntityManager()
				.createNativeQuery(
						"delete from " + this.ASGNLINE_TEMP_TABLE
								+ "  WHERE   selection_id = ? ")
				.setParameter(1, selectionId).executeUpdate();
		this.getEntityManager()
				.createNativeQuery(
						"delete from " + this.ASGN_TEMP_TABLE
								+ "   WHERE id = ? ")
				.setParameter(1, selectionId).executeUpdate();
		this.getEntityManager().flush();
	}

	/**
	 * Restores all the changes made by the user in the TEMP_ASGN_LINE table to
	 * the initial state.
	 * 
	 * @throws Exception
	 */
	public void doReset(IAsgnContext ctx, String selectionId, String objectId)
			throws Exception {
		this.getEntityManager()
				.createNativeQuery(
						"delete from " + this.ASGNLINE_TEMP_TABLE
								+ " where selection_id = ? ")
				.setParameter(1, selectionId).executeUpdate();
		this.getEntityManager().flush();

		this.getEntityManager()
				.createNativeQuery(
						"insert into " + this.ASGNLINE_TEMP_TABLE
								+ " (selection_id, itemId)" + " select ?, "
								+ ctx.getRightItemIdField() + " from "
								+ ctx.getRightTable() + " where "
								+ ctx.getRightObjectIdField() + " = ? ")
				.setParameter(1, selectionId).setParameter(2, objectId)
				.executeUpdate();
		this.getEntityManager().flush();
	}

	/**
	 * 
	 * @param ctx
	 * @param selectionId
	 * @param objectId
	 * @throws Exception
	 */
	public void doSave(IAsgnContext ctx, String selectionId, String objectId)
			throws Exception {
		this.getEntityManager()
				.createNativeQuery(
						"delete from " + ctx.getRightTable() + " where  "
								+ ctx.getRightObjectIdField() + " = ? ")
				.setParameter(1, objectId).executeUpdate();
		this.getEntityManager().flush();
		if (this.saveAsSqlInsert) {
			this.getEntityManager()
					.createNativeQuery(
							"insert into " + ctx.getRightTable() + " ( "
									+ ctx.getRightObjectIdField() + ",  "
									+ ctx.getRightItemIdField() + " ) "
									+ " select ?, itemId from  "
									+ this.ASGNLINE_TEMP_TABLE + " "
									+ "  where selection_id = ? ")
					.setParameter(1, objectId).setParameter(2, selectionId)
					.executeUpdate();
		} else {
			@SuppressWarnings("unchecked")
			List<Long> list = this
					.getEntityManager()
					.createNativeQuery(
							" select itemId from  " + this.ASGNLINE_TEMP_TABLE
									+ " " + "  where selection_id = ? ")
					.setParameter(1, selectionId).getResultList();
			this.onSave(list);
		}
	}

	protected void onSave(List<Long> ids) throws Exception {
	}

	// ==================== getters- setters =====================

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

}
