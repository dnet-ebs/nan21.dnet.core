/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.nan21.dnet.core.api.descriptor.IUploadedFileDescriptor;

public interface IFileUploadService {

	public List<String> getParamNames();

	public Map<String, Object> execute(IUploadedFileDescriptor fileDescriptor,
			InputStream inputStream, Map<String, String> params)
			throws Exception;

}
