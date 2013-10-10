/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define('dnet.core.dc.tools.DcSortItemSelector', {
	extend : 'Ext.ux.form.ItemSelector',
	alias : [ 'widget.dcsortitemselectorfield' ],
	buttons : [ 'top', 'up', 'add', 'remove', 'down', 'bottom', 'desc' ],

	buttonsText : {
		top : "Move to Top",
		up : "Move Up",
		add : "Add to Selected",
		remove : "Remove from Selected",
		down : "Move Down",
		bottom : "Move to Bottom",
		desc : "Change sort sense(ASC/DESC)"
	},

	/**
	 * Sort descending button handler
	 */
	onDescBtnClick : function() {

		var list = this.toField.boundList;
		var store = list.getStore();
		var selected = this.getSelections(list);

		var len = selected.length;
		var max = store.getCount();
		var selection = null;
		var index = null;

		store.suspendEvents();

		for ( var i = 0; i < len; ++i) {
			selection = selected[i];
			if (selection.data.sense == "DESC") {
				selection.data.sense = "";
				selection.data.text2 = selection.data.text;
			} else {
				selection.data.sense = "DESC";
				selection.data.text2 = selection.data.text + "-DESC";
			}
		}
		store.resumeEvents();
		list.refresh();
	},

	/**
	 * Remove sort field button handler
	 */
	onRemoveBtnClick : function() {
		var me = this;
		var toList = me.toField.boundList;
		var selected = this.getSelections(toList), len = selected.length;
		for ( var i = 0; i < len; ++i) {
			selection = selected[i];
			selection.data.sense = "";
			selection.data.text2 = selection.data.text;
		}
		toList.getStore().remove(selected);
		this.fromField.boundList.getStore().add(selected);
	}

});

Ext.define("dnet.core.dc.tools.DcSortWindow", {
	extend : "Ext.window.Window",

	/**
	 * 
	 * @type dnet.core.dc.AbstractDcvGrid
	 */
	_grid_ : null,
	_selectorId_ : null,

	initComponent : function(config) {

		var btn = Ext.create('Ext.Button', {
			text : Dnet.translate("dcvgrid", "sort_run"),
			scope : this,
			handler : this.executeTask
		});

		var avlCol = [];
		this._grid_._columns_.each(function(item, idx, len) {
			avlCol[avlCol.length] = [ item.name, item.header, '',
					item.header + '' ]
		})

		var selCol = [];
		this._grid_.store.sorters.each(function(item, idx, len) {
			selCol[selCol.length] = [ item.property ]
			if (item.direction == "DESC") {
				for ( var j = 0; j < avlCol.length; j++) {
					var e = avlCol[j];
					if (e[0] == item.property) {
						e[2] = "DESC";
						e[3] = e[1] + "-DESC";
					}
				}
			}
		})
		var ds = Ext.create('Ext.data.ArrayStore', {
			data : avlCol,
			fields : [ 'value', 'text', 'sense', 'text2' ],
			sortInfo : {
				field : 'text',
				direction : 'ASC'
			}
		});

		ds.sort();
		this._selectorId_ = Ext.id();
		var cfg = {
			title : Dnet.translate("dcvgrid", "sort_title"),
			border : true,
			width : 350,
			height : 300,
			resizable : true,
			closable : true,
			constrain : true,
			buttonAlign : "center",
			modal : true,
			layout : "fit",
			items : [ {
				xtype : 'dcsortitemselectorfield',
				id : this._selectorId_,
				store : ds,
				displayField : 'text2',
				valueField : 'value',
				value : selCol,
				msgTarget : 'top'
			} ],
			buttons : [ btn ]
		};

		Ext.apply(cfg, config);
		Ext.apply(this, cfg);
		this.callParent(arguments);
	},

	getSelector : function() {
		return Ext.getCmp(this._selectorId_);
	},

	executeTask : function() {
		var s = this.getSelector();
		var sorts = s.toField.boundList.store.data.items;
		var len = sorts.length;
		var store = this._grid_.store;
		var newsort = [];
		store.sorters.clear();
		for ( var i = 0; i < len; i++) {
			newsort.push({
				property : sorts[i].data.value,
				direction : sorts[i].data.sense || "ASC"
			})
		}
		this._grid_._controller_.store.sort(newsort);
		this.close();
	}

});
