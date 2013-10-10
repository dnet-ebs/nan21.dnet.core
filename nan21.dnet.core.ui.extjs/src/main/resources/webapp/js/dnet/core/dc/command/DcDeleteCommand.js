/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcDeleteCommand", {
	extend : "dnet.core.dc.command.AbstractDcSyncCommand",

	constructor : function(config) {
		this.callParent(arguments);
		this.confirmByUser = true;
		this.confirmMessageTitle = Dnet.translate("msg", "dc_confirm_action");
		this.confirmMessageBody = Dnet.translate("msg",
				"dc_confirm_delete_selection");
	},

	onExecute : function() {
		var dc = this.dc;
		dc.store.remove(dc.getSelectedRecords());
		if (!dc.multiEdit) {
			dc.store.sync();
		} else {
			dc.doDefaultSelection();
		}		
	},

	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isDeleteDisabled(this.dc)) {
			this.dc.warning(Dnet.msg.DC_DELETE_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	}

});