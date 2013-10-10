/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.ui.ActionBuilder", {

	/**
	 * Frame instance
	 */
	frame : null,

	/**
	 * Toolbar name
	 */
	name : null,

	/**
	 * Default bound data-control.
	 */
	dc : null,

	/**
	 * Index to count the separators.
	 */
	sepIdx : null,

	constructor : function(config) {
		config = config || {};
		Ext.apply(this, config);
		this.callParent(arguments);
	},

	setup : function() {
	},

	addSeparator : function() {
		if (this.sepIdx == null) {
			this.sepIdx = 0;
		}
		this.frame._tlbitms_.add(this.name + "___S" + (this.sepIdx++) + "_",
				"-");
		return this;
	},

	/**
	 * Add a generic button.
	 */
	addButton : function(config) {
		var cfg = config || {};
		Ext.applyIf(config, {
			id : Ext.id(),
			xtype : "button"
		});
		this.frame._tlbitms_.add(this.name + "__" + config.name, config);
		return this;
	},

	/**
	 * Add generic buttons.
	 */
	addButtons : function(btns) {
		if (Ext.isArray(btns)) {
			for ( var i = 0; i < btns.length; i++) {
				this.addButton(btns[i]);
			}
		}
		return this;
	},

	/**
	 * Add a label element
	 */
	addLabel : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc,
			xtype : "label",
			cls : "dnet-toolbar-label"
		});
		this.frame._tlbitms_.add(this.name + "__" + config.name, config);
		return this;
	},

	/**
	 * Add title for toolbar.
	 */
	addTitle : function(config) {
		var cfg = config || {};
		var ttlKey = this.name + "__ttl";
		Ext.applyIf(cfg, {
			dc : this.dc,
			xtype : "label",
			name : "title",
			height : 20,
			text : this.frame.translate(ttlKey),
			cls : "dnet-toolbar-title"
		});
		this.frame._tlbitms_.add(this.name + "__" + cfg.name, cfg);
		if (this.frame._tlbtitles_ == null) {
			this.frame._tlbtitles_ = new Ext.util.MixedCollection();
		}
		this.frame._tlbtitles_.add(this.name, cfg.text);
		return this;
	},

	/**
	 * Add do-query action.
	 */
	addQuery : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc
		});
		var a = this.frame._getDc_(cfg.dc).actions.doQuery;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);
		return this;
	},

	/**
	 * Add next record action
	 */
	addNextRec : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc
		});
		var a = this.frame._getDc_(cfg.dc).actions.doNextRec;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a); // new
		return this;
	},

	/**
	 * Add previous record action
	 */
	addPrevRec : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc
		});
		var a = this.frame._getDc_(cfg.dc).actions.doPrevRec;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a); // new
		return this;
	},

	/**
	 * Add cancel changes action
	 */
	addCancel : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc,
			tlb : this.name,
			autoBack : true
		});
		var a = this.frame._getDc_(cfg.dc).actions.doCancel;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);
		if (cfg.autoBack) {
			this.frame.mon(this.frame._getDc_(cfg.dc), 'recordChanged',
					function(event) {
						if (event.record == null) {
							this._invokeTlbItem_("doEditOut", cfg.tlb);
						}
					}, this.frame);
		}
		return this;
	},

	/**
	 * Add delete selected records action.
	 */
	addDeleteSelected : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc
		});
		var a = this.frame._getDc_(cfg.dc).actions.doDelete;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);
		return this;
	},

	/**
	 * Add save action.
	 * 
	 * @param {}
	 *            config
	 * @return {}
	 */
	addSave : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc
		});
		var a = this.frame._getDc_(cfg.dc).actions.doSave;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);
		return this;
	},

	/**
	 * End toolbar creation.
	 * 
	 * @return {}
	 */
	end : function() {
		var n = this.name, t = this.frame._tlbitms_.filterBy(function(o, k) {
			return (k.indexOf(n + "__") != -1);
		}), tarray = t.getRange();

		this.frame._tlbs_.add(this.name, tarray);
		return this.frame._getBuilder_();
	},

	/**
	 * Add edit action
	 * 
	 * @param {}
	 *            config
	 * @return {}
	 */
	addEdit : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc,
			tlb : this.name,
			scope : this.frame
		});
		if (!cfg.fn) {
			cfg.fn = function() {
				var ct = (cfg.inContainer) ? this._getElement_(cfg.inContainer)
						: this._getElement_("main");
				var _cmp = null;
				if (cfg.showView) {
					var cmp = this._get_(cfg.showView);
					if (cmp) {
						ct.getLayout().setActiveItem(cmp);
					} else {
						_cmp = this._getElementConfig_(cfg.showView).id;
						ct.getLayout().setActiveItem(_cmp);
					}
				} else {
					ct.getLayout().setActiveItem(1);
				}

			};
		}

		var a = this.frame._getDc_(cfg.dc).actions.doEditIn;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);
		this.frame.mon(this.frame._getDc_(cfg.dc), "onEditIn", cfg.fn,
				cfg.scope);
		return this;
	},

	/**
	 * Add back button. Move to a different canvas/stacked view.
	 */
	addBack : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc,
			tlb : this.name,
			scope : this.frame
		});

		if (!cfg.fn) {
			cfg.fn = function() {
				var ct = (cfg.inContainer) ? this._getElement_(cfg.inContainer)
						: this._getElement_("main");
				var _cmp = null;
				if (cfg.showView) {
					var cmp = this._get_(cfg.showView);
					if (cmp) {
						ct.getLayout().setActiveItem(cmp);
					} else {
						_cmp = this._getElementConfig_(cfg.showView).id;
						ct.getLayout().setActiveItem(_cmp);
					}
				} else {
					ct.getLayout().setActiveItem(0);
				}
			}
		}

		var a = this.frame._getDc_(cfg.dc).actions.doEditOut;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);
		this.frame.mon(this.frame._getDc_(cfg.dc), "onEditOut", cfg.fn,
				cfg.scope);
		return this;
	},

	/**
	 * Add action for auto-load. When toggled, the child data is automatically
	 * loaded on parent change.
	 */
	addAutoLoad : function() {
		var cfg = {
			name : "autoLoad",
			disabled : false,
			enableToggle : true,
			text : Dnet.translate("tlbitem", "autoload__lbl"),
			tooltip : Dnet.translate("tlbitem", "autoload__tlp"),
			scope : dc,
			handler : function(btn, evnt) {

				dc.dcContext.relation.fetchMode = (btn.pressed) ? "auto"
						: "manual";

			},
			dc : this.dc
		};

		var dc = this.frame._getDc_(cfg.dc);
		if (dc.dcContext.relation.fetchMode == "auto"
				|| dc.dcContext.relation.fetchMode == undefined) {
			cfg.pressed = true;
		}

		var a = new Ext.Action(cfg);
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);
		return this;
	},

	/**
	 * Add reports contributed by the extension providers.
	 */
	addReports : function() {
		if (this.frame._reports_ != null) {
			var r = [], rc = this.frame._reports_;

			for ( var i = 0, l = rc.length; i < l; i++) {
				if (rc[i].toolbar == this.name) {
					var fn = function(item, e) {
						var dcReport = new dnet.core.dc.DcReport();
						var rec = this._getDc_(item.dcAlias).record;
						if (!rec) {
							var _msg = Dnet.translate("msg",
									"no_current_record_for_report",
									[ item.title ]);

							Ext.Msg.show({
								title : Dnet.translate("msg",
										"no_current_record"),
								msg : _msg,
								icon : Ext.MessageBox.INFO,
								buttons : Ext.Msg.OK
							});
							return false;
						}
						dcReport.applyDsFieldValues(item.params, rec.data);
						dcReport.run({
							url : item.url,
							contextPath : item.contextPath,
							params : item.params
						});
					};
					var rcfg = Ext.apply({
						scope : this.frame,
						text : rc[i].title,
						handler : fn
					}, rc[i]);

					r.push(rcfg);
				}
			}
			if (r.length > 0) {
				this.addSeparator();
				this.frame._tlbitms_.add(this.name + "___REPS_", {
					text : "Reports",
					menu : r
				});

			}
		}
		return this;
	},

	/**
	 * Add create new record button
	 * 
	 * @param {}
	 *            config
	 * @return {}
	 */
	addNew : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc,
			tlb : this.name,
			autoEdit : true
		});

		var a = this.frame._getDc_(cfg.dc).actions.doNew;

		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);

		if (cfg.autoEdit != "false") {
			this.frame.mon(this.frame._getDc_(cfg.dc), "afterDoNew",
					function() {
						this._invokeTlbItem_("doEditIn", cfg.tlb);
					}, this.frame);

		} else {
			if (!Ext.isEmpty(cfg.showView)) {
				var fn = this.createHandler_New(cfg);
				this.frame.mon(this.frame._getDc_(cfg.dc), "afterDoNew", fn,
						this.frame);
			}
		}
		return this;
	},

	/**
	 * Create the handler function for the New action
	 */
	createHandler_New : function(cfg) {
		return function() {

			var ct = null;

			if (cfg.inContainer) {
				ct = this._getElement_(cfg.inContainer);
			} else {
				ct = this._getElement_("main");
			}

			if (cfg.showView) {
				var cmp = this._get_(cfg.showView);
				if (cmp) {
					ct.getLayout().setActiveItem(cmp);
				} else {
					var _id = this._getElementConfig_(cfg.showView).id;
					ct.getLayout().setActiveItem(_id);
				}
			} else {
				ct.getLayout().setActiveItem(1);
			}

		};
	},

	/**
	 * Add copy current record button
	 * 
	 * @param {}
	 *            config
	 * @return {}
	 */
	addCopy : function(config) {
		var cfg = config || {};
		Ext.applyIf(cfg, {
			dc : this.dc,
			tlb : this.name,
			autoEdit : true
		});
		var a = this.frame._getDc_(cfg.dc).actions.doCopy;
		this.frame._tlbitms_.add(this.name + "__" + a.initialConfig.name, a);
		if (cfg.autoEdit != "false") {
			this.frame.mon(this.frame._getDc_(cfg.dc), "afterDoNew",
					function() {
						this._invokeTlbItem_("doEditIn", cfg.tlb);
					}, this.frame);
		} else {
			if (!Ext.isEmpty(cfg.showView)) {
				var fn = this.createHandler_Copy(cfg);
				this.frame.mon(this.frame._getDc_(cfg.dc), "afterDoNew", fn,
						this.frame);
			}
		}
		return this;
	},

	/**
	 * Create the handler function for the Copy action
	 */
	createHandler_Copy : function(cfg) {
		return function() {
			var ct = (cfg.inContainer) ? this._getElement_(cfg.inContainer)
					: this._getElement_("main");
			if (cfg.showView) {
				var cmp = this._get_(cfg.showView);
				if (cmp) {
					ct.getLayout().setActiveItem(cmp);
				} else {
					ct.getLayout().setActiveItem(
							this._getElementConfig_(cfg.showView).id);
				}
			} else {
				ct.getLayout().setActiveItem(1);
			}
		};
	}

});