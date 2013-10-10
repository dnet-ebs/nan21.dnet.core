/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.nan21.dnet.core.api.ISettings;
import net.nan21.dnet.core.api.SysParams_Core;
import net.nan21.dnet.core.api.exceptions.NotAuthorizedRequestException;
import net.nan21.dnet.core.api.security.IAuthorizationFactory;
import net.nan21.dnet.core.api.security.ISessionUser;
import net.nan21.dnet.core.api.security.IUser;
import net.nan21.dnet.core.api.session.Session;
import net.nan21.dnet.core.presenter.service.ServiceLocator;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Root of the abstract controllers hierarchy.
 * 
 * @author amathe
 * 
 */
public abstract class AbstractDnetController implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * System configuration. May be null, use the getter.
	 */
	private ISettings settings;

	/**
	 * Presenter service locator. May be null, use the getter.
	 */
	private ServiceLocator serviceLocator;

	/**
	 * Authorization factory. May be null, use the getter.
	 */
	private IAuthorizationFactory authorizationFactory;

	/**
	 * Default transfer buffer size.
	 */
	protected final static int FILE_TRANSFER_BUFFER_SIZE = 4 * 1024;

	final static Logger logger = LoggerFactory
			.getLogger(AbstractDnetController.class);

	protected void prepareRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		ISessionUser sessionUser = null;
		IUser user = null;

		try {
			sessionUser = (ISessionUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			if (sessionUser.isSessionLocked()) {
				throw new Exception("Session has been locked. ");
			}
			user = sessionUser.getUser();
			if (logger.isDebugEnabled()) {
				logger.debug("Working with clientId = " + user.getClientId());
			}
		} catch (ClassCastException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Not authenticated request denied.");
			}
			throw new Exception("<b>Session expired.</b>"
					+ "<br> Logout from application and login again.");
		}

		Session.user.set(user);

		boolean checkIp = this.getSettings().getParamAsBoolean(
				SysParams_Core.CORE_SESSION_CHECK_IP);

		if (checkIp) {
			if (logger.isDebugEnabled()) {
				logger.debug(SysParams_Core.CORE_SESSION_CHECK_IP
						+ " enabled, checking IP against login IP...");
			}
			String ip = request.getRemoteAddr();
			if (!sessionUser.getRemoteIp().equals(ip)) {
				logger.debug("Request comes from different IP as expected. Expected: "
						+ sessionUser.getRemoteIp() + ", real " + ip);
				throw new Exception(
						"Security settings do not allow to process request. Check log file for details.");
			}
		}

		boolean checkAgent = this.getSettings().getParamAsBoolean(
				SysParams_Core.CORE_SESSION_CHECK_USER_AGENT);

		if (checkAgent) {
			if (logger.isDebugEnabled()) {
				logger.debug(SysParams_Core.CORE_SESSION_CHECK_USER_AGENT
						+ " enabled, checking user-agent agianst login user-agent...");
			}
			String agent = request.getHeader("User-Agent");
			if (!sessionUser.getUserAgent().equals(agent)) {
				logger.debug("Request comes from different user-agent as expected. Expected: "
						+ sessionUser.getUserAgent() + ", real " + agent);
				throw new Exception(
						"Security settings do not allow to process request. Check log file for details.");
			}
		}
	}

	protected void finishRequest() {
		Session.user.set(null);
	}

	/**
	 * Collect parameters from request.
	 * 
	 * ATTENTION!: Only the first value is considered.
	 * 
	 * @param request
	 * @param prefix
	 *            Collect only parameters which start with this prefix
	 * @param suffix
	 *            Collect only parameters which ends with this suffix
	 * @return
	 */
	protected Map<String, String> collectParams(HttpServletRequest request,
			String prefix, String suffix) {
		@SuppressWarnings("unchecked")
		Map<String, String[]> paramMap = (Map<String, String[]>) request
				.getParameterMap();
		Map<String, String> result = new HashMap<String, String>();
		for (Map.Entry<String, String[]> e : paramMap.entrySet()) {
			String k = e.getKey();
			if (prefix != null && k.startsWith(prefix)) {
				if (suffix != null && k.endsWith(suffix)) {
					result.put(k, e.getValue()[0]);
				}
			}
		}
		return result;
	}

	protected ObjectMapper getJsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
				false);
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	protected void sendFile(File file, ServletOutputStream outputStream)
			throws IOException {
		this.sendFile(file, outputStream, FILE_TRANSFER_BUFFER_SIZE);
	}

	protected void sendFile(File file, ServletOutputStream outputStream,
			int bufferSize) throws IOException {
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[bufferSize];
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				outputStream.write(buf, 0, bytesRead);
			}
		} finally {
			if (in != null)
				in.close();
		}
		outputStream.flush();
	}

	protected void sendFile(InputStream inputStream,
			ServletOutputStream outputStream) throws IOException {
		this.sendFile(inputStream, outputStream, FILE_TRANSFER_BUFFER_SIZE);
	}

	protected void sendFile(InputStream inputStream,
			ServletOutputStream outputStream, int bufferSize)
			throws IOException {
		try {
			byte[] buf = new byte[bufferSize];
			int bytesRead;
			while ((bytesRead = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, bytesRead);
			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
		outputStream.flush();
	}

	/* ================ EXCEPTIONS ======================== */

	/**
	 * Not authenticated
	 * 
	 * @param e
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(value = NotAuthorizedRequestException.class)
	@ResponseBody
	protected String handleException(NotAuthorizedRequestException e,
			HttpServletResponse response) throws IOException {
		response.setStatus(403);
		if (e.getCause() != null) {
			response.getOutputStream().print(e.getCause().getMessage());
		} else {
			response.getOutputStream().print(e.getMessage());
		}
		return null;
	}

	/**
	 * Generic exception handler
	 * 
	 * @param e
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	protected String handleException(Exception e, HttpServletResponse response)
			throws IOException {

		e.printStackTrace();

		if (e instanceof NotAuthorizedRequestException) {
			return this.handleException((NotAuthorizedRequestException) e,
					response);
		} else if (e instanceof InvocationTargetException) {
			StringBuffer sb = new StringBuffer();
			if (e.getMessage() != null) {
				sb.append(e.getMessage() + "\n");
			}
			Throwable exc = ((InvocationTargetException) e)
					.getTargetException();

			if (exc.getMessage() != null) {
				sb.append(exc.getMessage() + "\n");
			}

			if (exc.getCause() != null) {
				if (sb.length() > 0) {
					sb.append(" Reason: ");
				}
				sb.append(exc.getCause().getLocalizedMessage());
			}

			exc.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getOutputStream().print(sb.toString());
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if (e.getLocalizedMessage() != null) {
			sb.append(e.getLocalizedMessage());
		} else if (e.getCause() != null) {
			if (sb.length() > 0) {
				sb.append(" Reason: ");
			}
			sb.append(e.getCause().getLocalizedMessage());
		}
		if (sb.length() == 0) {
			if (e.getStackTrace() != null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				e.printStackTrace(response.getWriter());
				return null;
			}
		}
		response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
		response.getOutputStream().print(sb.toString());
		response.getOutputStream().flush();
		return null;

	}

	/* ================= GETTERS - SETTERS ================== */

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Get system configuration object. If it is null attempts to retrieve it
	 * from Spring context.
	 * 
	 * @return
	 */
	public ISettings getSettings() {
		if (this.settings == null) {
			this.settings = this.getApplicationContext().getBean(
					ISettings.class);
		}
		return settings;
	}

	/**
	 * Set system configuration object
	 * 
	 * @param systemConfig
	 */
	public void setSettings(ISettings settings) {
		this.settings = settings;
	}

	/**
	 * Get presenter service locator. If it is null attempts to retrieve it from
	 * Spring context.
	 * 
	 * @return
	 */
	public ServiceLocator getServiceLocator() {
		if (this.serviceLocator == null) {
			this.serviceLocator = this.getApplicationContext().getBean(
					ServiceLocator.class);
		}
		return serviceLocator;
	}

	/**
	 * Set presenter service locator.
	 * 
	 * @param serviceLocator
	 */
	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	/**
	 * Get authorization factory. If it is null attempts to retrieve it from
	 * Spring context.
	 * 
	 * @return
	 * @throws Exception
	 */
	public IAuthorizationFactory getAuthorizationFactory() {
		if (this.authorizationFactory == null) {
			this.authorizationFactory = this.getApplicationContext().getBean(
					IAuthorizationFactory.class);
		}
		return authorizationFactory;
	}

	/**
	 * Set authorization factory.
	 * 
	 * @param authorizationFactory
	 */
	public void setAuthorizationFactory(
			IAuthorizationFactory authorizationFactory) {
		this.authorizationFactory = authorizationFactory;
	}

	/**
	 * Authorize an assignment action.
	 * 
	 * @param asgnName
	 * @param action
	 * @throws Exception
	 */
	protected void authorizeAsgnAction(String asgnName, String action)
			throws Exception {
		this.getAuthorizationFactory().getAsgnAuthorizationProvider()
				.authorize(asgnName, action, null);
	}

	/**
	 * Authorize a DS action.
	 * 
	 * @param asgnName
	 * @param action
	 * @throws Exception
	 */
	protected void authorizeDsAction(String dsName, String action,
			String rpcName) throws Exception {
		this.getAuthorizationFactory().getDsAuthorizationProvider()
				.authorize(dsName, action, rpcName);
	}

}
