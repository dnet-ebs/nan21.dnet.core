package net.nan21.dnet.core.api.action.impex;

import java.util.List;

public interface IImportDataPackage {

	public String getLocation();

	public void setLocation(String location);

	public void addToList(IImportDataSet dataSet);

	public List<IImportDataSet> getDataSets();

	public void setDataSets(List<IImportDataSet> dataSets);

}
