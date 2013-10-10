package net.nan21.dnet.core.api.service.business;

import net.nan21.dnet.core.api.action.impex.IImportDataPackage;

/**
 * Interface for a transactional service to import an {@link IImportDataPackage}
 * . Execution must be started in a transaction, so that all data-files imported
 * from this package can participate in it.
 * 
 * @author amathe
 * 
 */
public interface IImportDataPackageService {

	public void doExecute(IImportDataPackage dataPackage) throws Exception;
}
