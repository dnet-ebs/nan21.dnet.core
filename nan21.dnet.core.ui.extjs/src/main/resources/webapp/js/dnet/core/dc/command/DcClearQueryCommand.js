/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcClearQueryCommand", {
	extend : "dnet.core.dc.command.AbstractDcSyncCommand",

	onExecute : function(options) {
		var dc = this.dc;

		for ( var k in dc.filter.data) {
			dc.setFilterValue(k, null, false, "clearQuery");
		}
		
		var p = dc.params;
		if (p) {
			for ( var k in dc.params.data) {
				if (p.fields.get(k).forFilter === true) {
					dc.setParamValue(k, null, false, "clearQuery");	
				}
			}
		}
		
		if (dc.dcContext) {
			dc.dcContext._updateChildFilter_();
		}
	}

});