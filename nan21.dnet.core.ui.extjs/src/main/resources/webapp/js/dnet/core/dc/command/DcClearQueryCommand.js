/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcClearQueryCommand", {
	extend : "dnet.core.dc.command.AbstractDcSyncCommand",

	onExecute : function() {
		var dc = this.dc;

		for ( var p in dc.filter.data) {
			dc.setFilterValue(p, null, false, "clearQuery");
		}

		if (dc.dcContext) {
			dc.dcContext._updateChildFilter_();
		}
	}

});