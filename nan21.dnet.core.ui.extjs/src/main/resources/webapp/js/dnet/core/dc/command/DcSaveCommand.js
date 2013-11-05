/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.command.DcSaveCommand", {
	extend : "dnet.core.dc.command.AbstractDcAsyncCommand",

	errorTpl : Ext.create('Ext.XTemplate', [
			'<ul style="list-style-type: none;padding:0; margin:0;">',
			'<tpl for=".">', '<li style="list-style-type: none;">',
			'<span class="field-name">', Dnet.translate("ds", "fld"),
			' `{fieldTitle}` </span>', '<span class="error">{message}</span>',
			'</li></tpl></ul>' ]),

	beforeExecute : function() {
		var dc = this.dc;
		if (!dc.multiEdit) {
			return this.isValid(dc.getRecord());
		} else {
			if (!this.isValid(dc.store.getUpdatedRecords())) {
				return false;
			}
			if (!this.isValid(dc.store.getAllNewRecords())) {
				return false;
			}
			return true;
		}
	},

	// activeElement : null,

	onExecute : function(options) {
		if (this.dc.params != null) {
			this.dc.store.proxy.extraParams.params = Ext.JSON
					.encode(this.dc.params.data);
		}
		// this.activeElement = document.activeElement;
		Dnet.working();
		this.dc.store.sync({
			success : this.onAjaxSuccess,
			scope : this,
			options: options
		});
	},

	onAjaxSuccess : function(batch, options) {
		Ext.Msg.hide();

		// if (options.operation.action == "update" || options.operation.action
		// == "create") {
		this.dc.afterDoSaveSuccess();
		// }

		this.dc.requestStateUpdate();
		this.dc.fireEvent("afterDoCommitSuccess", this.dc, options.options);

	},

	isActionAllowed : function() {
		if (dnet.core.dc.DcActionsStateManager.isSaveDisabled(this.dc)) {
			this.dc.info(Dnet.msg.DC_SAVE_NOT_ALLOWED, "msg");
			return false;
		}
		return true;
	},

	/**
	 * Add the translated field name to the error info.
	 * 
	 * @param {}
	 *            item
	 * @param {}
	 *            idx
	 * @param {}
	 *            len
	 */
	addFieldNameToError : function(item, idx, len) {
		var v = Dnet.translateModelField(this.dc._trl_, item.field);
		item["fieldTitle"] = v;
	},

	/**
	 * Validate the given list of records
	 * 
	 * @param {}
	 *            recs
	 * @return {Boolean}
	 */
	isValid : function(recs) {
		if (!Ext.isArray(recs)) {
			recs = [ recs ];
		}
		var len = recs.length;
		var errors = null;
		for ( var i = 0; i < len; i++) {
			errors = recs[i].validate();
			if (!errors.isValid()) {
				errors.each(this.addFieldNameToError, this);

				Ext.Msg.show({
					title : 'Invalid data',
					msg : this.errorTpl.apply(errors.getRange()),
					icon : Ext.MessageBox.ERROR,
					buttons : Ext.MessageBox.OK
				});
				return false;
			}
		}
		return true;
	}
});
