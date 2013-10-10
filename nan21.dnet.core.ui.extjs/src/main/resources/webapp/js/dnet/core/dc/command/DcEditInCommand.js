/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcEditInCommand", {
	extend : "dnet.core.dc.command.AbstractDcSyncCommand",
 
	onExecute : function() {
		this.dc.fireEvent("onEditIn", this);
	}
});
