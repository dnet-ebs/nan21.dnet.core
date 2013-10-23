/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.security;

import java.io.Serializable;

public class DnetClient implements IClient, Serializable {

	private static final long serialVersionUID = -9131543374115237340L;
	private final String id;
	private final String code;
	private final String name;

	public DnetClient(String id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
