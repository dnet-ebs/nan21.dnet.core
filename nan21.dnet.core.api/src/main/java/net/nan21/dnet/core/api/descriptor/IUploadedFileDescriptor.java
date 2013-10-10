/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.descriptor;

public interface IUploadedFileDescriptor {
	public String getOriginalName();

	public void setOriginalName(String originalName);

	public String getNewName();

	public void setNewName(String newName);

	public String getContentType();

	public void setContentType(String contentType);

	public long getSize();

	public void setSize(long size);
}
