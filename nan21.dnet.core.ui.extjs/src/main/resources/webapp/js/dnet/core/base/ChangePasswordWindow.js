/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.base.ChangePasswordWindow", {

	extend : "Ext.Window",
	title : Dnet.translate("cmp", "chpswd_title"),
	border : true,
	width : 400,
	resizable : false,
	closable : true,
	constrain : true,
	modal : true,

	items : {
		xtype : "form",
		frame : true,
		defaultType : "textfield",
		bodyPadding : 10,
		defaults : {
			anchor : "-20"
		},

		buttonAlign : "center",
		buttons : [ {
			text : Dnet.translate("tlbitem", "ok__lbl"),
			formBind : true,
			disabled : true,
			handler : function() {
				this.up("form").executeTask();
			}
		} ],

		fieldDefaults : {
			inputType : "password",
			labelAlign : "right",
			labelWidth : 130,
			msgTarget : "side",
			selectOnFocus : true,
			allowBlank : false
		},

		items : [ {
			name : "pswd",
			fieldLabel : Dnet.translate("cmp", "chpswd_pswd"),
			listeners : {
				change : {
					scope : this,
					fn : this.enableAction
				}
			}
		}, {
			name : "pswd1",
			fieldLabel : Dnet.translate("cmp", "chpswd_pswd1"),
			listeners : {
				change : {
					scope : this,
					fn : this.enableAction
				}
			}
		}, {
			name : "pswd2",
			fieldLabel : Dnet.translate("cmp", "chpswd_pswd2"),
			listeners : {
				change : {
					scope : this,
					fn : this.enableAction
				}
			}
		} ],

		/**
		 * Callback invoked on unsuccessful password change attempt.
		 * 
		 * @param {}
		 *            response
		 * @param {}
		 *            options
		 */
		onActionFailure : function(response, options) {
			Dnet.error(response.responseText);
		},

		/**
		 * Callback invoked on successful password change.
		 * 
		 * @param {}
		 *            response
		 * @param {}
		 *            options
		 */
		onActionSuccess : function(response, options) {
			Dnet.info(Dnet.translate("cmp", "chpswd_success"));
			this.up("window").close();
		},

		/**
		 * Execute change password action. The action button click handler.
		 * 
		 * @param {}
		 *            btn
		 * @param {}
		 *            evnt
		 */
		executeTask : function(btn, evnt) {
			var form = this.getForm();
			var wdw = this.up("window");
			var val = form.getValues();

			if (val.pswd1 != val.pswd2) {
				Dnet.error(Dnet.translate("cmp", "chpswd_nomatch"));
				return;
			}

			var p = {};

			p["npswd"] = val.pswd1;
			p["opswd"] = val.pswd;

			Ext.Ajax.request({
				method : "POST",
				params : p,
				failure : this.onActionFailure,
				success : this.onActionSuccess,
				scope : this,
				url : Dnet.sessionAPI("json").changePassword
			});
		}
	}
});