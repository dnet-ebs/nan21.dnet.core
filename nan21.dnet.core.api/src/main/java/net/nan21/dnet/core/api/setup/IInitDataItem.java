/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.setup;

import java.io.File;

public interface IInitDataItem {

	public abstract String getDestPath();

	public abstract void setDestPath(String destPath);

	public abstract String getSequence();

	public abstract void setSequence(String sequence);

	public abstract String getDsName();

	public abstract void setDsName(String dsName);

	public abstract String getFileName();

	public abstract void setFileName(String fileName);

	public abstract File getFile();

	public abstract void setFile(File file);

	public abstract String getUkFieldName();

	public abstract void setUkFieldName(String ukFieldName);

}