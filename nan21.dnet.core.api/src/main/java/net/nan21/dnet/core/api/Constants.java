/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api;

public class Constants {

	/* =========================================================== */
	/* ====================== data format ======================== */
	/* =========================================================== */

	public static final String DATA_FORMAT_CSV = "csv";
	public static final String DATA_FORMAT_JSON = "json";
	public static final String DATA_FORMAT_XML = "xml";
	public static final String DATA_FORMAT_HTML = "html";
	public static final String DATA_FORMAT_PDF = "pdf";

	/* =========================================================== */
	/* =================== request parameters ==================== */
	/* =========================================================== */

	public static final String REQUEST_PARAM_THEME = "theme";
	public static final String REQUEST_PARAM_LANG = "lang";
	public static final String REQUEST_PARAM_ACTION = "action";
	public static final String REQUEST_PARAM_DATA = "data";
	public static final String REQUEST_PARAM_FILTER = "data";
	public static final String REQUEST_PARAM_ADVANCED_FILTER = "filter";
	public static final String REQUEST_PARAM_PARAMS = "params";
	public static final String REQUEST_PARAM_SORT = "orderByCol";
	public static final String REQUEST_PARAM_SENSE = "orderBySense";
	public static final String REQUEST_PARAM_START = "resultStart";
	public static final String REQUEST_PARAM_SIZE = "resultSize";
	public static final String REQUEST_PARAM_ORDERBY = "orderBy";
	public static final String REQUEST_PARAM_EXPORT_INFO = "export_info";

	public static final String REQUEST_PARAM_SERVICE_NAME_PARAM = "rpcName";
	public static final String REQUEST_PARAM_ASGN_OBJECT_ID = "objectId";
	public static final String REQUEST_PARAM_ASGN_SELECTION_ID = "selectionId";

	/* =========================================================== */
	/* =============== Request parameter action ================== */
	/* =========================================================== */

	public static final String DS_ACTION_INFO = "info";
	public static final String DS_ACTION_QUERY = "find";
	public static final String DS_ACTION_INSERT = "insert";
	public static final String DS_ACTION_UPDATE = "update";
	public static final String DS_ACTION_DELETE = "delete";
	public static final String DS_ACTION_SAVE = "save";
	public static final String DS_ACTION_IMPORT = "import";
	public static final String DS_ACTION_EXPORT = "export";
	public static final String DS_ACTION_PRINT = "print";
	public static final String DS_ACTION_RPC = "rpc";

	public static final String ASGN_ACTION_QUERY_LEFT = "findLeft";
	public static final String ASGN_ACTION_QUERY_RIGHT = "findRight";
	public static final String ASGN_ACTION_MOVE_LEFT = "moveLeft";
	public static final String ASGN_ACTION_MOVE_RIGHT = "moveRight";
	public static final String ASGN_ACTION_MOVE_LEFT_ALL = "moveLeftAll";
	public static final String ASGN_ACTION_MOVE_RIGHT_ALL = "moveRightAll";
	public static final String ASGN_ACTION_SETUP = "setup";
	public static final String ASGN_ACTION_RESET = "reset";
	public static final String ASGN_ACTION_SAVE = "save";
	public static final String ASGN_ACTION_CLEANUP = "cleanup";

	public static final String SESSION_ACTION_SHOW_LOGIN = "login";
	public static final String SESSION_ACTION_LOGIN = "doLogin";
	public static final String SESSION_ACTION_LOGOUT = "doLogout";
	public static final String SESSION_ACTION_LOCK = "doLock";
	public static final String SESSION_ACTION_CHANGEPASSWORD = "changePassword";

	/* =========================================================== */
	/* ================ Servlet/Context path ==================== */
	/* =========================================================== */

	/* Servlet paths defined in web.xml */
	public static final String SERVLETPATH_UI_EXTJS = "/ui-extjs";
	public static final String SERVLETPATH_SECURITY = "/security";
	public static final String SERVLETPATH_UPLOAD = "/upload";
	public static final String SERVLETPATH_WORKFLOW = "/workflow";
	public static final String SERVLETPATH_DATA = "/data";

	/* Context paths */
	public static final String CTXPATH_SESSION = "/session";
	public static final String CTXPATH_ASGN = "/asgn";
	public static final String CTXPATH_DS = "/ds";

	public static final String URL_DNET_HOME = "/dnet-ebs";
	public static final String URL_DNET_WEB = "/nan21.dnet.core.web";

