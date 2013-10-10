package net.nan21.dnet.core.presenter.action.impex;

import net.nan21.dnet.core.api.action.impex.IImportDataFile;

public class DataFile implements IImportDataFile {

	private String ds;
	private String file;

	private String ukFieldName;

	public String getDs() {
		return ds;
	}

	public void setDs(String ds) {
		this.ds = ds;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getUkFieldName() {
		return ukFieldName;
	}

	public void setUkFieldName(String ukFieldName) {
		this.ukFieldName = ukFieldName;
	}

}
