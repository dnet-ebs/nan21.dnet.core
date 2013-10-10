package net.nan21.dnet.core.api.enums;

import net.nan21.dnet.core.api.Constants;

/**
 * Attributes which define patterns used for date-time handling in different
 * layers (Java/Javascript). For each date-format mask proper values must be
 * provided for each of these attributes.
 * 
 * @author amathe
 * 
 */
public enum DateFormatAttribute {

	// for javascript
	EXTJS_DATE_FORMAT("Date format in Extjs", Constants.PROP_EXTJS_DATE_FORMAT,
			false, true),

	EXTJS_TIME_FORMAT("Time format in Extjs", Constants.PROP_EXTJS_TIME_FORMAT,
			false, true),

	EXTJS_DATETIME_FORMAT("Date-time format in Extjs",
			Constants.PROP_EXTJS_DATETIME_FORMAT, false, true),

	EXTJS_DATETIMESEC_FORMAT("Date-time-second format in Extjs",
			Constants.PROP_EXTJS_DATETIMESEC_FORMAT, false, true),

	EXTJS_MONTH_FORMAT("Month format in Extjs",
			Constants.PROP_EXTJS_MONTH_FORMAT, false, true),

	EXTJS_ALT_FORMATS("Alternative date formats in Extjs",
			Constants.PROP_EXTJS_ALT_FORMATS, false, true),

	// for java
	JAVA_DATE_FORMAT("Date format in Java", Constants.PROP_JAVA_DATE_FORMAT,
			true, false),

	JAVA_TIME_FORMAT("Time format in Java", Constants.PROP_JAVA_TIME_FORMAT,
			true, false),

	JAVA_DATETIME_FORMAT("Date-time format in Java",
			Constants.PROP_JAVA_DATETIME_FORMAT, true, false),

	JAVA_DATETIMESEC_FORMAT("Date-time-second format in Java",
			Constants.PROP_JAVA_DATETIMESEC_FORMAT, true, false),

	JAVA_MONTH_FORMAT("Month format in Java", Constants.PROP_JAVA_MONTH_FORMAT,
			true, false);

	private String description;
	private String propertyFileKey;

	private boolean forJava;
	private boolean forJs;

	private DateFormatAttribute(String description, String propertyFileKey,
			boolean forJava, boolean forJs) {
		this.description = description;
		this.propertyFileKey = propertyFileKey;
		this.forJava = forJava;
		this.forJs = forJs;

	}

	public String getDescription() {
		return description;
	}

	public String getPropertyFileKey() {
		return propertyFileKey;
	}

	public boolean isForJava() {
		return forJava;
	}

	public boolean isForJs() {
		return forJs;
	}

}