	public static final String URL_DNET_UI_EXTJS = URL_DNET_WEB
			+ SERVLETPATH_UI_EXTJS;
	public static final String URL_DNET_SESSION = URL_DNET_WEB
			+ SERVLETPATH_SECURITY + CTXPATH_SESSION;
	public static final String URL_DNET_UPLOAD = URL_DNET_WEB
			+ SERVLETPATH_UPLOAD;
	public static final String URL_DNET_WORKFLOW = URL_DNET_WEB
			+ SERVLETPATH_WORKFLOW;

	public static final String URL_DNET_DATA_DS = URL_DNET_WEB
			+ SERVLETPATH_DATA + CTXPATH_DS;
	public static final String URL_DNET_DATA_ASGN = URL_DNET_WEB
			+ SERVLETPATH_DATA + CTXPATH_ASGN;

	/* =========================================================== */
	/* ====================== cookie name ======================== */
	/* =========================================================== */

	public static final String COOKIE_NAME_THEME = "dnet-theme";
	public static final String COOKIE_NAME_LANG = "dnet-lang";

	/* =========================================================== */
	/* ================== spring bean aliases ==================== */
	/* =========================================================== */

	public static final String SPRING_OSGI_ENTITY_SERVICE_FACTORIES = "osgiEntityServiceFactories";
	public static final String SPRING_OSGI_DS_SERVICE_FACTORIES = "osgiDsServiceFactories";
	public static final String SPRING_OSGI_ASGN_SERVICE_FACTORIES = "osgiAsgnServiceFactories";
	public static final String SPRING_OSGI_ASGN_TX_SERVICE_FACTORIES = "osgiAsgnTxServiceFactories";
	public static final String SPRING_OSGI_DS_DEFINITIONS = "osgiDsDefinitions";
	public static final String SPRING_OSGI_SYSPARAM_DEFINITIONS = "osgiSysParamDefinitions";
	public static final String SPRING_OSGI_JOB_DEFINITIONS = "osgiJobDefinitions";
	public static final String SPRING_OSGI_JOB_SCHEDULER = "osgiJobScheduler";
	public static final String SPRING_OSGI_PERSISTABLE_LOG_SERVICES = "osgiPersistableLogServices";
	public static final String SPRING_OSGI_INIT_DATA_PROVIDER_FACTORIES = "osgiInitDataProviderFactories";
	public static final String SPRING_OSGI_FILE_UPLOAD_SERVICE_FACTORIES = "osgiFileUploadServiceFactories";

	public static final String SPRING_DEFAULT_ASGN_TX_SERVICE = "defaultAsgnTxService";
	public static final String SPRING_AUTH_MANAGER = "authenticationManager";

	public static final String SPRING_MSG_IMPORT_DATA_FILE = "msgImportDataFile";
	
	/* =========================================================== */
	/* ===================== quartz scheduler ==================== */
	/* =========================================================== */

	public static final String QUARTZ_JOB_NAME = "__JOB_NAME__";

	/* =========================================================== */
	/* ==================== application roles ==================== */
	/* =========================================================== */

	public static final String ROLE_ADMIN_CODE = "ROLE_DNET_ADMIN";
	public static final String ROLE_ADMIN_NAME = "Administrator";
	public static final String ROLE_ADMIN_DESC = "Administrator role for un-restricted access to business functions";

	public static final String ROLE_USER_CODE = "ROLE_DNET_USER";
	public static final String ROLE_USER_NAME = "Application access";
	public static final String ROLE_USER_DESC = "Application role which allows a user to access the application";

	/* =========================================================== */
	/* =================== system properties ===================== */
	/* =========================================================== */

	public final static String PROP_WORKSPACE = "workspace";

	public final static String PROP_WORKING_MODE = "workingMode";
	public static final String PROP_WORKING_MODE_DEV = "dev";
	public static final String PROP_WORKING_MODE_PROD = "prod";

	public final static String PROP_LOGIN_PAGE = "loginPage";
	public static final String PROP_LOGIN_PAGE_LOGO = "loginPageLogo";
	public static final String PROP_LOGIN_PAGE_CSS = "loginPageCss";

	public final static String PROP_SERVER_DATE_FORMAT = "serverDateFormat";
	public final static String PROP_SERVER_TIME_FORMAT = "serverTimeFormat";
	public final static String PROP_SERVER_DATETIME_FORMAT = "serverDateTimeFormat";
	public final static String PROP_SERVER_ALT_FORMATS = "serverAltFormats";
	public final static String PROP_EXTJS_MODEL_DATE_FORMAT = "extjsModelDateFormat";

