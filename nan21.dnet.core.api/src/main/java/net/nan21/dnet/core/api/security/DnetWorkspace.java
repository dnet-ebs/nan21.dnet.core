package net.nan21.dnet.core.api.security;

public class DnetWorkspace implements IWorkspace {

	private final String workspacePath;
	private final String importPath;
	private final String exportPath;
	private final String tempPath;

	public DnetWorkspace(String workspacePath, String importPath,
			String exportPath, String tempPath) {
		super();
		this.workspacePath = workspacePath;
		this.importPath = importPath;
		this.exportPath = exportPath;
		this.tempPath = tempPath;
	}

	public DnetWorkspace(String workspacePath) {
		super();
		this.workspacePath = workspacePath;
		this.importPath = workspacePath + "/import";
		this.exportPath = workspacePath + "/export";
		this.tempPath = workspacePath + "/temp";
	}

	public String getWorkspacePath() {
		return workspacePath;
	}

	public String getImportPath() {
		return importPath;
	}

	public String getExportPath() {
		return exportPath;
	}

	public String getTempPath() {
		return tempPath;
	}

}
