/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.nan21.dnet.core.api.Constants;
import net.nan21.dnet.core.api.ISettings;
import net.nan21.dnet.core.api.enums.DateFormatAttribute;
import net.nan21.dnet.core.api.exceptions.InvalidConfiguration;

public class DnetUserSettings implements IUserSettings, Serializable {

	private static final long serialVersionUID = -9131543374115237340L;

	private String language = Constants.DEFAULT_LANGUAGE;

	/**
	 * Date format masks to be used in java/extjs. The keys are the names in
	 * {@link DateFormatAttribute}.
	 */
	private Map<String, String> dateFormats;

	/**
	 * Pattern to highlight the separators: 0.000,00 or 0,000.00 etc
	 */
	private String numberFormat = Constants.DEFAULT_NUMBER_FORMAT;

	/**
	 * Derived from numberFormatMask
	 */
	private String decimalSeparator;

	/**
	 * Derived from numberFormatMask
	 */
	private String thousandSeparator;

	private DnetUserSettings() {

	}

	public static DnetUserSettings newInstance(ISettings settings)
			throws InvalidConfiguration {

		DnetUserSettings usr = new DnetUserSettings();

		for (DateFormatAttribute a : DateFormatAttribute.values()) {
			usr.setDateFormat(a.name(), settings.get(a.getPropertyFileKey()));
		}

		usr.setLanguage(settings.get(Constants.PROP_LANGUAGE));
		usr.setNumberFormat(settings.get(Constants.PROP_NUMBER_FORMAT));

		return usr;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat)
			throws InvalidConfiguration {
		this.validateNumberFormat(numberFormat);
		this.numberFormat = numberFormat;
	}

	public String getDecimalSeparator() {
		if (this.decimalSeparator == null) {
			this.decimalSeparator = this.numberFormat.replace("0", "")
					.substring(1, 2);
		}
		return decimalSeparator;
	}

	public String getThousandSeparator() {
		if (this.thousandSeparator == null) {
			this.thousandSeparator = this.numberFormat.replace("0", "")
					.substring(0, 1);
		}
		return thousandSeparator;
	}

	public String getDateFormat(String key) {
		return this.dateFormats.get(key);
	}

	public void setDateFormat(String key, String value)
			throws InvalidConfiguration {
		if (this.dateFormats == null) {
			this.dateFormats = new HashMap<String, String>();
		}
		this.validateDateFormatKey(key);
		this.dateFormats.put(key, value);
	}

	private void validateDateFormatKey(String key) throws InvalidConfiguration {
		if (DateFormatAttribute.valueOf(key) == null) {
			throw new InvalidConfiguration(key
					+ " is not an accepted date-format-mask.");
		}
	}

	private void validateNumberFormat(String key) throws InvalidConfiguration {

		if (key != null && key.length() == 8) {
			String k = key.replaceAll("0", "");
			if (k.length() == 2) {
				return;
			}
		}

		throw new InvalidConfiguration(
				key
						+ " is not an accepted number-format-mask. It should be a pattern similar to `0,000.00`  ");
	}
}
