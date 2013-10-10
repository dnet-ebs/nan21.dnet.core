/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.web.controller.upload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.descriptor.IUploadedFileDescriptor;
import net.nan21.dnet.core.api.service.IFileUploadService;
import net.nan21.dnet.core.api.service.IFileUploadServiceFactory;
import net.nan21.dnet.core.presenter.descriptor.UploadedFileDescriptor;
import net.nan21.dnet.core.web.controller.AbstractDnetController;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController extends AbstractDnetController {

	private List<IFileUploadServiceFactory> fileUploadServiceFactories;

	final static Logger logger = LoggerFactory
			.getLogger(FileUploadController.class);

	/**
	 * Generic file upload. Expects an uploaded file and a handler alias to
	 * delegate the uploaded file processing.
	 * 
	 * @param handler
	 *            spring bean alias of the
	 *            {@link net.nan21.dnet.core.api.service.IFileUploadService}
	 *            which should process the uploaded file
	 * @param file
	 *            Uploaded file
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{handler}", method = RequestMethod.POST)
	@ResponseBody
	public String fileUpload(@PathVariable("handler") String handler,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Processing file upload request with-handler {} ",
					new String[] { handler });
		}

		if (file.isEmpty()) {
			throw new Exception("Upload was not succesful. Try again please.");
		}

		this.prepareRequest(request, response);

		IFileUploadService srv = this.getFileUploadService(handler);
		Map<String, String> paramValues = new HashMap<String, String>();

		for (String p : srv.getParamNames()) {
			paramValues.put(p, request.getParameter(p));
		}

		IUploadedFileDescriptor fileDescriptor = new UploadedFileDescriptor();
		fileDescriptor.setContentType(file.getContentType());
		fileDescriptor.setOriginalName(file.getOriginalFilename());
		fileDescriptor.setNewName(file.getName());
		fileDescriptor.setSize(file.getSize());
		Map<String, Object> result = srv.execute(fileDescriptor,
				file.getInputStream(), paramValues);

		this.finishRequest();
		result.put("success", true);
		ObjectMapper mapper = getJsonMapper();
		return mapper.writeValueAsString(result);
	}

	/**
	 * Try to locate a file-upload service by its name (spring-bean alias)
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	protected IFileUploadService getFileUploadService(String name)
			throws Exception {
		IFileUploadService srv = null;
		for (IFileUploadServiceFactory sf : getFileUploadServiceFactories()) {
			try {
				srv = sf.create(name);
				if (srv != null) {
					return srv;
				}
			} catch (NoSuchBeanDefinitionException e) {
				// service not found in this factory, ignore
			}
		}
		throw new Exception(name + "File upload service not found for name "
				+ name + "!");
	}

	@SuppressWarnings("unchecked")
	public List<IFileUploadServiceFactory> getFileUploadServiceFactories() {
		if (this.fileUploadServiceFactories == null) {
			this.fileUploadServiceFactories = (List<IFileUploadServiceFactory>) this
					.getApplicationContext()
					.getBean(
							Constants.SPRING_OSGI_FILE_UPLOAD_SERVICE_FACTORIES);
		}
		return this.fileUploadServiceFactories;
	}

	public void setFileUploadServiceFactories(
			List<IFileUploadServiceFactory> fileUploadServiceFactories) {

		this.fileUploadServiceFactories = fileUploadServiceFactories;
	}

}
