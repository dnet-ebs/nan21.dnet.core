/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.ns("dnet.core.base");

/**
 * Contributed menus
 */
dnet.core.base.ApplicationMenu$ContributedMenus = [];

/**
 * Themes
 */
dnet.core.base.ApplicationMenu$Themes = [ {
	text : Dnet.translate("appmenuitem", "theme_blue__lbl"),
	handler : function() {
		getApplication().changeCurrentTheme("classic");
	}
}, {
	text : Dnet.translate("appmenuitem", "theme_gray__lbl"),
	handler : function() {
		getApplication().changeCurrentTheme("gray");
	}
}, {
	text : Dnet.translate("appmenuitem", "theme_access__lbl"),
	handler : function() {
		getApplication().changeCurrentTheme("access");
	}
}, {
	text : Dnet.translate("appmenuitem", "theme_neptune__lbl"),
	handler : function() {
		getApplication().changeCurrentTheme("neptune");
	}
} ]

/**
 * Languages
 */
dnet.core.base.ApplicationMenu$Languages = [ {
	text : "English",
	handler : function() {
		getApplication().changeCurrentLanguage("en");
	}
}, {
	text : "Română",
	handler : function() {
		getApplication().changeCurrentLanguage("ro");
	}
} ]

/**
 * Help items
 */
dnet.core.base.ApplicationMenu$HelpItems = [ {
	text : Dnet.translate("appmenuitem", "frameInspector__lbl"),
	handler : function() {
		(new dnet.core.base.FrameInspector({})).show();
	}
}, {
	text : Dnet.translate("cmp", "imp_dp_title"),
	handler : function() {
		Ext.create('dnet.core.dc.tools.DcImportWindow', {}).show();
	}
}, "-", {
	text : Dnet.translate("appmenuitem", "about__lbl"),
	handler : function() {
		(new Ext.Window({
			bodyPadding : 10,
			title : Dnet.translate("appmenuitem", "about__lbl"),
			tpl : dnet.core.base.TemplateRepository.APP_ABOUT,
			data : {
				product : Dnet.productInfo
			},
			closable : true,
			modal : true,
			resizable : false
		})).show();
	}
} ]

/**
 * User account management
 */
dnet.core.base.ApplicationMenu$UserAccount = [ {
	text : Dnet.translate("appmenuitem", "changepswd__lbl"),
	handler : function() {
		(new dnet.core.base.ChangePasswordWindow({})).show();
	}
}, {
	text : Dnet.translate("appmenuitem", "mysettings__lbl"),
	handler : function() {
		var bundle = "nan21.dnet.module.ad.ui.extjs";
		var frame = "MyUser_Ui";
		var path = Dnet.buildUiPath(bundle, frame, false);
		getApplication().showFrame(frame, {
			url : path
		});
	}
}, {
	text : Dnet.translate("appmenuitem", "selectCompany__lbl"),
	handler : function() {
		(new dnet.core.base.SelectCompanyWindow({})).show();
	}
} ];

/**
 * Session management
 */
dnet.core.base.ApplicationMenu$SessionControl = [ {
	text : Dnet.translate("appmenuitem", "login__lbl"),
	handler : function() {
		getApplication().doLockSession();
	}
}, {
	text : Dnet.translate("appmenuitem", "logout__lbl"),
	handler : function() {
		getApplication().doLogout();
	}
} ];

/**
 * Main application menu items
 */
