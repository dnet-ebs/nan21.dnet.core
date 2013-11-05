/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcPrevRecCommand", {
	extend : "dnet.core.dc.command.AbstractDcSyncCommand",

	onExecute : function(options) {
		var dc = this.dc;
		if (dc.selectedRecords.length <= 1) {
			var crtIdx = dc.store.indexOf(dc.record);
			if (--crtIdx < 0) {
				this.dc.info(Dnet.msg.AT_FIRST_RECORD, "msg");
			} else {
				dc.setRecord(dc.store.getAt(crtIdx), true);
			}
		} else {
			var crtIdx = dc.selectedRecords.indexOf(dc.record);
			if (--crtIdx < 0) {
				this.dc.info(Dnet.msg.AT_FIRST_RECORD, "msg");
			} else {
				dc.setRecord(dc.selectedRecords[crtIdx]);
			}
		}
	},

	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isPrevRecDisabled(this.dc)) {
			this.dc.info(Dnet.msg.DC_RECORD_CHANGE_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	}

});
