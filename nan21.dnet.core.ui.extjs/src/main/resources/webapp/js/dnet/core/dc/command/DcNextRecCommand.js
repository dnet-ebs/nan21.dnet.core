/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcNextRecCommand", {
	extend : "dnet.core.dc.command.AbstractDcSyncCommand",

	onExecute : function() {
		var dc = this.dc;
		if (dc.selectedRecords.length <= 1) {
			var crtIdx = dc.store.indexOf(dc.record);
			if (++crtIdx >= dc.store.getCount()) {
				this.dc.info(Dnet.msg.AT_LAST_RECORD, "msg");
			} else {
				dc.setRecord(dc.store.getAt(crtIdx), true);
			}
		} else {
			var crtIdx = dc.selectedRecords.indexOf(dc.record);
			if (++crtIdx >= dc.selectedRecords.length) {
				this.dc.info(Dnet.msg.AT_LAST_RECORD, "msg");
			} else {
				dc.setRecord(dc.selectedRecords[crtIdx]);
			}
		}
	},

	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isNextRecDisabled(this.dc)) {
			this.dc.warning(Dnet.msg.DC_RECORD_CHANGE_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	}
});
