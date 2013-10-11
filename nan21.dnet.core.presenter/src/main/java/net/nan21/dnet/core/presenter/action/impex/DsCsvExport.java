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

import org.apache.commons.lang.StringEscapeUtils;

public class DsCsvExport<M> extends AbstractDsExport<M> implements IDsExport<M> {

	private char csvSeparator = ';';

	public DsCsvExport() {
		super();
		this.outFileExtension = "csv";
	}

	@Override
	public void write(M data, boolean isFirst) throws Exception {
		List<ExportField> columns = ((ExportInfo) this.getExportInfo())
				.getColumns();
		Iterator<ExportField> it = columns.iterator();

		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		while (it.hasNext()) {
			ExportField k = it.next();

			Object x = k._getFieldGetter().invoke(data);
			if (x != null) {
				String v = null;
				if (x instanceof Date) {
					v = this.getServerDateFormat().format(x);
				} else {
					v = x.toString();
				}
				sb.append(StringEscapeUtils.escapeCsv(v));
			}
			if (it.hasNext()) {
				sb.append(this.csvSeparator);
			}

		}
		this.writer.write(sb.toString());

	}

	@Override
	protected void beginContent() throws Exception {
		boolean isFirst = true;
		List<ExportField> columns = ((ExportInfo) this.getExportInfo())
				.getColumns();
		for (ExportField column : columns) {
			if (!isFirst) {
				this.writer.write(this.csvSeparator);
			}
			this.writer.write(StringEscapeUtils.escapeCsv(column.getName()));
			isFirst = false;
		}
	}

	@Override
	protected void endContent() throws Exception {
		// TODO Auto-generated method stub

	}

	public char getCsvSeparator() {
		return csvSeparator;
	}

	public void setCsvSeparator(char csvSeparator) {
		this.csvSeparator = csvSeparator;
	}

}
