package net.nan21.dnet.core.presenter.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

public class BigDecimalEditor extends PropertyEditorSupport {

	public BigDecimalEditor() {
		super();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String input = (text != null ? text.trim() : null);
		if (input == null || input.equals("")) {
			setValue(null);
		} else {
			try {
				setValue(new BigDecimal(text));
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid big-decimal value ["
						+ text + "]");
			}
		}
	}
}