dnet.core.base.ApplicationMenu$Items_ClientUser = [ {
	xtype : "splitbutton",
	text : Dnet.translate("appmenuitem", "myaccount__lbl"),
	menu : new Ext.menu.Menu({
		items : [ {
			text : Dnet.translate("appmenuitem", "theme__lbl"),
			menu : new Ext.menu.Menu({
				items : dnet.core.base.ApplicationMenu$Themes
			})
		}, {
			text : Dnet.translate("appmenuitem", "lang__lbl"),
			menu : new Ext.menu.Menu({
				items : dnet.core.base.ApplicationMenu$Languages
			})
		}, "-" ].concat(dnet.core.base.ApplicationMenu$UserAccount)
	})
}, {
	xtype : "splitbutton",
	text : Dnet.translate("appmenuitem", "session__lbl"),
	menu : new Ext.menu.Menu({
		items : dnet.core.base.ApplicationMenu$SessionControl
	})
}, {
	xtype : "splitbutton",
	text : Dnet.translate("appmenuitem", "help__lbl"),
	menu : new Ext.menu.Menu({
		items : dnet.core.base.ApplicationMenu$HelpItems
	})

} ];

dnet.core.base.ApplicationMenu$Items_SystemUser = [ {
	xtype : "splitbutton",
	text : Dnet.translate("appmenuitem", "myaccount__lbl"),
	menu : new Ext.menu.Menu({
		items : [ {
			text : Dnet.translate("appmenuitem", "theme__lbl"),
			menu : new Ext.menu.Menu({
				items : dnet.core.base.ApplicationMenu$Themes
			})
		}, {
			text : Dnet.translate("appmenuitem", "lang__lbl"),
			menu : new Ext.menu.Menu({
				items : dnet.core.base.ApplicationMenu$Languages
			})
		} ]
	})
}, {
	xtype : "splitbutton",
	text : Dnet.translate("appmenuitem", "session__lbl"),
	menu : new Ext.menu.Menu({
		items : dnet.core.base.ApplicationMenu$SessionControl
	})
}, "-", {
	xtype : "splitbutton",
	text : Dnet.translate("appmenuitem", "help__lbl"),
	menu : new Ext.menu.Menu({
		items : dnet.core.base.ApplicationMenu$HelpItems
	})

} ];

/**
 * Database menus
 */
Ext.define("dnet.core.base.DBMenu", {
	extend : "Ext.menu.Menu"
});

/**
 * Application header.
 */
