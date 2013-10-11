/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.action.impex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.action.impex.IExportInfo;

public abstract class AbstractDsExport<M> {

	private IExportInfo exportInfo;

	private File outFile;
	private String outFileName;
	private String outFilePath;
	protected String outFileExtension;

	protected Writer writer;

	private Map<String, Object> properties;

	private SimpleDateFormat serverDateFormat;

	public AbstractDsExport() {
		super();
		this.init();
	}

	public abstract void write(M data, boolean isFirst) throws Exception;

	private void init() {
		this.serverDateFormat = new SimpleDateFormat(
				Constants.get_server_datetime_format());

		if (this.outFileName == null) {
			this.outFileName = UUID.randomUUID().toString();
		}

	}

	public SimpleDateFormat getServerDateFormat() {
		return serverDateFormat;
	}

	public void setServerDateFormat(SimpleDateFormat serverDateFormat) {
		this.serverDateFormat = serverDateFormat;
	}

	public void begin() throws Exception {
		this.openWriter();
		this.beginContent();
	}

	public void end() throws Exception {
		this.endContent();
		this.closeWriter();
	}

	protected abstract void beginContent() throws Exception;

	protected abstract void endContent() throws Exception;

	private void openWriter() throws Exception {
		if (this.outFile == null) {
			if (this.outFilePath == null || this.outFileName == null
					|| this.outFileExtension == null) {
				throw new Exception(
						"Either a File or a file-path, file-name and file-extension must be provided");
			}
			File dir = new File(this.outFilePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			this.outFile = File.createTempFile(this.outFileName, "."
					+ this.outFileExtension, dir);
		}
		// FileWriter fstream = new FileWriter(this.outFile);
		// this.writer = new BufferedWriter(fstream);
		this.writer = new OutputStreamWriter(
				new FileOutputStream(this.outFile), "UTF-8");

	}

	private void closeWriter() throws IOException {
		this.writer.flush();
		this.writer.close();
	}

	public IExportInfo getExportInfo() {
		return exportInfo;
	}

	public void setExportInfo(IExportInfo exportInfo) {
		this.exportInfo = exportInfo;
	}

	public File getOutFile() {
		return outFile;
	}

	public void setOutFile(File outFile) {
		this.outFile = outFile;
	}

	public String getOutFilePath() {
		return outFilePath;
	}

	public void setOutFilePath(String outFilePath) {
		this.outFilePath = outFilePath;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	public String getOutFileExtension() {
		return outFileExtension;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
