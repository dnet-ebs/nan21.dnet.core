/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Builder for filter property grid views.
 */
Ext.define("dnet.core.dc.view.DcvFilterPropGridBuilder", {
	extend : "Ext.util.Observable",

	dcv : null,

	addTextField : function(config) {
		if (!config.editor) {
			config.editor = {};
		}
		var e = config.editor;

		if (e.maxLength) {
			e.enforceMaxLength = true;
		}
		if (e.caseRestriction) {
			e.fieldStyle += "text-transform:" + e.caseRestriction + ";";
		}
		config.editorInstance = Ext.create('Ext.form.field.Text', e);

		config._default_ = "";
		this.applySharedConfig(config);
		return this;
	},

	addDateField : function(config) {
		if (!config.editor) {
			config.editor = {};
		}
		var e = config.editor;
		// config.xtype = "datefield";
		config.editorInstance = Ext.create('Ext.form.field.Date', Ext.applyIf(
				e, {
					format : Dnet.DATE_FORMAT,
					selectOnFocus : true
				}));
		config._default_ = "";
		config.renderer = config.renderer
				|| Ext.util.Format.dateRenderer(Dnet.DATE_FORMAT);
		this.applySharedConfig(config);
		return this;
	},

	addNumberField : function(config) {
		if (!config.editor) {
			config.editor = {};
		}
		var e = config.editor;
		config.editorInstance = Ext.create('Ext.form.field.Number', Ext
				.applyIf(e, {
					fieldStyle : "text-align:right;",
					selectOnFocus : true
				}));
		this.applySharedConfig(config);
		return this;
	},

	addLov : function(config) {
		if (config.editor) {
			try {
				config.editorInstance = Ext.create(config.editor._fqn_, Ext.apply(
						config.editor, {
							selectOnFocus : true
						}));
				config._default_ = "";
			} catch (e) {
				alert('Cannot create LOV editor from xtype `' + config.editor._fqn_
						+ '`');
			}
		}		
		this.applySharedConfig(config);
		return this;
	},

	addCombo : function(config) {
		if (!config.editor) {
			config.editor = {};
		}
		var e = config.editor;

		if (e.maxLength) {
			e.enforceMaxLength = true;
		}
		if (e.caseRestriction) {
			e.fieldStyle += "text-transform:" + e.caseRestriction + ";";
		}
		config.editorInstance = Ext.create('Ext.form.field.ComboBox', e);

		config._default_ = "";
		this.applySharedConfig(config);
		return this;
	},

	addBooleanField : function(config) {

		config.editor = Ext.applyIf(config.editor || {}, {
			forceSelection : false
		});
		Ext.applyIf(config, {
			_default_ : null
		});
		var yesNoStore = Dnet.createBooleanStore();
		config.editorInstance = Ext.create('Ext.form.field.ComboBox', Ext
				.apply(config.editor || {}, {
					queryMode : "local",
					valueField : "bv",
					displayField : "tv",
					triggerAction : "all",
					store : yesNoStore
				}));

		config.renderer = function(v) {
			if (v == null) {
				return "";
			}
			return Dnet.translate("msg", "bool_" + ((!!v)));
		}
		this.applySharedConfig(config);
		return this;
	},

	// ==============================================

	applySharedConfig : function(config) {
		Ext.applyIf(config, {
			id : Ext.id()
		});
		if (config.editorInstance ) {
			config.editorInstance._dcView_ = this.dcv;
		}		
		if (config.allowBlank === false) {
			config.labelSeparator = "*";
		}
		this.dcv._elems_.add(config.name, config);
	}

});