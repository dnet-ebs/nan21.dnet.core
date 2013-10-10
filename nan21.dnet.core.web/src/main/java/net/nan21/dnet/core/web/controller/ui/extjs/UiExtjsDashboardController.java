/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller.ui.extjs;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.nan21.dnet.core.api.exceptions.NotAuthorizedRequestException;
import net.nan21.dnet.core.api.extensions.IExtensions;
import net.nan21.dnet.core.api.security.ISessionUser;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UiExtjsDashboardController extends AbstractUiExtjsController {

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	protected ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			@SuppressWarnings("unused")
			ISessionUser su = (ISessionUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		} catch (java.lang.ClassCastException e) {
			// anonymous user
			throw new NotAuthorizedRequestException("Not authenticated");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		this._prepare(model, request, response);

		/* ========== extensions =========== */

		model.put(
				"extensions",
				getExtensionFiles(IExtensions.UI_EXTJS_DASHBOARD,
						uiExtjsSettings.getUrlCore()));
		return new ModelAndView(this.jspName, model);
	}

}
