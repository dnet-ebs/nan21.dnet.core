package net.nan21.dnet.core.api.action.impex;

import java.util.List;

public interface IImportDataSet {

	public List<IImportDataFile> getDataFiles();

	public void setDataFiles(List<IImportDataFile> dataFiles);
}
