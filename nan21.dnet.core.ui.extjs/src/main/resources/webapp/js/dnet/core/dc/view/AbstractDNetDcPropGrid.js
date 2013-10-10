/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Base class for data-control property grid based views. It serves as base
 * class for edit-form as property grids and filter-forms as property grids .
 */
Ext.define("dnet.core.dc.view.AbstractDNetDcPropGrid", {
	extend : "Ext.grid.property.Grid",

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
	 * There may be situations when a form should not validate. For example a
	 * data-control may have one form for insert and another one to edit an
	 * existing record. These forms may have different rules to validate (for
	 * example some fields are mandatory on insert but not on update others on
	 * update but not on insert). Override this function to implement the
	 * decision rules when to invoke the form validation.
	 */
	_shouldValidate_ : function() {
		return true;
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
			this._controller_.error(Dnet.msg.INVALID_FORM, "msg");
			return false;
		}
	},

	// **************** Private methods *****************

	/**
	 * Postprocessor run to inject framework specific settings into the
	 * elements.
	 */
	_postProcessElem_ : function(item, idx, len) {
		item["_dcView_"] = this;
		if (item.fieldLabel == undefined) {
			Dnet.translateField(this._trl_, this._controller_._trl_, item);
		}
		return true;
	},

	beforeDestroy : function() {
		// call the contributed helpers from mixins
		this._beforeDestroyDNetDcView_();
		this._beforeDestroyDNetView_();
		this.callParent(arguments);
	},

	// **************** destroy component *****************

	// TODO: to be reviewed!!

	beforeDestroy : function() {
		this._controller_ = null;
		this.callParent();
		try {
			this._unlinkElemRefs_();
		} catch (e) {

		}
		this._elems_.each(this.unlinkElem, this);
		this._elems_.each(this.destroyElement, this);
	},

	unlinkElem : function(item, index, len) {
		item._dcView_ = null;
	},

	destroyElement : function(elemCfg) {
		try {
			var c = Ext.getCmp(elemCfg.id);
			if (c) {
				Ext.destroy(c);
			}
		} catch (e) {
			// alert(e);
		}
	}

});