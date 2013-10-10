/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.action.impex;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface IDsExport<M> {

	public IExportInfo getExportInfo();

	public void setExportInfo(IExportInfo exportInfo);

	public void begin() throws Exception;

	public void end() throws Exception;

	public void write(M data, boolean isFirst) throws Exception;

	public File getOutFile() throws IOException;

	public void setOutFile(File outFile);

	public String getOutFilePath();

	public void setOutFilePath(String outFilePath);

	public String getOutFileName();

	public void setOutFileName(String outFileName);

	public String getOutFileExtension();

	public Map<String, Object> getProperties();

	public void setProperties(Map<String, Object> properties);

}
