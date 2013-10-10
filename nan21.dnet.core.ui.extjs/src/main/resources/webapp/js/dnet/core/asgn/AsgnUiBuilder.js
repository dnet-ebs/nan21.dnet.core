/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.ns("dnet.core.asgn");

dnet.core.asgn.AsgnUiBuilder = function(config) {
	this.asgnUi = null;
	Ext.apply(this, config);
	dnet.core.asgn.AsgnUiBuilder.superclass.constructor.call(this, config);
};

Ext.extend(dnet.core.asgn.AsgnUiBuilder, Object, {

	/**
	 * Add the left grid (which lists the available elements for selection)
	 */
	addLeftGrid : function(config) {
		Ext.apply(config || {}, {
			name : "leftList",
			id : this.asgnUi._leftGridId_,
			_side_ : "left",
			flex : 100,
			_controller_ : this.asgnUi._controller_
		});
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Add the right grid (which lists the already selected elements)
	 */
	addRightGrid : function(config) {
		Ext.apply(config || {}, {
			name : "rightList",
			id : this.asgnUi._rightGridId_,
			_side_ : "right",
			flex : 100,
			_controller_ : this.asgnUi._controller_
		});
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Add a generic element.
	 */
	add : function(config) {
		this.applySharedConfig(config);
		return this;
	},

	/**
	 * Merge (keep existing) the configuration of the specified element with the
	 * new values.
	 */
	merge : function(name, config) {
		Ext.applyIf(this.asgnUi._elems_.get(name), config);
		return this;
	},

	/**
	 * Change (overwrite existing) the configuration of the specified element
	 * with the new values.
	 */
	change : function(name, config) {
		Ext.apply(this.asgnUi._elems_.get(name), config);
		return this;
	},

	/**
	 * Remove the specified element definition.
	 */
	remove : function(name) {
		this.asgnUi._elems_.remove(name);
		return this;
	},

	// private

	/**
	 * Apply shared configuration
	 */
	applySharedConfig : function(config) {
		Ext.applyIf(config, {
			id : Ext.id()
		});
		this.asgnUi._elems_.add(config.name, config);
	}

});
