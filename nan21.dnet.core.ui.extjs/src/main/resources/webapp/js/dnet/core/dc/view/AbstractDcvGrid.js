/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.view.AbstractDcvGrid", {
	extend : "dnet.core.dc.view.AbstractDNetDcGrid",

	// **************** Properties *****************

	/**
	 * Component builder
	 */
	_builder_ : null,

	// **************** Public API *****************

	/**
	 * Returns the builder associated with this type of component. Each
	 * predefined data-control view type has its own builder. If it doesn't
	 * exist yet attempts to create it.
	 */
	_getBuilder_ : function() {
		if (this._builder_ == null) {
			this._builder_ = new dnet.core.dc.view.DcvGridBuilder({
				dcv : this
			});
		}
		return this._builder_;
	},

	// **************** Private methods *****************

	initComponent : function(config) {

		this._initDcGrid_();
		var cfg = this._createDefaultGridConfig_();
		Ext.apply(cfg, {
			selModel : {
				mode : "MULTI",
				listeners : {
					"selectionchange" : {
						scope : this,
						fn : this._selectionHandler_,
						buffer : 200
					},
					"beforedeselect" : {
						scope : this,
						fn : function(sm, record, index, eopts) {

							if (record == this._controller_.record
									&& !this._controller_.dcState
											.isRecordChangeAllowed()) {
								return false;
							}
						}
					}
				}
			},
			listeners : {
				"itemdblclick" : {
					scope : this,
					fn : function(view, model, item, idx, evnt, evntOpts) {
						this._controller_.doEditIn();
					}
				}
			}

		});
		Ext.apply(this, cfg);
		this.callParent(arguments);
		this._registerListeners_();
		this._registerKeyBindings_();
	},

	/**
	 * Register event listeners
	 */
	_registerListeners_ : function() {
		var ctrl = this._controller_;
		var store = ctrl.store;

		// When edit-out requested, focus this grid view. It's very likely that
		// coming from an editor, the user wants to get to the list.
		this.mon(ctrl, "onEditOut", this._gotoFirstNavigationItem_, this);
		this.mon(ctrl, "selectionChange", this._onController_selectionChange,
				this);
		this.mon(store, "load", this._onStore_load_, this);

	},

	_gotoFirstNavigationItem_ : function() {
		var v = this.getView();
		v.focusRow(v.getSelectionModel().getLastSelected());
	},

	_registerKeyBindings_ : function() {
		var map = new Ext.util.KeyMap({
			target : this.view,
			eventName : 'itemkeydown',
			processEvent : function(view, record, node, index, event) {
				// event.view = view;
				// event.store = view.getStore();
				// event.sm = view.getSelectionModel();
				// event.record = record;
				// event.index = index;
				return event;
			},
			binding : [ Ext.apply(Dnet.keyBindings.dc.doEnterQuery, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doEnterQuery();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doClearQuery, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doClearQuery();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doQuery, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doQuery();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doNew, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doNew();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doCancel, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doCancel();
					this.view.focus();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doSave, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doSave();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doDelete, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doDeleteSelection();
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
			}), Ext.apply(Dnet.keyBindings.dc.nextPage, {
				fn : function(keyCode, e) {
					// console.log("AbstractDcvGrid.nextPage");
					e.stopEvent();
					this._controller_.nextPage();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.prevPage, {
				fn : function(keyCode, e) {
					// console.log("AbstractDcvGrid.prevPage");
					e.stopEvent();
					this._controller_.previousPage();
				},
				scope : this
			}), { // In the context of a grid, allow to switch to editor with
				// a plain ENTER key also, not just the doEditIn key binding
				key : Ext.EventObject.ENTER,
				ctrl : false,
				shift : false,
				alt : false,
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doEditIn();
				},
				scope : this
			} ]
		});
	}
});