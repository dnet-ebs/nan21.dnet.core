/* ==================== general javascript overrides ======================== */

String.prototype.endsWith = function(suffix) {
	return this.indexOf(suffix, this.length - suffix.length) !== -1;
};
String.prototype.toFirstUpper = function() {
	return this.substring(0, 1).toUpperCase() + this.substring(1, this.length);
};
/* ==================== Extjs overrides ======================== */

/**
 * Disable autoloading.
 */
Ext.Loader.setConfig({
	enabled : false
});

/**
 * Change the clientRecordId. The default value `clientId` is in conflict with
 * our clientId field which represents the tenant-id.
 */
Ext.override(Ext.data.Model, {
	clientIdProperty : "__clientRecordId__"
});

Ext.JSON.encodeDate = function(d) {
	return Ext.Date.format(d, '"' + Dnet.MODEL_DATE_FORMAT + '"');
};

/**
 * Override page loading: - if totalCount is set check if next-page doesn't
 * overflow - check previous page isn't negative - helper filter methods
 * 
 */
Ext.override(Ext.data.Store, {

	filterAllNew : function(item) {
		return item.phantom === true;
	},

	getAllNewRecords : function() {
		return this.data.filterBy(this.filterAllNew).items;
	},

	filterUpdated : function(item) {
		return item.dirty === true && item.phantom !== true;
	},

	getModifiedRecords : function() {
		return [].concat(this.getAllNewRecords(), this.getUpdatedRecords());
	},

	rejectChanges : function() {
		this.callParent();
		this.fireEvent('changes_rejected', this);
	}

});

/**
 * Override the picker expand on down arrow key. Ignore it if CTRL/SHIFT/ALT is
 * pressed also. It conflicts with the rest of the key bindings.
 */
Ext.override(Ext.form.field.Picker, {
	onDownArrow : function(e) {
		if (e.altKey || e.shiftKey || e.ctrlKey) {
			return;
		}
		if (!this.isExpanded) {
			// Don't call expand() directly as there may be additional
			// processing involved before
			// expanding, e.g. in the case of a ComboBox query.
			this.onTriggerClick();
		}
	}
});

/**
 * Override down/up arrow navigation. Ignore it ALT is pressed also. It
 * conflicts with the rest of the key bindings.
 */
Ext.override(Ext.selection.RowModel, {

	onKeyDown : function(e) {
		if (e.altKey) {
			return;
		}
		var newRecord = this.views[0].walkRecs(e.record, 1);
		if (newRecord) {
			this.afterKeyNavigate(e, newRecord);
		}
	},

	onKeyUp : function(e) {
		if (e.altKey) {
			return;
		}
		var newRecord = this.views[0].walkRecs(e.record, -1);
		if (newRecord) {
			this.afterKeyNavigate(e, newRecord);
		}
	}

});

Ext.override(Ext.form.Basic, {
	findField : function(id) {
		return this.getFields().findBy(function(f) {
			return f.dataIndex === id || f.id === id || f.getName() === id;
		});
	}
});

//

Ext.data.validations.presenceMessage = Dnet.translate("msg", "valid_not_null");

/**
 * Disabled fields do not allow focus, so user cannot copy-paste values from
 * them. This override allows to perform enable/disable as read-only based on
 * the Dnet.viewConfig.DISABLE_AS_READONLY flag. All the field enable/disabe
 * calls are routed through these dispatcher functions.
 * 
 */
Ext.override(Ext.form.field.Base, {

	_enable_ : function() {
		if (Dnet.viewConfig.DISABLE_AS_READONLY === true) {
			if (this.readOnly === true) {
				this.setReadOnly(false);
			}
		} else {
			if (this.disabled === true) {
				this.enable();
			}
		}
	},

	_disable_ : function() {
		if (Dnet.viewConfig.DISABLE_AS_READONLY === true) {
			if (this.readOnly === false) {
				this.setReadOnly(true);
			}
		} else {
			if (this.disabled === false) {
				this.disable();
			}
		}
	},

	_setDisabled_ : function(v) {
		if (Dnet.viewConfig.DISABLE_AS_READONLY === true) {
			if (this.readOnly !== v) {
				this.setReadOnly(v);
			}

		} else {
			if (this.disabled !== v) {
				this.setDisabled(v);
			}
		}
	}

});

// TODO: check if ext 4.2.1 has some changes here

Ext.override(Ext.form.field.Text, {
	getRawValue : function() {
		var me = this, v = me.callParent();
		if (v === me.emptyText) {
			v = '';
		}
		if (this.caseRestriction && v != '') {
			if (this.caseRestriction == "uppercase") {
				v = v.toUpperCase();
			} else {
				v = v.toLowerCase();
			}
		}
		me.rawValue = v;
		return v;
	}
});

/**
 * With an LOV type of editor do not complete edit on first ENTER, just collapse
 * the picker list view
 * 
 */
