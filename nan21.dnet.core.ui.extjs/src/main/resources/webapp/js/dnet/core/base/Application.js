/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
function getApplication() {
	if (!__application__ && !window.parent) {
		alert("Cannot find application.");
		return;
	}
	return (__application__) ? __application__ : window.parent.__application__;
}
Ext.ns("dnet.core.base");
dnet.core.base.Application = {

	/**
	 * Session object.
	 * 
	 * @type dnet.core.base.Session
	 */
	session : dnet.core.base.Session,

	/**
	 * Main application view.
	 * 
	 * @type
	 */
	view : null,

	/**
	 * Main application menu.
	 * 
	 * @type
	 */
	menu : null,

	/**
	 * Frame navigator instance
	 * 
	 * @type dnet.core.base.FrameNavigatorWithIframe
	 */
	navigator : null,

	/**
	 * Map which stores the registered callbacks used by the inter-frame
	 * communication.
	 */
	frameCallbacks : new Ext.util.MixedCollection(),

	/**
	 * Login window object.
	 * 
	 * @type dnet.core.base.LoginWindow
	 */
	loginWindow : null,

	/**
	 * Getter for the session object.
	 * 
	 * @return {}
	 */
	getSession : function() {
		return this.session;
	},

	/**
	 * Getter for the main view object.
	 * 
	 * @return {}
	 */
	getView : function() {
		return this.view;
	},

	/**
	 * Getter for the application home view.
	 * 
	 * @return {}
	 */
	getViewHome : function() {
		return Ext.getCmp(__CmpId__.APP_VIEW_HOME);
	},

	/**
	 * Getter for application header view.
	 * 
	 * @return {}
	 */
	getViewHeader : function() {
		return Ext.getCmp(__CmpId__.APP_VIEW_HEADER);
	},

	/**
	 * Getter for the application footer view.
	 * 
	 * @return {}
	 */
	getViewFooter : function() {
		return Ext.getCmp(__CmpId__.APP_VIEW_FOOTER);
	},

	/**
	 * Getter for the application body view.
	 * 
	 * @return {}
	 */
	getViewBody : function() {
		return Ext.getCmp(__CmpId__.APP_VIEW_BODY);
	},

	/**
	 * Show the specified frame
	 * 
	 * @param {}
	 *            name
	 * @param {}
	 *            bundle
	 */
	showFrameByName : function(bundle, name) {
		this.showFrame(name, {
			url : Dnet.buildUiPath(bundle, name, false)
		});
	},

	/**
	 * Open the specified frame.
	 * 
	 * @param options:
	 *            callback: the callback function to be executed after the frame
	 *            is rendered. params: the arguments used in callback call
	 */
	showFrame : function(frame, options) {
		var theFrame = this.navigator.getFrameInstance(frame);
		var applyCallbackNow = !Ext.isEmpty(theFrame);
		this.navigator.showFrame(frame, options);
		this.registerFrameCallback(frame, options);
		if (applyCallbackNow) {
			this.applyFrameCallback(frame, theFrame);
		}
	},

	
	gotoNextTab: function() {
		var vb = this.getViewBody();
		var crt = vb.getActiveTab();
		var next = crt.nextSibling();
		if (next) {
			vb.setActiveTab(next);
		} else {
			vb.setActiveTab(0);
		}		
	},
	
	/**
	 * Returns the active tab.
	 */
	getActiveTab : function() {
		return this.getViewBody().getActiveTab();
	},

	/**
	 * Get the application frame instance for the active tab.
	 * 
	 * @return {}
	 */
	getActiveFrameInstance : function() {
		if (this.getActiveTab()) {
			return this.navigator
					.getFrameInstance(this.getActiveTab().initialConfig.title);
		}
		return null;
	},

	/**
	 * Register a callback function to be executed in a certain frame. The frame
	 * may not be open at the time the callback execution is requested. It is
	 * placed in the queue and executed when the frame is rendered.
	 * 
	 * @param {}
	 *            frame
	 * @param {}
	 *            options
	 */
	registerFrameCallback : function(frame, options) {
		if (options && !Ext.isEmpty(options.tocElement)
				&& Ext.isEmpty(options.callback)) {
			options.callback = function(params) {
				this._showTocElement_(params.tocElement);
			}
			options.params = {
				tocElement : options.tocElement
			}
		}
		if (options && options.callback) {
			this.frameCallbacks.add(frame, options);
		}
	},

	/**
	 * Execute the registered callback
	 * 
	 * @param {}
	 *            frame
	 * @param {}
	 *            theFrameObject
	 */
	applyFrameCallback : function(frame, theFrameObject) {
		if (this.frameCallbacks.containsKey(frame)) {
			var opt = this.frameCallbacks.get(frame);
			opt.callback.call(theFrameObject, opt.params);
			this.frameCallbacks.removeAtKey(frame);
		}
	},

	/**
	 * Entry point function to start-up an application.
	 */
	run : function() {

		this.navigator = dnet.core.base.FrameNavigatorWithIframe;
		this.navigator.maxOpenTabs = __MAX_OPEN_TABS__;

		if (this.loginWindow == null) {
			this.loginWindow = new dnet.core.base.LoginWindow({});
		}

		if (!this.session.isAuthenticated()) {
			this.loginWindow.show();
			this.loginWindow.clearInvalid();
		} else if (this.session.locked) {
			this.loginWindow.applyState_LockSession();
			this.showLoginView();
			this.loginWindow.getPasswordField().focus(false, 200);
		} else {
			this.view.getLayout().setActiveItem(1);
			this.menu.setUserText(this.session.user.name);
			this.menu.setClientText(this.session.client.code);
			if (this.session.user.systemUser) {
				this.menu.addSystemMenu();
			} else {
				this.menu.removeSystemMenu();
			}
		}
	},

	/**
	 * On login success hide the login window and show the application canvas.
	 * Add whatever else is necessary.
	 */
	onLoginSuccess : function(accountChange) {
		if (accountChange) {
			this.getViewBody().plugins[0].onCloseAll(); // .removeAll(true);
		}
		this.showMainApplicationView();
		this.loginWindow.hide();

		this.menu.setUserText(this.session.user.name);
		this.menu.setClientText(this.session.client.code);

		if (this.session.user.systemUser) {
			this.menu.addSystemMenu();
		} else {
			this.menu.removeSystemMenu();
		}
	},

	/**
	 * When default company has been changed, update the text in the application
	 * header
	 */
	onCompanyChange : function() {
		this.menu.setCompanyText(this.session.company.code);
	},

	/**
	 * Logout from application
	 */
	doLogout : function() {
		Ext.Ajax.request({
			method : "POST",
			scope : this,
			url : Dnet.sessionAPI("json").logout,
			callback : function() {
				window.location.href = Dnet.urlSession + "/login";
			}
		});
	},

	/**
	 * Lock active session.
	 */
	doLockSession : function() {
		Ext.Ajax.request({
			method : "GET",
			scope : this,
			url : Dnet.sessionAPI("json").lock,
			timeout : 600000
		});
		this.loginWindow.applyState_LockSession();
		this.showLoginView();
		this.loginWindow.getPasswordField().focus(false, 200);
	},

	/**
	 * Activate the empty canvas and show the login window.
	 */
	showLoginView : function() {
		this.view.getLayout().setActiveItem(0);
		this.loginWindow.show();
	},

	/**
	 * Activate the application view canvas.
	 */
	showMainApplicationView : function() {
		this.view.getLayout().setActiveItem(1);
	},

	/**
	 * Change the current theme with the provided one.
	 * 
	 * @param {}
	 *            t selected theme
	 */
	changeCurrentTheme : function(t) {
		window.location.href = Dnet.urlUiExtjs + "/?theme=" + t
	},

	/**
	 * Change current language with the provided one.
	 * 
	 * @param {}
	 *            l selected language
	 */
	changeCurrentLanguage : function(l) {
		window.location.href = Dnet.urlUiExtjs + "/?lang=" + l
	},

	/**
	 * Set the tab title according to the nested frame title.
	 * 
	 * @param {}
	 *            frame
	 * @param {}
	 *            title
	 */
	setFrameTabTitle : function(frame, title) {
		var n = __CmpId__.FRAME_TAB_PREFIX + frame;
		var c = Ext.getCmp(n);
		c.setTitle(title);
	}

};
