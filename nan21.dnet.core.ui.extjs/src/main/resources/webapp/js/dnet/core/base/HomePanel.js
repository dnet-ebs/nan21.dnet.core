/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.base.HomePanel", {

	extend : "Ext.Panel",
	alias : "widget.dnetHomePanel",

	_TEXT_TITLE : Dnet.translate("appmenuitem", "home__lbl"),
	_TEXT_STD_MENU : Dnet.translate("appmenuitem", "appmenus__lbl"),
	_TEXT_USR_MENU : Dnet.translate("appmenuitem", "bookmark__lbl"),

	_Menu_StdMenuId_ : "dnet-application-view-menu-std-menu",
	_Menu_UsrMenuId_ : "dnet-application-view-menu-usr-menu",
	_Menu_AccordeonId_ : "dnet-application-view-menu-accordeon",

	initComponent : function(config) {
		var tr = dnet.core.base.TemplateRepository;
		var menuConfig = [];

		for ( var k in Dnet.navigationTreeMenus) {
			menuConfig[menuConfig.length] = Dnet.navigationTreeMenus[k];
		}
		var navigAccordeonCfg = {
			layout : 'accordion',
			layoutConfig : {
				animate : false,
				activeOnTop : true

			},
			id : this._Menu_AccordeonId_,
			region : 'west',
			width : 350,
			split : true,
			minSize : 200,
			maxSize : 500,
			title : Dnet.translate("appmenuitem", "appmenus__lbl"),
			collapsible : true,
			items : []
		}

		for ( var i = 0; i < menuConfig.length; i++) {
			navigAccordeonCfg.items[i] = {
				title : menuConfig[i]["title"],
				layout : {
					type : 'fit'
				},
				items : [ {
					xtype : "dnetNavigationTree",
					id : "dnet-application-view-menu-" + menuConfig[i]["name"],
					_menuId_ : 1,
					_menuName_ : menuConfig[i]["name"],
					withStdFilterHeader : true,
					loader_PreloadChildren : true,
					listeners : {
						openMenuLink : {
							scope : this,
							fn : function(model) {
								var bundle = model.raw.bundle;
								var frame = model.raw.frame;
								var path = Dnet.buildUiPath(bundle, frame,
										false);
								getApplication().showFrame(frame, {
									url : path
								});
							}
						}
					}
				} ]
			};
		}

		var cfg = {
			layout : "border",
			title : this._TEXT_TITLE,
			items : [ {
				region : "center",				 
				frame : true,			 
				tpl : dnet.core.base.TemplateRepository.APP_ABOUT,
				data : {
					product : Dnet.productInfo
				}
			}, {
				region : "south",
				border : false,
				frame : true,
				tpl : dnet.core.base.TemplateRepository.APP_FOOTER,
				data : {
					product : Dnet.productInfo
				},
				padding: 6,
				cls : "dnet-home",
				id : "dnet-application-view-footer"
			}, navigAccordeonCfg ]
		}
		Ext.apply(this, cfg);
		this.callParent(arguments);
	}

});
