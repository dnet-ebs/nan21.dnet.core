package net.nan21.dnet.core.presenter.action.impex;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.nan21.dnet.core.api.action.impex.IExportInfo;

import org.springframework.util.StringUtils;

public class ExportInfo implements IExportInfo {

	private String title;
	private String layout;

	private List<ExportField> filter;

	private List<ExportField> columns;

	public ExportInfo() {
		this.filter = new ArrayList<ExportField>();
		this.columns = new ArrayList<ExportField>();
	}

	public void prepare(Class<?> modelClass) {

		if (this.columns != null) {
			this.prepareFields(modelClass);
		} else {
			this.prepareAllFields(modelClass);
		}

	}

	/**
	 * 
	 * @param rt
	 *            return type class
	 * @param ef
	 *            export field
	 */
	private void resolveFieldType(Class<?> rt, ExportField ef) {

		if (rt == Date.class) {
			ef.setType("date");
		} else if (rt == Boolean.class) {
			ef.setType("boolean");
		} else if (rt == Integer.class || rt == Float.class
				|| rt == BigDecimal.class || rt == Long.class) {
			ef.setType("number");
		}
	}

	private void prepareFields(Class<?> modelClass) {
		for (ExportField ef : this.columns) {
			String fn = ef.getName();
			Method m;
			ExportField _ef = (ExportField) ef;
			try {
				m = modelClass.getMethod("get" + StringUtils.capitalize(fn),
						new Class<?>[] {});
				_ef._setFieldGetter(m);
				Class<?> rt = m.getReturnType();
				this.resolveFieldType(rt, _ef);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void prepareAllFields(Class<?> modelClass) {
		this.columns = new ArrayList<ExportField>();
		Class<?> clz = modelClass;
		while (clz != null) {
			Method[] methods = clz.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method m = methods[i];
				if (m.getName().startsWith("get")) {
					String fn = m.getName().substring(3);
					fn = fn.substring(0, 1).toLowerCase() + fn.substring(1);
					ExportField _ef = new ExportField();
					_ef.setName(fn);
					_ef.setTitle(fn);
					_ef._setFieldGetter(m);
					Class<?> rt = m.getReturnType();
					this.resolveFieldType(rt, _ef);
					this.columns.add(_ef);
				}
			}
			clz = clz.getSuperclass();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public List<ExportField> getFilter() {
		return filter;
	}

	public void setFilter(List<ExportField> filter) {
		this.filter = filter;
	}

	public List<ExportField> getColumns() throws Exception {
		return columns;
	}

	public void setColumns(List<ExportField> columns) {
		this.columns = columns;
	}

}
