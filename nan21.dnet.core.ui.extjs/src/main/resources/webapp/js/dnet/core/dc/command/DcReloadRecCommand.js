/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcReloadRecCommand", {
	extend : "dnet.core.dc.command.AbstractDcAsyncCommand",

	beforeExecute : function() {
		var r = this.dc.getRecord();
		if (!r || r.phantom || !r.data.id) {
			return false;
		}
		return true;
	},

	onExecute : function(dc) {
		var dc = this.dc;

		Ext.Ajax.request({
			url : dc.store.proxy.api.load,
			params : {
				data : Ext.encode({
					id : dc.record.data.id
				})
			},
			success : this.onReload,
			scope : this
		});

	},

	onReload : function(response, opts) {
		var dc = this.dc;
		var rs = dc.store.proxy.reader.read(response);
		var r = dc.record;
		var nr = rs.records[0];
		var shouldCommit = !r.dirty;
		r.beginEdit();
		for ( var p in dc.record.data) {
			dc.record.set(p, nr.data[p]);
		}
		r.endEdit();
		if (shouldCommit) {
			r.commit();
		}
		dc.fireEvent('recordReload', {
			dc : dc,
			record : r
		});
	},

	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isReloadRecDisabled(this.dc)) {
			this.dc.info(Dnet.msg.DC_RELOAD_RECORD_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	}

});
