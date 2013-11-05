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

	onExecute : function(options) {
		var dc = this.dc;
		dc.store.remove(dc.getSelectedRecords());
		if (!dc.multiEdit) {
			dc.store.sync({
				success : this.onAjaxSuccess,
				scope : this,
				options: options
			});
		} else {
			dc.doDefaultSelection();
		}		
	},

	onAjaxSuccess : function(batch, options) {
		Ext.Msg.hide();

		// if (options.operation.action == "update" || options.operation.action
		// == "create") {
		//this.dc.afterDoSaveSuccess();
		// }

		this.dc.requestStateUpdate();
		this.dc.fireEvent("afterDoCommitSuccess", this.dc, options.options);

	},
	
	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isDeleteDisabled(this.dc)) {
			this.dc.warning(Dnet.msg.DC_DELETE_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	}

});