/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller.session;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.enums.DateFormatAttribute;
import net.nan21.dnet.core.api.security.DnetSessionUser;
import net.nan21.dnet.core.api.security.IChangePasswordService;
import net.nan21.dnet.core.api.security.ISessionUser;
import net.nan21.dnet.core.api.security.IUser;
import net.nan21.dnet.core.api.security.IUserSettings;
import net.nan21.dnet.core.api.security.LoginParams;
import net.nan21.dnet.core.api.security.LoginParamsHolder;
import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.web.controller.AbstractDnetController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping(value = Constants.CTXPATH_SESSION)
public class SessionController extends AbstractDnetController {

	final static Logger logger = LoggerFactory
			.getLogger(SessionController.class);

	private AuthenticationManager authenticationManager;

	/**
	 * Show login page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/" + Constants.SESSION_ACTION_SHOW_LOGIN)
	public ModelAndView showLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// if user already authenticated redirect
		SecurityContext ctx = (SecurityContext) request
				.getSession()
				.getAttribute(
						HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if (ctx != null && ctx.getAuthentication() != null) {
			response.sendRedirect(Constants.URL_DNET_WEB);
			return null;
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("loginPageCss",
				this.getSettings().get(Constants.PROP_LOGIN_PAGE_CSS));
		model.put("loginPageLogo",
				this.getSettings().get(Constants.PROP_LOGIN_PAGE_LOGO));

		return new ModelAndView("login", model);

	}

	/**
	 * Process login action
	 * 
	 * @param username
	 * @param password
	 * @param clientCode
	 * @param language
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/" + Constants.SESSION_ACTION_LOGIN, method = RequestMethod.POST)
	public ModelAndView login(
			@RequestParam(value = "user", required = true) String username,
			@RequestParam(value = "pswd", required = true) String password,
			@RequestParam(value = "client", required = true) String clientCode,
			@RequestParam(value = "lang", required = false) String language,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			if (logger.isInfoEnabled()) {
				logger.info("Session request: -> login ");
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-params: user={}, client={}, pswd=*** ",
						new String[] { username, clientCode });
			}

			request.getSession().invalidate();
			request.getSession();

			prepareLoginParamsHolder(clientCode, language, request);

			String hashedPass = getMD5Password(password);

			Authentication authRequest = new UsernamePasswordAuthenticationToken(
					username, hashedPass);
			Authentication authResponse = this.getAuthenticationManager()
					.authenticate(authRequest);
			SecurityContextHolder.getContext().setAuthentication(authResponse);

			response.sendRedirect(Constants.URL_DNET_UI_EXTJS);
			return null;
		} catch (Exception e) {
			ModelAndView err = this.showLogin(request, response);
			String msg = "Authentication failed. ";
			if (e.getMessage() != null && !"".equals(e.getMessage())) {
				msg += e.getMessage();
			}
			err.getModel().put("error", msg);
			return err;
		}
	}

	/**
	 * Process login action from an AJAX context
	 * 
	 * @param username
	 * @param password
	 * @param clientCode
	 * @param language
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(params = Constants.REQUEST_PARAM_ACTION + "="
			+ Constants.SESSION_ACTION_LOGIN)
	public String loginExtjs(
			@RequestParam(value = "user", required = true) String username,
			@RequestParam(value = "pswd", required = true) String password,
			@RequestParam(value = "client", required = true) String clientCode,
			@RequestParam(value = "lang", required = false) String language,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			if (logger.isInfoEnabled()) {
				logger.info("Session request: -> login ");
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"  --> request-params: user={}, client={}, pswd=*** ",
						new String[] { username, clientCode });
			}

			// TODO: copy attributes ?
			request.getSession().invalidate();
			request.getSession();

			prepareLoginParamsHolder(clientCode, language, request);

			String hashedPass = getMD5Password(password);

			Authentication authRequest = new UsernamePasswordAuthenticationToken(
					username, hashedPass);
			Authentication authResponse = this.getAuthenticationManager()
					.authenticate(authRequest);
			SecurityContextHolder.getContext().setAuthentication(authResponse);

			ISessionUser sessionUser = (ISessionUser) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();

			IUser user = sessionUser.getUser();

			IUserSettings prefs = user.getSettings();
			StringBuffer sb = new StringBuffer();
			String userRolesStr = null;

			sb.append(",\"extjsDateFormat\":\""
					+ prefs.getDateFormat(DateFormatAttribute.EXTJS_DATE_FORMAT
							.name()) + "\"");
			sb.append(" , \"extjsTimeFormat\": \""
					+ prefs.getDateFormat(DateFormatAttribute.EXTJS_TIME_FORMAT
							.name()) + "\"");
			sb.append(" , \"extjsDateTimeFormat\": \""
					+ prefs.getDateFormat(DateFormatAttribute.EXTJS_DATETIME_FORMAT
							.name()) + "\"");
			sb.append(" , \"extjsMonthFormat\": \""
					+ prefs.getDateFormat(DateFormatAttribute.EXTJS_MONTH_FORMAT
							.name()) + "\"");
			sb.append(" , \"extjsAltFormats\": \""
					+ prefs.getDateFormat(DateFormatAttribute.EXTJS_ALT_FORMATS
							.name()) + "\"");

			sb.append(" , \"decimalSeparator\": \""
					+ prefs.getDecimalSeparator() + "\"");
			sb.append(" , \"thousandSeparator\": \""
					+ prefs.getThousandSeparator() + "\"");

			StringBuffer sbroles = new StringBuffer();
			int i = 0;
			for (String role : user.getProfile().getRoles()) {
				if (i > 0) {
					sbroles.append(",");
				}
				sbroles.append("\"" + role + "\"");
				i++;
			}
			userRolesStr = sbroles.toString();
			sb.append(" , \"roles\": [" + userRolesStr + "]");

			request.getSession()
					.setAttribute(
							HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
							SecurityContextHolder.getContext());
			String clientId = user.getClient().getId();
			if (clientId == null) {
				clientId = "";
			}
			return "{ \"success\": true , \"data\": {\"code\":\""
					+ user.getCode() + "\", \"name\":\"" + user.getName()
					+ "\",\"loginName\":\"" + user.getLoginName()
					+ "\", \"clientId\":\"" + clientId + "\" }  }";
		} catch (Exception e) {
			return this.handleException(e, response);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/" + Constants.SESSION_ACTION_LOGOUT)
	public String logout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Session request: -> logout ");
		}

		SecurityContextHolder.getContext().setAuthentication(null);
		request.getSession().invalidate();

		return "";
	}

	@ResponseBody
	@RequestMapping(value = "/" + Constants.SESSION_ACTION_LOCK)
	public String lock(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Session request: -> lock ");
		}

		ISessionUser sessionUser = (ISessionUser) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		((DnetSessionUser) sessionUser).setSessionLocked(true);
		return "";
	}

	/**
	 * Change current user password.
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, params = Constants.REQUEST_PARAM_ACTION
			+ "=" + Constants.SESSION_ACTION_CHANGEPASSWORD)
	public String changePassword(
			@RequestParam(value = "opswd", required = true) String oldPassword,
			@RequestParam(value = "npswd", required = true) String newPassword,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			if (logger.isInfoEnabled()) {
				logger.info("Session request: -> changePassword ");
			}

			if (logger.isDebugEnabled()) {
				logger.debug("  --> request-params: opswd=***, npswd=*** ");
			}

			SecurityContext ctx = (SecurityContext) request
					.getSession()
					.getAttribute(
							HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
			if (ctx == null || ctx.getAuthentication() == null) {
				throw new Exception("Not authenticated");
			}

			IUser user = ((ISessionUser) ctx.getAuthentication().getPrincipal())
					.getUser();

			Session.user.set(user);

			// -------------------------------------------------

			if (user.isSystemUser()) {
				throw new Exception(
						"The password of a system-user cannot be changed from the application.");
			}
			IChangePasswordService service = this.getApplicationContext()
					.getBean(IChangePasswordService.class);

			service.doChangePassword(user.getCode(), newPassword, oldPassword,
					user.getClient().getId(), user.getClient().getCode());
			return "{success: true}";
		} catch (Exception e) {
			return this.handleException(e, response);
		} finally {
			this.finishRequest();
		}
	}

	/**
	 * Pack extra information about login into a ThreadLocal to be passed to the
	 * authentication-provider service
	 * 
	 * @param clientCode
	 * @param language
	 * @param request
	 */
	private void prepareLoginParamsHolder(String clientCode, String language,
			HttpServletRequest request) {
		LoginParams lp = new LoginParams();

		lp.setRemoteIp(request.getRemoteAddr());
		lp.setUserAgent(request.getHeader("User-Agent"));
		lp.setRemoteHost(request.getRemoteHost());
		lp.setLanguage(language);
		lp.setClientCode(clientCode);
		LoginParamsHolder.params.set(lp);
	}

	/**
	 * Generic exception handler
	 */
	protected String handleException(Exception e, HttpServletResponse response)
			throws IOException {
		response.setStatus(403);
		return e.getLocalizedMessage();
	}

	/**
	 * Helper function to return a MD5 encryption of the given string
	 * 
	 * @param thePassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected String getMD5Password(String thePassword)
			throws NoSuchAlgorithmException {

		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(thePassword.getBytes(), 0, thePassword.length());
		String hashedPass = new BigInteger(1, messageDigest.digest())
				.toString(16);
		if (hashedPass.length() < 32) {
			hashedPass = "0" + hashedPass;
		}
		return hashedPass;
	}

	public AuthenticationManager getAuthenticationManager() {
		if (this.authenticationManager == null) {
			this.authenticationManager = this.getApplicationContext().getBean(
					Constants.SPRING_AUTH_MANAGER, AuthenticationManager.class);
		}
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}
