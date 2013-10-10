/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext
		.define(
				"dnet.core.dc.view.AbstractDcvFilterPropGrid",
				{
					extend : "dnet.core.dc.view.AbstractDNetDcPropGrid",

					// **************** Properties *****************
					/**
					 * Component builder
					 */
					_builder_ : null,

					/**
					 * Helper property to identify this dc-view type as filter
					 * property grid.
					 */
					_dcViewType_ : "filter-propgrid",

					// **************** Public API *****************

					/**
					 * Returns the builder associated with this type of
					 * component. Each predefined data-control view type has its
					 * own builder. If it doesn't exist yet attempts to create
					 * it.
					 */
					_getBuilder_ : function() {
						if (this._builder_ == null) {
							this._builder_ = new dnet.core.dc.view.DcvFilterPropGridBuilder(
									{
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
					 * After the form is rendered invoke the record binding.
					 * This is necessary as the form may be rendered
					 * lazily(delayed) and the data-control may already have a
					 * current record set.
					 */
					afterRender : function() {
						this.callParent(arguments);
						if (this._controller_ && this._controller_.getFilter()) {
							this._onBind_(this._controller_.getFilter());
						} else {
							this._onUnbind_(null);
						}
						this._registerKeyBindings_();
					},

					// **************** Private API *****************

					/**
					 * Register event listeners
					 */
					_registerListeners_ : function() {
						var ctrl = this._controller_;

						// When enter-query requested, move the focus to the
						// first navigation
						// item.
						this.mon(ctrl, "onEnterQuery",
								this._gotoFirstNavigationItem_, this);

						this.mon(ctrl, "parameterValueChanged",
								this._onParameterValueChanged_, this);

						this.mon(ctrl, "filterValueChanged",
								this._onFilterValueChanged_, this);

						this.mon(this, "edit", this._onEdit_, this);
					},

					_gotoFirstNavigationItem_ : function() {
						if (this.collapsed !== false) {
							this.expand();
						}
						this.getView().focus();
					},

					/**
					 * On-edit handler
					 */
					_onEdit_ : function(editor, evnt, eOpts) {
						var n = evnt.record.data.name;
						var p = this._getElementConfig_(n);
						if (p.paramIndex != undefined) {
							this._controller_.setParamValue(n, evnt.value);
						} else {
							this._controller_.setFilterValue(n, evnt.value);
						}
						return true;
					},

					/**
					 * Bind the current filter model of the data-control to the
					 * form.
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
					 * When the filter has been changed in any way other than
					 * user interaction, update the fields of the form with the
					 * changed values from the model. Such change may happen by
					 * custom code snippets updating the model in a
					 * beginEdit-endEdit block, filter-service methods which
					 * returns changed data from server, etc.
					 */
					_updateBound_ : function(filter) {
						if (!filter) {
							this.disable();
						} else {
							if (this.disabled) {
								this.enable();
							}
							var s = this.getSource();
							for ( var p in s) {
								if (filter.data.hasOwnProperty(p) === true) {
									this.setProperty(p, filter.data[p], true);
								}
							}
						}
					},

					/**
					 * The filter model is not part of a store, so we have
					 * listen to changes made to the model through the
					 * `filterValueChanged` event raised by the data-control. So
					 * changes to the filter model should be done through the
					 * setFilterValue method of the data-control in order to be
					 * listened and picked-up to refresh the correcponding
					 * filter-form fields.
					 * 
					 */
					_onFilterValueChanged_ : function(dc, property, ov, nv) {

						var s = this.getSource();

						if (s.hasOwnProperty(property)) {
							this.setProperty(property, nv, false);
						}
					},

					/**
					 * The parameters model is not part of a store, so we have
					 * listen to changes made to the model through the
					 * `parameterValueChanged` event raised by the data-control.
					 * Changes to the parameters model should be done through
					 * the setParamValue method of the data-control in order to
					 * be listened and picked-up to refresh the corresponding
					 * form fields.
					 * 
					 */
					_onParameterValueChanged_ : function(dc, property, ov, nv) {
						var s = this.getSource();

						if (s.hasOwnProperty(property)) {
							this.setProperty(property, nv, false);
						}
					},

					_registerKeyBindings_ : function() {
						var map = new Ext.util.KeyMap({
							target : this.getEl(),
							eventName : 'keydown',
							processEvent : function(event, source, options) {
								return event;
							},
							binding : [
									Ext.apply(Dnet.keyBindings.dc.doClearQuery,
											{
												fn : function(keyCode, e) {
													e.stopEvent();
													this._controller_
															.doClearQuery();
												},
												scope : this
											}),
									Ext.apply(Dnet.keyBindings.dc.doQuery, {
										fn : function(keyCode, e) {
											e.stopEvent();
											this._controller_.doQuery();
										},
										scope : this
									}),
									Ext.apply(Dnet.keyBindings.dc.nextPage, {
										fn : function(keyCode, e) {
											e.stopEvent();
											this._controller_.store.nextPage();
										},
										scope : this
									}),
									Ext.apply(Dnet.keyBindings.dc.prevPage, {
										fn : function(keyCode, e) {
											e.stopEvent();
											this._controller_.store
													.previousPage();
										},
										scope : this
									}) ]
						});
					}

				});