/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcCopyCommand", {
	extend : "dnet.core.dc.command.AbstractDcSyncCommand",

	dcApiMethod : dnet.core.dc.DcActionsFactory.COPY,

	onExecute : function(options) {
		var dc = this.dc;
		var store = dc.store;

		var source = dc.getRecord();
		if (!source) {
			return;
		}
		var target = source.copy();
		target.data.id = null;
		if (target.data.code) {
			target.data.code = null;
		}
		if (target.data.name) {
			target.data.name = 'Copy of ' + target.data.name;
		}
		if (target.data.refid) {
			target.data.refid = null;
		}
		target.phantom = true;
		target.dirty = true;
		Ext.data.Model.id(target);
		if (dc.dcContext) {
			dc.dcContext._applyContextData_(target);
		}
		dc.setRecord(target, true);
		var idx = store.indexOf(source);
		store.insert(idx + 1, target);

		dc.fireEvent("afterDoNew", {
			dc : dc
		});

	},

	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isCopyDisabled(this.dc)) {
			this.dc.warning(Dnet.msg.DC_COPY_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	}

});
