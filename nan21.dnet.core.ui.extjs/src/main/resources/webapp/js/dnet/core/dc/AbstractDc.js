/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.AbstractDc", {

	mixins : {
		observable : 'Ext.util.Observable'
	},

	dsName : "",

	/**
	 * Collection of flags which describe the dc-state.
	 */
	dcState : null,

	/**
	 * List with action names implemented
	 */
	actionNames : null,

	/**
	 * Action instances. Used to create control widgets in UI to be triggered by
	 * user, mainly toolbar items.
	 */
	actions : null,

	/**
	 * Executable functions. Implements workers for API methods of a
	 * data-control.
	 */
	commands : null,

	/**
	 * Allow to edit more than one record at once.
	 */
	multiEdit : false,

	/**
	 * Filter model instance. Has the same signature as the data model instance
	 * 
	 * @type Ext.data.Model
	 */
	filter : null,

	advancedFilter : null,

	/**
	 * Data model instance.
	 * 
	 * @type Ext.data.Model
	 */
	record : null,

	/**
	 * Parameters model instance
	 */
	params : null,

	/**
	 * Data model signature - record constructor.
	 * 
	 * @type Ext.data.Model
	 */
	recordModel : null,

	/**
	 * Filter model signature - filter constructor. Defaults to recordModel if
	 * not specified.
	 * 
	 * @type Ext.data.Model
	 */
	filterModel : null,

	/**
	 * Parameters model signature - record constructor.
	 */
	paramModel : null,

	/**
	 * Keep track of the selected records
	 */
	selectedRecords : null,

	/**
	 * Various runtime configuration properties.
	 */
	tuning : {
		/**
		 * Timeout of Ajax call in store's proxy Default 15 minutes
		 */
		storeTimeout : 900000,

		/**
		 * Timeout of Ajax call in RPC commands Default 15 minutes
		 */
		rpcTimeout : 900000,

		/**
		 * Page-size for a query
		 */
		fetchSize : 30
	},

	/**
	 * Children data-controls, similar to a `HasMany` association.
	 */
	children : null,

	/**
	 * Parent data-control, similar to a `BelongsTo` association.
	 * 
	 * @type dnet.core.dc.AbstractDc
	 */
	parent : null,

	/**
	 * Array with form-views registered to data-binding.
	 */
	bindedFormViews : null,

	/**
	 * Array with filter-views registered to data-binding.
	 */
	bindedFilterViews : null,

	/**
	 * Should apply a default selection on store load ?
	 */
	afterStoreLoadDoDefaultSelection : true,

	/**
	 * Local reference to the data-source store.
	 * 
	 * @type Ext.data.Store
	 */
	store : null,

	/**
	 * 
	 * @type dnet.core.dc.DcContext
	 */
	dcContext : null,

	readOnly : false,

	_trl_ : null,

	lastStateManagerFlags : null,

	constructor : function(config) {
		config = config || {};
		Ext.apply(this, config);
		this.dcState = Ext.create("dnet.core.dc.DcState");
		this.children = [];
		this.selectedRecords = [];
		this.dsName = this.recordModel.substring(this.recordModel
				.lastIndexOf('.') + 1, this.recordModel.length);
		this.store = Ext.create("Ext.data.Store", {
			model : this.recordModel,
			remoteSort : true,

			autoLoad : false,
			autoSync : false,
			clearOnPageLoad : true,
			pageSize : this.tuning.fetchSize,
			proxy : {
				type : 'ajax',
				timeout : this.tuning.storeTimeout,
				api : Dnet.dsAPI(this.dsName, "json"),
				model : this.recordModel,
				extraParams : {
					params : {}
				},
				actionMethods : {
					create : 'POST',
					read : 'POST',
					update : 'POST',
					destroy : 'POST'
				},
				reader : {
					type : 'json',
					root : 'data',
					idProperty : 'id',
					totalProperty : 'totalCount',
					messageProperty : 'message'
				},
				writer : {
					type : 'json',
					encode : true,
					root : "data",
					allowSingle : false,
					writeAllFields : true
				},
				listeners : {
					"exception" : {
						fn : this.proxyException,
						scope : this
					}
				},
				startParam : Dnet.requestParam.START,
				limitParam : Dnet.requestParam.SIZE,
				sortParam : Dnet.requestParam.ORDERBY
			}

		});

		this.addEvents(
		/**
		 * Current record instance changed
		 */
		"recordChange",

		/**
		 * Selected records changed
		 */
		"selectionChange",

		"onEditIn", "onEditOut", "onEnterQuery", "message");

		this.mixins.observable.constructor.call(this);
		this._setup_();
	},

	_setup_ : function() {

		try {
			this._trl_ = Ext.create(this.recordModel + "$Trl");
		} catch (e) {

		}

		if (Ext.isEmpty(this.filterModel)) {
			Dnet.createFilterModelFromRecordModel({
				recordModelFqn : this.recordModel
			});
			this.filterModel = this.recordModel + "Filter";
		}
		this.setFilter(this.newFilterInstance());

		if (!Ext.isEmpty(this.paramModel)) {
			var _p = Ext.create(this.paramModel, {});
			if (_p.initParam) {
				_p.beginEdit();
				_p.initParam();
				_p.endEdit();
			}
			this.setParams(_p);
		}

		this.actionNames = dnet.core.dc.DcActionsFactory.actionNames();
		this.commandNames = dnet.core.dc.DcCommandFactory.commandNames();

		this.actions = dnet.core.dc.DcActionsFactory.createActions(this,
				this.actionNames);
		this.commands = dnet.core.dc.DcCommandFactory.createCommands(this,
				this.commandNames.concat(this.actionNames));

		this._registerListeners_();

	},

	_registerListeners_ : function() {

		this.mon(this.store, "beforeload", this.onStore_beforeload, this);
		this.mon(this.store, "update", this.onStore_update, this);
		this.mon(this.store, "datachanged", this.onStore_datachanged, this);
		this
				.mon(this.store, "changes_rejected", this.onStore_datachanged,
						this);

		/* after the store is loaded apply an initial selection */
		if (this.afterStoreLoadDoDefaultSelection) {
			this.mon(this.store, "load", this.onStore_load, this);
		}

		/* invoke the action state update whenever necessary */
		this.mon(this, "recordChange", this.requestStateUpdate, this);
		//
		this.mon(this, "selectionChange", this.requestStateUpdate, this);
		this.mon(this, "stateUpdateRequest", this.updateDcState, this, {
			buffer : 100
		});

	},

	onStore_load : function(store, records, succes, eopts) {
		if (this.afterStoreLoadDoDefaultSelection) {
			this.doDefaultSelection();
		}
	},

	onStore_beforeload : function(store, operation, eopts) {
		if (dnet.core.dc.DcActionsStateManager.isQueryDisabled(this)) {
			return false;
		}
	},

	onStore_update : function(store, rec, operation, eopts) {
		this.fireEvent("statusChange", {
			dc : this
		});
		this.requestStateUpdate();
	},

	onStore_datachanged : function(store, eopts) {
		this.requestStateUpdate();
	},

	onStore_remove : function(store, records, index, eopts) {
		this.requestStateUpdate();
		this.doDefaultSelection();
	},

	/* ***************************************************** */
	/* ***************** Public API ************************ */
	/* ***************************************************** */

	/**
	 * Execute query to fetch data.
	 */
	doQuery : function(options) {
		this.commands.doQuery.execute(options);
	},

	/**
	 * Clear query criteria -> reset filter to its initial state
	 */
	doClearQuery : function(options) {
		this.commands.doClearQuery.execute(options);
	},

	/**
	 * Enter query. Fire the onEnterQuery event which should be listened by
	 * filter forms to acquire focus.
	 * 
	 */
	doEnterQuery : function(options) {
		this.fireEvent("onEnterQuery", this, options);
	},

	/**
	 * Create a new record.
	 */
	doNew : function(options) {
		this.commands.doNew.execute(options);
	},

	/**
	 * Copy the current record reset its ID and make it current record ready to
	 * be edited.
	 */
	doCopy : function(options) {
		this.commands.doCopy.execute(options);
	},

	/**
	 * Save changes.
	 */
	doSave : function(options) {
		this.commands.doSave.execute(options);
	},

	/**
	 * Discard changes to the last clean state.
	 */
	doCancel : function(options) {
		this.commands.doCancel.execute(options);
	},

	/**
	 * Delete current record.
	 */
	doDelete : function(options) {
		this.commands.doDelete.execute(options);
	},

	/**
	 * Delete selected records.
	 */
	doDeleteSelection : function(options) {
		this.commands.doDelete.execute(options);
	},

	/**
	 * Start edit process. Fires onEditIn event which Edit forms should listen
	 * to in order to acquire focus. Used for data-controls which are not multi
	 * edit and work with a grid/form pairs.
	 */
	doEditIn : function(options) {
		this.commands.doEditIn.execute(options);
	},

	/**
	 * Finish edit process. Fires onEditOut event which grid lists should listen
	 * to in order to acquire focus. Used for data-controls which are not multi
	 * edit and work with a grid/form pairs.
	 */
	doEditOut : function(options) {
		this.commands.doEditOut.execute(options);
	},

	/**
	 * Reload the current record data from server
	 */
	doReloadRecord : function(options) {
		this.commands.doReloadRec.execute(options);
	},

	/**
	 * Call a server side RPC with the filter instance
	 */
	doRpcFilter : function(options) {
		this.commands.doRpcFilter.execute(options);
	},

	/**
	 * Call a server side RPC with the model instance
	 */
	doRpcData : function(options) {
		this.commands.doRpcData.execute(options);
	},

	/**
	 * Call a server side RPC with the model instance list
	 */
	doRpcDataList : function(options) {
		this.commands.doRpcDataList.execute(options);
	},

	/**
	 * Set the previous available record as current record.
	 */
	setPreviousAsCurrent : function(options) {
		this.commands.doPrevRec.execute(options);
	},

	/**
	 * Set the next available record as current record.
	 */
	setNextAsCurrent : function(options) {
		this.commands.doNextRec.execute(options);
	},

	/**
	 * Load the next page of records
	 */
	nextPage : function(options) {
		var s = this.store;
		if (s.loading) {
			return;
		}
		if (dnet.core.dc.DcActionsStateManager.isQueryDisabled(this)) {
			this.info(Dnet.msg.DC_QUERY_NOT_ALLOWED, "msg");
			return false;
		}
		if (s.totalCount) {
			if (s.currentPage * s.pageSize < s.totalCount) {
				s.loadPage(s.currentPage + 1, options);
			} else {
				this.info(Dnet.msg.AT_LAST_PAGE, "msg");
			}
		} else {
			s.loadPage(s.currentPage + 1, options);
		}

	},

	/**
	 * Load the previous page of records
	 */
	previousPage : function(options) {
		var s = this.store;
		if (s.loading) {
			return;
		}
		if (dnet.core.dc.DcActionsStateManager.isQueryDisabled(this)) {
			this.info(Dnet.msg.DC_QUERY_NOT_ALLOWED, "msg");
			return false;
		}
		if (s.currentPage > 1) {
			s.loadPage(s.currentPage - 1, options);
		} else {
			this.info(Dnet.msg.AT_FIRST_PAGE, "msg");
		}
	},

	// --------------- other ---------------

	/**
	 * Filter validator. It should get interceptor functions chain injected by
	 * the filter forms.
	 */
	isFilterValid : function() {
		return true;
	},

	/**
	 * Record validator. It should get interceptor functions chain injected by
	 * the editor forms.
	 */
	isRecordValid : function() {
		return this.record.isValid();
	},

	/**
	 * Get current record state: dirty/clean
	 */
	getRecordState : function() {
		if (this.record)
			return (this.isCurrentRecordDirty()) ? 'dirty' : 'clean';
		else
			return null;
	},

	/**
	 * Get current record status: insert/update
	 */
	getRecordStatus : function() {
		if (this.record)
			return (this.record.phantom) ? 'insert' : 'update';
		else
			return null;
	},

	requestStateUpdate : function() {
		this.fireEvent("stateUpdateRequest", {
			dc : this
		});
	},

	/**
	 * Update the enabled/disabled states of the actions. Delegate the work to
	 * the states manager.
	 */
	updateDcState : function(ifNeeded) {
		// console.log("AbstractDc.updateActionsState (" + this.$className +
		// ")");
		if (this.dcState.run(this)) {
			this.fireEvent("stateUpdatePerformed", {
				dc : this
			});
		}
	},

	/**
	 * Returns true if any of the child data-controls is dirty
	 */
	isAnyChildDirty : function() {
		var dirty = false, l = this.children.length;
		for ( var i = 0; i < l; i++) {
			if (this.children[i].isDirty()) {
				dirty = true;
				i = l;
			}
		}
		return dirty;
	},

	/**
	 * Returns true if the current record instance is dirty
	 */
	isCurrentRecordDirty : function() {
		if (this.record != null && (this.record.dirty || this.record.phantom)) {
			return true;
		}
		return false;
	},

	/**
	 * Returns true if the store is dirty. Is relevant only if
	 * <code>multiEdit=true</code>
	 */
	isStoreDirty : function() {
		return this.store.getRemovedRecords().length > 0
				|| this.store.getUpdatedRecords().length > 0
				|| this.store.getAllNewRecords().length > 0;
	},

	/**
	 * Returns true if the data-control is dirty i.e either some the own records
	 * or any child
	 */
	isDirty : function() {
		return this.isCurrentRecordDirty() || this.isStoreDirty()
				|| this.isAnyChildDirty();
	},

	/**
	 * Default initial selection
	 */
	doDefaultSelection : function() {
		if (this.store.getCount() > 0) {
			this.setRecord(this.store.getAt(0), true);
		} else {
			this.setRecord(null);
		}
	},

	/**
	 * Helper function which packs the necessary information for a query. It is
	 * used in queryCommand, and export and print windows.
	 * 
	 * @return {}
	 */
	buildRequestParamsForQuery : function() {
		var _fd = this.filter.data;
		for ( var key in _fd) {
			if (_fd[key] === "") {
				_fd[key] = null;
			}
		}
		var _p = {};
		_p[Dnet.requestParam.FILTER] = Ext.encode(this.filter.data);
		_p[Dnet.requestParam.ADVANCED_FILTER] = Ext.encode(this.advancedFilter
				|| []);
		if (this.params != null) {
			_p[Dnet.requestParam.PARAMS] = Ext.encode(this.params.data);
		}
		return _p;
	},

	// messages

	/**
	 * Publish a message to notify subscribers. Used by a frame to write
	 * published messages in its notifications area ( status bar )
	 */
	message : function(type, message, trlGroup, params) {
		this.fireEvent("message", {
			type : type,
			message : message,
			trlGroup : trlGroup,
			params : params
		});
	},

	/**
	 * Publish an info message
	 */
	info : function(message, trlGroup, params) {
		this.message(Dnet.msgType.INFO, message, trlGroup, params);
	},

	/**
	 * Publish an info message
	 */
	warning : function(message, trlGroup, params) {
		this.message(Dnet.msgType.WARNING, message, trlGroup, params);
	},

	/**
	 * Publish an error message
	 */
	error : function(message, trlGroup, params) {
		this.message(Dnet.msgType.ERROR, message, trlGroup, params);

	},

	// --------------------- getters / setters ----------------------

	/**
	 * Returns the filter instance
	 */
	getFilter : function() {
		return this.filter;
	},

	/**
	 * Set the filter instance
	 */
	setFilter : function(v) {
		var of = this.filter;
		this.filter = v;
		this.fireEvent("filterChanged", {
			dc : this,
			newFilter : this.filter,
			oldFilter : of
		});
	},

	/**
	 * Get filter property value
	 */
	getFilterValue : function(n) {
		return this.filter.get(n);
	},

	/**
	 * Set filter property value. As currently the filter model is not part of a
	 * store, changes to properties must be fired so that filter views bound to
	 * the filter model can be notified to refresh their fields values.
	 * 
	 * In some future release it is likely to be enhanced so that filters can be
	 * saved and reused.
	 * 
	 * @param {String}
	 *            property filter-model property to change
	 * @param {Object}
	 *            newValue The new value
	 * @param {boolean}
	 *            silent Do not fire the event.
	 */
	setFilterValue : function(property, newValue, silent, operation) {
		var oldValue = this.filter.get(property);
		if (oldValue != newValue) {
			this.filter.set(property, newValue);
			if (!(silent === true)) {
				this.fireEvent("filterValueChanged", this, property, oldValue,
						newValue, operation);
			}
		}
	},

	/**
	 * Get the parameters instance
	 */
	getParams : function() {
		return this.params;
	},

	/**
	 * Set the parametr instance
	 */
	setParams : function(v) {
		this.params = v;
	},

	/**
	 * Get parameter property value
	 */
	getParamValue : function(n) {
		return this.params.get(n);
	},

	/**
	 * Set parameter property value
	 */
	setParamValue : function(property, newValue, silent) {
		var ov = this.params.get(property);
		if (ov != newValue) {
			this.params.set(property, newValue);
			if (silent !== true) {
				this.fireEvent("parameterValueChanged", this, property, ov,
						newValue);
			}
		}
	},

	/**
	 * Returns the current record
	 * 
	 * @return {Ext.data.Model} Current record or null
	 */
	getRecord : function() {
		return this.record;
	},

	/**
	 * Template method to override with instance specific logic in case is
	 * necessary
	 */
	beforeSetRecord : function() {
		return true;
	},

	/**
	 * Set the given record as the current working record. If the selectIt
	 * parameter is true make it the selection.
	 * 
	 * @param {Ext.data.Model}
	 *            p Record to be set as current record
	 * @param {Boolean}
	 *            selectIt Update the selected records
	 */
	setRecord : function(p, selectIt, eOpts) {

		p = (p != undefined) ? p : null;

		if (!this.multiEdit) {
			if (this.isDirty()) {
				this.error(Dnet.msg.DIRTY_DATA_FOUND, Dnet.msgType.ERROR);
				return false;
			}
		}

		if (this.beforeSetRecord() === false) {
			return false;
		}
		var rec, idx, changed = false, oldrec;
		if (p != null) {
			if (Ext.isNumber(p)) {
				// idx = p;
				rec = this.store.getAt(p);
				if (rec && (this.record != rec)) {
					oldrec = this.record;
					this.record = rec;
					changed = true;
				}
			} else {
				rec = p;
				// idx = this.store.indexOf(p);
				if (rec && (this.record != rec)) {
					oldrec = this.record;
					this.record = rec;
					changed = true;
				}
			}
		} else {
			rec = p;
			oldrec = this.record;
			this.record = rec;
			changed = (oldrec != null);

		}

		if (changed) {
			this.fireEvent('recordChange', {
				dc : this,
				newRecord : rec,
				oldRecord : oldrec,
				status : this.getRecordStatus(),
				eOpts : eOpts
			});
			if (selectIt === true) {
				if (rec != null) {
					this.setSelectedRecords([ rec ], eOpts);
				} else {
					this.setSelectedRecords([], eOpts);
				}
			}
		}
	},

	/**
	 * Returns the selected records
	 */
	getSelectedRecords : function() {
		return this.selectedRecords;
	},

	/**
	 * Set the selected records
	 */
	setSelectedRecords : function(recArray, eOpts) {
		recArray = recArray || [];
		if (!Ext.isArray(recArray)) {
			recArray = [ recArray ];
		}

		if (this.selectedRecords !== recArray) {
			this.selectedRecords = recArray;
			var l = this.selectedRecords.length || 0;
			// console.log("AbstractDc-selectedRecords.length=" + l);
			if (this.record != null) {
				// console.log("AbstractDc-record.code=" +
				// this.record.data.code);
				var found = false;
				for ( var i = 0; i < l; i++) {
					// console.log("AbstractDc-looking for match ="
					// + this.selectedRecords[i].data.code);
					if (this.record === this.selectedRecords[i]) {
						found = true;
						break;
					}
				}
				if (!found) {
					this.setRecord(this.selectedRecords[0], false);
				}
			} else {
				this.setRecord(this.selectedRecords[0], false);
			}

			this.fireEvent('selectionChange', {
				dc : this,
				eOpts : eOpts
			});
		}
	},

	/**
	 * Return parent data-control
	 */
	getParent : function() {
		return (this.dcContext) ? this.dcContext.parentDc : null;
	},

	/**
	 * Return children data-controls list
	 */
	getChildren : function() {
		return this.children;
	},

	/**
	 * Register a child data-control
	 */
	addChild : function(dc) {
		this.children[this.children.length] = dc;
	},

	isReadOnly : function() {
		return this.readOnly;
	},

	setReadOnly : function(v, silent) {
		this.readOnly = v;
		if (silent !== true) {
			dnet.core.dc.DcActionsStateManager.applyStates(this);
			this.fireEvent("readOnlyChanged", this, v);
		}
	},

	/**
	 * Creates a new record instance and initialize it
	 */
	newRecordInstance : function() {
		var r = Ext.create(this.recordModel);
		if (r.initRecord) {
			r.beginEdit();
			r.initRecord();
			r.endEdit();
		}
		return r;
	},

	/**
	 * Creates a new filter instance and initialize it
	 */
	newFilterInstance : function() {
		var f = Ext.create(this.filterModel);
		if (f.initFilter) {
			f.beginEdit();
			f.initFilter();
			f.endEdit();
		}
		return f;
	},

	/* ********************************************************** */
	/* ******************** Internal API ************************ */
	/* ********************************************************** */

	/**
	 * Default proxy-exception handler
	 */
	proxyException : function(proxy, response, operation, eOpts) {
		if (operation && operation.action == "destroy" && !this.multiEdit) {
			this.store.rejectChanges();
		}
	},

	/**
	 * Show errors to user. TODO: Externalize it as command.
	 */
	showAjaxErrors : function(response, options) {
		try {
			Ext.MessageBox.hide();
		} catch (e) {

		}
		var msg, withDetails = false;
		if (response.responseText) {
			if (response.responseText.length > 2000) {
				msg = response.responseText.substr(0, 2000);
				withDetails = true;
			} else {
				msg = response.responseText;
			}
		} else {
			msg = "No response received from server.";
		}
		var alertCfg = {
			title : "Server message",
			msg : msg,
			scope : this,
			icon : Ext.MessageBox.ERROR,
			buttons : Ext.MessageBox.OK
		}
		if (withDetails) {
			alertCfg.buttons['cancel'] = 'Details';
			alertCfg['detailedMessage'] = response.responseText;
		}
		Ext.Msg.show(alertCfg);

	},

	// ************************************************
	// to be reviewd
	// ************************************************

	afterDoQuerySuccess : function() {
		this.fireEvent("afterDoQuerySuccess", this);
	},

	afterDoQueryFailure : function() {
		this.fireEvent("afterDoQueryFailure", this);
	},

	beforeDoSave : function() {
		this.fireEvent("beforeDoSave", this);
	},

	afterDoSave : function() {
		this.fireEvent("afterDoSave", this);
	},

	afterDoSaveSuccess : function() {
		this.fireEvent("afterDoSaveSuccess", this);
	},

	afterDoSaveFailure : function() {
		this.fireEvent("afterDoSaveFailure", this);
	},

	/* *********** SERVICE DATA ********************* */

	// doServiceUrl : function(serviceName, specs) {
	// if (Ext.isEmpty(this.record)) {
	// throw (dnet.core.dc.DcExceptions.NO_CURRENT_RECORD);
	// }
	// var s = specs || {};
	//
	// var p = {
	// data : Ext.encode(this.record.data)
	// };
	// p[Dnet.requestParam.SERVICE_NAME_PARAM] = serviceName;
	// p["rpcType"] = "data";
	// if (s.modal) {
	// Dnet.working();
	// }
	// return Dnet.dsAPI(this.dsName, "stream").service + "&"
	// + Ext.urlEncode(p);
	// },
	/**
	 * Default AJAX request failure handler.
	 */
	onAjaxRequestSuccess : function(response, options) {
		try {
			Ext.MessageBox.hide();
		} catch (e) {

		}
		var o = options.options || {};
		if (o.action) {
			if (o.action == "doQuery") {
				this.afterDoQuerySuccess();
			}
			if (o.action == "doSave") {
				this.afterDoSaveSuccess();
			}
			if (o.action == "doService") {
				this.afterDoServiceSuccess(response, o.serviceName, o.specs);
			}
			if (o.action == "doServiceFilter") {
				this.afterDoServiceFilterSuccess(response, o.serviceName,
						o.specs);
			}
		}
	},

	/**
	 * Default AJAX request failure handler.
	 */
	onAjaxRequestFailure : function(response, options) {
		try {
			Ext.MessageBox.hide();
		} catch (e) {

		}
		var o = options.options || {};
		if (o.action) {
			if (o.action == "doQuery") {
				this.afterDoQueryFailure();
			}
			if (o.action == "doSave") {
				this.afterDoSaveFailure();
			}
			if (o.action == "doService") {
				this.afterDoServiceFailure(response, o.serviceName, o.specs);
			}
			if (o.action == "doServiceFilter") {
				this.afterDoServiceFilterFailure(response, o.serviceName,
						o.specs);
			}
		}
	},

	setDcContext : function(dcCtx) {
		this.dcContext = dcCtx;
		// this.mon(this.dcContext, "dataContextChanged",
		// this.onDcContext_dataContextChanged, this);
	},

	// onDcContext_dataContextChanged : function(dctx) {
	// // dnet.core.dc.DcActionsStateManager.applyStates(this);
	// // if (dctx.parentDc.getRecord() && dctx.parentDc.getRecord().phantom) {
	// // this.fireEvent("inContextOfNewRecord", this);
	// // } else {
	// // this.fireEvent("inContextOfEditRecord", this);
	// // }
	// },

	onCleanDc : function() {
		this.fireEvent('cleanDc', {
			dc : this
		});
		if (this.dcContext != null) {
			this.dcContext._onChildCleaned_();
		}
	},

	onChildCleaned : function() {
		if (!this.isStoreDirty() && !this.isAnyChildDirty()) {
			this.onCleanDc();
		}
	},

	destroy : function() {
		// console.log("AbstractDc.destroy");
		for ( var p in this.actions) {
			delete this.actions[p].dc;
		}
		for ( var p in this.commands) {
			delete this.commands[p].dc;
		}

		this.store.clearListeners();
		this.store.destroyStore();

		if (this.dcContext) {
			this.dcContext.destroy();
		}

		delete this.children;
		delete this.parent;
		delete this.dcContext;

	}
});
