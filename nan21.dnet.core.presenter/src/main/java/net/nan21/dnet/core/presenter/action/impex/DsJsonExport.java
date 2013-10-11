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

public class DsJsonExport<M> extends AbstractDsExport<M> implements
		IDsExport<M> {

	public DsJsonExport() {
		super();
		this.outFileExtension = "json";
	}

	@Override
	public void write(M data, boolean isFirst) throws Exception {
		List<ExportField> columns = ((ExportInfo) this.getExportInfo())
				.getColumns();
		Iterator<ExportField> it = columns.iterator();

		StringBuffer sb = new StringBuffer();
		if (!isFirst) {
			sb.append(',');
		}
		sb.append("{");
		while (it.hasNext()) {
			ExportField k = it.next();

			Object x = k._getFieldGetter().invoke(data);
			if (x != null) {
				if (x instanceof Date) {
					sb.append('"' + k.getName() + '"' + ':' + '"'
							+ this.getServerDateFormat().format(x) + '"');
				} else {
					sb.append('"' + k.getName() + '"' + ':' + '"'
							+ x.toString() + '"');
				}
			} else {
				sb.append('"' + k.getName() + '"' + ":null");
			}

			if (it.hasNext()) {
				sb.append(',');
			}

		}
		sb.append("}");
		this.writer.write(sb.toString());

	}

	@Override
	protected void beginContent() throws Exception {
		this.writer.write("[");
	}

	@Override
	protected void endContent() throws Exception {
		this.writer.write("]");
	}

}
