/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.view.AbstractDcvEditForm", {
	extend : "dnet.core.dc.view.AbstractDNetDcForm",

	// **************** Properties *****************

	/**
	 * Flag to automatically disable form fields if the data-control is marked
	 * as read-only.
	 */
	_shouldDisableWhenDcIsReadOnly_ : true,

	/**
	 * Specify how to apply the form disable when shouldDisableWhenDcIsReadOnly
	 * property is true. Possible values are : fields - call disable on all
	 * fields (default value) panel - disable the form panel elems - call
	 * disable on all elements
	 * 
	 */
	_disableModeWhenDcIsReadOnly_ : "fields",

	/**
	 * Component builder
	 */
	_builder_ : null,

	/**
	 * Helper property to identify this dc-view type as edit form.
	 */
	_dcViewType_ : "edit-form",

	/**
	 * Array of image fields. Kept separately to manage binding.
	 */
	_images_ : null,

	/**
	 * Flag to automatically acquire focus when create new record.
	 */
	_acquireFocusInsert_ : true,

	/**
	 * Flag to automatically acquire focus when edit a record
	 */
	_acquireFocusUpdate_ : true,

	// **************** Public API *****************

	/**
	 * Returns the builder associated with this type of component. Each
	 * predefined data-control view type has its own builder. If it doesn't
	 * exist yet attempts to create it.
	 */
	_getBuilder_ : function() {
		if (this._builder_ == null) {
			this._builder_ = new dnet.core.dc.view.DcvEditFormBuilder({
				dcv : this
			});
		}
		return this._builder_;
	},

	_setShouldDisableWhenDcIsReadOnly_ : function(v, immediate) {
		this._shouldDisableWhenDcIsReadOnly_ = v;
		if (immediate && v) {
			this._doDisableWhenDcIsReadOnly_();
		}
	},

	// **************** Defaults and overrides *****************

	frame : true,
	border : true,
	bodyPadding : '8px 5px 3px 5px',
	maskOnDisable : false,
	layout : "fit",
	buttonAlign : "left",
	bodyCls : 'dcv-edit-form',
	trackResetOnLoad : false,
	fieldDefaults : {
		labelAlign : "right",
		labelWidth : 100
	},

	defaults : {
		frame : false,
		border : false,
		bodyBorder : false,
		bodyStyle : " background:transparent "
	},

	initComponent : function() {
		this._runElementBuilder_();
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
		this._bindParams_();
		this._registerKeyBindings_();

		// acquire first time focus

		if (this._controller_.record && this._controller_.record.phantom
				&& this._acquireFocusInsert_) {
			(new Ext.util.DelayedTask(this._gotoFirstNavigationItem_, this))
					.delay(200);
		}
		if (this._controller_.record && !this._controller_.record.phantom
				&& this._acquireFocusUpdate_) {
			(new Ext.util.DelayedTask(this._gotoFirstNavigationItem_, this))
					.delay(200);
		}
	},

	beforeDestroy : function() {
		this._elems_.each(function(item, idx, len) {
			delete item._dcView_;
		}, this);
		this.callParent(arguments);
	},
	// **************** Private API *****************

	/**
	 * Register event listeners
	 */
	_registerListeners_ : function() {
		var ctrl = this._controller_;
		var store = ctrl.store;

		// controller listeners

		this.mon(ctrl, "recordChange", this._onController_recordChange_, this);
		this.mon(ctrl, "recordReload", this._onController_recordReload_, this);
		this.mon(ctrl, "readOnlyChanged", function() {
			this._applyStates_(this._controller_.getRecord());
		}, this);
		this.mon(ctrl, "parameterValueChanged", this._onParameterValueChanged_,
				this);

		if (this._acquireFocusInsert_) {
			this.mon(ctrl, "afterDoNew", this._gotoFirstNavigationItem_, this);
		}
		if (this._acquireFocusUpdate_) {
			this.mon(ctrl, "onEditIn", this._gotoFirstNavigationItem_, this);
			this.mon(store, "write", this._gotoFirstNavigationItem_, this);
		}

		// store listeners

		this.mon(store, "datachanged", this._onStore_datachanged_, this);
		this.mon(store, "update", this._onStore_update_, this);

		if (this._controller_.commands.doSave) {
			this._controller_.commands.doSave.beforeExecute = Ext.Function
					.createInterceptor(
							this._controller_.commands.doSave.beforeExecute,
							function() {
								if (this._shouldValidate_()
										&& !this.getForm().isValid()) {
									this._controller_.error(
											Dnet.msg.INVALID_FORM, "msg");
									return false;
								} else {
									return true;
								}
							}, this, -1);
		}
	},

	_gotoFirstNavigationItem_ : function() {
		this.down(" textfield").focus();
	},

	// /**
	// * If it is not a multiEdit data-control, is very likely editing is done
	// * with such an edit form. After save, refocus the first textfield.
	// */
	// _onStore_write_ : function(store, operation, eopts) {
	// this.down(" textfield").focus();
	// },

	/**
	 * Update the bound record when the store data is changed.
	 */
	_onStore_datachanged_ : function(store, eopts) {
		this._updateBound_(this._controller_.getRecord(), null, null);
	},

	/**
	 * Update the bound record when the store data is updated.
	 */
	_onStore_update_ : function(store, rec, op, modFieldNames, eopts) {
		if (this.getForm().getRecord() === rec) {
			this._updateBound_(rec, op, modFieldNames);
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
	},

	_onController_recordReload_ : function(evnt) {
		var r = evnt.record;
		if (this.getForm()._record == r) {
			this._applyStates_(r);
		}
	},
	/**
	 * Bind the current record of the data-control to the form.
	 * 
	 */
	_onBind_ : function(record) {

		if (record) {
			if (this.disabled) {
				this.enable();
			}
			if (this._images_ != null) {
				for ( var i = 0, l = this._images_.length; i < l; i++) {
					var img = this._getElement_(this._images_[i]);
					img.setSrc(record.get(img.dataIndex));
				}
			}
			var fields = this.getForm().getFields();
			var trackResetOnLoad = this.getForm().trackResetOnLoad;
			fields.each(function(field) {
				if (field.dataIndex) {
					field.suspendEvents();
					field.setValue(record.get(field.dataIndex));
					if (trackResetOnLoad) {
						field.resetOriginalValue();
					}
					field.resumeEvents();
				}
			});
			this.getForm()._record = record;
			this._applyStates_(record);
			this.getForm().isValid();
		}
		this._afterBind_(record);
	},

	/**
	 * Un-bind the record from the form.
	 */
	_onUnbind_ : function(record) {
		if (this._images_ != null) {
			for ( var i = 0, l = this._images_.length; i < l; i++) {
				var img = this._getElement_(this._images_[i]);
				img.setSrc("");
			}
		}
		this.getForm().getFields().each(function(field) {
			if (field.dataIndex) {
				field.setRawValue(null);
				field.clearInvalid();
			}
			// field._disable_();
		});
		if (!this.disabled) {
			this.disable();
		}

		this._afterUnbind_(record);
	},

	/**
	 * When the record has been changed in any way other than user interaction,
	 * update the fields of the form with the changed values from the model.
	 * Such change may happen by custom code snippets updating the model in a
	 * beginEdit-endEdit block, reload record from server, service methods which
	 * returns changed record data from server, etc.
	 * 
	 */
	_updateBound_ : function(record, op, modFieldNames) {
		var msg = "null";
		if (record) {
			var fields = this.getForm().getFields();
			if (modFieldNames) {
				var l = modFieldNames.length;
				for ( var i = 0; i < l; i++) {
					var field = this._findFieldByDataIndex(fields,
							modFieldNames[i]);
					if (field) {
						var nv = record.data[field.dataIndex];
						if (field.getValue() != nv) {
							if (op == "reject"
									|| !(field.hasFocus && field.isDirty)) {
								field.setValue(nv);
							}
						}
					}
				}
			} else {
				fields.each(function(field) {
					if (field.dataIndex) {
						var nv = record.data[field.dataIndex];
						if (field.getValue() != nv) {
							if (op == "reject"
									|| !(field.hasFocus && field.isDirty)) {
								field.setValue(nv);
							}
						}
					}
				});
			}
		}
	},

	/**
	 * Helper method to find a form field by its dataIndex property.
	 */
	_findFieldByDataIndex : function(fields, dataIndex) {
		var items = fields.items, i = 0, len = items.length;

		for (; i < len; i++) {
			if (items[i]["dataIndex"] == dataIndex) {
				return items[i];
			}
		}
		return null;
	},

	/**
	 * The edit-form specific state rules. The flow is: If the fields are marked
	 * with noInsert, noUpdate or noEdit these rules are applied and no other
	 * option checked If no such constraint, the _canSetEnabled_ function is
	 * checked for each element.
	 * 
	 */
	_onApplyStates_ : function(record) {
		// the form has been disabled by the (un)bind.
		// Nothing to change
		if (record == null || record == undefined) {
			return;
		}
		if (this._shouldDisableWhenDcIsReadOnly_
				&& this._controller_.isReadOnly()) {
			this._doDisableWhenDcIsReadOnly_();
			return;
		}
		if (record.phantom) {
			this.getForm().getFields().each(
					function(item, index, length) {
						if (item._visibleFn_ != undefined) {
							item.setVisible(this._canSetVisible_(item.name,
									record));
						}
						if (item.noEdit === true || item.noInsert === true) {
							item._disable_();
						} else {
							if (item._enableFn_ != undefined) {
								item._setDisabled_(!this._canSetEnabled_(
										item.name, record));
							} else {
								item._enable_();
							}
						}
					}, this);
		} else {
			this.getForm().getFields().each(
					function(item, index, length) {
						if (item._visibleFn_ != undefined) {
							item.setVisible(this._canSetVisible_(item.name,
									record));
						}
						if (item.noEdit === true || item.noUpdate === true) {
							item._disable_();
						} else {
							if (item._enableFn_ != undefined) {
								item._setDisabled_(!this._canSetEnabled_(
										item.name, record));
							} else {
								item._enable_();
							}
						}
					}, this);
		}

	},

	// /**
	// * Replaced by the _applyStates_ method. Kept only for backward
	// * compatibility but will be removed. Please update your code.
	// */
	// _applyContextRules_ : function(record) {
	// this._applyStates_(record);
	// },

	_doDisableWhenDcIsReadOnly_ : function() {
		if (this._shouldDisableWhenDcIsReadOnly_
				&& this._controller_.isReadOnly()) {
			this["_doDisableWhenDcIsReadOnly_"
					+ this._disableModeWhenDcIsReadOnly_ + "_"]();
		}
	},

	_doDisableWhenDcIsReadOnly_fields_ : function() {
		this.getForm().getFields().each(this._disableElement_);
	},

	_doDisableWhenDcIsReadOnly_panel_ : function() {
		this.disable();
	},

	_doDisableWhenDcIsReadOnly_elems_ : function() {
		this._elems_.each(this._disableElement_);
	},

	_disableElement_ : function(e) {
		e._disable_();
	},

	_registerKeyBindings_ : function() {
		var map = new Ext.util.KeyMap({
			target : this.getEl(),
			eventName : 'keydown',
			processEvent : function(event, source, options) {
				return event;
			},
			binding : [ Ext.apply(Dnet.keyBindings.dc.doQuery, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doQuery();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doNew, {
				fn : function(keyCode, e) {
					console.log("AbstractDcvEditForm.doNew");
					e.stopEvent();
					this._controller_.doNew();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doCancel, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doCancel();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doSave, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doSave();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doCopy, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doCopy();
					this._controller_.doEditIn();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doEditIn, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doEditIn();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doEditOut, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doEditOut();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.nextRec, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.setNextAsCurrent();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.prevRec, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.setPreviousAsCurrent();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.nextPage, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.nextPage();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.prevPage, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.previousPage();
				},
				scope : this
			}) ]
		});
	}

});