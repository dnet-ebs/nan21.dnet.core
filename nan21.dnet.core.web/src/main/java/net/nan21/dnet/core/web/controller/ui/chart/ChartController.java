package net.nan21.dnet.core.web.controller.ui.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.action.query.IQueryBuilder;
import net.nan21.dnet.core.api.action.result.IDsMarshaller;
import net.nan21.dnet.core.api.security.ISessionUser;
import net.nan21.dnet.core.api.service.presenter.IDsService;
import net.nan21.dnet.core.presenter.action.query.FilterRule;
import net.nan21.dnet.core.presenter.action.query.SortToken;
import net.nan21.dnet.core.web.controller.AbstractDnetController;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChartController extends AbstractDnetController {

	@RequestMapping(value = "/{resourceName}", method = RequestMethod.GET)
	protected ModelAndView home(
			@PathVariable String resourceName,
			@RequestParam(value = "chartType", required = true, defaultValue = "line") String chartType,
			@RequestParam(value = "xField", required = true) String xField,
			@RequestParam(value = "yField", required = true) String yField,
			@RequestParam(value = "title", required = true, defaultValue="") String title,
			@RequestParam(value = Constants.REQUEST_PARAM_FILTER, required = false, defaultValue = "{}") String filterString,
			@RequestParam(value = Constants.REQUEST_PARAM_ADVANCED_FILTER, required = false, defaultValue = "") String filterRulesString,
			@RequestParam(value = Constants.REQUEST_PARAM_PARAMS, required = false, defaultValue = "{}") String paramString,
			@RequestParam(value = Constants.REQUEST_PARAM_SORT, required = false, defaultValue = "") String orderByCol,
			@RequestParam(value = Constants.REQUEST_PARAM_SENSE, required = false, defaultValue = "") String orderBySense,
			@RequestParam(value = Constants.REQUEST_PARAM_ORDERBY, required = false, defaultValue = "") String orderBy,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String jspName = "chart/google-chart";
		try {
			@SuppressWarnings("unused")
			ISessionUser su = (ISessionUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

		} catch (java.lang.ClassCastException e) {
			// TODO: parameterize
			response.sendRedirect(this.getSettings().get(
					Constants.PROP_LOGIN_PAGE));
			return null;
		}

		Map<String, Object> model = new HashMap<String, Object>();
		this.prepareRequest(request, response);

		// -------------------------------

		this.authorizeDsAction(resourceName, Constants.DS_ACTION_QUERY, null);

		IDsService<Object, Object, Object> service = this.getServiceLocator()
				.findDsService(resourceName);

		IDsMarshaller<Object, Object, Object> marshaller = service
				.createMarshaller(IDsMarshaller.JSON);

		Object filter = marshaller.readFilterFromString(filterString);
		Object params = marshaller.readParamsFromString(paramString);

		IQueryBuilder<Object, Object, Object> builder = service
				.createQueryBuilder().addFetchLimit(0, 10000).addFilter(filter)
				.addParams(params);

		if (orderBy != null && !orderBy.equals("")) {
			List<SortToken> sortTokens = marshaller.readListFromString(orderBy,
					SortToken.class);
			builder.addSortInfo(sortTokens);
		} else {
			builder.addSortInfo(orderByCol, orderBySense);
		}

		if (filterRulesString != null && !filterRulesString.equals("")) {
			List<FilterRule> filterRules = marshaller.readListFromString(
					filterRulesString, FilterRule.class);
			builder.addFilterRules(filterRules);
		}

		List<?> list = service.find(builder);

		model.put("dataList", list);

		model.put("xField", xField);
		model.put("yField", yField);
		model.put("title", title);
		
		// -------------------------------

		if (chartType.equals("line")) {
			model.put("chart", "google.visualization.LineChart");
		} else if (chartType.equals("pie")) {
			model.put("chart", "google.visualization.PieChart");
		} else if (chartType.equals("bar")) {
			model.put("chart", "google.visualization.BarChart");
		} else if (chartType.equals("column")) {
			model.put("chart", "google.visualization.ColumnChart");
		} else if (chartType.equals("candlestick")) {
			model.put("chart", "google.visualization.CandlestickChart");
		} else if (chartType.equals("geo")) {
			model.put("chart", "google.visualization.GeoChart");
		}

		/* ========== extensions =========== */

		this.finishRequest();
		return new ModelAndView(jspName, model);
	}
}