Ext.override(Ext.Editor, {

	onSpecialKey : function(field, event) {
		var me = this, key = event.getKey(), complete = me.completeOnEnter
				&& key == event.ENTER, cancel = me.cancelOnEsc
				&& key == event.ESC;

		var editPrev = (key == event.UP && event.ctrlKey);
		var editNext = (key == event.DOWN && event.ctrlKey);

		complete = complete || editPrev || editNext;

		if (field._isLov_ && field.isExpanded === true) {
			complete = false;
		}
		if (complete || cancel) {
			event.stopEvent();
			// Must defer this slightly to prevent exiting edit mode before the
			// field's own
			// key nav can handle the enter key, e.g. selecting an item in a
			// combobox list
			Ext.defer(function() {
				if (complete) {
					me.completeEdit();
					var _col = field.column;

					if (field.triggerBlur) {
						// console.log("Ext.override(Ext.Editor face --------->
						// field.triggerBlur(event)");						
						if (_col && _col._dcView_) {
							_col._dcView_.getView().focus();
						}
						field.triggerBlur(event);
					}
					// TODO: move this part to the navigation
					if (editNext) {
						if (_col && _col._dcView_) {
							var sm = _col._dcView_.getSelectionModel();
							sm.selectNext();
							_col._dcView_.getPlugin("cellEditingPlugin").startEdit(
									sm.getSelection()[0], _col);
						}
					} else if (editPrev) {
						if (_col && _col._dcView_) {
							var sm = _col._dcView_.getSelectionModel();
							sm.selectPrevious();
							_col._dcView_.getPlugin("cellEditingPlugin").startEdit(
									sm.getSelection()[0], _col);
						}
					}
				} else {
					me.cancelEdit();
					if (field.triggerBlur) {
						field.triggerBlur(event);
					}
				}
			}, 10);
		}

		me.fireEvent('specialkey', me, field, event);
	}
});

Ext.override(Ext.grid.plugin.CellEditing, {

	/**
	 * Context content { grid : grid, record : record, field :
	 * columnHeader.dataIndex, value : record.get(columnHeader.dataIndex), row :
	 * view.getNode(rowIdx), column : columnHeader, rowIdx : rowIdx, colIdx :
	 * colIdx }
	 * 
	 * @param {}
	 *            context
	 * @return {Boolean}
	 */
	beforeEdit : function(context) {
		if (context.grid && context.grid.beforeEdit) {
			return context.grid.beforeEdit(context);
		}
	},

	_isEditAllowed_ : function(record, column, field) {
		if (field && field.noEdit) {
			return false;
		}
		if (field && field.noUpdate === true && !record.phantom) {
			return false;
		} else if (field && field.noInsert === true && record.phantom) {
			return false;
		}
		return true;
	},

	getEditor : function(record, column) {

		var me = this;
		if (me.grid._getCustomCellEditor_) {
			var editor = me.grid._getCustomCellEditor_(record, column);
			if (editor != null) {
				if (!this._isEditAllowed_(record, column, editor.field
						|| editor)) {
					return null;
				}

				if (!(editor instanceof Ext.grid.CellEditor)) {
					editorId = column.id + record.id;
					editor = new Ext.grid.CellEditor({
						floating : true,
						editorId : editorId,
						field : editor
					});
				}
				var editorOwner = me.grid.ownerLockable || me.grid;
				editorOwner.add(editor);
				editor.on({
					scope : me,
					specialkey : me.onSpecialKey,
					complete : me.onEditComplete,
					canceledit : me.cancelEdit
				});

				column.on('removed', me.cancelActiveEdit, me);
				editor.field["_targetRecord_"] = record;

				editor.grid = me.grid;
				// Keep upward pointer correct for each use - editors are shared
				// between locking sides
				editor.editingPlugin = me;

				return editor;
			}
		}

		var editor = this.callParent(arguments);

		var editAllowed = this._isEditAllowed_(record, column, editor.field
				|| editor);
		if (editor && !editAllowed) {
			return false;
		}

		if (editor.field.caseRestriction) {
			editor.field.fieldStyle = "text-transform:"
					+ editor.field.caseRestriction + ";";
		}

		if (editor.field) {
			if (me.grid && (me.grid._dcViewType_ == "filter-propgrid")) {
				editor.field["_targetRecord_"] = me.grid._controller_.filter;
			} else {
				editor.field["_targetRecord_"] = record;
			}

		}

		return editor;
	}

});

/**
 * When delete multiple times in an editable grid then call cancel, usually
 * throws an exception either in all.last() or in count == 0 part.
 * 
 */
Ext.override(Ext.grid.View, {
	doAdd : function(records, index) {
		var me = this;
		var nodes = me.bufferRender(records, index, true);
		var all = me.all;
		var count = all.getCount();
		if (count === 0) {
			for ( var i = 0, l = nodes.length; i < l; i++) {
				this.getNodeContainer().appendChild(nodes[i]);
			}
		} else if (index < count) {
			if (index === 0) {
				all.item(index).insertSibling(nodes, 'before', true);
			} else {
				all.item(index - 1).insertSibling(nodes, 'after', true);
			}
		} else {
			/* start my */
			var _l = all.last();
			if (_l) {
				all.last().insertSibling(nodes, 'after', true);
			}
			/* end my */
		}
		all.insert(index, nodes);
		return nodes;
	}

});