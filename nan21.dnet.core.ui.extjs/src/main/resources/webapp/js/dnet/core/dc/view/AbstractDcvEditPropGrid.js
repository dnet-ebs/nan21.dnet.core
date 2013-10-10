/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.view.AbstractDcvEditPropGrid", {
	extend : "dnet.core.dc.view.AbstractDNetDcPropGrid",

	// **************** Properties *****************
	/**
	 * Component builder
	 */
	_builder_ : null,

	/**
	 * Helper property to identify this dc-view type as filter property grid.
	 */
	_dcViewType_ : "edit-propgrid",

	// **************** Public API *****************

	/**
	 * Returns the builder associated with this type of component. Each
	 * predefined data-control view type has its own builder. If it doesn't
	 * exist yet attempts to create it.
	 */
	_getBuilder_ : function() {
		if (this._builder_ == null) {
			this._builder_ = new dnet.core.dc.view.DcvEditPropGridBuilder({
				dcv : this
			});
		}
		return this._builder_;
	},

	// **************** Defaults and overrides *****************

	initComponent : function(config) {
		this._runElementBuilder_();
		var sourceObj = {};
		var propertyNames = {};
		var customEditors = {};
		var customRenderers = {};

		var fnSourceObj = function(item, idx, len) {
			sourceObj[item.name] = item._default_;
		};
		var fnPropertyName = function(item, idx, len) {
			propertyNames[item.name] = item.fieldLabel;
		};
		var fnCustomEditors = function(item, idx, len) {
			if (item.editorInstance) {
				customEditors[item.name] = item.editorInstance;
			}
		};
		var fnCustomRenderers = function(item, idx, len) {
			if (item.renderer) {
				customRenderers[item.name] = item.renderer;
			}
		};

		this._elems_.each(fnSourceObj, this);
		this._elems_.each(fnPropertyName, this);
		this._elems_.each(fnCustomEditors, this);
		this._elems_.each(fnCustomRenderers, this);

		var cfg = {
			autoScroll : true,
			source : sourceObj,
			propertyNames : propertyNames,
			customEditors : customEditors,
			customRenderers : customRenderers
		};
		Ext.apply(this, cfg);
		this.callParent(arguments);
		this._registerListeners_();
	},

	/**
	 * After the form is rendered invoke the record binding. This is necessary
	 * as the form may be rendered lazily(delayed) and the data-control may
	 * already have a current record set.
	 */
	afterRender : function() {
		this.callParent(arguments);
		if (this._controller_ && this._controller_.getRecord()) {
			this._onBind_(this._controller_.getRecord());
		} else {
			this._onUnbind_(null);
		}
	},

	// **************** Private API *****************

	/**
	 * Register event listeners
	 */
	_registerListeners_ : function() {

		this.mon(this._controller_.store, "datachanged",
				this._onStore_datachanged_, this);
		this
				.mon(this._controller_.store, "update", this._onStore_update_,
						this);
		this.mon(this._controller_.store, "write", this._onStore_write_, this);

		this.mon(this._controller_, "recordChange",
				this._onController_recordChange_, this);

		this.mon(this._controller_, "parameterValueChanged",
				this._onParameterValueChanged_, this);

		this.mon(this, "edit", function(editor, evnt, eOpts) {
			this._controller_.getRecord()
					.set(evnt.record.data.name, evnt.value);
			return true;
		}, this);
	},

	/**
	 * Bind the current filter model of the data-control to the form.
	 * 
	 */
	_onBind_ : function(record) {
		this._updateBound_(record);
		this._applyStates_(record);
		this._afterBind_(record);
	},

	/**
	 * Un-bind the filter from the form.
	 * 
	 */
	_onUnbind_ : function(record) {
		this._updateBound_(record);
		this._afterUnbind_(record);
	},

	/**
	 * When the filter has been changed in any way other than user interaction,
	 * update the fields of the form with the changed values from the model.
	 * Such change may happen by custom code snippets updating the model in a
	 * beginEdit-endEdit block, filter-service methods which returns changed
	 * data from server, etc.
	 * 
	 */
	_updateBound_ : function(record) {
		if (!record) {
			this.disable();
			// this.form.reset();
		} else {
			if (this.disabled) {
				this.enable();
			}
			var s = this.getSource();
			for ( var p in s) {
				if (record.data.hasOwnProperty(p)) {
					this.setProperty(p, record.data[p], true);
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
		var s = this.getSource();

		if (s.hasOwnProperty(property)) {
			this.setProperty(property, nv, false);
		}
	},

	/**
	 * Update the bound record when the store data is changed.
	 */
	_onStore_datachanged_ : function(store, eopts) {
		this._updateBound_(this._controller_.getRecord());
	},

	/**
	 * Update the bound record when the store data is updated.
	 */
	_onStore_update_ : function(store, rec, op, eopts) {
		this._updateBound_(rec);
	},

	/**
	 * Update the bound record after a succesful save.
	 */
	_onStore_write_ : function(store, operation, eopts) {
		/**
		 * use the first record from the result list as reference see Store on
		 * write event handler defined in AbstractDc.
		 */
		if (operation.action == "create") {
			this._applyContextRules_(operation.resultSet.records[0]);
		}
	},

	/**
	 * When the current record of the data-control is changed bind it to the
	 * form.
	 */
	_onController_recordChange_ : function(evnt) {
		var newRecord = evnt.newRecord;
		var oldRecord = evnt.oldRecord;
		var newIdx = evnt.newIdx;
		if (newRecord != oldRecord) {
			this._onUnbind_(oldRecord);
			this._onBind_(newRecord);
		}
	}
});