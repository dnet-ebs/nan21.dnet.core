/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.action.impex;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.nan21.dnet.core.api.action.impex.IDsExport;

public class DsHtmlExport<M> extends AbstractDsExport<M> implements
		IDsExport<M> {

	public DsHtmlExport() {
		super();
		this.outFileExtension = "html";
	}

	@Override
	public void write(M data, boolean isFirst) throws Exception {

		List<ExportField> columns = ((ExportInfo) this.getExportInfo())
				.getColumns();
		Iterator<ExportField> it = columns.iterator();
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		int i = 1;
		int size = columns.size();
		while (it.hasNext()) {
			ExportField k = it.next();
			Object x = k._getFieldGetter().invoke(data);
			if (x != null) {
				if (x instanceof Date) {
					sb.append("<td  class=\""
							+ this.getCssClass(i, size, "data") + "\">"
							+ this.getServerDateFormat().format(x) + "</td>");
				} else {
					sb.append("<td  class=\""
							+ this.getCssClass(i, size, "data") + "\">"
							+ x.toString() + "</td>");
				}
			} else {
				sb.append("<td class=\"" + this.getCssClass(i, size, "data")
						+ "\">-</td>");
			}

			i++;
		}
		sb.append("</tr>");
		this.bufferedWriter.write(sb.toString());

	}

	private String getCssClass(int idx, int len, String cssClass) {
		if (idx == 1) {
			return "first-" + cssClass;
		} else if (idx == len) {
			return "last-" + cssClass;
		} else {
			return "middle-" + cssClass;
		}
	}

	@Override
	protected void beginContent() throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append("<html><head>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""
				+ this.getProperties().get("cssUrl") + "\"/>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<table class=\"result\">");
		sb.append("<thead><tr>");
		int i = 1;
		List<ExportField> columns = ((ExportInfo) this.getExportInfo())
				.getColumns();
		int size = columns.size();
		for (ExportField column : columns) {
			sb.append("<th class=\"" + this.getCssClass(i, size, "title")
					+ "\">" + column.getName() + "</th>");
			i++;
		}
		sb.append("</tr></thead>");
		sb.append("<tbody>");
		this.bufferedWriter.write(sb.toString());
	}

	@Override
	protected void endContent() throws Exception {
		this.bufferedWriter.write("</tbody>");
		this.bufferedWriter.write("</table>");
		this.bufferedWriter.write("</body>");
		this.bufferedWriter.write("</html>");
	}

}
