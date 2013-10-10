package net.nan21.dnet.core.presenter.action.impex;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import net.nan21.dnet.core.api.action.impex.IImportDataFile;
import net.nan21.dnet.core.api.action.impex.IImportDataSet;

@XmlRootElement(name = "initData")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSet implements IImportDataSet {

	@XmlElementWrapper(name = "files")
	@XmlElement(name = "file", type = DataFile.class)
	private List<IImportDataFile> dataFiles;

	public List<IImportDataFile> getDataFiles() {
		return dataFiles;
	}

	public void setDataFiles(List<IImportDataFile> dataFiles) {
		this.dataFiles = dataFiles;
	}

	public void addToList(IImportDataFile dataFile) {
		if (this.dataFiles == null) {
			this.dataFiles = new ArrayList<IImportDataFile>();
		}
		this.dataFiles.add(dataFile);
	}

}
