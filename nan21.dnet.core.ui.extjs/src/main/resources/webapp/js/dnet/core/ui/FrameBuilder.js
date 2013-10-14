/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define('dnet.core.ui.FrameBuilder$TocModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'name',
		type : 'string'
	}, {
		name : 'title',
		type : 'string'
	} ]
});

Ext.define("dnet.core.ui.FrameBuilder", {

	/**
	 * Frame instance
	 */
	frame : null,

	constructor : function(config) {
		config = config || {};
		Ext.apply(this, config);
		this.callParent(arguments);
	},

	/**
	 * Add a data-control controller
	 */
	addDc : function(name, obj) {
		this.frame._dcs_.add(name, obj);
		obj._instanceKey_ = name;
		this.frame.mon(obj, "message", this.frame.message, this.frame);
		return this;
	},

	/**
	 * Link data-controls with the given rules.
	 */
	linkDc : function(childName, parentName, relation) {
		Ext.applyIf(relation, {
			fetchMode : "manual",
			strict : true
		});
		var c = this.frame._dcs_.get(childName);
		var p = this.frame._dcs_.get(parentName);
		var ctx = new dnet.core.dc.DcContext({
			childDc : c,
			parentDc : p,
			relation : relation
		});
		p.addChild(c);
		c.setDcContext(ctx);
		return this;
	},

	/**
	 * Add a data-control edit form view.
	 */
	addDcFormView : function(dc, config) {
		this.addDcView(dc, config);
		return this;
	},

	/**
	 * Add a data-control filter form view.
	 */
	addDcFilterFormView : function(dc, config) {
		this.addDcView(dc, config);
		return this;
	},

	/**
	 * Add a data-control grid view.
	 */
	addDcGridView : function(dc, config) {
		config.stateId = this.frame.$className + "-" + config.name;
		if (getApplication().getSession().rememberViewState) {
			config.stateful = true;
		}
		return this.addDcView(dc, config);
	},

	/**
	 * Add a data-control edit grid view.
	 */
	addDcEditGridView : function(dc, config) {
		return this.addDcGridView(dc, config);
	},

	/**
	 * Add a free-hand data-control view.
	 */
	addDcView : function(dc, config) {
		Ext.apply(config, {
			_controller_ : this.frame._dcs_.get(dc)
		});
		if (config._hasTitle_ === true) {
			config.title = this.frame.translate(config.name + "__ttl");
		}
		this.applyViewSharedConfig(config);
		return this;
	},

	/**
	 * Add a presentation panel
	 */
	addPanel : function(config) {
		config.listeners = config.listeners || {};
		if (config.onActivateDoLayoutFor) {
			var onActivateDoLayoutFor = config.onActivateDoLayoutFor;
			delete config.onActivateDoLayoutFor;
			var activate = {
				scope : this.frame,
				fn : function(cmp, opt) {
					for ( var i = 0; i < onActivateDoLayoutFor.length; i++) {
						var e = this._getElement_(onActivateDoLayoutFor[i]);
						e.doLayout();
					}
				}
			}
			if (!config.listeners.activate) {
				config.listeners.activate = activate;
			}
		}
		Ext.applyIf(config, {
			id : Ext.id()
		});

		var ttlKey = config.name + "__ttl";
		if (config._hasTitle_ === true) {
			config.title = this.frame.translate(config.name + "__ttl");
		}
		this.applyViewSharedConfig(config);
		return this;
	},

	/**
	 * Add child elements to the given container
	 */
	addChildrenTo : function(c, list, regions) {
		var isWrapped = this.frame._elems_.get(c)._wrapped_;
		var items = ((isWrapped) ? this.frame._elems_.get(c)["items"]["items"]
				: this.frame._elems_.get(c)["items"])
				|| [];
		for ( var i = 0, len = list.length; i < len; i++) {
			var cmp = this.frame._elems_.get(list[i]);
			items[items.length] = cmp;
			if (regions) {
				cmp.region = regions[i];
			}
		}
		if (isWrapped) {
			this.frame._elems_.get(c)["items"]["items"] = items;
		} else {
			this.frame._elems_.get(c)["items"] = items;
		}
		return this;
	},

	/**
	 * Link a toolbar to the given component.
	 */
	addToolbarTo : function(c, tlb) {
		this.frame._linkToolbar_(tlb, c);
		return this;
	},

	/**
	 * Start a toolbar creation process. The returned ActionBuilder provides
	 * methods to add elements with a fluid API
	 */
	beginToolbar : function(name, config) {
		return new dnet.core.ui.ActionBuilder({
			name : name,
			frame : this.frame,
			dc : config.dc
		});
	},

	/**
	 * Add a table-of-contents elements.Used to include several independent root
	 * components , each on its own canvas with a ToC like navigation.
	 */
	addToc : function(canvases) {
		var data = [];
		for ( var i = 0; i < canvases.length; i++) {
			data[i] = {
				"name" : canvases[i],
				"title" : this.frame.translate(canvases[i] + "__ttl")
			};
		}
		var store = Ext.create('Ext.data.Store', {
			model : 'dnet.core.ui.FrameBuilder$TocModel',
			data : data
		});

		var config = {
			name : "_toc_",
			collapsible : true,
			layout : "fit",
			id : Ext.id(),
			region : "west",
			title : 'Navigation',
			width : 200,
			frame : false,
			items : [ {
				name : "_toc_items_",
				xtype : 'gridpanel',
				id : Ext.id(),
				hideHeaders : true,
				autoScroll : true,
				viewConfig : {
					stripeRows : false
				},
				singleSelect : true,
				forceFit : true,
				store : store,
				columns : [ {
					header : 'title',
					dataIndex : 'title'
				} ],
				selModel : {
					mode : "SINGLE",
					listeners : {

						"selectionchange" : {
							scope : this.frame,
							fn : function(sm, selected, options) {
								if (this._getElement_("main").rendered) {
									this._showStackedViewElement_("main",
											selected[0].data.name);
								}
							}
						}
					}
				},
				listeners : {
					scope : this.frame,

					afterrender : function() {
						this._showTocElement_(0);
					}
				}
			} ]

		}
		this.frame._elems_.add(config.name, config);
		return this;
	},

	/**
	 * Create a button
	 * 
	 * @param {}
	 *            config
	 * @return {}
	 */
	addButton : function(config) {
		Ext.applyIf(config, {
			id : Ext.id(),
			xtype : "button"
		});

		var lblKey = config.name + "__lbl";
		var tlpKey = config.name + "__tlp";

		Ext.apply(config, {
			text : this.frame.translate(lblKey),
			tooltip : this.frame.translate(tlpKey)
		});

		if (!Dnet.viewConfig.USE_BUTTON_ICONS) {
			config.iconCls = null;
		}

		this.frame._elems_.add(config.name, config);

		if (config.stateManager) {
			var options = {
				and : config.stateManager.and
			};
			if (options.and) {
				this.frame._buttonStateRules_[config.name] = options.and;
			}
			dnet.core.ui.FrameButtonStateManager.register(config.name,
					config.stateManager.name, config.stateManager.dc,
					this.frame, options);
		}
		return this;
	},

	/**
	 * Add assignment component.
	 */
	addAsgn : function(config) {
		Ext.applyIf(config, {
			id : Ext.id(),
			objectIdField : "id"
		});
		this.frame._elems_.add(config.name, config);
		return this;
	},

	/**
	 * Add a window to the elements list.
	 */
	addWindow : function(config) {
		Ext.applyIf(config, {
			id : Ext.id(),
			_window_ : true
		});
		if (!config.listeners) {
			config.listeners = {}
		}
		if (!config.listeners.show) {
			config.listeners.show = {
				fn : function() {
					try {
						this.down(' textfield').focus();
					} catch (e) {

					}
				}
			}
		}
		config.title = this.frame.translate(config.name + "__ttl");
		this.frame._elems_.add(config.name, config);
		return this;
	},

	/**
	 * Add a generic free-hand component.
	 */
	add : function(config) {
		Ext.applyIf(config, {
			id : Ext.id()
		});
		this.frame._elems_.add(config.name, config);
		return this;
	},

	/**
	 * Merge (add missing, leave existing unchanged) existing configuration of
	 * the specified element with the new configuration.
	 * 
	 */
	merge : function(name, config) {
		Ext.applyIf(this.frame._elems_.get(name), config);
		return this;
	},

	/**
	 * Change (overwrite) existing configuration of the specified element with
	 * the new configuration.
	 */
	change : function(name, config) {
		Ext.apply(this.frame._elems_.get(name), config);
		return this;
	},

	/**
	 * Remove the specified element from the list.
	 */
	remove : function(name) {
		this.frame._elems_.remove(name);
		return this;
	},

	// private

	applyViewSharedConfig : function(config) {
		Ext.applyIf(config, {
			id : Ext.id(),
			itemId : config.name
		});
		this.frame._elems_.add(config.name, config);
	}
});