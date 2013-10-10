/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller.ui.extjs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.SysParams_Core;
import net.nan21.dnet.core.api.extensions.IExtensionContentProvider;
import net.nan21.dnet.core.api.extensions.IExtensionProvider;
import net.nan21.dnet.core.api.extensions.IExtensions;
import net.nan21.dnet.core.api.security.ISessionUser;
import net.nan21.dnet.core.api.setup.ISetupParticipant;
import net.nan21.dnet.core.api.setup.IStartupParticipant;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UiExtjsMainController extends AbstractUiExtjsController {

	protected List<ISetupParticipant> setupParticipants;
	protected List<IStartupParticipant> startupParticipants;

	@RequestMapping(value = "*", method = RequestMethod.GET)
	protected ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// if (this.setupParticipants != null) {
		// for (ISetupParticipant sp : setupParticipants) {
		// if (sp.hasWorkToDo()) {
		// response.sendRedirect("/nan21.dnet.core.setup");
		// return null;
		// }
		// }
		// }

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
		this._prepare(model, request, response);

		/* ========== extensions =========== */

		model.put(
				"extensions",
				getExtensionFiles(IExtensions.UI_EXTJS_MAIN,
						uiExtjsSettings.getUrlCore()));

		model.put("extensionsContent",
				getExtensionContent(IExtensions.UI_EXTJS_MAIN));

		String logo = this.getSettings().getParam(
				SysParams_Core.CORE_LOGO_URL_EXTJS);

		if (logo != null && !logo.equals("")) {
			model.put("logo", logo);
		}
		return new ModelAndView(this.jspName, model);
	}

	public List<IExtensionProvider> getExtensionProviders() {
		return extensionProviders;
	}

	public void setExtensionProviders(
			List<IExtensionProvider> extensionProviders) {
		this.extensionProviders = extensionProviders;
	}

	public List<ISetupParticipant> getSetupParticipants() {
		return setupParticipants;
	}

	public void setSetupParticipants(List<ISetupParticipant> setupParticipants) {
		this.setupParticipants = setupParticipants;
	}

	public List<IStartupParticipant> getStartupParticipants() {
		return startupParticipants;
	}

	public void setStartupParticipants(
			List<IStartupParticipant> startupParticipants) {
		this.startupParticipants = startupParticipants;
	}

	public List<IExtensionContentProvider> getExtensionContentProviders() {
		return extensionContentProviders;
	}

	public void setExtensionContentProviders(
			List<IExtensionContentProvider> extensionContentProviders) {
		this.extensionContentProviders = extensionContentProviders;
	}

}
