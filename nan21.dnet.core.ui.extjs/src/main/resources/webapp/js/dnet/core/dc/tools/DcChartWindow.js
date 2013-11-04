/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.dc.tools.DcChartWindow", {
	extend : "Ext.Window",
	title : Dnet.translate("cmp", "chart_title"),
	border : true,
	width : 350,
	resizable : false,
	closable : true,
	constrain : true,
	modal : true,
	_grid_ : null,

	items : {
		xtype : "form",
		frame : true,
		defaultType : "textfield",
		bodyPadding : 10,
		defaults : {
			anchor : "-20"
		},
		buttonAlign : "center",
		fieldDefaults : {
			labelAlign : "right",
			labelWidth : 100,
			msgTarget : "side",
			selectOnFocus : true,
			allowBlank : false
		},

		buttons : [ {
			text : Dnet.translate("tlbitem", "ok__lbl"),
			formBind : true,
			disabled : true,
			handler : function() {
				this.up("form").executeTask();
			}
		} ],

		items : [ {
			name : "fld_type",
			fieldLabel : Dnet.translate("cmp", "chart_type"),
			xtype : "combo",
			forceSelection : true,
			triggerAction : "all",
			store : [ "line" ],
			value : "line"
		}, {
			name : "fld_title",
			fieldLabel : Dnet.translate("cmp", "chart_title"),
			xtype : "textfield",
			value : "Chart"

		}, {
			name : "fld_x",
			fieldLabel : Dnet.translate("cmp", "chart_x"),
			xtype : "combo",
			forceSelection : false,
			triggerAction : "all",
			store : []
		}, {
			name : "fld_y",
			fieldLabel : Dnet.translate("cmp", "chart_y"),
			xtype : "combo",
			forceSelection : false,
			triggerAction : "all",
			store : []

		}, ],

		/**
		 * Handler. Run in button scope
		 */
		executeTask : function() {

			var form = this.getForm();
			var wdw = this.up("window");
			var grid = wdw._grid_;
			var val = form.getValues();
			var ctrl = grid._controller_;

			var url = '/nan21.dnet.core.web/chart/' + ctrl.dsName + '?';
			var _p = ctrl.buildRequestParamsForQuery();
			_p.chartType = val.fld_type;
			var sortCols = "";
			var sortDirs = "";
			var first = true;
			ctrl.store.sorters.each(function(item, idx, len) {
				if (!first) {
					sortCols += ",";
					sortDirs += ",";
				}
				sortCols += item.property;
				sortDirs += item.direction || "ASC";
				first = false;
			}, this);

			_p[Dnet.requestParam.SORT] = sortCols;
			_p[Dnet.requestParam.SENSE] = sortDirs;
			_p.xField = val.fld_x;
			_p.yField = val.fld_y;
			_p.title = val.fld_title;

			// _p[Dnet.requestParam.EXPORT_INFO] = Ext.encode(_exp);

			var opts = "adress=yes, width=710, height=450,"
					+ "scrollbars=yes, resizable=yes,menubar=yes";
			var v = window.open(url + Ext.urlEncode(_p), '', opts);
			v.focus();
			wdw.close();
		}
	}

});
