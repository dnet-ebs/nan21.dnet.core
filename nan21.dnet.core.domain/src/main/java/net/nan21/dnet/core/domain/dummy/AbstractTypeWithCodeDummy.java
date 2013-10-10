/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.domain.dummy;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.nan21.dnet.core.domain.impl.AbstractTypeWithCode;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.NoSql;

@Entity
@NoSql(dataFormat = DataFormatType.MAPPED)
@Table(name = AbstractTypeWithCodeDummy.TABLE_NAME)
public class AbstractTypeWithCodeDummy extends AbstractTypeWithCode {

	public static final String TABLE_NAME = "X_DUMMY2";

	private static final long serialVersionUID = -8865917134914502125L;

}
