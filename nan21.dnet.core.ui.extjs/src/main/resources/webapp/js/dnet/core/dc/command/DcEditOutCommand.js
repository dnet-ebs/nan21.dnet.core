/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcEditOutCommand", {
	extend : "dnet.core.dc.command.AbstractDcSyncCommand",

	onExecute : function() {
		this.dc.fireEvent("onEditOut", this);
	},

	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isEditOutDisabled(this.dc)) {
			this.dc.warning(Dnet.msg.DC_EDIT_OUT_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	}
});
