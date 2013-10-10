/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.base.SelectCompanyWindow", {
	extend : "Ext.Window",
	title : Dnet.translate("cmp", "selcomp_title"),
	border : true,
	width : 350,
	resizable : false,
	closable : true,
	constrain : true,
	modal : true,
	_grid_ : null,

	items : {
		xtype : "form",
		frame : true,
		bodyPadding : 10,
		defaults : {
			anchor : "-20"
		},
		buttonAlign : "center",
		fieldDefaults : {
			labelAlign : "right",
			labelWidth : 100,
			msgTarget : "side",
			selectOnFocus : true,
			allowBlank : false
		},

		buttons : [ {
			text : Dnet.translate("tlbitem", "ok__lbl"),
			formBind : true,
			disabled : true,
			handler : function() {
				this.up("form").executeTask();
			}
		} ],

		items : [ {
			name : "companyId",
			xtype : "hidden"
		}, {
			name : "companyCode",
			fieldLabel : Dnet.translate("cmp", "selcomp_companyCode"),
			xtype : "combo",
			forceSelection : true,
			triggerAction : "all",
			store : [ "NAN21" ],
			listeners : {
				"change" : {
					scope : this,
					fn : function(fld, nv, ov) {

					}
				}
			}
		} ],

		/**
		 * Handler. Run in button scope
		 */
		executeTask : function() {
			var val = this.getForm().getValues();
			var cmp = getApplication().getSession().company;

			cmp.id = val.companyId;
			cmp.code = val.companyCode;
			getApplication().onCompanyChange();
			this.up("window").close();
		}
	}

});
