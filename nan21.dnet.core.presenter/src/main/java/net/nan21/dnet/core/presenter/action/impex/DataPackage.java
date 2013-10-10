package net.nan21.dnet.core.presenter.action.impex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import net.nan21.dnet.core.api.action.impex.IImportDataPackage;
import net.nan21.dnet.core.api.action.impex.IImportDataSet;

@XmlRootElement(name = "package")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataPackage implements IImportDataPackage {

	@XmlElementWrapper(name = "sets")
	@XmlElement(name = "set", type = DataSet.class)
	private List<IImportDataSet> dataSets;

	private String location;

	public void addToList(IImportDataSet dataSet) {
		if (this.dataSets == null) {
			this.dataSets = new ArrayList<IImportDataSet>();
		}
		this.dataSets.add(dataSet);
	}

	public List<IImportDataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<IImportDataSet> dataSets) {
		this.dataSets = dataSets;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public static IImportDataPackage forIndexFile(String location) throws Exception {
		File file = new File(location);
		JAXBContext jaxbContext = JAXBContext.newInstance(DataPackage.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		IImportDataPackage dp = (IImportDataPackage) jaxbUnmarshaller.unmarshal(file);
		String path = file.getParentFile().getCanonicalPath();
		dp.setLocation(path);
		return dp;
	}

	public static IImportDataPackage forDataFile(DataFile dataFile) throws Exception {
		IImportDataPackage dp = new DataPackage();
		DataSet set = new DataSet();
		set.addToList(dataFile);
		dp.addToList(set);
		return dp;
	}

}
