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
import net.nan21.dnet.core.api.action.query.IQueryBuilder;
import net.nan21.dnet.core.api.action.result.IActionResultFind;
import net.nan21.dnet.core.api.action.result.IDsMarshaller;
import net.nan21.dnet.core.api.service.presenter.IAsgnService;
import net.nan21.dnet.core.presenter.action.query.SortToken;
import net.nan21.dnet.core.web.controller.AbstractDnetController;
import net.nan21.dnet.core.web.result.ActionResultFind;

public abstract class AbstractAsgnController<M, F, P> extends
		AbstractDnetController {

	final static Logger logger = LoggerFactory
			.getLogger(AbstractAsgnController.class);

	/**
	 * Default handler for find action.
	 * 
	 * @param resourceName
	 * @param dataformat
	 * @param dataString
	 * @param paramString
	 * @param resultStart
	 * @param resultSize
	 * @param orderByCol
	 * @param orderBySense
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_QUERY_LEFT)
	@ResponseBody
	public String findLeft(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			@RequestParam(value = Constants.REQUEST_PARAM_FILTER, required = false, defaultValue = "{}") String dataString,
			@RequestParam(value = Constants.REQUEST_PARAM_PARAMS, required = false, defaultValue = "{}") String paramString,
			@RequestParam(value = Constants.REQUEST_PARAM_START, required = false, defaultValue = "0") int resultStart,
			@RequestParam(value = Constants.REQUEST_PARAM_SIZE, required = false, defaultValue = "500") int resultSize,
			@RequestParam(value = Constants.REQUEST_PARAM_SORT, required = false, defaultValue = "") String orderByCol,
			@RequestParam(value = Constants.REQUEST_PARAM_SENSE, required = false, defaultValue = "") String orderBySense,
			@RequestParam(value = Constants.REQUEST_PARAM_ORDERBY, required = false, defaultValue = "") String orderBy,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_QUERY_LEFT });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={} data={}",
						new String[] { objectId, selectionId, dataString });
				logger.debug("  --> request-params: {} ",
						new String[] { paramString });
				logger.debug(
						"  --> request-orderBy: sort={}, sense={}, orderBy={}",
						new String[] { orderByCol, orderBySense, orderBy });
				logger.debug("  --> request-result-range: {} ", new String[] {
						resultStart + "", (resultStart + resultSize) + "" });
			}

			this.prepareRequest(request, response);

			this.authorizeAsgnAction(resourceName, "find");

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));
			IDsMarshaller<M, F, P> marshaller = service
					.createMarshaller(dataFormat);

			IQueryBuilder<M, F, P> builder = service.createQueryBuilder()
					.addFetchLimit(resultStart, resultSize);

			if (orderBy != null && !orderBy.equals("")) {
				List<SortToken> sortTokens = marshaller.readListFromString(
						orderBy, SortToken.class);
				builder.addSortInfo(sortTokens);
			} else {
				builder.addSortInfo(orderByCol, orderBySense);
			}

			F filter = marshaller.readFilterFromString(dataString);
			P params = marshaller.readParamsFromString(paramString);

			List<M> list = service.findLeft(selectionId, filter, params,
					builder);
			long totalCount = service.countLeft(selectionId, filter, params,
					builder);

			IActionResultFind result = this.packfindResult(list, params,
					totalCount);
			stopWatch.stop();
			result.setExecutionTime(stopWatch.getTime());

			return marshaller.writeResultToString(result);
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}

	}

	/**
	 * Default handler for find action.
	 * 
	 * @param resourceName
	 * @param dataformat
	 * @param dataString
	 * @param paramString
	 * @param resultStart
	 * @param resultSize
	 * @param orderByCol
	 * @param orderBySense
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_QUERY_RIGHT)
	@ResponseBody
	public String findRight(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			@RequestParam(value = Constants.REQUEST_PARAM_FILTER, required = false, defaultValue = "{}") String dataString,
			@RequestParam(value = Constants.REQUEST_PARAM_PARAMS, required = false, defaultValue = "{}") String paramString,
			@RequestParam(value = Constants.REQUEST_PARAM_START, required = false, defaultValue = "0") int resultStart,
			@RequestParam(value = Constants.REQUEST_PARAM_SIZE, required = false, defaultValue = "500") int resultSize,
			@RequestParam(value = Constants.REQUEST_PARAM_SORT, required = false, defaultValue = "") String orderByCol,
			@RequestParam(value = Constants.REQUEST_PARAM_SENSE, required = false, defaultValue = "") String orderBySense,
			@RequestParam(value = Constants.REQUEST_PARAM_ORDERBY, required = false, defaultValue = "") String orderBy,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_QUERY_RIGHT });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={} data={}",
						new String[] { objectId, selectionId, dataString });
				logger.debug("  --> request-params: {} ",
						new String[] { paramString });
				logger.debug(
						"  --> request-orderBy: sort={}, sense={}, orderBy={}",
						new String[] { orderByCol, orderBySense, orderBy });
				logger.debug("  --> request-result-range: {} ", new String[] {
						resultStart + "", (resultStart + resultSize) + "" });
			}

			this.prepareRequest(request, response);

			this.authorizeAsgnAction(resourceName, "find");

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			IDsMarshaller<M, F, P> marshaller = service
					.createMarshaller(dataFormat);

			IQueryBuilder<M, F, P> builder = service.createQueryBuilder()
					.addFetchLimit(resultStart, resultSize);

			if (orderBy != null && !orderBy.equals("")) {
				List<SortToken> sortTokens = marshaller.readListFromString(
						orderBy, SortToken.class);
				builder.addSortInfo(sortTokens);
			} else {
				builder.addSortInfo(orderByCol, orderBySense);
			}

			F filter = marshaller.readFilterFromString(dataString);
			P params = marshaller.readParamsFromString(paramString);

			List<M> list = service.findRight(selectionId, filter, params,
					builder);
			long totalCount = service.countRight(selectionId, filter, params,
					builder);

			IActionResultFind result = this.packfindResult(list, params,
					totalCount);
			stopWatch.stop();
			result.setExecutionTime(stopWatch.getTime());

			return marshaller.writeResultToString(result);
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}
	}

	/**
	 * 
	 * @param resourceName
	 * @param dataFormat
	 * @param objectId
	 * @param selectionId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_SETUP)
	@ResponseBody
	public String setup(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_SETUP });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={}",
						new String[] { objectId, selectionId });
			}

			this.prepareRequest(request, response);

			this.authorizeAsgnAction(resourceName, "find");

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			return service.setup(resourceName, objectId);
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}

	}

	/**
	 * 
	 * @param resourceName
	 * @param dataFormat
	 * @param objectId
	 * @param selectionId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_MOVE_LEFT)
	@ResponseBody
	public String moveLeft(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			@RequestParam(value = "p_selected_ids", required = true) String selectedIds,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_MOVE_LEFT });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={}, selectedIds={} ",
						new String[] { objectId, selectionId, selectedIds });
			}

			this.prepareRequest(request, response);

			this.authorizeAsgnAction(resourceName, "update");

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			service.moveLeft(selectionId, this.selectedIdsAsList(selectedIds));
			stopWatch.stop();

			return "";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}

	}

	/**
	 * 
	 * @param resourceName
	 * @param dataFormat
	 * @param objectId
	 * @param selectionId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_MOVE_RIGHT)
	@ResponseBody
	public String moveRight(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			@RequestParam(value = "p_selected_ids", required = true) String selectedIds,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_MOVE_RIGHT });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={}, selectedIds={} ",
						new String[] { objectId, selectionId, selectedIds });
			}

			this.prepareRequest(request, response);

			this.authorizeAsgnAction(resourceName, "update");

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			service.moveRight(selectionId, this.selectedIdsAsList(selectedIds));
			stopWatch.stop();

			return "";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}
	}

	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_MOVE_LEFT_ALL)
	@ResponseBody
	public String moveLeftAll(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			@RequestParam(value = "data", required = false, defaultValue = "{}") String dataString,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String paramString,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_MOVE_LEFT_ALL });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={} data={}",
						new String[] { objectId, selectionId, dataString });
				logger.debug("  --> request-params: {} ",
						new String[] { paramString });
			}

			this.prepareRequest(request, response);

			this.authorizeAsgnAction(resourceName, "update");

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			IDsMarshaller<M, F, P> marshaller = service
					.createMarshaller(dataFormat);

			F filter = marshaller.readFilterFromString(dataString);
			P params = marshaller.readParamsFromString(paramString);

			service.moveLeftAll(selectionId, filter, params);
			stopWatch.stop();

			return "";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}
	}

	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_MOVE_RIGHT_ALL)
	@ResponseBody
	public String moveRightAll(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			@RequestParam(value = "data", required = false, defaultValue = "{}") String dataString,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String paramString,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_MOVE_RIGHT_ALL });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={} data={}",
						new String[] { objectId, selectionId, dataString });
				logger.debug("  --> request-params: {} ",
						new String[] { paramString });
			}

			this.prepareRequest(request, response);

			this.authorizeAsgnAction(resourceName, "update");

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			IDsMarshaller<M, F, P> marshaller = service
					.createMarshaller(dataFormat);

			F filter = marshaller.readFilterFromString(dataString);
			P params = marshaller.readParamsFromString(paramString);

			service.moveRightAll(selectionId, filter, params);
			stopWatch.stop();

			return "";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}

	}

	/**
	 * Cancel changes
	 * 
	 * @param resourceName
	 * @param dataFormat
	 * @param objectId
	 * @param selectionId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_RESET)
	@ResponseBody
	public String reset(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_RESET });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={} ",
						new String[] { objectId, selectionId });
			}

			this.prepareRequest(request, response);

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			service.reset(selectionId, objectId);
			stopWatch.stop();

			return "";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}
	}

	/**
	 * Save changes
	 * 
	 * @param resourceName
	 * @param dataFormat
	 * @param objectId
	 * @param selectionId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_SAVE)
	@ResponseBody
	public String save(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_OBJECT_ID, required = true) String objectId,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_SAVE });
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-filter: objectId={}, selectionId={} ",
						new String[] { objectId, selectionId });
			}

			this.prepareRequest(request, response);

			this.authorizeAsgnAction(resourceName, "update");

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			service.save(selectionId, objectId);
			stopWatch.stop();

			return "";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}
	}

	/**
	 * 
	 * @param resourceName
	 * @param dataFormat
	 * @param selectionId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.ASGN_ACTION_CLEANUP)
	@ResponseBody
	public String delete(
			@PathVariable String resourceName,
			@PathVariable String dataFormat,
			@RequestParam(value = Constants.REQUEST_PARAM_ASGN_SELECTION_ID, required = true) String selectionId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			if (logger.isInfoEnabled()) {
				logger.info("Processing request: {}.{} -> action = {} ",
						new String[] { resourceName, dataFormat,
								Constants.ASGN_ACTION_CLEANUP });
			}

			if (logger.isDebugEnabled()) {
				logger.debug("  --> request-filter: selectionId={} ",
						new String[] { selectionId });
			}

			this.prepareRequest(request, response);

			IAsgnService<M, F, P> service = this.findAsgnService(this
					.serviceNameFromResourceName(resourceName));

			service.cleanup(selectionId);
			stopWatch.stop();

			return "";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}
	}

	protected String serviceNameFromResourceName(String resourceName) {
		return resourceName;
	}

	protected List<String> selectedIdsAsList(String selectedIds) {
		String[] tmp = selectedIds.split(",");
		List<String> ids = new ArrayList<String>();
		for (String i : tmp) {
			ids.add(i);
		}
		return ids;
	}

	public IActionResultFind packfindResult(List<M> data, P params,
			long totalCount) {
		IActionResultFind pack = new ActionResultFind();
		pack.setData(data);
		pack.setParams(params);
		pack.setTotalCount(totalCount);
		return pack;
	}

	public IAsgnService<M, F, P> findAsgnService(String asgnName)
			throws Exception {
		return this.getServiceLocator().findAsgnService(asgnName);
	}

}
