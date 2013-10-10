/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.action.query;

import net.nan21.dnet.core.api.action.query.ISortToken;

public class SortToken implements ISortToken {

	private String property;
	private String direction;

	public SortToken() {
	}

	public SortToken(String property) {
		this.property = property;
	}

	public SortToken(String property, String direction) {
		this.property = property;
		this.direction = direction;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
