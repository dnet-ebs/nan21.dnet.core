package net.nan21.dnet.core.business.service.other;

import java.io.File;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.action.impex.IImportDataFile;
import net.nan21.dnet.core.api.action.impex.IImportDataPackage;
import net.nan21.dnet.core.api.action.impex.IImportDataSet;
import net.nan21.dnet.core.api.service.business.IImportDataPackageService;
import net.nan21.dnet.core.business.service.AbstractBusinessBaseService;

public class ImportDataPackageService extends AbstractBusinessBaseService
		implements IImportDataPackageService {

	/**
	 * Import a data-package.
	 */
	@Override
	public void doExecute(IImportDataPackage dataPackage) throws Exception {

		for (IImportDataSet set : dataPackage.getDataSets()) {

			for (IImportDataFile df : set.getDataFiles()) {

				String fileName = df.getFile();

				if (fileName == null || fileName.equals("")) {
					fileName = df.getDs() + ".csv";
				}
				fileName = dataPackage.getLocation() + File.separator
						+ fileName;
				df.setFile(fileName);

				this.sendMessage(Constants.SPRING_MSG_IMPORT_DATA_FILE, df);
			}
		}
	}

}
