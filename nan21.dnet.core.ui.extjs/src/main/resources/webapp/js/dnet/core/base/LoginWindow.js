/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Login window form
 */
Ext.define("dnet.core.base.LoginForm", {

	extend : "Ext.form.Panel",

	/**
	 * Action button rendered in the window, given as a reference to be managed
	 * from the form.
	 * 
	 * @type
	 */
	actionButton : null,

	initComponent : function(config) {

		this.actionButton = this.initialConfig.actionButton;
		this.actionButton.setHandler(this.doLogin, this);

		var cfg = {
			frame : true,
			bodyPadding : 10,
			fieldDefaults : {
				labelAlign : 'right',
				labelWidth : 100,
				msgTarget : 'side'
			},
			defaults : {
				anchor : '-20',
				selectOnFocus : true,
				allowBlank : false
			},
			items : this._buildItems_() 
			
		};
		Ext.apply(this, cfg);
		this.callParent(arguments);
		
	},

	/**
	 * Builder method which constructs the form elements.
	 * 
	 * @return {Array}
	 */
	_buildItems_ : function() {
		return [ {
			xtype : "textfield",
			itemId : "usr",
			fieldLabel : Dnet.translate("cmp", "login_user"),
			value : getApplication().getSession().getUser().loginName,
			listeners : {
				change : {
					scope : this,
					fn : this.enableAction
				}
			}
		}, {
			xtype : "textfield",
			itemId : "pswd",
			fieldLabel : Dnet.translate("cmp", "login_pswd"),
			inputType : "password",
			listeners : {
				change : {
					scope : this,
					fn : this.enableAction
				}
			}
		}, {
			xtype : "textfield",
			itemId : "client",
			fieldLabel : Dnet.translate("cmp", "login_client"),
			value : getApplication().getSession().getClient().code,
			listeners : {
				change : {
					scope : this,
					fn : this.enableAction
				}
			}
		} ];
	},

	/**
	 * Getter for the login button.
	 * 
	 * @return {}
	 */
	getActionButton : function() {
		return this.actionButton;
	},

	/**
	 * Getter for the username field.
	 * 
	 * @return {Ext.form.field.Text} The component (if found)
	 */
	getUserField : function() {
		return this.getComponent("usr");
	},

	/**
	 * Getter for the password field
	 * 
	 * @return {Ext.form.field.Text} The component (if found)
	 */
	getPasswordField : function() {
		return this.getComponent("pswd");
	},

	/**
	 * Getter for the client field
	 * 
	 * @return {Ext.form.field.Text} The component (if found)
	 */
	getClientField : function() {
		return this.getComponent("client");
	},

	/**
	 * Getter for the language field
	 * 
	 * @return {Ext.form.field.ComboBox} The component (if found)
	 */
	getLanguageField : function() {
		return this.getComponent("lang");
	},

	/**
	 * Callback invoked on unsuccessful login attempt.
	 * 
	 * @param {}
	 *            response
	 * @param {}
	 *            options
	 */
	onActionFailure : function(response, options) {
		Dnet.error(response.responseText);		 
		this.getActionButton().enable();
	},

	/**
	 * Callback invoked on successful login.
	 * 
	 * @param {}
	 *            response
	 * @param {}
	 *            options
	 */
	onActionSuccess : function(response, options) {
		var r = Ext.JSON.decode(response.responseText);
		var s = getApplication().getSession();
		var u = s.getUser();
		var c = s.getClient();
		var accountChange = false;
		if (c.id != r.data.clientId || u.code != r.data.code) {
			accountChange = true;
		}
		s.roles = r.roles;
		u.name = r.data.name;
		c.id = r.data.clientId;

		if (r.data.extjsDateFormat) {
			Dnet.DATE_FORMAT = r.data.extjsDateFormat;
		}
		if (r.data.extjsTimeFormat) {
			Dnet.TIME_FORMAT = r.data.extjsTimeFormat;
		}
		if (r.data.extjsDateTimeFormat) {
			Dnet.DATETIME_FORMAT = r.data.extjsDateTimeFormat;
		}
		if (r.data.extjsDateTimeSecFormat) {
			Dnet.DATETIMESEC_FORMAT = r.data.extjsDateTimeSecFormat;
		}
		if (r.data.extjsMonthFormat) {
			Dnet.MONTH_FORMAT = r.data.extjsMonthFormat;
		}
		if (r.data.extjsAltFormats) {
			Dnet.DATE_ALTFORMATS = r.data.extjsAltFormats;
		}

		if (r.data.decimalSeparator) {
			Dnet.DECIMAL_SEP = r.data.decimalSeparator;
		}
		if (r.data.thousandSeparator) {
			Dnet.THOUSAND_SEP = r.data.thousandSeparator;
		}
		Dnet.initFormats();

		getApplication().onLoginSuccess(accountChange);
	},

	/**
	 * Execute login action. The login button click handler.
	 * 
	 * @param {}
	 *            btn
	 * @param {}
	 *            evnt
	 */
	doLogin : function(btn, evnt) {
		var s = getApplication().getSession();
		var u = s.getUser();

		u.loginName = this.getUserField().getValue();
		s.client.code = this.getClientField().getValue();
		var p = {};

		p["user"] = this.getUserField().getValue();
		p["pswd"] = this.getPasswordField().getValue();
		p["client"] = this.getClientField().getValue();

		Ext.Ajax.request({
			method : "POST",
			params : p,
			failure : this.onActionFailure,
			success : this.onActionSuccess,
			scope : this,
			url : Dnet.sessionAPI("json").login
		});
		this.getActionButton().disable();
	},

	/**
	 * Clear the form fields.
	 */
	clearFields : function() {
		this.items.each(function(item) {
			item.setValue(null);
		}, this);
	},

	clearInvalid : function() {
		this.getUserField().clearInvalid();
		this.getClientField().clearInvalid();
	},

	/**
	 * Apply states on form elements after logout.
	 */
	applyState_Logout : function() {
		this.getUserField().enable();

		this.getPasswordField().enable();
		this.getPasswordField().setValue(null);
		this.getPasswordField().clearInvalid();

		this.getClientField().enable();
		this.getClientField().setValue(null);
		this.getClientField().clearInvalid();

		this.getActionButton().disable();
	},

	/**
	 * Apply states on form elements after lock session.
	 */
	applyState_LockSession : function() {
		this.getUserField().disable();
		this.getPasswordField().setValue(null);
		this.getPasswordField().clearInvalid();
		this.getClientField().disable();

		this.getActionButton().disable();
	},

	/**
	 * Setter for the user-name field value.
	 * 
	 * @param {}
	 *            v
	 */
	setUserName : function(v) {
		this.getUserField().setValue(v);
	},

	/**
	 * Enable disable action button.
	 */
	enableAction : function() {
		if (this.getForm().isValid()) {
			this.getActionButton().enable();
		} else {
			this.getActionButton().disable();
		}
	}
});
/**
 * Login window
 */
