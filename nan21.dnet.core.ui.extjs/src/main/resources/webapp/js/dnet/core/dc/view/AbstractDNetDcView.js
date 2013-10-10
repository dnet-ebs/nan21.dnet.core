/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Mixin which provides DC-view support.
 */
Ext.define("dnet.core.dc.view.AbstractDNetDcView", {

	// **************** Properties *****************

	/**
	 * DC-controller
	 */
	_controller_ : null,

	// **************** Public API *****************

	/**
	 * Returns the controller of this view
	 */
	_getController_ : function() {
		return this._controller_;
	},

	/**
	 * Apply state rules to enable/disable or show/hide components.
	 * 
	 * The model parameter is either the current record or current filter bound
	 * to this form-view.
	 * 
	 */
	_applyStates_ : function(model) {
		if (this._beforeApplyStates_(model) !== false) {
			this._onApplyStates_(model);
		}
		this._afterApplyStates_(model);
	},

	/**
	 * Template method checked before applying states.
	 */
	_beforeApplyStates_ : function(model) {
		return true;
	},

	/**
	 * Template method invoked after the state rules are applied.
	 */
	_afterApplyStates_ : function(model) {
	},

	/**
	 * Implement the state control logic in subclasses.
	 * 
	 */
	_onApplyStates_ : function(model) {

	},

	/**
	 * Template method called after a new model is bound to the form. Add custom
	 * logic in subclasses if necessary.
	 * 
	 * Model parameter: Current record or current filter depending on the DC
	 * view type
	 */
	_afterBind_ : function(model) {
	},

	/**
	 * Template method called after a model is un-bound from the form. Add
	 * custom logic in subclasses if necessary.
	 * 
	 * Model parameter: Current record or current filter depending on the DC
	 * view type
	 */
	_afterUnbind_ : function(model) {
	},

	// **************** Private methods *****************

	/**
	 * The filter model is not part of a store, so we have listen to changes
	 * made to the model through the `filterValueChanged` event raised by the
	 * data-control. Changes to the filter model should be done through the
	 * setFilterValue method of the data-control in order to be listened and
	 * picked-up to refresh the corresponding filter-form fields.
	 * 
	 */
	_onFilterValueChanged_ : function(dc, property, ov, nv, op) {
		// console.log(this.$className + ": " + property + " = " + nv );
		var fld = this._elems_.findBy(function(item) {
			return (item.dataIndex == property);
		});
		if (fld) {
			fld = this._getElement_(fld.name);
			if (fld.getValue() != nv) {
				if (op == "clearQuery" || !fld.hasFocus) {
					fld.suspendEvents();
					fld.setValue(nv);
					fld.resumeEvents();
				}
			}
		}
	},
	/**
	 * The parameters model is not part of a store, so we have listen to changes
	 * made to the model through the `parameterValueChanged` event raised by the
	 * data-control. Changes to the parameters model should be done through the
	 * setParamValue method of the data-control in order to be listened and
	 * picked-up to refresh the correcponding form fields.
	 * 
	 */
	_onParameterValueChanged_ : function(dc, property, ov, nv) {
		var fld = this._elems_.findBy(function(item) {
			return (item.paramIndex == property);
		});
		if (fld) {
			fld = this._getElement_(fld.name);
			if (!fld.hasFocus) {
				if (fld.getValue() != nv) {
					fld.suspendEvents();
					fld.setValue(nv);
					fld.resumeEvents();
				}
			}
		}
	},

	/**
	 * Bind the parameters to the elements which declare a paramIndex based
	 * binding.
	 */
	_bindParams_ : function() {
		var params = this._controller_.params;
		var fields = this._elems_.filterBy(function(item) {
			return (!Ext.isEmpty(item.paramIndex));
		});
		fields.each(function(field) {
			var f = this._getElement_(field.name);
			f.suspendEvents();
			f.setValue(params.get(field.paramIndex));
			f.resumeEvents();
		}, this);
	},

	/**
	 * Get the translation from the resource bundle for the specified key.
	 * 
	 * @param {String}
	 *            k Key to be translated
	 * @return {String} Translation of the key or the key itself if not
	 *         translation found.
	 */
	_getRBValue_ : function(k) {
		if (this._trl_ != null && this._trl_[k]) {
			return this._trl_[k];
		}
		if (this._controller_._trl_ != null && this._controller_._trl_[k]) {
			return this._controller_._trl_[k];
		} else {
			return k;
		}
	},

	_beforeDestroyDNetDcView_ : function() {
		this._controller_ = null;
		if (this._builder_) {
			this._builder_.dcv = null;
		}
	}

});