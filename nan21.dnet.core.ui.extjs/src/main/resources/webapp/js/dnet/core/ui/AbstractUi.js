/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.ui.AbstractUi", {
	extend : "Ext.panel.Panel",

	mixins : {
		elemBuilder : "dnet.core.base.AbstractDNetView"
	},

	// **************** Properties *****************

	/**
	 * Component builder
	 */
	_builder_ : null,

	/**
	 * Data-controls map
	 */
	_dcs_ : null,

	/**
	 * Toolbar definitions map
	 */
	_tlbs_ : null,

	/**
	 * Toolbar items definitions map
	 */
	_tlbitms_ : null,

	/**
	 * Toolbar titles map. Used to retrieve the title element from a toolbar in
	 * a later stage, for example by the FrameInspector
	 */
	_tlbtitles_ : null,

	/**
	 * Custom reports attached to this frame. It is an array of configuration
	 * objects with the following attributes: toolbar: the name of toolbar where
	 * the link to this report should be attached report: code of the report
	 * used to lookup the definition and rules to be invoked. title : title of
	 * the report to display in the menu
	 */
	_reports_ : null,

	/**
	 * Buttons state rules. Map with functions executed in frame context to
	 * apply enabled/disabled visible/hidden state to a button. Usually the
	 * framework invokes this functions whenever a state change occurs in the
	 * frame like record change in a data-control, save, selection, etc but it
	 * can be called programmatically also whenever is necessary.
	 * 
	 * The function is called with a DC as single argument
	 * 
	 */
	_buttonStateRules_ : {},

	/**
	 * Frame status bar
	 */
	_statusBar_ : null,

	/**
	 * Assignments called in this frame. This collects an array of assignment
	 * descriptors to be listed in the frame descriptor. As these components are
	 * not explicitely listed as elements, they are registerd here by the
	 * _showAsgnWindow_ function.
	 * 
	 */
	_asgns_ : null,

	// ************** to be reviewed

	/**
	 * Injected configuration variables map
	 */
	_configVars_ : null,

	/**
	 * Actions map
	 */
	_actions_ : null,

	_name_ : null,

	/**
	 * Frame title
	 */
	_title_ : null,

	_header_ : null,

	// **************** Public API *****************

	/**
	 * Get a data-control instance.
	 * 
	 * @param {String}
	 *            name
	 * @return {}
	 */
	_getDc_ : function(name) {
		return this._dcs_.get(name);
	},

	/**
	 * 
	 */
	_getRootDc_ : function() {
		// TODO: check for top level dc and if there are more than 1
		return this._dcs_.getAt(0);
	},

	/**
	 * Get a toolbar instance.
	 * 
	 * @param {String}
	 *            name
	 * @return {}
	 */
	_getToolbar_ : function(name) {
		return Ext.getCmp(this._tlbs_.get(name).id);
	},

	/**
	 * Get a toolbar's configuration.
	 * 
	 * @param {String}
	 *            name
	 * @return {Object}
	 */
	_getToolbarConfig_ : function(name) {
		return this._tlbs_.get(name);
	},

	/**
	 * Get a toolbar item instance.
	 * 
	 * @param {String}
	 *            name
	 * @return {}
	 */
	_getToolbarItem_ : function(name) {
		return Ext.getCmp(this._tlbitms_.get(name).id);
	},

	/**
	 * Get a toolbar item's configuration object.
	 * 
	 * @param {String}
	 *            name
	 * @return {Object}
	 */
	_getToolbarItemConfig_ : function(name) {
		return this._tlbitms_.get(name);
	},

	/**
	 * Factory method to create the data-control instances used in this frame.
	 */
	_defineDcs_ : function() {
	},

	/**
	 * Template method checked during elements definition flow. Return false to
	 * skip the _defineDcs_ call.
	 * 
	 * @return {Boolean}
	 */
	_beforeDefineDcs_ : function() {
		return true;
	},

	/**
	 * Template method invoked after the elements definition flow. Used mainly
	 * to add overrides to existing components.
	 */
	_afterDefineDcs_ : function() {
	},

	/**
	 * Factory method to create the toolbars
	 */
	_defineToolbars_ : function() {
	},

	/**
	 * Template method checked during toolbarss definition flow. Return false to
	 * skip the _defineToolbars_ call.
	 * 
	 * @return {Boolean}
	 */
	_beforeDefineToolbars_ : function() {
		return true;
	},

	/**
	 * Template method invoked after the toolbar definition.
	 */
	_afterDefineToolbars_ : function() {
	},

	/**
	 * Link toolbars to view components.
	 * 
	 * @param {String}
	 *            tlbName Toolbar name
	 * @param {String}
	 *            viewName View name
	 */
	_linkToolbar_ : function(tlbName, viewName) {
		this._linkToolbarImpl_(tlbName, viewName)
	},
	/**
	 * Returns the builder associated with this type of component. If it doesn't
	 * exist yet attempts to create it.
	 * 
	 * @return {dnet.core.ui.FrameBuilder}
	 */
	_getBuilder_ : function() {
		if (this._builder_ == null) {
			this._builder_ = new dnet.core.ui.FrameBuilder({
				frame : this
			});
		}
		return this._builder_;
	},

	/**
	 * TOC stands for table of contents. Used when grouping multiple independent
	 * data-controls into one frame. Activates the canvas associated with the
	 * specified TOC element name.
	 * 
	 * @param {String}
	 *            name TOC element name
	 */
	_showTocElement_ : function(name) {
		if (Ext.isNumber(name)) {
			var theToc = this._getElement_("_toc_").items.items[0];
			var r = theToc.store.getAt(name);
			theToc.getSelectionModel().select(r);
		} else {
			var theToc = this._getElement_("_toc_").items.items[0];
			var r = theToc.store.findRecord("name", name);
			theToc.getSelectionModel().select(r);
		}

	},

	/**
	 * Programmatically invoke a toolbar item action.
	 * 
	 * @param {String}
	 *            name Toolbar-item name
	 * @param {String}
	 *            tlbName Toolbar name
	 */
	_invokeTlbItem_ : function(name, tlbName) {
		var b = null;
		if (!tlbName) {
			b = this._tlbitms_.get(name);
		} else {
			b = this._tlbitms_.get(tlbName + "__" + name);
		}
		if (b) {
			b.execute();
		}
	},

	/**
	 * Opens an assignment window component.
	 * 
	 * @param {String}
	 *            asgnWdwClass
	 * @param {Object}
	 *            cfg Extra configuration to apply
	 */
	_showAsgnWindow_ : function(asgnWdwClass, cfg) {
		var _recData = this._dcs_.get(cfg.dc).record.data;
		var objectId = _recData[cfg.objectIdField];
		var objectDesc = "";
		if (!Ext.isEmpty(cfg.objectDescField)) {
			objectDesc = _recData[cfg.objectDescField];
		} else {
			if (_recData.hasOwnProperty("name")) {
				objectDesc = _recData["name"];
			}
		}

		var aw = Ext.create(asgnWdwClass, cfg);

		aw._controller_.params.objectId = objectId;
		if (this._asgns_ == null) {
			this._asgns_ = new Ext.util.MixedCollection();
		}
		if (!this._asgns_.containsKey(asgnWdwClass)) {
			this._asgns_.add(asgnWdwClass, {
				title : aw.title,
				className : asgnWdwClass
			});
		}
		aw.setTitle(aw.title + " | " + objectDesc);
		aw._controller_.initAssignement();
		aw.show();
	},

	// Private

	/**
	 * Add a toolbar to a view-element.
	 * 
	 * @param {}
	 *            tlbName
	 * @param {}
	 *            viewName
	 */
	_linkToolbarImpl_ : function(tlbName, viewName) {
		var tlb = this._tlbs_.get(tlbName);
		if (Ext.isEmpty(tlb)) {
			return;
		}
		var view = this._elems_.get(viewName);
		view["tbar"] = tlb;
	},

	/**
	 * Associate a grid with a toolbar to derive the _printTitle_ for the simple
	 * dynamic print list.
	 * 
	 * @param {}
	 *            dcvGridViewName
	 * @param {}
	 *            tlbName
	 */
	_setGridPrintTitle_ : function(dcvGridViewName, tlbName) {
		if (this._tlbtitles_ != null && this._tlbtitles_.containsKey(tlbName)) {
			var _t = this._tlbtitles_.get(tlbName);
			this._elems_.get(dcvGridViewName)["_printTitle_"] = _t;
		}
	},

	_onReady_ : function(p) {
		var s = this.$className;
		var simpleName = s.substring(s.lastIndexOf(".") + 1, s.length);
		getApplication().setFrameTabTitle(simpleName, this._title_);
		getApplication().applyFrameCallback(simpleName, this);
	},

	_config_ : function() {
	},

	_createKeyBindingHandler_ : function(itemName, tlbName) {
		return function() {
			this._invokeTlbItem_(itemName, tlbName);
		}
	},

	_applyStateButton_ : function(btnName) {
		var btn = this._getElement_(btnName);
		if (btn) {
			var sm = btn.initialConfig.stateManager;
			if (sm) {
				var bsr = this._buttonStateRules_[btnName];
				var theDc = this._getDc_(sm.dc);
				var smstate = dnet.core.ui.FrameButtonStateManager["is_"
						+ sm.name](theDc);
				if (smstate && bsr) {
					smstate = smstate && bsr.call(this, theDc);
				}
				if (smstate) {
					btn.enable();
				} else {
					btn.disable();
				}
			}
		}
	},

	_applyStateButtons_ : function(buttonNames) {
		for ( var i = 0, l = buttonNames.length; i < l; i++) {
			this._applyStateButton_(buttonNames[i]);
		}
	},

	_applyStateAllButtons_ : function() {
		this._elems_.filterBy(function(o, k) {
			return o.xtype == "button"
		}).eachKey(function(key, item) {
			this._applyStateButton_(key);
		}, this)
	},

	initComponent : function() {
		if (getApplication().getSession().rememberViewState) {
			Ext.state.Manager
					.setProvider(new Ext.state.LocalStorageProvider({}));
		}
		this._mainViewName_ = "main";
		try {
			this._trl_ = Ext.create(this.$className + "$Trl");
		} catch (e) {
			Ext.Msg.show({
				title : "Invalid language-pack",
				msg : "No translation file found for " + this.$className
						+ "$Trl <br> Using the default system language.",
				icon : Ext.MessageBox.INFO,
				buttons : Ext.Msg.OK
			});
		}

		this._elems_ = new Ext.util.MixedCollection();
		this._configVars_ = new Ext.util.MixedCollection();
		this._tlbs_ = new Ext.util.MixedCollection();
		this._tlbitms_ = new Ext.util.MixedCollection();
		this._actions_ = new Ext.util.MixedCollection();
		this._dcs_ = new Ext.util.MixedCollection();
		this._buttonStateRules_ = {};

		this._statusBar_ = new Ext.ux.StatusBar({
			id : 'ui-status-bar',
			defaultText : 'Status bar. Watch for messages ... ',
			defaultIconCls : 'default-icon',
			text : 'Ready',
			iconCls : 'x-status-valid',
			ui : "status-bar",
			items : [ '-', this.$className ]
		});

		Ext.Ajax.on("beforerequest",
				function() {
					this._statusBar_.setText(Dnet.translate("msg", "working")
							+ "... ");
				}, this);

		Ext.Ajax.on("requestcomplete",
				function(conn, response, options, eOpts) {
					if (!Ext.Ajax.isLoading()) {
						this._statusBar_.setText("Ready");
					}
				}, this);

		Ext.Ajax.on("requestexception",
				function(conn, response, options, eOpts) {
					if (!Ext.Ajax.isLoading()) {
						this._statusBar_.setText("Ready");
					}
					this.showAjaxErrors(response, options);
				}, this);

		this._config_();
		this._startDefine_();

		/* define data-controls */
		if (this._beforeDefineDcs_() !== false) {
			this._defineDcs_();
			this._afterDefineDcs_();
		}

		/* define stand-alone user-interface elements */
		if (this._beforeDefineElements_() !== false) {
			this._defineElements_();
			this._afterDefineElements_();
		}

		/* define toolbars */
		if (this._beforeDefineToolbars_() !== false) {
			this._defineToolbars_();
			this._afterDefineToolbars_();
		}

		/* build the ui, linking elements */
		if (this._beforeLinkElements_() !== false) {
			this._linkElements_();
			this._afterLinkElements_();
		}

		this._endDefine_();

		// try to figure out print tiles for grid views, if no explicit values
		// set
		// look for a toolbar with name tlbViewName and get its title.

		var _ttl_fn = function(item, idx, len) {
			if (!item._printTitle_) {
				this._setGridPrintTitle_(item.name, "tlb"
						+ item.name.toFirstUpper());
			}
		}

		this._elems_.each(_ttl_fn, this);

		Ext.apply(this, {
			layout : "fit",
			bbar : this._statusBar_,
			items : [ this._elems_.get(this._mainViewName_) ]
		});

		if (this._trl_ && this._trl_.title) {
			this._title_ = this._trl_.title;
		} else {
			this._title_ = Dnet.translate("ui", this.$className
					.substring(this.$className.lastIndexOf(".") + 1));
		}

		this.callParent(arguments);
		this.mon(this, "afterlayout", this._onReady_, this);
		this._registerKeyBindings_();
	},

	_registerKeyBindings_ : function() {
		var map = new Ext.util.KeyMap({
			target : document.body,
			eventName : 'keyup',
			processEvent : function(event, source, options) {
				return event;
			},
			binding : [ Ext.apply(Dnet.keyBindings.app.gotoNextTab, {
				fn : function(keyCode, e) {
					getApplication().gotoNextTab();
					e.stopEvent();
				},
				scope : this
			}) ]
		});
	},

	/**
	 * Translate a frame element.
	 * 
	 * @param {}
	 *            key
	 * @return {}
	 */
	translate : function(key) {
		if (this._trl_ && this._trl_[key]) {
			return this._trl_[key];
		} else {
			return key;
		}
	},

	/**
	 * Handle messages published by data-controls. Write them into the status
	 * bar notifications area.
	 * 
	 * The options argument has the following content: { type : type, message :
	 * message, trlGroup : trlGroup, params : params }
	 * 
	 * If a translation group is specified, attempt to translate it using the
	 * message as a translation key for the given translation group and the
	 * params as replacement for the placeholders.
	 * 
	 */
	message : function(options) {
		var o = options || {};
		var msg = o.message;
		if (o.trlGroup) {
			msg = Dnet.translate(o.trlGroup, msg, o.params);
		}
		this._statusBar_.setText(msg);
	},

	showAjaxErrors : function(response, options) {
		var msg, withDetails = false;
		if (response.responseText) {
			if (response.responseText.length > 2000) {
				msg = response.responseText.substr(0, 2000);
				withDetails = true;
			} else {
				msg = response.responseText;
			}
		} else {
			msg = "No response received from server.";
		}
		var alertCfg = {
			title : "Server message",
			msg : msg,
			scope : this,
			icon : Ext.MessageBox.ERROR,
			buttons : Ext.MessageBox.OK
		}
		if (withDetails) {
			alertCfg.buttons['cancel'] = 'Details';
			alertCfg['detailedMessage'] = response.responseText;
		}
		Ext.Msg.show(alertCfg);
	},

	beforeDestroy : function() {
		this._beforeDestroyDNetView_();
		this._elems_.each(this.destroyElement, this);
		this._tlbitms_.each(function(item) {
			try {
				Ext.destroy(item);
			} catch (e) {
				alert(e);
			}
		});
		this._tlbs_.each(function(item) {
			try {
				Ext.destroy(item);
			} catch (e) {
				alert(e);
			}
		});
		this._dcs_.each(function(item) {
			try {
				Ext.destroy(item);
			} catch (e) {
				alert(e);
			}
		});
	},

	destroyElement : function(elemCfg) {
		try {
			var c = Ext.getCmp(elemCfg.id);
			if (c) {
				Ext.destroy(c);
			}
		} catch (e) {
		}
	}

});