Ext.define("dnet.core.base.LoginWindow", {
	extend : "Ext.Window",

	initComponent : function(config) {

		var btn = Ext.create('Ext.Button', {
			text : Dnet.translate("cmp", "login_btn"),
			disabled : true
		});

		var cfg = {
			title : Dnet.translate("cmp", "login_title"),
			border : true,
			width : 350,
			resizable : false,
			closeAction : "hide",
			closable : false,
			constrain : true,
			buttonAlign : "center",
			modal : true,
			items : new dnet.core.base.LoginForm({
				actionButton : btn
			}),
			buttons : [ btn ]
		};

		Ext.apply(cfg, config);
		Ext.apply(this, cfg);
		this.callParent(arguments);
	},

	/**
	 * Getter for the login form.
	 * 
	 * @return {}
	 */
	getLoginForm : function() {
		return this.items.getAt(0);
	},

	/**
	 * Wrapper around the login-form action button getter.
	 * 
	 * @return {}
	 */
	getActionButton : function() {
		return this.getLoginForm().getActionButton();
	},

	/**
	 * Wrapper around the login-form user field getter.
	 * 
	 * @return {}
	 */
	getUserField : function() {
		return this.getLoginForm().getUserField();
	},

	/**
	 * Wrapper around the login-form password field getter.
	 * 
	 * @return {}
	 */
	getPasswordField : function() {
		return this.getLoginForm().getPasswordField();
	},

	/**
	 * Wrapper around the login-form client field getter.
	 * 
	 * @return {}
	 */
	getClientField : function() {
		return this.getLoginForm().getClientField();
	},

	/**
	 * Wrapper around the login-form language field getter.
	 * 
	 * @return {}
	 */
	getLanguageField : function() {
		return this.getLoginForm().getLanguageField();
	},

	/**
	 * Wrapper around the login-form applyState_Logout function.
	 */
	applyState_Logout : function() {
		this.getLoginForm().applyState_Logout();
	},

	/**
	 * Wrapper around the login-form applyState_LockSession function.
	 */
	applyState_LockSession : function() {
		this.getLoginForm().applyState_LockSession();
	},

	/**
	 * Wrapper around the login-form clearInvalid function.
	 */
	clearInvalid : function() {
		this.getLoginForm().clearInvalid();
	}

});