	public final static String PROP_EXTJS_DATE_FORMAT = "extjsDateFormat";
	public final static String PROP_EXTJS_TIME_FORMAT = "extjsTimeFormat";
	public final static String PROP_EXTJS_DATETIME_FORMAT = "extjsDateTimeFormat";
	public final static String PROP_EXTJS_DATETIMESEC_FORMAT = "extjsDateTimeSecFormat";
	public final static String PROP_EXTJS_MONTH_FORMAT = "extjsMonthFormat";
	public final static String PROP_EXTJS_ALT_FORMATS = "extjsAltFormats";

	public final static String PROP_JAVA_DATE_FORMAT = "javaDateFormat";
	public final static String PROP_JAVA_TIME_FORMAT = "javaTimeFormat";
	public final static String PROP_JAVA_DATETIME_FORMAT = "javaDateTimeFormat";
	public final static String PROP_JAVA_DATETIMESEC_FORMAT = "javaDateTimeSecFormat";
	public final static String PROP_JAVA_MONTH_FORMAT = "javaMonthFormat";
	public final static String PROP_JAVA_ALT_FORMATS = "javaAltFormats";

	public final static String PROP_LANGUAGE = "language";
	public final static String PROP_NUMBER_FORMAT = "numberFormat";

	public final static String PROP_DISABLE_FETCH_GROUPS = "disableFetchGroups";

	/* =========================================================== */
	/* ==================== default values ======================= */
	/* =========================================================== */

	public static final String DEFAULT_LANGUAGE = "en_US";
	public static final String DEFAULT_NUMBER_FORMAT = "0,000.00";

	private static String DEFAULT_SERVER_DATE_FORMAT = "yyyy-MM-dd";
	private static String DEFAULT_SERVER_TIME_FORMAT = "HH:mm:ss";
	private static String DEFAULT_SERVER_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static String DEFAULT_SERVER_ALT_FORMATS = "yyyy-MM-dd'T'HH:mm:ss;yyyy-MM-dd HH:mm:ss;yyyy-MM-dd'T'HH:mm;yyyy-MM-dd HH:mm;yyyy-MM-dd";
	private static String DEFAULT_EXTJS_MODEL_DATE_FORMAT = "Y-m-d\\TH:i:s";

	/* =========================================================== */
	/* ================ entity code allocation =================== */
	/* =========================================================== */

	/*
	 * Possible modes to allocate code for entities with code and name field
	 */
	/**
	 * Manually allocate code
	 */
	public static final int ENTITY_CODE_MANUAL = 1;
	/**
	 * Derive code from name.
	 */
	public static final int ENTITY_CODE_DERIVED = 2;
	/**
	 * Use a sequence to create a code from.
	 */
	public static final int ENTITY_CODE_SEQUENCE = 3;

	/* =========================================================== */
	/* ========================= others ========================== */
	/* =========================================================== */

	public static final String UUID_GENERATOR_NAME = "system-uuid";

	/**
	 * Hack to expose the server date format masks as statics for property
	 * editors.
	 * 
	 * The values are set by the {@link Settings} constructor
	 */
	public static final String get_server_date_format() {
		return DEFAULT_SERVER_DATE_FORMAT;
	}

	public static final String get_server_time_format() {
		return DEFAULT_SERVER_TIME_FORMAT;
	}

	public static final String get_server_datetime_format() {
		return DEFAULT_SERVER_DATETIME_FORMAT;
	}

	public static final String get_server_alt_formats() {
		return DEFAULT_SERVER_ALT_FORMATS;
	}

	public static final String get_extjs_model_date_format() {
		return DEFAULT_EXTJS_MODEL_DATE_FORMAT;
	}

	protected static final void set_server_date_format(String v) {
		DEFAULT_SERVER_DATE_FORMAT = v;
	}

	protected static final void set_server_time_format(String v) {
		DEFAULT_SERVER_TIME_FORMAT = v;
	}

	protected static final void set_server_datetime_format(String v) {
		DEFAULT_SERVER_DATETIME_FORMAT = v;
	}

	protected static final void set_server_alt_formats(String v) {
		DEFAULT_SERVER_ALT_FORMATS = v;
	}

	protected static final void set_extjs_model_date_format(String v) {
		DEFAULT_EXTJS_MODEL_DATE_FORMAT = v;
	}

}
