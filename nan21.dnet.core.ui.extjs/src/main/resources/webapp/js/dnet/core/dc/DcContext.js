/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Defines a parent-child relationship between data-controls in a frame.
 */
Ext.define("dnet.core.dc.DcContext", {
	mixins : {
		observable : 'Ext.util.Observable'
	},

	/**
	 * Reference to the parent data-control. Must be specified in initial
	 * configuration.
	 * 
	 * @type dnet.core.dc.AbstractDc
	 */
	parentDc : null,

	/**
	 * Reference to the child data-control. Must be specified in initial
	 * configuration.
	 * 
	 * @type dnet.core.dc.AbstractDc
	 */
	childDc : null,

	/**
	 * Relation definition. Must be specified in initial configuration. Supports
	 * the following attributes:
	 * <li>fetchMode: 'auto' Automatically load the children data on parent
	 * record change. </li>
	 * <li>strict: true Enable /disable masterless operations, i.e. load
	 * children outside of the parent context</li>
	 * <li>fields: [ {childField|childParam:"field_name",
	 * parentField|parentParam|value:"field_name" (, noFilter: true|false)?},
	 * {...} ... ] Relation mapping fields. </li>
	 * 
	 * The noFilter attribute specifies if a relation field is used just as a
	 * helper for client side, but the value is not submitted on queries.
	 * 
	 * Example: a master detail relation {masterId: id}, {masterCode: code,
	 * noFilter: true } The detail data-source can be used both stand-alone and
	 * in a master-detail relation. It has a validation rule on masterCode of
	 * type not-null for stand-alone usage.
	 * 
	 * When used as a detail in context of a master, the masterCode usually is
	 * hidden as the masterId only is enough on the server to lookup the
	 * reference.
	 * 
	 * However the client validation for the detail DS will fail as it expects a
	 * not-null masterCode. So the dc-relation includes the masterCode also, but
	 * doesn't submit it to server to avoid a redundant filter criteria: select
	 * from master where id=:masterId and code like :masterCode
	 * 
	 */
	relation : null,

	/**
	 * The relation context data.
	 * 
	 * An array of relation data objects like: [{ type: "field"|"param", name:
	 * name-of-the-field-or-param, value: value-of-the-field-or-param, noFilter:
	 * true|false}]
	 * 
	 */
	ctxData : null,

	/**
	 * Flag used to automatically reload the data in the child DC after a new
	 * parent record has been saved. Useful if the creation of a parent
	 * generates child records in the business logic.
	 */
	reloadChildrenOnParentInsert : true,

	/**
	 * Flag used to automatically reload the data in the child DC after the
	 * parent record has been saved. Useful if the update of a parent alters
	 * data in the children or if the relation contains fields which are allowed
	 * to be modified by the user.
	 */
	reloadChildrenOnParentUpdate : false,

	/**
	 * Delay to trigger the fetch for the children when the parent context is
	 * changed. Applies only when the auto-load feature is enabled
	 * (fetchMode=auto).
	 * 
	 * @type Number
	 */
	autoFetchDelay : 600,

	doQueryTask : null,

	constructor : function(config) {
		config = config || {};
		if (!config || !config.relation || !config.parentDc) {
			alert ( "DCCONTEXT_INVALID_SETUP" );
		}

		Ext.apply(this, config);

		if (this.relation.strict == undefined) {
			this.relation["strict"] = true;
		}
		if (this.relation.fetchMode == undefined) {
			this.relation["fetchMode"] = "auto";
		}

		//this.addEvents("dataContextChanged");

		this.mixins.observable.constructor.call(this);
		this._setup_();

	},

	_setup_ : function() {

		this._updateCtxData_();

		this.doQueryTask = new Ext.util.DelayedTask(function() {
			this.childDc.doQuery();
		}, this);

		this.parentDc.mon(this.parentDc, "recordChange", function() {
			this._updateCtxData_("recordChange");
		}, this);

		this.parentDc.mon(this.parentDc, "statusChange", function() {
			this.childDc.requestStateUpdate();
		}, this);
		
		this.childDc.mon(this.childDc, "stateUpdatePerformed", function() {
			this.parentDc.requestStateUpdate();
		}, this);

		this.parentDc.mon(this.parentDc.store, "write", function(store,
				operation, eopts) {

			if (this.reloadChildrenOnParentInsert
					&& operation.action == "create") {
				this._updateCtxData_("parent_store_write");
			} else if (this.reloadChildrenOnParentUpdate
					&& operation.action == "update") {
				this._updateCtxData_("parent_store_write");
			} else {
				this._updateCtxData_();
			}
		}, this);

	},

	/**
	 * Update context data whenever context is changed.
	 */
	_updateCtxData_ : function(eventName) {

		this.ctxData = [];
		var f = this.relation.fields;
		var l = f.length;
		var r = this.parentDc.record;
		var p = this.parentDc.params;
		var changed = false;
		var nv = null;
		var ov = null;
		var isChildParam = false;

		for ( var i = 0; i < l; i++) {
			var _newCtxObj = {
				type : "field",
				name : "",
				value : null,
				noFilter : f[i]["noFilter"]
			};
			isChildParam = (f[i]["childParam"] != undefined);
			if (isChildParam) {
				ov = this.ctxData[f[i]["childParam"]];
			} else {
				ov = this.ctxData[f[i]["childField"]];
			}
			if (f[i]["parentParam"] != undefined) {
				nv = (p) ? p.get(f[i]["parentParam"]) : null;
			} else if (f[i]["parentField"] != undefined) {
				nv = (r) ? r.get(f[i]["parentField"]) : null;
			} else {
				nv = f[i]["value"];
			}

			if (isChildParam) {
				_newCtxObj.type = "param";
				_newCtxObj.name = f[i]["childParam"];
				_newCtxObj.value = nv;
			} else {
				_newCtxObj.type = "field";
				_newCtxObj.name = f[i]["childField"];
				_newCtxObj.value = nv;
			}

			this.ctxData[this.ctxData.length] = _newCtxObj;

			if (nv !== ov) {
				changed = true;
			}
		}

		// maybe one of the relation fields has been changed with a save
		// need to update the context filter, so a subsequent query works
		// correctly
		// it is not necessary to reload children

		if (changed) {
			this._updateChildFilter_();
		}

		if (!eventName) {
			return;
		}

		if (changed) {

			this.childDc.setRecord(null);

			this.childDc.store.loadData([], false);

			//this.fireEvent("dataContextChanged", this);

			if (this.relation.fetchMode == "auto" && this.parentDc.getRecord()
					&& !this.parentDc.getRecord().phantom) {

				this.doQueryTask.delay(this.autoFetchDelay);
			}
		}
	},

	/**
	 * Update the filter values in the child. Called usually after a context
	 * change.
	 */
	_updateChildFilter_ : function() {
		var cf = this.childDc.filter;
		var cp = this.childDc.params;
		cf.beginEdit();
		for ( var i = 0, l = this.ctxData.length; i < l; i++) {
			var _cd = this.ctxData[i];
			if (_cd.noFilter !== true) {
				if (_cd.type == "param") {
					cp.set(_cd.name, _cd.value);
				} else {
					cf.set(_cd.name, _cd.value);
				}
			}
		}
		cf.endEdit();
	},

	_applyContextData_ : function(model, isParamType) {
		for ( var i = 0, l = this.ctxData.length; i < l; i++) {
			var _cd = this.ctxData[i];
			if (isParamType !== true && _cd.type == "field") {
				model.data[_cd.name] = _cd.value;
			} else if (isParamType === true && _cd.type == "param") {
				model.data[_cd.name] = _cd.value;
			}
		}
	},

	destroy : function() {
		this.clearListeners();
		delete this.childDc;
		delete this.parentDc;
	}
});
