/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */

/**
 * Text display field. Doesn't provide edit support.
 */
Ext.define("dnet.base.DisplayFieldText", {
	extend : "Ext.form.field.Display",
	alias : "widget.displayfieldtext",

	asText : false,

	valueToRaw : function(value) {
		return value;
	},

	setRawValue : function(value) {
		var me = this;
		value = Ext.value(value, '');
		me.rawValue = value;
		if (me.rendered) {
			if (this.asText) {
				me.inputEl.dom.innerHTML = "<pre>" + value + "</pre>";
			} else {
				me.inputEl.dom.innerHTML = me.htmlEncode ? Ext.util.Format
						.htmlEncode(value) : value;
			}

		}
		return value;
	}
});

/**
 * Date display field. Doesn't provide edit support.
 */
Ext.define("dnet.base.DisplayFieldDate", {
	extend : "Ext.form.field.Display",
	alias : "widget.displayfielddate",
	valueToRaw : function(value) {
		return Ext.util.Format.date(value, Dnet.DATE_FORMAT);
	}
});

/**
 * Number display field. Doesn't provide edit support.
 */
Ext.define("dnet.base.DisplayFieldNumber", {
	extend : "Ext.form.field.Display",
	alias : "widget.displayfieldnumber",

	valueToRaw : function(value) {
		return Ext.util.Format.number(value, this.format || "0");
	}
});
/**
 * Boolean display field. Doesn't provide edit support.
 */
Ext.define("dnet.base.DisplayFieldBoolean", {
	extend : "Ext.form.field.Display",
	alias : "widget.displayfieldboolean",
	renderer : function(rawValue, field) {
		if (rawValue === "false") {
			return Dnet.translate("msg", "bool_false");
		}
		return Dnet.translate("msg", "bool_" + (!!rawValue));
	}
});