/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller.data;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.action.result.IActionResultDelete;
import net.nan21.dnet.core.api.action.result.IActionResultSave;
import net.nan21.dnet.core.api.action.result.IDsMarshaller;
import net.nan21.dnet.core.api.model.IModelWithId;
import net.nan21.dnet.core.api.service.presenter.IDsService;
import net.nan21.dnet.core.web.result.ActionResultDelete;
import net.nan21.dnet.core.web.result.ActionResultSave;

public class AbstractDsWriteController<M, F, P> extends
		AbstractDsRpcController<M, F, P> {

	final static Logger logger = LoggerFactory
			.getLogger(AbstractDsWriteController.class);

	/**
	 * Default handler for insert action.
	 * 
	 * @param resourceName
	 * @param dataformat
	 * @param dataString
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.DS_ACTION_INSERT)
	@ResponseBody
	public String insert(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_DATA, required = false, defaultValue = "{}") String dataString,
			@RequestParam(value = Constants.REQUEST_PARAM_PARAMS, required = false, defaultValue = "{}") String paramString,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.DS_ACTION_INSERT });
			}

			if (logger.isDebugEnabled()) {
				logger.debug("  --> request-data: {} ",
						new String[] { dataString });
				logger.debug("  --> request-params: {} ",
						new String[] { paramString });
			}

			this.prepareRequest(request, response);

			this.authorizeDsAction(resourceName, Constants.DS_ACTION_INSERT,
					null);

			if (!dataString.startsWith("[")) {
				dataString = "[" + dataString + "]";
			}

			IDsService<M, F, P> service = this.findDsService(resourceName);
			IDsMarshaller<M, F, P> marshaller = service
					.createMarshaller(IDsMarshaller.JSON);

			List<M> list = marshaller.readListFromString(dataString);
			P params = marshaller.readParamsFromString(paramString);

			service.insert(list, params);

			IActionResultSave result = this.packResultSave(list, params);
			stopWatch.stop();
			result.setExecutionTime(stopWatch.getTime());

			String out = null;

			if (dataFormat.equals(IDsMarshaller.XML)) {
				IDsMarshaller<M, F, P> resultMarshaller = service
						.createMarshaller(dataFormat);
				out = resultMarshaller.writeResultToString(result);
				response.setContentType("text/xml; charset=UTF-8");
			} else {
				out = marshaller.writeResultToString(result);
				response.setContentType("text/plain; charset=UTF-8");
			}

			return out;

		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}
	}

	/**
	 * Default handler for update action.
	 * 
	 * @param resourceName
	 * @param dataformat
	 * @param dataString
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.DS_ACTION_UPDATE)
	@ResponseBody
	public String update(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_DATA, required = false, defaultValue = "{}") String dataString,
			@RequestParam(value = Constants.REQUEST_PARAM_PARAMS, required = false, defaultValue = "{}") String paramString,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.DS_ACTION_UPDATE });
			}

			if (logger.isDebugEnabled()) {
				logger.debug("  --> request-data: {} ",
						new String[] { dataString });
				logger.debug("  --> request-params: {} ",
						new String[] { paramString });
			}

			this.prepareRequest(request, response);

			this.authorizeDsAction(resourceName, Constants.DS_ACTION_UPDATE,
					null);

			if (!dataString.startsWith("[")) {
				dataString = "[" + dataString + "]";
			}
			IDsService<M, F, P> service = this.findDsService(resourceName);
			IDsMarshaller<M, F, P> marshaller = service
					.createMarshaller(IDsMarshaller.JSON);

			List<M> list = marshaller.readListFromString(dataString);
			P params = marshaller.readParamsFromString(paramString);

			service.update(list, params);

			IActionResultSave result = this.packResultSave(list, params);
			stopWatch.stop();
			result.setExecutionTime(stopWatch.getTime());

			String out = null;

			if (dataFormat.equals(IDsMarshaller.XML)) {
				IDsMarshaller<M, F, P> resultMarshaller = service
						.createMarshaller(dataFormat);
				out = resultMarshaller.writeResultToString(result);
				response.setContentType("text/xml; charset=UTF-8");
			} else {
				out = marshaller.writeResultToString(result);
				response.setContentType("text/plain; charset=UTF-8");
			}

			return out;
		} catch (Exception e) {
			this.handleException(e, response);
			return null;
		} finally {
			this.finishRequest();
		}
	}

	/**
	 * Default handler for delete action.
	 * 
	 * @param resourceName
	 * @param dataformat
	 * @param idsString
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.DS_ACTION_DELETE)
	@ResponseBody
	public String delete(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_DATA, required = false, defaultValue = "{}") String dataString,
			@RequestParam(value = Constants.REQUEST_PARAM_PARAMS, required = false, defaultValue = "{}") String paramString,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.DS_ACTION_DELETE });
			}

			this.prepareRequest(request, response);

			this.authorizeDsAction(resourceName, Constants.DS_ACTION_DELETE,
					null);

			if (!dataString.startsWith("[")) {
				dataString = "[" + dataString + "]";
			}
			IDsService<M, F, P> service = this.findDsService(resourceName);
			IDsMarshaller<M, F, P> marshaller = service
					.createMarshaller(IDsMarshaller.JSON);

			List<M> list = marshaller.readListFromString(dataString);
			// P params = marshaller.readParamsFromString(paramString);

			List<Object> ids = new ArrayList<Object>();
			for (M ds : list) {
				ids.add(((IModelWithId) ds).getId());
			}
			service.deleteByIds(ids);

			IActionResultDelete result = this.packResultDelete();
			stopWatch.stop();
			result.setExecutionTime(stopWatch.getTime());

			String out = null;

			if (dataFormat.equals(IDsMarshaller.XML)) {
				IDsMarshaller<M, F, P> resultMarshaller = service
						.createMarshaller(dataFormat);
				out = resultMarshaller.writeResultToString(result);
				response.setContentType("text/xml; charset=UTF-8");
			} else {
				out = marshaller.writeResultToString(result);
				response.setContentType("text/plain; charset=UTF-8");
			}

			return out;

		} catch (Exception e) {
			this.handleException(e, response);
			return null;
		} finally {
			this.finishRequest();
		}
	}

	/**
	 * Default handler for delete action.
	 * 
	 * @param resourceName
	 * @param dataformat
	 * @param idsString
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.DS_ACTION_DELETE + "ById")
	@ResponseBody
	public String deleteById(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_DATA, required = false, defaultValue = "{}") String idsString,
			@RequestParam(value = Constants.REQUEST_PARAM_PARAMS, required = false, defaultValue = "{}") String paramString,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			this.prepareRequest(request, response);

			this.authorizeDsAction(resourceName, Constants.DS_ACTION_DELETE,
					null);

			if (!idsString.startsWith("[")) {
				idsString = "[" + idsString + "]";
			}
			IDsService<M, F, P> service = this.findDsService(resourceName);
			IDsMarshaller<M, F, P> marshaller = service
					.createMarshaller(IDsMarshaller.JSON);

			List<Object> list = marshaller.readListFromString(idsString,
					Object.class);

			service.deleteByIds(list);

			IActionResultDelete result = this.packResultDelete();
			stopWatch.stop();
			result.setExecutionTime(stopWatch.getTime());

			String out = null;

			if (dataFormat.equals(IDsMarshaller.XML)) {
				IDsMarshaller<M, F, P> resultMarshaller = service
						.createMarshaller(dataFormat);
				out = resultMarshaller.writeResultToString(result);
				response.setContentType("text/xml; charset=UTF-8");
			} else {
				out = marshaller.writeResultToString(result);
				response.setContentType("text/plain; charset=UTF-8");
			}

			return out;
		} catch (Exception e) {
			this.handleException(e, response);
			return null;
		} finally {
			this.finishRequest();
		}
	}

	public IActionResultSave packResultSave(List<M> data, P params) {
		IActionResultSave pack = new ActionResultSave();
		pack.setData(data);
		pack.setParams(params);
		return pack;
	}

	public IActionResultDelete packResultDelete() {
		IActionResultDelete pack = new ActionResultDelete();
		return pack;
	}

}
