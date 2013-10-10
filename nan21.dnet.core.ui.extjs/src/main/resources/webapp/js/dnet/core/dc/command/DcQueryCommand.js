/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcQueryCommand", {
	extend : "dnet.core.dc.command.AbstractDcAsyncCommand",

	beforeExecute : function() {
		var dc = this.dc;
		if (!dc.filter.isValid()) {
			this.dc.error(Dnet.msg.INVALID_FILTER, "msg");
			return;
		}
	},

	onExecute : function() {
		var dc = this.dc;
		var _p = dc.buildRequestParamsForQuery();
		Ext.apply(dc.store.proxy.extraParams, _p);
		dc.store.load({
			page : 1,
			scope : dc
		});
	},

	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isQueryDisabled(this.dc)) {
			this.dc.warning(Dnet.msg.DC_QUERY_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	}
});
