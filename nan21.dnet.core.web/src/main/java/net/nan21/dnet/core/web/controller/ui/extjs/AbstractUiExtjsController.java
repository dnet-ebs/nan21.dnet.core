/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller.ui.extjs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.SysParams_Core;
import net.nan21.dnet.core.api.enums.DateFormatAttribute;
import net.nan21.dnet.core.api.extensions.IExtensionContentProvider;
import net.nan21.dnet.core.api.extensions.IExtensionFile;
import net.nan21.dnet.core.api.extensions.IExtensionProvider;
import net.nan21.dnet.core.api.security.ISessionUser;
import net.nan21.dnet.core.api.security.IUser;
import net.nan21.dnet.core.api.security.IUserSettings;
import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.web.controller.AbstractDnetController;
import net.nan21.dnet.core.web.settings.UiExtjsSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AbstractUiExtjsController extends AbstractDnetController {

	private String constantsJsFragment;

	/**
	 * List of extension file providers.
	 */
	protected List<IExtensionProvider> extensionProviders;

	/**
	 * List of js content providers.
	 */
	protected List<IExtensionContentProvider> extensionContentProviders;

	/**
	 * JSP page name which renders the html
	 */
	protected String jspName;

	/**
	 * Various settings to be propagated to client.
	 */
	protected UiExtjsSettings uiExtjsSettings;

	final static Logger logger = LoggerFactory
			.getLogger(AbstractUiExtjsController.class);

	protected void _prepare(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		if (logger.isInfoEnabled()) {
			logger.info("Handling request for ui.extjs: ",
					request.getPathInfo());
		}

		String server = request.getServerName();
		int port = request.getServerPort();
		// String contextPath = request.getContextPath();
		// String path = request.getServletPath();

		String userRolesStr = null;

		try {
			ISessionUser su = (ISessionUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			IUser user = su.getUser();

			IUserSettings prefs = user.getSettings();

			Session.user.set(user);

			model.put("constantsJsFragment", this.getConstantsJsFragment());
			model.put("user", user);

			DateFormatAttribute[] masks = DateFormatAttribute.values();
			Map<String, String> dateFormatMasks = new HashMap<String, String>();
			for (int i = 0, len = masks.length; i < len; i++) {
				DateFormatAttribute mask = masks[i];
				if (mask.isForJs()) {
					dateFormatMasks.put(mask.name().replace("EXTJS_", ""),
							prefs.getDateFormat(mask.name()));
				}
			}

			model.put("dateFormatMasks", dateFormatMasks);

			model.put(
					"modelDateFormat",
					this.getSettings().get(
							Constants.PROP_EXTJS_MODEL_DATE_FORMAT));

			model.put("decimalSeparator", prefs.getDecimalSeparator());
			model.put("thousandSeparator", prefs.getThousandSeparator());

			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (String role : user.getProfile().getRoles()) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append("\"" + role + "\"");
				i++;
			}
			userRolesStr = sb.toString();

		} catch (ClassCastException e) {
			// not authenticated
		}
		String hostUrl = ((request.isSecure()) ? "https" : "http") + "://"
				+ server + ((port != 80) ? (":" + port) : "");// + contextPath;

		model.put("productName", this.getSettings().getProductName());
		model.put("productVersion", this.getSettings().getProductVersion());
		model.put("hostUrl", hostUrl);

		// themes
		model.put("urlUiExtjsThemes", getUiExtjsSettings().getUrlThemes());

		// DNet extjs components in core and modules
		model.put("urlUiExtjsCore", getUiExtjsSettings().getUrlCore());
		model.put("urlUiExtjsModules", getUiExtjsSettings().getUrlModules());
		model.put("urlUiExtjsModuleSubpath", getUiExtjsSettings()
				.getModuleSupath());

		// translations for core and modules
		model.put("urlUiExtjsCoreI18n", getUiExtjsSettings().getUrlCoreI18n());
		model.put("urlUiExtjsModulesI18n", getUiExtjsSettings()
				.getUrlModulesI18n());

		model.put("shortLanguage", this.resolveLang(request, response));
		model.put("theme", this.resolveTheme(request, response));
		model.put("sysCfg_workingMode",
				this.getSettings().get(Constants.PROP_WORKING_MODE));

		model.put("userRolesStr", userRolesStr);

	}

	private void addConstant(StringBuffer sb, String name, String value) {
		this.addConstant(sb, name, value, false);
	}

	private void addConstant(StringBuffer sb, String name, String value,
			boolean isLast) {
		sb.append(name + ":\"" + value + "\"");
		if (!isLast) {
			sb.append(",\n");
		}
	}

	private synchronized String getConstantsJsFragment() {

		if (this.constantsJsFragment == null) {

			StringBuffer sb = new StringBuffer("Constants={");

			addConstant(sb, "DATA_FORMAT_CSV", Constants.DATA_FORMAT_CSV);
			addConstant(sb, "DATA_FORMAT_HTML", Constants.DATA_FORMAT_HTML);
			addConstant(sb, "DATA_FORMAT_JSON", Constants.DATA_FORMAT_JSON);
			addConstant(sb, "DATA_FORMAT_XML", Constants.DATA_FORMAT_XML);
			addConstant(sb, "DATA_FORMAT_PDF", Constants.DATA_FORMAT_PDF);

			addConstant(sb, "REQUEST_PARAM_THEME",
					Constants.REQUEST_PARAM_THEME);
			addConstant(sb, "REQUEST_PARAM_LANG", Constants.REQUEST_PARAM_LANG);

			addConstant(sb, "REQUEST_PARAM_ACTION",
					Constants.REQUEST_PARAM_ACTION);
			addConstant(sb, "REQUEST_PARAM_DATA", Constants.REQUEST_PARAM_DATA);
			addConstant(sb, "REQUEST_PARAM_FILTER",
					Constants.REQUEST_PARAM_FILTER);
			addConstant(sb, "REQUEST_PARAM_ADVANCED_FILTER",
					Constants.REQUEST_PARAM_ADVANCED_FILTER);

			addConstant(sb, "REQUEST_PARAM_PARAMS",
					Constants.REQUEST_PARAM_PARAMS);
			addConstant(sb, "REQUEST_PARAM_SORT", Constants.REQUEST_PARAM_SORT);
			addConstant(sb, "REQUEST_PARAM_SENSE",
					Constants.REQUEST_PARAM_SENSE);
			addConstant(sb, "REQUEST_PARAM_START",
					Constants.REQUEST_PARAM_START);
			addConstant(sb, "REQUEST_PARAM_SIZE", Constants.REQUEST_PARAM_SIZE);
			addConstant(sb, "REQUEST_PARAM_ORDERBY",
					Constants.REQUEST_PARAM_ORDERBY);
			addConstant(sb, "REQUEST_PARAM_SERVICE_NAME_PARAM",
					Constants.REQUEST_PARAM_SERVICE_NAME_PARAM);
			addConstant(sb, "REQUEST_PARAM_EXPORT_INFO",
					Constants.REQUEST_PARAM_EXPORT_INFO);

			addConstant(sb, "DS_INFO", Constants.DS_ACTION_INFO);
			addConstant(sb, "DS_QUERY", Constants.DS_ACTION_QUERY);
			addConstant(sb, "DS_INSERT", Constants.DS_ACTION_INSERT);
			addConstant(sb, "DS_UPDATE", Constants.DS_ACTION_UPDATE);
			addConstant(sb, "DS_DELETE", Constants.DS_ACTION_DELETE);
			addConstant(sb, "DS_SAVE", Constants.DS_ACTION_SAVE);
			addConstant(sb, "DS_IMPORT", Constants.DS_ACTION_IMPORT);
			addConstant(sb, "DS_EXPORT", Constants.DS_ACTION_EXPORT);
			addConstant(sb, "DS_PRINT", Constants.DS_ACTION_PRINT);
			addConstant(sb, "DS_RPC", Constants.DS_ACTION_RPC);

			addConstant(sb, "ASGN_QUERY_LEFT", Constants.ASGN_ACTION_QUERY_LEFT);
			addConstant(sb, "ASGN_QUERY_RIGHT",
					Constants.ASGN_ACTION_QUERY_RIGHT);
			addConstant(sb, "ASGN_MOVE_LEFT", Constants.ASGN_ACTION_MOVE_LEFT);
			addConstant(sb, "ASGN_MOVE_RIGHT", Constants.ASGN_ACTION_MOVE_RIGHT);
			addConstant(sb, "ASGN_MOVE_LEFT_ALL",
					Constants.ASGN_ACTION_MOVE_LEFT_ALL);
			addConstant(sb, "ASGN_MOVE_RIGHT_ALL",
					Constants.ASGN_ACTION_MOVE_RIGHT_ALL);
			addConstant(sb, "ASGN_SETUP", Constants.ASGN_ACTION_SETUP);
			addConstant(sb, "ASGN_RESET", Constants.ASGN_ACTION_RESET);
			addConstant(sb, "ASGN_SAVE", Constants.ASGN_ACTION_SAVE);
			addConstant(sb, "ASGN_CLEANUP", Constants.ASGN_ACTION_CLEANUP);

			addConstant(sb, "SESSION_LOGIN", Constants.SESSION_ACTION_LOGIN);
			addConstant(sb, "SESSION_LOGOUT", Constants.SESSION_ACTION_LOGOUT);
			addConstant(sb, "SESSION_LOCK", Constants.SESSION_ACTION_LOCK);
			addConstant(sb, "SESSION_CHANGEPASSWORD",
					Constants.SESSION_ACTION_CHANGEPASSWORD, true);

			sb.append("}");

			this.constantsJsFragment = sb.toString();

		}

		return this.constantsJsFragment;
	}

	/**
	 * Get files to be included provided by extensions for the given target.
	 * Target can be a frame's FQN or one of the {@link IExtensions} constants
	 * value.
	 * 
	 * @param targetName
	 * @param baseUrl
	 * @return
	 * @throws Exception
	 */
	protected String getExtensionFiles(String targetName, String baseUrl)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		for (IExtensionProvider provider : this.extensionProviders) {

			List<IExtensionFile> files = provider.getFiles(targetName);

			for (IExtensionFile file : files) {

				String location = null;

				if (file.isRelativePath()) {
					location = baseUrl + "/" + file.getLocation();
				} else {
					location = file.getLocation();
				}

				if (file.isCss()) {
					sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""
							+ location + "\" />\n");
				} else if (file.isJs()) {
					sb.append("<script type=\"text/javascript\" src=\""
							+ location + "\"></script>\n");
				} else {
					throw new Exception("Extension provider file `"
							+ file.getLocation()
							+ "` should be of type .js or .css");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Get content provided by extensions for the given target. Target can be a
	 * frame's FQN or one of the {@link IExtensions} constants value.
	 * 
	 * @param targetName
	 * @return
	 * @throws Exception
	 */
	protected String getExtensionContent(String targetName) throws Exception {
		StringBuffer sb = new StringBuffer(
				"/* BEGIN-EXTENSION-CONTENT-PROVIDER */");
		for (IExtensionContentProvider provider : this.extensionContentProviders) {
			String content = provider.getContent(targetName);
			if (content != null) {
				sb.append(content);
			}
		}
		sb.append("/* END-EXTENSION-CONTENT-PROVIDER */");
		return sb.toString();
	}

	/**
	 * Resolve the user's current theme from the cookie.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private String resolveTheme(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Cookie[] cookies = request.getCookies();
		Cookie c = this.getCookie(cookies, Constants.COOKIE_NAME_THEME);
		if (c == null) {

			String value = this.getSettings().getParam(
					SysParams_Core.CORE_DEFAULT_THEME_EXTJS);
			c = this.createCookie(Constants.COOKIE_NAME_THEME, value,
					60 * 60 * 24 * 365);
			response.addCookie(c);
		}

		String theme = request.getParameter(Constants.REQUEST_PARAM_THEME);
		if (theme == null || theme.equals("")) {
			theme = c.getValue();
		} else {
			c.setMaxAge(0);
			c = this.createCookie(Constants.COOKIE_NAME_THEME, theme,
					60 * 60 * 24 * 365);
			response.addCookie(c);
		}
		return theme;
	}

	/**
	 * Resolve the user's current language from the cookie.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private String resolveLang(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Cookie[] cookies = request.getCookies();
		Cookie c = this.getCookie(cookies, Constants.COOKIE_NAME_LANG);
		if (c == null) {

			String value = this.getSettings().getParam(
					SysParams_Core.CORE_DEFAULT_LANGUAGE);

			c = this.createCookie(Constants.COOKIE_NAME_LANG, value,
					60 * 60 * 24 * 365);
			response.addCookie(c);
		}

		String lang = request.getParameter(Constants.REQUEST_PARAM_LANG);
		if (lang == null || lang.equals("")) {
			lang = c.getValue();
		} else {
			c.setMaxAge(0);
			c = this.createCookie(Constants.COOKIE_NAME_LANG, lang,
					60 * 60 * 24 * 365);
			response.addCookie(c);
		}
		return lang;
	}

	private Cookie getCookie(Cookie[] cookies, String name) {
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}

	private Cookie createCookie(String name, String value, int age) {
		Cookie c = new Cookie(name, value);
		c.setMaxAge(age);
		return c;
	}

	/**
	 * Get Extjs user interface specific a settings object. If it is null
	 * attempts to retrieve it from Spring context.
	 * 
	 * @return
	 */
	public UiExtjsSettings getUiExtjsSettings() {
		if (this.uiExtjsSettings == null) {
			this.uiExtjsSettings = this.getApplicationContext().getBean(
					UiExtjsSettings.class);
		}
		return uiExtjsSettings;
	}

	public void setUiExtjsSettings(UiExtjsSettings uiExtjsSettings) {
		this.uiExtjsSettings = uiExtjsSettings;
	}

	public String getJspName() {
		return jspName;
	}

	public void setJspName(String jspName) {
		this.jspName = jspName;
	}

	public List<IExtensionProvider> getExtensionProviders() {
		return extensionProviders;
	}

	public void setExtensionProviders(
			List<IExtensionProvider> extensionProviders) {
		this.extensionProviders = extensionProviders;
	}

	public List<IExtensionContentProvider> getExtensionContentProviders() {
		return extensionContentProviders;
	}

	public void setExtensionContentProviders(
			List<IExtensionContentProvider> extensionContentProviders) {
		this.extensionContentProviders = extensionContentProviders;
	}

}
