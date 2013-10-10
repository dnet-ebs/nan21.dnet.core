/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.presenter;

import java.io.InputStream;
import java.util.List;

public interface IDsWriteService<M, F, P> extends IDsReadService<M, F, P> {

	/**
	 * Handler for insert event.
	 * 
	 * @param ds
	 *            data-source instance
	 */
	public void insert(M ds, P params) throws Exception;

	/**
	 * Handler for insert event.
	 * 
	 * @param list
	 *            data-source instances list
	 */
	public void insert(List<M> list, P params) throws Exception;

	/**
	 * Handler for update event.
	 * 
	 * @param ds
	 *            data-source instance
	 */
	public void update(M ds, P params) throws Exception;

	/**
	 * Handler for update event.
	 * 
	 * @param list
	 *            data-source instances list
	 */
	public void update(List<M> list, P params) throws Exception;

	// public void delete(M id) throws Exception;
	// public void delete(List<M> list) throws Exception;

	/**
	 * Handler for delete given a data-source id.
	 * 
	 * @param id
	 */
	public void deleteById(Object id) throws Exception;

	/**
	 * Handler for delete given a list of data-source id`s.
	 * 
	 * @param ids
	 */
	public void deleteByIds(List<Object> ids) throws Exception;

	/**
	 * Handler for basic data import given an input stream. Performs an insert.
	 * 
	 * @param inputStream
	 * @param sourceName
	 * @throws Exception
	 */
	public void doImport(InputStream inputStream, String sourceName,
			Object config) throws Exception;

	/**
	 * Handler for basic data import given a file-name as absolute location.
	 * Performs an insert.
	 * 
	 * @param absoluteFileName
	 */
	public void doImport(String absoluteFileName, Object config)
			throws Exception;

	/**
	 * Handler for basic data import given a file-name and directory where it
	 * resides. Performs an insert.
	 * 
	 * @param relativeFileName
	 * @param path
	 */
	public void doImport(String relativeFileName, String path, Object config)
			throws Exception;

	/**
	 * Handler for basic data import given a file-name and directory where it
	 * resides. <br>
	 * Performs an update. <br>
	 * Tries to read the value from the database using the
	 * <code>ukFieldName</code> as an unique key field, apply the changes read
	 * from the file to be imported and updates the result.
	 * 
	 * @param relativeFileName
	 * @param path
	 */
	public void doImport(String absoluteFileName, String ukFieldName,
			int batchSize, Object config) throws Exception;

	/**
	 * Handler for basic data import given a file-name and directory where it
	 * resides. <br>
	 * Performs an update. <br>
	 * Tries to read the value from the database using the
	 * <code>ukFieldName</code> as an unique key field, apply the changes read
	 * from the file to be imported and updates the result.
	 * 
	 * @param relativeFileName
	 * @param path
	 */
	public void doImport(String relativeFileName, String path,
			String ukFieldName, int batchSize, Object config) throws Exception;

}
