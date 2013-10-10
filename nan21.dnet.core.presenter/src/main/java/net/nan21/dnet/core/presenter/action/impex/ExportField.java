package net.nan21.dnet.core.presenter.action.impex;

import java.lang.reflect.Method;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "fieldGetterName", "fieldGetter" })
public class ExportField {

	private String name;
	private String title;
	private String width;
	private String mask;
	private String type = "string";

	@JsonIgnore
	private String fieldGetterName;

	@JsonIgnore
	private Method fieldGetter;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public Method _getFieldGetter() {
		return fieldGetter;
	}

	public void _setFieldGetter(Method fieldGetter) {
		this.fieldGetter = fieldGetter;
	}

}
