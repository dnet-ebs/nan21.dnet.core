/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.exceptions;

public class ActionNotSupportedException extends AbstractException {

  	private static final long serialVersionUID = 1L;
  	 
  	public ActionNotSupportedException() {
        super("Action not supported.");
    }
  	public ActionNotSupportedException(String message) {
  		super(message);
  	}
  	  
}