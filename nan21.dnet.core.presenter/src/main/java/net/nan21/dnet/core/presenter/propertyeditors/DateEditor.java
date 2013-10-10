/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.nan21.dnet.core.api.Constants;

public class DateEditor extends PropertyEditorSupport {

	private String mask;
	private String altMask;

	private SimpleDateFormat format;
	private List<SimpleDateFormat> altFormats;

	public DateEditor() {
		super();
		this.initDefaults();
	}

	public DateEditor(Object source) {
		super(source);
		this.initDefaults();
	}

	private void initDefaults() {

		this.mask = Constants.get_server_date_format();
		this.altMask = Constants.get_server_alt_formats();
		this.format = new SimpleDateFormat(this.mask);

		this.altFormats = new ArrayList<SimpleDateFormat>();
		String[] t = this.altMask.split(";");
		for (int i = 0, len = t.length; i < len; i++) {
			this.altFormats.add(new SimpleDateFormat(t[i]));
		}
	}

	@Override
	public String getAsText() {
		return this.format.format((Date) this.getValue());
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || text.equals("")) {
			setValue(null);
		} else {
			boolean ok = false;
			for (SimpleDateFormat df : this.altFormats) {
				try {
					setValue(df.parse(text));
					ok = true;
					break;
				} catch (Exception e) {
					// ignore and try the next one
				}
			}
			if (!ok) {
				throw new IllegalArgumentException("Date value [" + text
						+ "] doesn't match any of the expected formats:  ["
						+ this.altMask + "]");
			}
		}
	}

}