Ext.define("dnet.core.base.ApplicationMenu", {
	extend : "Ext.toolbar.Toolbar",

	padding : 0,
	height : 50,
	ui : "appheader",

	systemMenu : null,
	systemMenuAdded : null,

	dbMenu : null,
	dbMenuAdded : null,

	/**
	 * Set the user name in the corresponding element.
	 */
	setUserText : function(v) {
		try {
			this.items.get("dnet.core.menu.ApplicationMenu$Item$UserName")
					.setText(v);
		} catch (e) {
		}
	},

	/**
	 * Set the client name in the corresponding element.
	 */
	setClientText : function(v) {
		try {
			var _v = (!Ext.isEmpty(v)) ? v : "--";
			this.items.get("dnet.core.menu.ApplicationMenu$Item$ClientName")
					.setText(_v);
		} catch (e) {
		}
	},

	/**
	 * Set the default company name in the corresponding element.
	 */
	setCompanyText : function(v) {
		try {
			var _v = (!Ext.isEmpty(v)) ? v : "--";
			this.items.get("dnet.core.menu.ApplicationMenu$Item$CompanyName")
					.setText(_v);
		} catch (e) {
		}
	},

	/**
	 * Create the application logo element using the URL set in Dnet.logoUrl
	 */
	_createAppLogo_ : function() {
		return {
			xtype : "container",
			height : 48,
			width : 120,
			style : "background: url('" + Dnet.logo
					+ "') no-repeat ;background-position:center;  "
		}
	},

	/**
	 * Create the application's product info element using the corresponding
	 * Dnet properties
	 */
	_createAppInfo_ : function() {
		return {
			xtype : "tbtext",
			id : "dnet.core.menu.ApplicationMenu$Item$ProductInfo",
			text : "<span>" + Dnet.productInfo.name + " </span><br><span>"
					+ Dnet.translate("appmenuitem", "version__lbl") + ": "
					+ Dnet.productInfo.version + "</span></span>"
		};
	},

	/**
	 * Create the header's left part
	 */
	_createLeft_ : function() {
		return [ this._createAppLogo_(), {
			xtype : "tbspacer",
			width : 10
		} ];
	},

	/**
	 * Create the header's middle part
	 */
	_createMiddle_ : function() {
		if (getApplication().session.user.systemUser === true) {
			return dnet.core.base.ApplicationMenu$Items_SystemUser;
		} else {
			return dnet.core.base.ApplicationMenu$Items_ClientUser;
		}

	},

	/**
	 * Create the header's right part
	 */
	_createRight_ : function() {
		return [ "->", {
			xtype : "tbtext",
			id : "dnet.core.menu.ApplicationMenu$Item$UserLabel",
			text : Dnet.translate("appmenuitem", "user__lbl")
		}, {
			xtype : "tbtext",
			id : "dnet.core.menu.ApplicationMenu$Item$UserName",
			text : "--",
			style : "font-weight:bold;"
		}, "-", {
			xtype : "tbtext",
			id : "dnet.core.menu.ApplicationMenu$Item$ClientLabel",
			text : Dnet.translate("appmenuitem", "client__lbl")
		}, {
			xtype : "tbtext",
			id : "dnet.core.menu.ApplicationMenu$Item$ClientName",
			text : "--",
			style : "font-weight:bold;"
		}, "-", {
			xtype : "tbtext",
			id : "dnet.core.menu.ApplicationMenu$Item$CompanyLabel",
			text : Dnet.translate("appmenuitem", "company__lbl")
		}, {
			xtype : "tbtext",
			id : "dnet.core.menu.ApplicationMenu$Item$CompanyName",
			text : "--",
			style : "font-weight:bold;"
		}, {
			xtype : "tbspacer",
			width : 20
		}, this._createAppInfo_() ];
	},

	initComponent : function(config) {

		var _items = [].concat(this._createLeft_(), this._createMiddle_(), this
				._createRight_());

		this.systemMenuAdded = false;

		var cfg = {
			border : false,
			frame : false,
			items : _items
		};

		Ext.apply(this, cfg);
		this.callParent(arguments);

		this.on("afterrender", function() {
			Ext.Function.defer(this._insertDBMenus_, 500, this);
		}, this);
	},

	/**
	 * System client menu management. A system client can manage application
	 * clients (tenants). This feature will be moved in future to a stand-alone
	 * system module where a platform administrator can manage clients as well
	 * as other platform level management tasks.
	 */
	createSystemMenu : function() {
		var list = Dnet.systemMenus;
		if (!Ext.isEmpty(list) && Ext.isArray(list)) {
			var _items = [];
			for ( var i = 0; i < list.length; i++) {
				var e = list[i];
				var t = e.labelKey.split("/");
				var b = e.bundle;
				var f = e.frame;
				var _item = {
					text : Dnet.translate(t[0], t[1]),
					_bundle : e.bundle,
					_frame : e.frame,
					handler : function() {
						getApplication().showFrameByName(this._bundle,
								this._frame);
					}
				}
				_items[_items.length] = _item;
			}
		}

		var _menu = {
			xtype : "splitbutton",
			text : Dnet.translate("appmenuitem", "system__lbl"),
			menu : new Ext.menu.Menu({
				items : _items
			})
		};
		this.systemMenu = Ext.create('Ext.button.Split', _menu);
	},

	/**
	 * Add the system client menu to the menu bar
	 */
	addSystemMenu : function() {
		if (!this.systemMenuAdded) {
			this.createSystemMenu();
			if (this.dbMenu == null) {
				this.insert(2, this.systemMenu);
			} else {
				this.insert(2 + this.dbMenu.length, this.systemMenu);
			}
			this.systemMenuAdded = true;
		}
	},

	/**
	 * Remove the system client menu from the menu bar
	 */
	removeSystemMenu : function() {
		if (this.systemMenuAdded) {
			this.remove(this.systemMenu);
			this.systemMenuAdded = false;
			this.systemMenu = null;
		}
	},

	/**
	 * Insert menu elements loaded from database.
	 */
	_insertDBMenus_ : function() {
		if (this.rendered && this.dbMenu != null && this.dbMenuAdded !== true) {
			var l = this.dbMenu.length;
			for ( var i = 0; i < l; i++) {
				this.insert(2 + i, this.dbMenu[i]);
			}
			this.dbMenuAdded = true;
		}
	},

	/**
	 * Create a menu item which opens a standard application frame.
	 */
	_createFrameMenuItem_ : function(config) {
		var bundle_ = config.bundle;
		var frame_ = config.frame;
		var title_ = config.title;
		return {
			text : title_,
			handler : function() {
				var bundle = bundle_;
				var frame = frame_;
				var path = Dnet.buildUiPath(bundle, frame, false);
				getApplication().showFrame(frame, {
					url : path
				});
			}
		};
	},

	/**
	 * Create a menu from configuration object
	 */
	_createMenu_ : function(config) {
		return Ext.apply(
				{
					maybeShowMenu : function() {
						if (!this.menu.loader._isLoaded_) {
							if (!this.menu.loader._isLoading_) {
								this.menu.loader.load();
							}
							return false;
						} else {
							var me = this;
							if (me.menu && !me.hasVisibleMenu()
									&& !me.ignoreNextClick) {
								me.showMenu();
							}
						}
					},
					menu : {
						loader : this._createLoader_({
							menuId : config.db_id
						}, true)

					}
				}, config);

	},

	/**
	 * Create a menu-item from configuration object
	 */
	_createMenuMenuItem_ : function(config) {
		return {
			text : config.title,
			deferExpandMenu : function() {
				if (!this.menu.loader._isLoaded_) {
					if (!this.menu.loader._isLoading_) {
						this.menu.loader.load();
					}
					return false;
				} else {
					var me = this;

					if (!me.menu.rendered || !me.menu.isVisible()) {
						me.parentMenu.activeChild = me.menu;
						me.menu.parentItem = me;
						me.menu.parentMenu = me.menu.ownerCt = me.parentMenu;
						me.menu.showBy(me, me.menuAlign,
								((!Ext.isStrict && Ext.isIE) || Ext.isIE6) ? [
										-2, -2 ] : undefined);
					}
				}

			},
			menu : {
				loader : this._createLoader_({
					menuItemId : config.db_id
				}, false)
			}
		};
	},

	/**
	 * Create a database menu items loader.
	 */
	_createLoader_ : function(params, autoload) {
		return {
			url : Dnet.dsAPI("MenuItemDs", "json").read,
			renderer : 'data',
			autoLoad : autoload,
			_isLoaded_ : false,
			_isLoading_ : false,
			listeners : {
				scope : this,
				beforeload : {
					fn : function(loader, options, eopts) {
						loader._isLoaded_ = false;
						loader._isLoading_ = true;
					}
				},
				load : {
					fn : function(loader, response, options, eopts) {
						var res = Ext.JSON.decode(response.responseText).data;
						var mitems = [];
						for ( var i = 0; i < res.length; i++) {
							var e = res[i];
							if (e.frame) {
								mitems.push(this._createFrameMenuItem_({
									db_id : e.id,
									title : e.title,
									frame : e.frame,
									bundle : e.bundle
								}));
							} else {
								mitems.push(this._createMenuMenuItem_({
									db_id : e.id,
									title : e.title
								}));
							}
						}
						loader.target.add(mitems);
						loader._isLoaded_ = true;
						loader._isLoading_ = false;
					}
				}
			},
			ajaxOptions : {
				method : "POST"

			},
			params : {
				data : Ext.JSON.encode(params),
				orderBy : Ext.JSON.encode([ {
					property : "sequenceNo",
					direction : "ASC"
				} ])
			}

		};
	}

});
