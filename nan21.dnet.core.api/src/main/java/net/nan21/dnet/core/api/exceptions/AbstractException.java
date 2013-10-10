/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.exceptions;

  
	public abstract class AbstractException extends Exception {
	   	 
		private static final long serialVersionUID = 1L;
		protected String errorCode;

		public AbstractException() {
			super();
		}
		
		protected AbstractException(String message) {
			super(message);
			this.errorCode = null;
		}
		
		protected AbstractException(String errorCode, String message) {
			super(message);
			this.errorCode = errorCode;
		}
		
		protected AbstractException(String message, Throwable exception) {
			super(message);		
			this.initCause(exception);
			this.errorCode = null;
		}
		
		protected AbstractException(String errorCode, String message, Throwable exception) {
			super(message);		
			this.initCause(exception);
			this.errorCode = errorCode;
		}
	 
	}
