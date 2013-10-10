/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.ns("dnet.core.asgn");

dnet.core.asgn.AsgnGridBuilder = function(config) {
	this.asgnGrid = null;
	Ext.apply(this, config);
	dnet.core.asgn.AsgnGridBuilder.superclass.constructor.call(this, config);
};

Ext.extend(dnet.core.asgn.AsgnGridBuilder, Object, {

	/**
	 * Add a text column to grid
	 */
	addTextColumn : function(config) {
		config.xtype = "gridcolumn";
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Add a boolean column to grid
	 */
	addBooleanColumn : function(config) {
		config.xtype = "booleancolumn";
		Ext.apply(config, {
			trueText : Dnet.translate("msg", "bool_true"),
			falseText : Dnet.translate("msg", "bool_false")
		});
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Add a date column to grid
	 */
	addDateColumn : function(config) {
		config.xtype = "datecolumn";
		Ext.applyIf(config, {
			format : Ext.DATE_FORMAT
		});
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Add a number column to grid
	 */
	addNumberColumn : function(config) {
		config.xtype = "numbercolumn";
		Ext.applyIf(config, {
			align : "right"
		});
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Add a column to grid with the given configuration
	 */
	add : function(config) {
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Merge (keep existing) the configuration of the specified column with the
	 * new values.
	 */
	merge : function(name, config) {
		Ext.applyIf(this.asgnGrid._columns_.get(name), config);
		return this;
	},

	/**
	 * Change (overwrite existing) the configuration of the specified column
	 * with the new values.
	 */
	change : function(name, config) {
		Ext.apply(this.asgnGrid._columns_.get(name), config);
		return this;
	},

	/**
	 * Remove the specified column definition.
	 */
	remove : function(name) {
		this.asgnGrid._columns_.remove(name);
		return this;
	},

	// private
	/**
	 * Apply shared configuration
	 */
	applySharedConfig : function(config) {
		Ext.applyIf(config, {
			id : Ext.id(),
			sortable : true,
			hidden : false
		});
		this.asgnGrid._columns_.add(config.name, config);
	},

	/**
	 * Helper method to create column definitions for all fields from the record
	 * model.
	 */
	addAllFromDataSource : function() {

		var f = this.asgnGrid._controller_.ds.recordFields;
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
