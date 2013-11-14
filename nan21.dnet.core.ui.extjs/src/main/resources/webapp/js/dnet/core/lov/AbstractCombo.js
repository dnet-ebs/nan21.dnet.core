/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */

Ext.define("dnet.core.lov.AbstractCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : "widget.xcombo",

	// DNet properties

	/**
	 * 
	 * Link to the dc-view fields to filter the records in this combo.
	 * 
	 * Example: ,filterFieldMapping: [{lovField:"...lovFieldName", dsField:
	 * "...dsFieldName"} ]
	 * 
	 * Or: ,filterFieldMapping: [{lovField:"...lovFieldName", value: "...static
	 * value"} ]
	 */
	filterFieldMapping : null,

	/**
	 * Specify what values should this combo return to the dc-record.
	 * 
	 * @type Array
	 */
	retFieldMapping : null,

	_dataProviderFields_ : null,
	_dataProviderName_ : null,
	_dummyValue_ : null,
	_editDialog_ : null,
	openDialog : null,
	_isLov_ : true,

	/**
	 * Data-control view type this field belongs to. Injected by the
	 * corresponding view builder.
	 */
	_dcView_ : null,

	/**
	 * Data model signature - record constructor.
	 * 
	 * @type Ext.data.Model
	 */
	recordModel : null,
	/**
	 * Parameters model signature - record constructor.
	 * 
	 * @type Ext.data.Model
	 */
	paramModel : null,
	/**
	 * Parameters model instance
	 */
	params : null,

	// defaults
	triggerAction : "query",
	matchFieldWidth : false,
	pageSize : 20,
	autoSelect : true,
	minChars : 0,
	queryMode : "remote",

	trigger1Cls : Ext.baseCSSPrefix + 'x-form-trigger',
	trigger2Cls : Ext.baseCSSPrefix + 'form-search-trigger',

	defaultListConfig : {
		minWidth : 70,
		width : 230,
		shadow : "sides",
		autoScroll : true
	},

	autoScroll : true,

	initComponent : function() {
		this._createStore_();
		if (!Ext.isEmpty(this.paramModel)) {
			this.params = Ext.create(this.paramModel, {});
		}
		if (this.retFieldMapping == null) {
			this.retFieldMapping = [];
		}
		if (this.dataIndex) {
			this.retFieldMapping[this.retFieldMapping.length] = {
				lovField : this.displayField,
				dsField : this.dataIndex
			};
		} else if (this.paramIndex) {
			this.retFieldMapping[this.retFieldMapping.length] = {
				lovField : this.displayField,
				dsParam : this.paramIndex
			};
		}
		this.on("select", this.assertValue, this);

		this.callParent(arguments);
	},

	onTrigger2Click : function() {
		if (this._editDialog_ == null) {
			alert("No destination frame specified.");
			return;
		} else {
			if (this._editDialog_.custom == undefined) {
				this._editDialog_.custom = false;
			}
		}
		getApplication().showFrame(
				this._editDialog_.name,
				{
					url : Dnet.buildUiPath(this._editDialog_.bundle,
							this._editDialog_.name, this._editDialog_.custom),
					tocElement : this._editDialog_.tocElement
				});
	},

	_createStore_ : function() {
		if (this._dataProviderName_ == null) {
			this._dataProviderName_ = this.recordModel.substring(
					this.recordModel.lastIndexOf('.') + 1,
					this.recordModel.length);
		}
		this.store = Ext.create("Ext.data.Store", {
			model : this.recordModel,
			remoteSort : true,
			autoLoad : false,
			autoSync : false,
			clearOnPageLoad : true,
			pageSize : this.pageSize,
			proxy : {
				type : 'ajax',
				api : Dnet.dsAPI(this._dataProviderName_, "json"),
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
				listeners : {
					"exception" : {
						fn : this.proxyException,
						scope : this
					}
				},
				startParam : Dnet.requestParam.START,
				limitParam : Dnet.requestParam.SIZE,
				sortParam : Dnet.requestParam.SORT,
				directionParam : Dnet.requestParam.SENSE
			}
		});
	},

	/**
	 * Map fields from the combo-record received as argument (crec) to the
	 * target record according to fieldMappings
	 * 
	 * @param {}
	 *            crec Combo selected record
	 */
	_mapReturnFields_ : function(crec) {
		var dcv = this._dcView_;
		var mrec = null;
		var targetIsFilter = false;
		if (this.inEditor && dcv._dcViewType_ != "edit-propgrid"
				&& dcv._dcViewType_ != "filter-propgrid") {
			if (dcv && dcv._dcViewType_ == "bulk-edit-field") {
				/*
				 * is a bulk editor for one ds field in a property grid
				 */
				mrec = dcv.getSource();
				this._mapReturnFieldsExecuteBulkEdit_(crec, mrec);
			} else {
				mrec = this._targetRecord_;
				this._mapReturnFieldsExecute_(crec, mrec);
			}
		} else {
			if (dcv._dcViewType_ == "edit-form"
					|| dcv._dcViewType_ == "edit-propgrid") {
				mrec = dcv._controller_.getRecord();
			}
			if (dcv._dcViewType_ == "filter-form"
					|| dcv._dcViewType_ == "filter-propgrid") {
				mrec = dcv._controller_.getFilter();
				targetIsFilter = true;
			}
			this._mapReturnFieldsExecute_(crec, mrec, dcv._controller_
					.getParams(), targetIsFilter);
		}
	},

	_mapReturnFieldsExecuteBulkEdit_ : function(crec, recdata) {
		if (!recdata) {
			return;
		}
		if (this.retFieldMapping != null) {
			for ( var i = this.retFieldMapping.length - 1; i >= 0; i--) {

				var retDataIndex = null;
				var nv = null;
				isParam = !Ext.isEmpty(this.retFieldMapping[i]["dsParam"]);
				if (isParam) {
					retDataIndex = this.retFieldMapping[i]["dsParam"];
					ov = prec.get(retDataIndex);
				} else {
					retDataIndex = this.retFieldMapping[i]["dsField"];
					ov = recdata[retDataIndex];
				}

				if (crec && crec.data) {
					nv = crec.data[this.retFieldMapping[i]["lovField"]];
					recdata[retDataIndex] = nv;
				} else {

				}
			}
		}
	},

	/**
	 * Params: crec: combo selected record
	 */
	_mapReturnFieldsExecute_ : function(crec, mrec, prec, targetIsFilter) {
		if (!mrec) {
			return;
		}
		if (this.retFieldMapping != null) {
			var nv, ov, isParam, rawv = this.getRawValue();

			for ( var i = this.retFieldMapping.length - 1; i >= 0; i--) {

				var retDataIndex = null;
				isParam = !Ext.isEmpty(this.retFieldMapping[i]["dsParam"]);
				if (isParam) {
					retDataIndex = this.retFieldMapping[i]["dsParam"];
					ov = prec.get(retDataIndex);
				} else {
					retDataIndex = this.retFieldMapping[i]["dsField"];
					ov = mrec.get(retDataIndex);
				}

				if (crec && crec.data) {
					nv = crec.data[this.retFieldMapping[i]["lovField"]];
					if (nv != ov) {
						if (isParam) {
							this._dcView_._controller_.setParamValue(
									retDataIndex, nv);
						} else {
							if (targetIsFilter) {
								this._dcView_._controller_.setFilterValue(
										retDataIndex, nv);
							} else {
								mrec.set(retDataIndex, nv);
							}
						}
					}
				} else {

					if (retDataIndex == this.dataIndex) {
						if (this._validateListValue_ && rawv != ov) {
							rawv = null;
							this.setRawValue(rawv);
						}
						if (rawv != ov) {
							if (isParam) {
								this._dcView_._controller_.setParamValue(
										retDataIndex, rawv);
							} else {
								if (targetIsFilter) {
									this._dcView_._controller_.setFilterValue(
											retDataIndex, rawv);
								} else {
									mrec.set(retDataIndex, rawv);
								}
							}
						}

					} else {
						if ((ov != null && ov != "")) {
							if (isParam) {
								this._dcView_._controller_.setParamValue(
										retDataIndex, null);
							} else {
								if (targetIsFilter) {
									this._dcView_._controller_.setFilterValue(
											retDataIndex, null);
								} else {
									mrec.set(retDataIndex, null);
								}
							}
						}
					}
				}
			}
		}
	},

	_mapFilterFields_ : function(bp) {

		var mrec = null;
		var dcv = this._dcView_;
		var prec = dcv._controller_.getParams();

		if (this.inEditor) {
			mrec = this._targetRecord_;
			this._mapFilterFieldsExecute_(bp, mrec, prec);
		} else {
			if (dcv._dcViewType_ == "edit-form") {
				mrec = dcv._controller_.getRecord();
			}
			if (dcv._dcViewType_ == "filter-form") {
				mrec = dcv._controller_.getFilter();
			}
			this._mapFilterFieldsExecute_(bp, mrec, prec);
		}
	},

	/**
	 * Parameters: bp: base params for the store
	 */
	_mapFilterFieldsExecute_ : function(bp, mrec, prec) {
		if (!mrec) {
			return;
		}
		if (this.filterFieldMapping != null) {
			var len = this.filterFieldMapping.length;
			for ( var i = 0; i < len; i++) {
				var isLovMemberParam = !Ext
						.isEmpty(this.filterFieldMapping[i]["lovParam"]);
				var _val = null;

				if (this.filterFieldMapping[i]["value"] != undefined) {
					_val = this.filterFieldMapping[i]["value"];
				} else {
					if (!Ext.isEmpty(this.filterFieldMapping[i]["dsParam"])) {
						_val = prec.get(this.filterFieldMapping[i]["dsParam"])
					} else {
						_val = mrec.get(this.filterFieldMapping[i]["dsField"])
					}
				}

				if (_val == null) {
					_val = "";
				}

				if (isLovMemberParam) {
					this.params.set(this.filterFieldMapping[i]["lovParam"],
							_val);
				} else {
					bp[this.filterFieldMapping[i]["lovField"]] = _val;
				}

			}
		}
	},

	/**
	 * Default proxy-exception handler
	 */
	proxyException : function(proxy, response, operation, eOpts) {
		this.showAjaxErrors(response, eOpts);
	},

	/**
	 * Show errors to user. TODO: Externalize it as command.
	 */
	showAjaxErrors : function(response, options) {
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

	// **************************************************
	// *********************** OVERRIDES ****************
	// **************************************************

	doQuery : function(queryString, forceAll, rawQuery) {
		var bp = {};
		var extraParams = this.store.proxy.extraParams;
		bp[this.displayField] = queryString + "*";

		if (this.filterFieldMapping != null) {
			this._mapFilterFields_(bp);
			this.queryCaching = false;
		}
		extraParams[Dnet.requestParam.FILTER] = Ext.encode(bp);
		if (this.params != null) {
			extraParams[Dnet.requestParam.PARAMS] = Ext
					.encode(this.params.data);
		}
		this.callParent(arguments);
	},

	assertValue : function() {
		var me = this;
		var val = me.getRawValue(), rec;

		rec = this.findRecord(this.displayField, val);

		if (!rec && this.forceSelection) {
			this.setRawValue("");
			if (val.length > 0 && val != this.emptyText) {

				this.applyEmptyText();
			} else {
				this.clearValue();
			}
			this._mapReturnFields_(null);

		} else {
			if (rec) {
				this._mapReturnFields_(rec);
				if (val == rec.get(this.displayField)
						&& this.value == rec.get(this.valueField)) {
					return;
				}
				val = rec.get(this.valueField || this.displayField);
			} else {
				if (val != this.value) {
					this._mapReturnFields_(null);
				}
			}
			if (this.getValue() != val) {
				this.setValue(val);
			}
		}
		// me.collapse();
	},

	onKeyUp : function(e, t) {
		var key = e.getKey();
		var kbs = Dnet.keyBindings.dc;
		var dcv = this._dcView_;
		// bindigs to check
		var btc = null;

		// ignore query related keyboard shortcuts
		if (dcv._dcViewType_ == "filter-form"
				|| dcv._dcViewType_ == "filter-propgrid") {
			btc = [ kbs.doEnterQuery, kbs.doClearQuery, kbs.doQuery, kbs.doEditOut ];
			var l = btc.length;
			for ( var i = 0; i < l; i++) {
				var b = btc[i];
				if (key == b.key && e.shiftKey == b.shift
						&& e.ctrlKey == b.ctrl && e.altKey == b.alt) {
					return;
				}
			}
		}

		// ignore edit related keyboard shortcuts
		if (dcv._dcViewType_ == "edit-form"
				|| dcv._dcViewType_ == "edit-propgrid") {
			btc = [ kbs.doNew, kbs.doCopy, kbs.doCancel, kbs.doSave,
					kbs.doEditOut, kbs.nextRec, kbs.prevRec, kbs.nextPage,
					kbs.prevPage ];
			var l = btc.length;
			for ( var i = 0; i < l; i++) {
				var b = btc[i];
				if (key == b.key && e.shiftKey == b.shift
						&& e.ctrlKey == b.ctrl && e.altKey == b.alt) {
					return;
				}
			}
		}
		this.callParent(arguments);
	}

});