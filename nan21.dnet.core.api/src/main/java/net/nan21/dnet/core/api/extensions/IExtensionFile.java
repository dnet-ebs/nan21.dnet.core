package net.nan21.dnet.core.api.extensions;

public interface IExtensionFile {

	/**
	 * Returns true if this is a .js file
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract boolean isJs() throws Exception;

	/**
	 * Returns true if this is a .css file
	 * 
	 * @return
	 */
	public abstract boolean isCss();

	public abstract String getFileExtension();

	public abstract String getLocation();

	public abstract void setLocation(String location);

	public abstract boolean isRelativePath();

	public abstract void setRelativePath(boolean relativePath);

}