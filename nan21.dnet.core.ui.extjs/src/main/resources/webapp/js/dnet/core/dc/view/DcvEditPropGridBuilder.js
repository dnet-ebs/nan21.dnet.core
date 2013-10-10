/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Builder for edit property grid views.
 */
Ext.define("dnet.core.dc.view.DcvEditPropGridBuilder", {
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
		config.xtype = "datefield";
		config.editorInstance = Ext.create('Ext.form.field.Date', Ext.apply(
				config.editor || {}, {
					format : Dnet.DATE_FORMAT
				}));
		config.renderer = config.renderer
				|| Ext.util.Format.dateRenderer(Dnet.DATE_FORMAT);
		this.applySharedConfig(config);
		return this;
	},

	addNumberField : function(config) {
		config.xtype = "numberfield";
		config.editorInstance = Ext.create('Ext.form.field.Number', Ext.apply(
				config.editor || {}, {
					fieldStyle : "text-align:right;"
				}));
		this.applySharedConfig(config);
		return this;
	},

	addLov : function(config) {
		if (config.editor && config.editor.xtype != "textfield") {
			config.editorInstance = Ext.create(config.editor._fqn_, Ext.apply(
					config.editor, {
						selectOnFocus : true
					}));
		}
		this.applySharedConfig(config);
		return this;
	},

	addBooleanField : function(config) {
		config.editor = Ext.applyIf(config.editor || {}, {
			forceSelection : true
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
		config.editorInstance._dcView_ = this.dcv;
		if (config.allowBlank === false) {
			config.labelSeparator = "*";
		}
		this.dcv._elems_.add(config.name, config);
	}

});