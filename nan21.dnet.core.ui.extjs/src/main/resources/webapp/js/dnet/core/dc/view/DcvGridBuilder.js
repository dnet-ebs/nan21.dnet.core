/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Builder for read-only grid views.
 */
Ext.define("dnet.core.dc.view.DcvGridBuilder", {
	extend : "Ext.util.Observable",

	/**
	 * Target grid
	 */
	dcv : null,

	addTextColumn : function(config) {
		config.xtype = "gridcolumn";
		config.field = {
			xtype : 'textfield',
			readOnly : true
		};
		this.applySharedConfig(config);
		return this;
	},

	addBooleanColumn : function(config) {
		config.xtype = "booleancolumn";
		Ext.apply(config, {
			trueText : Dnet.translate("msg", "bool_true"),
			falseText : Dnet.translate("msg", "bool_false")
		});
		Ext.applyIf(config, {
			width : Dnet.viewConfig.BOOLEAN_COL_WIDTH
		});
		this.applySharedConfig(config);
		return this;
	},

	addDateColumn : function(config) {
		config.xtype = "datecolumn";
		Ext.applyIf(config, {
			_mask_ : Masks.DATE
		});
		config.format = Dnet[config._mask_];
		this.applySharedConfig(config);
		return this;
	},

	addNumberColumn : function(config) {
		config.xtype = "numbercolumn";		
		config.format = Dnet.getNumberFormat(config.decimals || 0);
		Ext.applyIf(config, {
			align : "right"
		});
		this.applySharedConfig(config);
		return this;
	},

	shouldCreateField : function(r, cols, name) {
		return (r.fields.containsKey(name) && !cols.containsKey(name));
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
			sortable : true,
			hidden : false
		});
		this.dcv._columns_.add(config.name, config);
	},

	addAllFromDataSource : function() {

		var f = this.dcv._controller_.ds.recordFields;
		for ( var i = 0, len = f.length; i < len; i++) {
			var name = f[i]["name"];
			var type = f[i]["type"];
			var cfg = {
				name : name,
				dataIndex : name
			};

			// try to guess something
			if (name == "id" || name == "createdAt" || name == "createdBy"
					|| name == "version" || name == "clientId") {
				cfg.hidden = true;
			}

			if (type == "string") {
				this.addTextColumn(cfg);
			}
			if (type == "date") {
				this.addDateColumn(cfg);
			}
			if (type == "boolean") {
				this.addBooleanColumn(cfg);
			}
			if (type == "int") {
				this.addNumberColumn(cfg);
			}

		}
	}
});
