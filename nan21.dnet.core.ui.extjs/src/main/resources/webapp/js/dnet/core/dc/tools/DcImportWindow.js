/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.tools.DcImportWindow", {
	extend : "Ext.Window",
	title : Dnet.translate("cmp", "imp_dp_title"),
	border : true,
	width : 500,
	resizable : false,
	closable : true,
	constrain : true,
	modal : true,
	items : {
		xtype : "form",
		layout : "anchor",
		frame : true,
		url : Dnet.urlDs + "/import",
		defaultType : "textfield",
		buttonAlign : "center",
		bodyPadding : 10,
		defaults : {
			anchor : "-20"
		},

		fieldDefaults : {
			labelAlign : "right",
			labelWidth : 100,
			msgTarget : "side",
			selectOnFocus : true,
			allowBlank : false
		},

		items : [ {
			xtype : "label",
			html : Dnet.translate("cmp", "imp_dp_desc")
		}, {
			fieldLabel : Dnet.translate("cmp", "imp_dp_loc"),
			name : "dataPackage",
			padding : "10 0 0 0"
		} ],

		buttons : [ {
			text : Dnet.translate("tlbitem", "ok__lbl"),
			formBind : true,
			disabled : true,
			handler : function() {
				var form = this.up("form").getForm();
				form.url = Dnet.urlDs + "/import";
				if (form.isValid()) {
					form.submit({
						success : function(form, action) {
							Dnet.info(Dnet.translate("cmp", "imp_dp_success"));
							this.up("window").close();
						},
						failure : function(form, action) {
							Dnet.error(action.response.responseText);
						},
						scope : this
					});
				}
			}
		} ]
	}

});
