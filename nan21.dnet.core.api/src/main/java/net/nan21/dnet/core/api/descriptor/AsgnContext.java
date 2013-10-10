package net.nan21.dnet.core.api.descriptor;

public class AsgnContext implements IAsgnContext {

	protected String leftTable;
	protected String leftPkField = "id";

	protected String rightTable;
	protected String rightObjectIdField;
	protected String rightItemIdField;

	public String getLeftTable() {
		return leftTable;
	}

	public void setLeftTable(String leftTable) {
		this.leftTable = leftTable;
	}

	public String getLeftPkField() {
		return leftPkField;
	}

	public void setLeftPkField(String leftPkField) {
		this.leftPkField = leftPkField;
	}

	public String getRightTable() {
		return rightTable;
	}

	public void setRightTable(String rightTable) {
		this.rightTable = rightTable;
	}

	public String getRightObjectIdField() {
		return rightObjectIdField;
	}

	public void setRightObjectIdField(String rightObjectIdField) {
		this.rightObjectIdField = rightObjectIdField;
	}

	public String getRightItemIdField() {
		return rightItemIdField;
	}

	public void setRightItemIdField(String rightItemIdField) {
		this.rightItemIdField = rightItemIdField;
	}

}
