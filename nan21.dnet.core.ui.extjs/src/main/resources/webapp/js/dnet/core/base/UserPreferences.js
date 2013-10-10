Ext.define("dnet.core.base.UserPreferences", {
	extend : "Ext.window.Window",

	initComponent : function(config) {

		var change_fn = function(field, nv, ov) {
			getApplication().getSession().useFocusManager = nv;
		};

		var afterRender_fn = function(field) {
			var b = getApplication().getSession().useFocusManager;
			field.setValue(b);

		};

		var cfg = {
			title : Dnet.translate("msg", "preferences_wdw"),
			border : true,
			width : 350,
			height : 120,
			resizable : true,
			closable : true,
			constrain : true,
			buttonAlign : "center",
			defaults : {
				labelAlign : "right",
				labelWidth : 150
			},
			modal : true,
			layout : "form",
			items : [ {
				xtype : 'checkbox',
				fieldLabel : "Use focus manager",
				listeners : {
					change : {
						scope : this,
						fn : change_fn
					},
					afterrender : {
						scope : this,
						fn : afterRender_fn
					}
				}
			} ],
			buttons : [ {
				xtype : "button",
				text : Dnet.translate("tlbitem", "ok__lbl"),
				scope : this,
				handler : function() {
					this.close();
				}
			} ]
		};

		Ext.apply(cfg, config);
		Ext.apply(this, cfg);
		this.callParent(arguments);
	}
});