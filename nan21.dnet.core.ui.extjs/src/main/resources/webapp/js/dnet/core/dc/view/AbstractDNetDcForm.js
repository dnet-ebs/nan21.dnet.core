/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Base panel used for data-control views. It serves as base class for
 * edit-forms and filter-forms.
 */
Ext.define("dnet.core.dc.view.AbstractDNetDcForm", {
	extend : "Ext.form.Panel",

	mixins : {
		elemBuilder : "dnet.core.base.AbstractDNetView",
		dcViewSupport : "dnet.core.dc.view.AbstractDNetDcView"
	},

	// **************** Properties *****************

	// **************** Public API *****************

	/**
	 * Helper method to disable all fields.
	 */
	_disableAllFields_ : function() {
		this.getForm().getFields().each(function(item, index, length) {
			item.disable();
		});
	},

	/**
	 * Helper method to enable all fields.
	 */
	_enableAllFields_ : function() {
		this.getForm().getFields().each(function(item, index, length) {
			item.enable();
		});
	},

	/**
	 * There may be situations when a form should not validate. For example a
	 * data-control may have one form for insert and another one to edit an
	 * existing record. These forms may have different rules to validate (for
	 * example some fields are mandatory on insert but not on update others on
	 * update but not on insert). Override this function to implement the
	 * decision rules when to invoke the form validation.
	 * 
	 */
	_shouldValidate_ : function() {
		return true;
	},

	/**
	 * For the given field names apply the enabled / disabled state based on the
	 * provided business rules
	 */
	_setFieldsEnabledState_ : function(names, model) {
		if (!model) {
			return;
		}
		var l = names.length;
		for ( var i = 0; i < l; i++) {
			var n = names[i];
			var b = !this._canSetEnabled_(n, model);
			this._getElement_(n)._setDisabled_(b);
		}
	},

	/**
	 * For the given field names apply the visible / hidden state based on the
	 * provided business rules
	 */
	_setFieldsVisibleState_ : function(names, model) {
		if (!model)
			return;
		var fields = this.getForm().getFields();
		for ( var i = 0, l = names.length; i < l; i++) {
			var n = names[i];
			var b = this._canSetVisible_(n, model);
			this._getElement_(n).setVisible(b);
		}
	},

	_canSetEnabled_ : function(name, model) {
		var fn = this._elems_.get(name)._enableFn_;
		if (fn) {
			return fn.call(this, this._controller_, model);
		} else {
			return true;
		}
	},

	_canSetVisible_ : function(name, model) {
		var fn = this._elems_.get(name)._visibleFn_;
		if (fn) {
			return fn.call(this, this._controller_, model);
		} else {
			return true;
		}
	},

	/**
	 * Helper method to disable the specified fields.
	 */
	_disableFields_ : function(fldNamesArray) {
		for ( var i = 0, l = fldNamesArray.length; i < l; i++) {
			this._get_(fldNamesArray[i]).disable();
		}
	},

	/**
	 * Generic validation method which displays an message box for the user.
	 */
	_isValid_ : function() {
		if (this.getForm().isValid()) {
			return true;
		} else {
			this._controller.error(Dnet.msg.INVALID_FORM, "msg");
			return false;
		}
	},

	// **************** Defaults and overrides *****************

	beforeDestroy : function() {
		// call the contributed helpers from mixins
		this._beforeDestroyDNetDcView_();
		this._beforeDestroyDNetView_();
		this.callParent(arguments);
	},

	// **************** Private methods *****************

	/**
	 * Post-processor run to inject framework specific settings into the
	 * elements.
	 * 
	 */
	_postProcessElem_ : function(item, idx, len) {
		item["_dcView_"] = this;
		if (item.fieldLabel == undefined) {
			Dnet.translateField(this._trl_, this._controller_._trl_, item);
		}
		return true;
	}

});