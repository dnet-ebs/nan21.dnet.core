/**
 * DNet eBusiness Suite
 * Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Date-time format mask names.
 * 
 * @type
 */
Masks = {
	DATE : "DATE_FORMAT",
	TIME : "TIME_FORMAT",
	DATETIME : "DATETIME_FORMAT",
	DATETIMESEC : "DATETIMESEC_FORMAT",
	MONTH : "MONTH_FORMAT"
}

/**
 * Dnet main class configuration class.
 */
Ext.ns("dnet.core.base");
Dnet = {

	productInfo : {
		name : "DNet eBusiness Suite",
		vendor : "Nan21 Electronics s.r.l.",
		version : "0.0.0",
		description : "Open source ERP",
		url : "www.dnet-ebusiness-suite.com"
	},

	/**
	 * Namespace aliases to be used in class declarations
	 * 
	 * @type
	 */
	ns : {
		ad : "net.nan21.dnet.module.ad.",
		bd : "net.nan21.dnet.module.bd.",
		md : "net.nan21.dnet.module.md.",
		sd : "net.nan21.dnet.module.sd.",
		sc : "net.nan21.dnet.module.sc.",
		pj : "net.nan21.dnet.module.pj.",
		hr : "net.nan21.dnet.module.hr."
	},

	/**
	 * Application bundles which contain extjs based user interface components.
	 * 
	 * @type
	 */
	bundle : {
		ad : "nan21.dnet.module.ad.ui.extjs",
		bd : "nan21.dnet.module.bd.ui.extjs",
		md : "nan21.dnet.module.md.ui.extjs",
		sd : "nan21.dnet.module.sd.ui.extjs",
		sc : "nan21.dnet.module.sc.ui.extjs",
		pj : "nan21.dnet.module.pj.ui.extjs",
		hr : "nan21.dnet.module.hr.ui.extjs"
	},

	dsName : {
		VIEW_STATE_LOV : "UiViewStateRTLov",
		VIEW_STATE : "UiViewStateRT",
		MENU : "MenuRtLov",
		MENU_ITEM : "MenuItemRtLov",
		COMPANY : "OrgLov"
	},

	msgType : {
		INFO : Ext.MessageBox.INFO,
		WARNING : Ext.MessageBox.WARNING,
		ERROR : Ext.MessageBox.ERROR
	},

	msg : {

		AT_FIRST_RECORD : "AT_FIRST_RECORD",
		AT_LAST_RECORD : "AT_LAST_RECORD",

		AT_FIRST_PAGE : "AT_FIRST_PAGE",
		AT_LAST_PAGE : "AT_LAST_PAGE",

		DCCONTEXT_INVALID_SETUP : "DCCONTEXT_INVALID_SETUP",

		PARENT_RECORD_NEW : "PARENT_RECORD_NEW",
		NO_CURRENT_RECORD : "NO_CURRENT_RECORD",
		NO_SELECTED_RECORDS : "NO_SELECTED_RECORDS",
		DIRTY_DATA_FOUND : "DIRTY_DATA_FOUND",

		INVALID_FILTER : "INVALID_FILTER",
		INVALID_FORM : "INVALID_FORM",

		DC_NEW_NOT_ALLOWED : "DC_NEW_NOT_ALLOWED",
		DC_SAVE_NOT_ALLOWED : "DC_SAVE_NOT_ALLOWED",
		DC_QUERY_NOT_ALLOWED : "DC_QUERY_NOT_ALLOWED",
		DC_COPY_NOT_ALLOWED : "DC_COPY_NOT_ALLOWED",
		DC_DELETE_NOT_ALLOWED : "DC_DELETE_NOT_ALLOWED",
		DC_EDIT_OUT_NOT_ALLOWED : "DC_EDIT_OUT_NOT_ALLOWED",
		DC_RECORD_CHANGE_NOT_ALLOWED : "DC_RECORD_CHANGE_NOT_ALLOWED",
		DC_RELOAD_RECORD_NOT_ALLOWED : "DC_RELOAD_RECORD_NOT_ALLOWED"

	// CURRENT_RECORD_DIRTY : "Programmimg error! Error thrown
	// CURRENT_RECORD_DIRTY is deprecated. Use DIRTY_DATA_FOUND instead
	// ",
	},

	/**
	 * Application logo url.
	 * 
	 * @type String
	 */
	logo : "http://nan21.net/logo.png",

	// =======================================
	// Base URLs amd fragments
	// These are provided by the server when constructing the main entry point
	// html file.
	// =======================================

	/**
	 * Host url, protocol://domain:port
	 * 
	 * @type String
	 */
	urlHost : null,

	/**
	 * URL for data-source transactional requests.
	 * 
	 * @type String
	 */
	urlDs : null,

	/**
	 * URL for assignment transactional requests.
	 * 
	 * @type String
	 */
	urlAsgn : null,

	/**
	 * URL for workflow requests.
	 * 
	 * @type String
	 */
	urlWorkflow : null,

	/**
	 * URL for Extjs user interface.
	 * 
	 * @type String
	 */
	urlUiExtjs : null,

	/**
	 * URL for session related requests
	 * 
	 * @type String
	 */
	urlSession : null,

	/**
	 * URL for file upload.
	 * 
	 * @type String
	 */
	urlUpload : null,

	/**
	 * URL where the static resources are served from for the core framework
	 * 
	 * @type String
	 */
	urlStaticCore : null,

	/**
	 * URL where the static resources are served from for the application
	 * modules
	 * 
	 * @type String
	 */
	urlStaticModules : null,

	/**
	 * URL where the translation files are served from for the core framework
	 * 
	 * @type String
	 */
	urlStaticCoreI18n : null,

	/**
	 * URL where the translation files are served from for the application
	 * modules
	 * 
	 * @type String
	 */
	urlStaticModulesI18n : null,

	/**
	 * Helper tag for the location of the application modules static resources
	 * 
	 * @type String
	 */
	urlStaticModulesSubpath : "/webapp/js",

	/**
	 * configuration variables
	 * 
	 * @type Object
	 */
	config : {

	},

	/**
	 * Default date and time formats. They are overwritten according to the user
	 * locale settings.
	 */
	/**
	 * Format for year + month + day
	 * 
	 * @type String
	 */
	DATE_FORMAT : Constants.EXTJS_DATE_FORMAT,
	/**
	 * Format for hour + minutes
	 * 
	 * @type String
	 */
	TIME_FORMAT : Constants.EXTJS_TIME_FORMAT,

	/**
	 * Format for year + month + day + hour + minutes
	 * 
	 * @type String
	 */
	DATETIME_FORMAT : Constants.EXTJS_DATETIME_FORMAT,

	/**
	 * Format for year + month + day + hour + minutes + seconds
	 * 
	 * @type String
	 */
	DATETIMESEC_FORMAT : Constants.EXTJS_DATETIMESEC_FORMAT,

	/**
	 * Format for year + month
	 * 
	 * @type String
	 */
	MONTH_FORMAT : Constants.EXTJS_MONTH_FORMAT,

	/**
	 * Format for communication with server.
	 * 
	 * @type String
	 */
	MODEL_DATE_FORMAT : Constants.EXTJS_MODEL_DATE_FORMAT,

	/**
	 * Alternative formats to be used in a date/time field to create a date from
	 * the user input
	 * 
	 * @type String
	 */
	DATE_ALTFORMATS : Constants.EXTJS_ALT_FORMATS,

	THOUSAND_SEP : Constants.THOUSAND_SEPARATOR,
	DECIMAL_SEP : Constants.DECIMAL_SEPARATOR,

	numberFormats : null,

	DEFAULT_THEME : "gray",
	DEFAULT_LANGUAGE : "en",

	/**
	 * Various global view configuration defaults
	 * 
	 * @type Object
	 */
	viewConfig : {
		BOOLEAN_COL_WIDTH : 60,
		DATE_COL_WIDTH : 80,
		DISABLE_AS_READONLY : true,
		USE_TOOLBAR_ICONS : false,
		USE_BUTTON_ICONS : false
	},

	/**
	 * Creates a set of number formats up to 6 decimals according to the user
	 * locale.
	 */
	initFormats : function() {
		Ext.util.Format.decimalSeparator = this.DECIMAL_SEP;
		Ext.util.Format.thousandSeparator = this.THOUSAND_SEP;
		this.numberFormats = new Ext.util.MixedCollection();
		var _fmt = "0,000";
		this.numberFormats.add(0, _fmt);
		_fmt = _fmt + ".";
		for ( var i = 1; i < 6; i++) {
			var fmt = _fmt + Ext.util.Format.leftPad("0", i, "0");
			this.numberFormats.add(i, fmt);
		}
	},

	/**
	 * Returns a number format string based on the provided decimals number and
	 * the user locale.
	 * 
	 * @param {}
	 *            decimals
	 * @return {}
	 */
	getNumberFormat : function(decimals) {
		if (this.numberFormats == null) {
			this.initFormats();
		}
		return this.numberFormats.get(decimals);
	},

	/**
	 * Request data formats
	 * 
	 * @type Object
	 */
	dataFormat : {
		HTML : Constants.DATA_FORMAT_HTML,
		CSV : Constants.DATA_FORMAT_CSV,
		PDF : Constants.DATA_FORMAT_XML,
		XML : Constants.DATA_FORMAT_XML,
		JSON : Constants.DATA_FORMAT_JSON
	},

	/**
	 * Request parameter mappings. These values must be kept in sync with the
	 * mappings at server-side, thus they derive their values from the Constants
	 * sent by the server.
	 * 
	 * @type Object
	 */
	requestParam : {
		ACTION : Constants.REQUEST_PARAM_ACTION,
		DATA : Constants.REQUEST_PARAM_DATA,
		FILTER : Constants.REQUEST_PARAM_FILTER,
		PARAMS : Constants.REQUEST_PARAM_PARAMS,
		ADVANCED_FILTER : Constants.REQUEST_PARAM_ADVANCED_FILTER,
		SORT : Constants.REQUEST_PARAM_SORT,
		SENSE : Constants.REQUEST_PARAM_SENSE,
		START : Constants.REQUEST_PARAM_START,
		SIZE : Constants.REQUEST_PARAM_SIZE,
		ORDERBY : Constants.REQUEST_PARAM_ORDERBY,
		SERVICE_NAME_PARAM : Constants.REQUEST_PARAM_SERVICE_NAME_PARAM,
		EXPORT_INFO : Constants.REQUEST_PARAM_EXPORT_INFO
	},

	/**
	 * Request actions for ds components
	 * 
	 * @type Object
	 */
	dsAction : {
		INFO : Constants.DS_INFO,
		QUERY : Constants.DS_QUERY,
		INSERT : Constants.DS_INSERT,
		UPDATE : Constants.DS_UPDATE,
		DELETE : Constants.DS_DELETE,
		SAVE : Constants.DS_SAVE,
		IMPORT : Constants.DS_IMPORT,
		EXPORT : Constants.DS_EXPORT,
		PRINT : Constants.DS_PRINT,
		RPC : Constants.DS_RPC
	},

	/**
	 * Request actions for assignment components
	 * 
	 * @type Object
	 */
	asgnAction : {
		QUERY_LEFT : Constants.ASGN_QUERY_LEFT,
		QUERY_RIGHT : Constants.ASGN_QUERY_RIGHT,
		MOVE_LEFT : Constants.ASGN_MOVE_LEFT,
		MOVE_RIGHT : Constants.ASGN_MOVE_RIGHT,
		MOVE_LEFT_ALL : Constants.ASGN_MOVE_LEFT_ALL,
		MOVE_RIGHT_ALL : Constants.ASGN_MOVE_RIGHT_ALL,
		SAVE : Constants.ASGN_SAVE,
		SETUP : Constants.ASGN_SETUP,
		RESET : Constants.ASGN_RESET,
		CLEANUP : Constants.ASGN_CLEANUP
	},

	/**
	 * Request actions for user's session management
	 * 
	 * @type Object
	 */
	sessionAction : {
		LOGIN : Constants.SESSION_LOGIN,
		LOGOUT : Constants.SESSION_LOGOUT,
		LOCK : Constants.SESSION_LOCK,
		CHANGEPSWD : Constants.SESSION_CHANGEPASSWORD
	},

	/**
	 * Creates the URL to load a frame.
	 */
	buildUiPath : function(bundle, frame, isSpecial) {
		if (isSpecial) {
			return this.urlUiExtjs + "/spframe/" + bundle + "/" + frame;
		} else {
			return this.urlUiExtjs + "/frame/" + bundle + "/" + frame;
		}
	},

	/**
	 * URLs for session management related requests.
	 */
	sessionAPI : function(format) {
		return {
			login : this.urlSession + "?" + this.requestParam.ACTION + "="
					+ this.sessionAction.LOGIN,
			logout : this.urlSession + "/" + this.sessionAction.LOGOUT,
			lock : this.urlSession + "/" + this.sessionAction.LOCK,
			changePassword : this.urlSession + "?" + this.requestParam.ACTION
					+ "=" + this.sessionAction.CHANGEPSWD,
			userSettings : this.urlSession + "?action=userSettings"
		};
	},

	/**
	 * URLs for data-source (DS) related requests.
	 */
	dsAPI : function(resource, format) {
		return {
			info : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.INFO,
			read : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.QUERY,
			load : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.QUERY,
			print : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.PRINT,
			exportdata : this.urlDs + "/" + resource + "." + format
					+ "?action=" + this.dsAction.EXPORT,
			create : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.INSERT,
			update : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.UPDATE,
			save : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.SAVE,
			destroy : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.DELETE,
			service : this.urlDs + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "=" + this.dsAction.RPC
		};
	},

	/**
	 * URLs for assignment components requests for the available elements
	 * (available to be assigned).
	 */
	asgnLeftAPI : function(resource, format) {
		return {
			read : this.urlAsgn + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "="
					+ this.asgnAction.QUERY_LEFT

		};
	},

	/**
	 * URLs for assignment components requests for the selected elements
	 * (already assigned).
	 */
	asgnRightAPI : function(resource, format) {
		return {
			read : this.urlAsgn + "/" + resource + "." + format + "?"
					+ this.requestParam.ACTION + "="
					+ this.asgnAction.QUERY_RIGHT
		};
	},

	/**
	 * URLs for data-source RPC type requests.
	 */
	rpcAPI : function(resource, fnName, format) {
		return this.rpcUrl + "/" + resource + "." + format + "?"
				+ this.requestParam.ACTION + "=" + fnName
	},

	/**
	 * Workflow api: Process definition
	 */
	wfProcessDefinitionAPI : function(processDefinitionId) {
		return {
			form : this.urlWorkflow + "/process-definition/"
					+ processDefinitionId + "/form",
			diagram : this.urlWorkflow + "/process-definition/"
					+ processDefinitionId + "/diagram",
			xml : this.urlWorkflow + "/process-definition/"
					+ processDefinitionId + "/xml",
			properties : this.urlWorkflow + "/process-definition/"
					+ processDefinitionId + "/properties"
		};
	},

	/**
	 * Workflow api: Process instance
	 */
	wfProcessInstanceAPI : function(processInstanceId) {
		return {
			start : this.urlWorkflow + "/process-instance/start",
			diagram : this.urlWorkflow + "/process-instance/"
					+ processInstanceId + "/diagram"
		};
	},

	/**
	 * Workflow api: Task management
	 */
	wfTaskAPI : function(taskId) {
		return {
			form : this.urlWorkflow + "/task/" + taskId + "/form",
			complete : this.urlWorkflow + "/task/" + taskId + "/complete",
			properties : this.urlWorkflow + "/task/" + taskId + "/properties"
		};
	},

	/**
	 * Workflow api: Deployment management
	 */
	wfDeploymentAPI : function(deploymentId) {
		return {
			destroy : this.urlWorkflow + "/deployment/delete"
		};
	},

	/**
	 * Translate a group/key pair from the translations pack. Optionally replace
	 * place holders with given values.
	 */
	translate : function(group, key, params) {
		if (dnet.Translation == undefined
				|| dnet.Translation[group] == undefined) {
			return key;
		}
		var v = dnet.Translation[group][key] || key;
		if (Ext.isArray(params)) {
			for ( var i = 0, len = params.length; i < len; i++) {
				v = v.replace("{" + i + "}", params[i]);
			}
		}
		return v;
	},

	/**
	 * Translation for a model field. Tries to find a translation in the model
	 * RB or in the shared translations
	 * 
	 * @param {}
	 *            mrb - model resource bundle
	 * @param {}
	 *            name - field name
	 * @return {}
	 */
	translateModelField : function(mrb, name) {
		if (mrb != null && mrb[name + "__lbl"]) {
			return mrb[name + "__lbl"];
		} else {
			return Dnet.translate("ds", name);
		}
	},

	/**
	 * Translation for a form field. Tries to find a translation in the view RB,
	 * model RB or in the shared translations
	 * 
	 * @param {}
	 *            vrb - view resource bundle
	 * @param {}
	 *            mrb - model resource bundle
	 * @param {}
	 *            item - the field
	 * @return {Boolean}
	 */

	translateField : function(vrb, mrb, item) {
		// check if the view has its own resource bundle
		if (vrb != undefined && vrb[item.name]) {
			item.fieldLabel = vrb[item.name];
			return true;
		}
		// try to translate it from the model"s resource bundle
		if (item.dataIndex != undefined && mrb != null
				&& mrb[item.dataIndex + "__lbl"]) {
			item.fieldLabel = mrb[item.dataIndex + "__lbl"];
			return true;
		}
		if (item.paramIndex != undefined && mrb != null
				&& mrb[item.paramIndex + "__lbl"]) {
			item.fieldLabel = mrb[item.paramIndex + "__lbl"];
			return true;
		}
		// try to translate from the shared resource-bundle
		item.fieldLabel = Dnet.translate("ds", item.dataIndex
				|| item.paramIndex);
	},

	/**
	 * Translation for a grid column. Tries to find a translation in the view
	 * RB, model RB or in the shared translations
	 * 
	 * @param {}
	 *            vrb grid resource bundle
	 * @param {}
	 *            mrb model resource bundle
	 * @param {}
	 *            item - the column
	 * @return {Boolean}
	 */

	translateColumn : function(vrb, mrb, item) {
		// check if the view has its own resource bundle
		if (vrb != undefined && vrb[item.name]) {
			item.header = vrb[item.name];
			return true;
		}
		// try to translate it from the model"s resource bundle
		if (item.dataIndex != undefined && mrb != null
				&& mrb[item.dataIndex + "__lbl"]) {
			item.header = mrb[item.dataIndex + "__lbl"];
			return true;
		}
		// try to translate from the shared resource-bundle
		item.header = Dnet.translate("ds", item.dataIndex);
	},

	/**
	 * Create and return a yes/no store for comboboxes
	 * 
	 * @return {}
	 */
	createBooleanStore : function() {
		return Ext.create("Ext.data.Store", {
			fields : [ "bv", "tv" ],
			data : [ {
				"bv" : true,
				"tv" : Dnet.translate("msg", "bool_true")
			}, {
				"bv" : false,
				"tv" : Dnet.translate("msg", "bool_false")
			} ]
		});
	},

	/**
	 * Create a filter model from a record model. In many situations the filter
	 * model is very much the same as the record model, although there some
	 * differences so that the record model cannot be used.
	 * 
	 * The validations are different, the boolean usage, etc.
	 * 
	 * @param {}
	 *            cfg
	 */
	createFilterModelFromRecordModel : function(cfg) {
		var rm = Ext.create(cfg.recordModelFqn);
		var flds = [];
		var x = rm.fields.items;
		for ( var i = 0; i < x.length; i++) {
			f = {
				name : x[i].name,
				type : x[i].type
			}
			if (f.type.type == "bool" || f.type.type == "int"
					|| f.type.type == "float") {
				f.useNull = true;
			}
			if (f.type.type == "date") {
				f.dateFormat = x[i].dateFormat;
			}
			flds[i] = f;
		}
		if (cfg != null && cfg.fields != null) {
			for ( var j = 0; j < cfg.fields; j++) {
				flds[flds.length + 1] = cfg.fields[j];
			}
		}
		var fmn = cfg.recordModelFqn + "Filter";
		var newCfg = cfg;
		newCfg.fields = flds;
		newCfg.extend = "Ext.data.Model";
		delete newCfg.recordModelFqn;
		Ext.define(fmn, newCfg);
	},

	/**
	 * Shorthand to send an ajax request of type GET and apply the specified
	 * function as callback
	 * 
	 * @param {}
	 *            url
	 * @param {}
	 *            params
	 * @param {}
	 *            fn
	 * @param {}
	 *            scope
	 */
	doWithGetResult : function(url, params, fn, scope) {
		Ext.Ajax.request({
			url : url,
			method : "GET",
			params : params,
			success : function(response, options) {
				var r = Ext.decode(response.responseText);
				fn.call(scope || window, r, response, options);
			},
			failure : function(response, options) {
				try {
					Ext.Msg.hide();
				} catch (e) {
				}
				dnet.core.dc.AbstractDc.prototype.showAjaxErrors(response,
						options)
			},
			scope : scope
		});
	},

	error : function(msg, params) {
		this.alert(this.msgType.ERROR, msg, params);
	},

	info : function(msg, params) {
		this.alert(this.msgType.INFO, msg, params);
	},

	alert : function(type, msg, params) {
		var title = "Error";
		if (type == this.msgType.INFO) {
			title = "Info"
		} else if (type == this.msgType.WARNING) {
			title = "Warning"
		}
		Ext.Msg.show({	
			title : title,
			msg : Dnet.translate("msg", msg, null),
			buttons : Ext.MessageBox.OK,
			scope : this,
			icon : type
		});
	},

	navigationTreeMenus : {

	},

	/**
	 * Return a textual representation of the given keyboard shortcut
	 * definition.
	 */
	keyBindingToString : function(b) {
		var r = "";
		if (b.ctrl) {
			r += "Ctrl "
		}
		if (b.shift) {
			r += "Shift "
		}
		if (b.alt) {
			r += "Alt "
		}
		r += b.keyText;
		return r;
	},

	/**
	 * Global keyboard shortcuts
	 */
	keyBindings : {
		dc : {
			doNew : {
				keyText : "F2",
				key : Ext.EventObject.F2,
				ctrl : false,
				shift : false,
				alt : false
			},
			doCopy : {
				keyText : "F3",
				key : Ext.EventObject.F3,
				ctrl : false,
				shift : false,
				alt : false
			},
			doDelete : {
				keyText : "F4",
				key : Ext.EventObject.F4,
				ctrl : false,
				shift : false,
				alt : false
			},
			doCancel : {
				keyText : "Z",
				key : Ext.EventObject.Z,
				ctrl : true,
				shift : true,
				alt : false
			},
			doEnterQuery : {
				keyText : "F7",
				key : Ext.EventObject.F7,
				ctrl : false,
				shift : false,
				alt : false
			},
			doClearQuery : {
				keyText : "F7",
				key : Ext.EventObject.F7,
				ctrl : false,
				shift : true,
				alt : false
			},
			doQuery : {
				keyText : "F8",
				key : Ext.EventObject.F8,
				ctrl : false,
				shift : false,
				alt : false
			},
			doSave : {
				keyText : "F10",
				key : Ext.EventObject.F10,
				ctrl : false,
				shift : false,
				alt : false
			},
			doEditIn : {
				keyText : "Enter",
				key : Ext.EventObject.ENTER,
				ctrl : true,
				shift : false,
				alt : false
			},
			doEditOut : {
				keyText : "Q",
				key : Ext.EventObject.Q,
				ctrl : true,
				shift : true,
				alt : false
			},
			nextRec : {
				keyText : "Down",
				key : Ext.EventObject.DOWN,
				ctrl : true,
				shift : false,
				alt : false
			},
			prevRec : {
				keyText : "Up",
				key : Ext.EventObject.UP,
				ctrl : true,
				shift : false,
				alt : false
			},
			nextPage : {
				keyText : "Down",
				key : Ext.EventObject.DOWN,
				ctrl : false,
				shift : false,
				alt : true
			},
			prevPage : {
				keyText : "Up",
				key : Ext.EventObject.UP,
				ctrl : false,
				shift : false,
				alt : true
			}
		},

		app : {
			gotoNextTab : {
				keyText : "T",
				key : Ext.EventObject.T,
				ctrl : true,
				shift : true,
				alt : false
			}
		}

	}

};
