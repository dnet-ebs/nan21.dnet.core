/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.view.AbstractDcvFilterForm", {
	extend : "dnet.core.dc.view.AbstractDNetDcForm",

	// **************** Properties *****************
	/**
	 * Component builder
	 */
	_builder_ : null,

	/**
	 * Helper property to identify this dc-view type as filter-form.
	 */
	_dcViewType_ : "filter-form",

	// **************** Public API *****************

	/**
	 * Returns the builder associated with this type of component. Each
	 * predefined data-control view type has its own builder. If it doesn't
	 * exist yet attempts to create it.
	 */
	_getBuilder_ : function() {
		if (this._builder_ == null) {
			this._builder_ = new dnet.core.dc.view.DcvFilterFormBuilder({
				dcv : this
			});
		}
		return this._builder_;
	},

	// **************** Defaults and overrides *****************

	frame : true,
	border : true,
	bodyPadding : '8px 5px 3px 5px',
	maskDisabled : false,
	layout : "fit",
	buttonAlign : "left",
	bodyCls : 'dcv-filter-form',
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

	initComponent : function(config) {
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
		var ctrl = this._controller_;
		if (ctrl && ctrl.getFilter()) {
			this._onBind_(ctrl.getFilter());
		} else {
			this._onUnbind_(null);
		}
		this._bindParams_();
		this._registerKeyBindings_();
	},

	beforeDestroy : function() {
		this._elems_.each(function(item, idx, len) {
			delete item._dcView_;
		}, this)
		this.callParent(arguments);
	},

	// **************** Private API *****************

	/**
	 * Register event listeners
	 */
	_registerListeners_ : function() {
		var ctrl = this._controller_;

		// When enter-query requested, move the focus to the
		// first navigation item.
		var s = this;
		this.mon(ctrl, "onEnterQuery", this._gotoFirstNavigationItem_, s);
		this.mon(ctrl, "parameterValueChanged", this._onParameterValueChanged_,
				s);
		this.mon(ctrl, "filterValueChanged", this._onFilterValueChanged_, s);

		var cmd = ctrl.commands.doQuery;
		if (cmd) {
			var fn = function() {
				if (this._shouldValidate_() && !this.getForm().isValid()) {
					this._controller_.error(Dnet.msg.INVALID_FILTER, "msg");
					return false;
				} else {
					return true;
				}
			};

			cmd.beforeExecute = Ext.Function.createInterceptor(
					cmd.beforeExecute, fn, this, -1);

		}

	},

	_gotoFirstNavigationItem_ : function() {
		this.down(" textfield").focus();
	},

	/**
	 * Bind the current filter model of the data-control to the form.
	 * 
	 */
	_onBind_ : function(filter) {
		this._updateBound_(filter);
		this._applyStates_(filter);
		this._afterBind_(filter);
	},

	/**
	 * Un-bind the filter from the form.
	 */
	_onUnbind_ : function(filter) {
		this._updateBound_(filter);
		this._afterUnbind_(filter);
	},

	/**
	 * When the filter has been changed in any way other than user interaction,
	 * update the fields of the form with the changed values from the model.
	 * Such change may happen by custom code snippets updating the model in a
	 * beginEdit-endEdit block, filter-service methods which returns changed
	 * data from server, etc.
	 */
	_updateBound_ : function(filter) {
		if (!filter) {
			this.disable();
			this.form.reset();
		} else {
			if (this.disabled) {
				this.enable();
			}

			var fields = this.getForm().getFields();

			fields.each(function(field) {
				if (field.dataIndex) {
					var nv = filter.data[field.dataIndex];
					if (field.getValue() != nv) {
						if (!(field.hasFocus && field.isDirty)) {
							field.suspendEvents();
							field.setValue(nv);
							field.resumeEvents();
						}
					}
				}
			});
		}
	},

	_registerKeyBindings_ : function() {
		var map = new Ext.util.KeyMap({
			target : this.getEl(),
			eventName : 'keydown',
			processEvent : function(event, source, options) {
				return event;
			},
			binding : [ Ext.apply(Dnet.keyBindings.dc.doClearQuery, {
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
			}), Ext.apply(Dnet.keyBindings.dc.doEditOut, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.doEditOut();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.nextPage, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.store.nextPage();
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.prevPage, {
				fn : function(keyCode, e) {
					e.stopEvent();
					this._controller_.store.previousPage();
				},
				scope : this
			}) ]
		});
	}

});