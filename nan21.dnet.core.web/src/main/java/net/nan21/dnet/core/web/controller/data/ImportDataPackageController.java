package net.nan21.dnet.core.web.controller.data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.action.impex.IImportDataPackage;
import net.nan21.dnet.core.api.service.business.IImportDataPackageService;
import net.nan21.dnet.core.presenter.action.impex.DataFile;
import net.nan21.dnet.core.presenter.action.impex.DataPackage;
import net.nan21.dnet.core.web.controller.AbstractDnetController;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = Constants.CTXPATH_DS)
public class ImportDataPackageController extends AbstractDnetController {

	final static Logger logger = LoggerFactory
			.getLogger(ImportDataPackageController.class);

	/**
	 * Import data from a data-file or a data-package located on the server.
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/"
			+ Constants.DS_ACTION_IMPORT)
	@ResponseBody
	public String importData(
			@RequestParam(value = "dataPackage", required = false) String dataPackage,
			@RequestParam(value = "dataFile", required = false) String dataFile,
			@RequestParam(value = "dsName", required = false) String dsName,
			@RequestParam(value = "ukFieldName", required = false) String ukFieldName,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		if (logger.isInfoEnabled()) {
			logger.info("Processing request: import data from file(s) located on server");
		}

		if (logger.isDebugEnabled()) {
			if (dataPackage != null) {
				logger.debug("  --> dataPackage: {} ",
						new String[] { dataPackage });
			} else {
				logger.debug(
						"  --> dataFile: {}, dsName: {}, ukFieldName: {} ",
						new String[] { dataFile, dsName, ukFieldName });
			}
		}

		this.prepareRequest(request, response);
		try {
			IImportDataPackage dp = null;
			if (dataPackage != null && !"".equals(dataPackage)) {
				dp = DataPackage.forIndexFile(dataPackage);
			} else if (dataFile != null && !"".equals(dataFile)) {
				DataFile df = new DataFile();
				df.setDs(dsName);
				df.setFile(dataFile);
				df.setUkFieldName(ukFieldName);
				dp = DataPackage.forDataFile(df);
			}

			if (dp != null) {
				this.getApplicationContext()
						.getBean(IImportDataPackageService.class).doExecute(dp);
			}
			return "{\"success\":true}";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}

	}
}
