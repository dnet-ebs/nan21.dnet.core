/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.action.impex;

public class ConfigCsvImport {

	private char separator = ',';
	private char quoteChar = '"';
	private String encoding = "UTF-8";

	public ConfigCsvImport() {
		super();
	}

	public ConfigCsvImport(char separator, char quoteChar, String encoding) {
		super();
		this.separator = separator;
		this.quoteChar = quoteChar;
		this.encoding = encoding;
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	public char getQuoteChar() {
		return quoteChar;
	}

	public void setQuoteChar(char quoteChar) {
		this.quoteChar = quoteChar;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
