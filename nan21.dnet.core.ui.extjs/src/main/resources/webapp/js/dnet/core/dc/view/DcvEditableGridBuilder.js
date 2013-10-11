/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Builder for edit-grid views.
 */
Ext.define("dnet.core.dc.view.DcvEditableGridBuilder", {
	extend : "Ext.util.Observable",

	dcv : null,

	/**
	 * Create a text-column. If no editor specified and the column is not marked
	 * with noEdit:true, it creates a default text editor
	 */
	addTextColumn : function(config) {

		Ext.applyIf(config, {
			xtype : "gridcolumn",
			noInsert : false,
			noUpdate : false
		});

		if (!config.editor && config.noEdit !== true) {
			config.editor = {
				xtype : "textfield",
				selectOnFocus : true,
				noInsert : config.noInsert,
				noUpdate : config.noUpdate,
				allowBlank : ((config.allowBlank === false )? false : true)
			}
			if (config.maxLength) {
				config.editor.maxLength = config.maxLength;
			}
		}

		if (config.editor) {
			if (config.editor.maxLength) {
				config.editor.enforceMaxLength = true;
			}
			if (config.caseRestriction) {
				config.editor.caseRestriction = config.caseRestriction;
			}
		}
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Creates a date-column. If no editor specified and the column is not
	 * marked with noEdit:true, it creates a default date editor
	 */
	addDateColumn : function(config) {
		var _mask_ = Masks.DATE;
		Ext.applyIf(config, {
			xtype : "datecolumn",
			noInsert : false,
			noUpdate : false,
			format : ((config._mask_) ? Dnet[config._mask_] : Dnet[_mask_]),
			_mask_ : _mask_
		});
		if (!config.editor && config.noEdit !== true) {
			config.editor = {
				xtype : "datefield",
				format : Dnet[config._mask_],
				_mask_ : config._mask_,
				selectOnFocus : true,
				noInsert : config.noInsert,
				noUpdate : config.noUpdate,
				allowBlank : config.allowBlank
			}
		}
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Creates a number-column. If no editor specified and the column is not
	 * marked with noEdit:true, it creates a default number editor
	 */
	addNumberColumn : function(config) {
		Ext.applyIf(config, {
			xtype : "numbercolumn",
			format : Dnet.getNumberFormat(config.decimals || 0),
			align : "right",
			noInsert : false,
			noUpdate : false
		});

		if (!config.editor && config.noEdit !== true) {
			config.editor = {
				xtype : "numberfield",
				format : config.format,
				decimalPrecision : config.decimals,
				selectOnFocus : true,
				allowBlank : config.allowBlank,
				noInsert : config.noInsert,
				noUpdate : config.noUpdate,
				fieldStyle : "text-align:right;",
				hideTrigger : true,
				keyNavEnabled : false,
				mouseWheelEnabled : false
			}
		}

		this.applySharedConfig(config);
		return this;
	},

	addBooleanColumn : function(config) {
		Ext.applyIf(config, {
			width : Dnet.viewConfig.BOOLEAN_COL_WIDTH
		});

		if (config._noEdit_ !== true) {
			config.xtype = "checkcolumn";
		} else {
			config.xtype = "booleancolumn";
			Ext.apply(config, {
				trueText : Dnet.translate("msg", "bool_true"),
				falseText : Dnet.translate("msg", "bool_false")
			});
		}
		this.applySharedConfig(config);
		return this;
	},

	addComboColumn : function(config) {
		config.xtype = "gridcolumn";
		this.applySharedConfig(config);
		return this;
	},

	addLov : function(config) {
		this.applySharedConfig(config);
		return this;
	},

	shouldCreateField : function(r, cols, name) {
		return (r.fields.containsKey(name) && !cols.containsKey(name));
	},

	add : function(config) {
		this.applySharedConfig(config);
		return this;
	},

	merge : function(name, config) {
		Ext.applyIf(this.dcv._columns_.get(name), config);
		return this;
	},

	change : function(name, config) {
		Ext.apply(this.dcv._columns_.get(name), config);
		return this;
	},

	remove : function(name) {
		this.dcv._columns_.remove(name);
		return this;
	},

	// private

	applySharedConfig : function(config) {
		Ext.applyIf(config, {
			id : Ext.id(),
			headerId : config.name,
			selectOnFocus : true,
			sortable : true,
			_dcView_ : this.dcv
		});
		if (config.editor) {
			Ext.applyIf(config.editor, {
				_dcView_ : this.dcv
			});
		}
		this.dcv._columns_.add(config.name, config);
	},

	addDefaults : function() {
		var r = Ext.create(this.dcv._controller_.recordModel, {});
		var cols = this.dcv._columns_;

		if (this.shouldCreateField(r, cols, "notes")) {
			this.addTextColumn({
				name : "notes",
				dataIndex : "notes",
				hidden : true,
				width : 150
			});
		}

		if (this.shouldCreateField(r, cols, "createdAt")) {
			this.addDateColumn({
				name : "createdAt",
				dataIndex : "createdAt",
				hidden : true,
				width : 140,
				_mask_ : Masks.DATETIMESEC
			});
		}

		if (this.shouldCreateField(r, cols, "modifiedAt")) {
			this.addDateColumn({
				name : "modifiedAt",
				dataIndex : "modifiedAt",
				hidden : true,
				width : 140,
				_mask_ : Masks.DATETIMESEC
			});
		}

		if (this.shouldCreateField(r, cols, "createdBy")) {
			this.addTextColumn({
				name : "createdBy",
				dataIndex : "createdBy",
				hidden : true,
				width : 100
			});
		}

		if (this.shouldCreateField(r, cols, "modifiedBy")) {
			this.addTextColumn({
				name : "modifiedBy",
				dataIndex : "modifiedBy",
				hidden : true,
				width : 100
			});
		}

		if (this.shouldCreateField(r, cols, "id")) {
			this.addTextColumn({
				name : "id",
				dataIndex : "id",
				hidden : true,
				width : 100
			});
		}

		if (this.shouldCreateField(r, cols, "refid")) {
			this.addTextColumn({
				name : "refid",
				dataIndex : "refid",
				hidden : true,
				width : 100
			});
		}
		return this;
	}

});