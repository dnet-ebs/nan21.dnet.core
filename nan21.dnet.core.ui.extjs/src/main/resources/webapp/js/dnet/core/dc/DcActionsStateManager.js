/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Singleton class used to implement the state management rules for the
 * registered actions.
 */
dnet.core.dc.DcActionsStateManager = {

	applyStates : function(dc) {
		// console.log("DcActionsStateManager.applyStates dc = "
		// +dc.$className);
		var flags = dc.dcState;
		var names = dc.actionNames;
		
		if ((flags.hasParent && flags.parentIsNull)
				|| (flags.hasParent && flags.parentIsNew)) {
			this.disableAll(dc, names);
			return;
		}
		for ( var i = 0, l = names.length; i < l; i++) {
			var n = names[i];
			var an = "do" + n;
			if (dc.commands[an].locked === true) {
				dc.actions[an].setDisabled(true);
			} else {
				var b = this["_is" + n + "Disabled"](flags);
				dc.actions[an].setDisabled(b);
			}
		}
	},

	disableAll : function(dc, names) {
		for ( var i = 0, l = names.length; i < l; i++) {
			var n = names[i];
			dc.actions["do" + n].setDisabled(true);
		}
	},

	/* public helpers */

	isSaveEnabled : function(dc) {
		return this._isSaveEnabled(dc.dcState);
	},

	isSaveDisabled : function(dc) {
		return !this.isSaveEnabled(dc);
	},

	isCancelEnabled : function(dc) {
		return this._isCancelEnabled(dc.dcState);
	},

	isCancelDisabled : function(dc) {
		return !this.isCancelEnabled(dc);
	},

	isQueryDisabled : function(dc) {
		return this._isQueryDisabled(dc.dcState);
	},

	isQueryEnabled : function(dc) {
		return !this.isQueryDisabled(dc);
	},

	isNewDisabled : function(dc) {
		return this._isNewDisabled(dc.dcState);
	},

	isNewEnabled : function(dc) {
		return !this.isNewDisabled(dc);
	},

	isCopyDisabled : function(dc) {
		return this._isCopyDisabled(dc.dcState);
	},

	isCopyEnabled : function(dc) {
		return !this.isCopyDisabled(dc);
	},

	isDeleteDisabled : function(dc) {
		return this._isDeleteDisabled(dc.dcState);
	},

	isDeleteEnabled : function(dc) {
		return !this.isDeleteDisabled(dc);
	},

	isPrevRecDisabled : function(dc) {
		return this._isPrevRecDisabled(dc.dcState);
	},

	isPrevRecEnabled : function(dc) {
		return !this.isPrevRecDisabled(dc);
	},

	isNextRecDisabled : function(dc) {
		return this._isNextRecDisabled(dc.dcState);
	},

	isNextRecEnabled : function(dc) {
		return !this.isNextRecDisabled(dc);
	},

	isReloadRecDisabled : function(dc) {
		return this._isReloadRecDisabled(dc.dcState);
	},

	isReloadRecEnabled : function(dc) {
		return !this.isReloadRecDisabled(dc);
	},

	isEditOutDisabled : function(dc) {
		return this._isEditOutDisabled(dc.dcState);
	},

	isEditOutEnabled : function(dc) {
		return !this.isEditOutDisabled(dc);
	},

	/* private helpers - decision makers implementation */

	_isQueryDisabled : function(flags) {
		return (flags.isDirty || (flags.hasParent && (flags.parentIsNull
				|| flags.parentIsNew || flags.parentIsDirty)));
	},

	_isQueryEnabled : function(flags) {
		return !this._isQueryDisabled(flags);
	},

	_isClearQueryDisabled : function(flags) {
		return true;
	},

	_isClearQueryEnabled : function(flags) {
		return !this._isClearQueryDisabled(flags);
	},

	_isNewDisabled : function(flags) {
		return ((flags.isCurrentRecordDirty && !flags.multiEdit)
				|| (flags.hasParent && (flags.parentIsNull || flags.parentIsNew || flags.parentIsDirty))
				|| flags.isAnyChildDirty || flags.isReadOnly);
	},

	_isNewEnabled : function(flags) {
		return !this._isNewDisabled(flags);
	},

	_isCopyDisabled : function(flags) {
		return (flags.isReadOnly || flags.recordIsNull
				|| flags.selectedRecordsLength != 1 || flags.storeCount == 0
				|| flags.isAnyChildDirty || (flags.isCurrentRecordDirty && !flags.multiEdit));
	},

	_isCopyEnabled : function(flags) {
		return !this._isCopyDisabled(flags);
	},

	_isSaveEnabled : function(flags) {
		return (!flags.isReadOnly && (flags.isCurrentRecordDirty || flags.isStoreDirty));
	},
	_isSaveDisabled : function(flags) {
		return !this._isSaveEnabled(flags);
	},

	_isCancelEnabled : function(flags) {
		return (flags.isDirty);
	},

	_isCancelDisabled : function(flags) {
		return !this._isCancelEnabled(flags);
	},

	_isDeleteDisabled : function(flags) {
		return (flags.isReadOnly || flags.selectedRecordsLength == 0
				|| flags.isAnyChildDirty || flags.storeCount == 0 || (flags.isCurrentRecordDirty && !flags.multiEdit));
	},

	_isDeleteEnabled : function(flags) {
		return !this._isDeleteDisabled(flags);
	},

	_isPrevRecDisabled : function(flags) {
		return (flags.storeCount == 0
				|| (flags.isCurrentRecordDirty && !flags.multiEdit) || flags.isAnyChildDirty);
	},

	_isPrevRecEnabled : function(flags) {
		return !this._isPrevRecDisabled(flags);
	},

	_isNextRecDisabled : function(flags) {
		return (flags.storeCount == 0
				|| (flags.isCurrentRecordDirty && !flags.multiEdit) || flags.isAnyChildDirty); // and
	},

	_isNextRecEnabled : function(flags) {
		return !this._isNextRecDisabled(flags);
	},

	_isReloadRecDisabled : function(flags) {
		return (flags.storeCount == 0 || (flags.isCurrentRecordDirty));
	},

	_isReloadRecEnabled : function(flags) {
		return !this._isReloadRecDisabled(flags);
	},

	_isEditInEnabled : function(flags) {
		!this._isEditInDisabled(flags);
	},
	_isEditInDisabled : function(flags) {
		return flags.recordIsNull;
	},

	_isEditOutDisabled : function(flags) {
		return (flags.isAnyChildDirty || (flags.isCurrentRecordDirty && !flags.multiEdit));
	},

	_isEditOutEnabled : function(flags) {
		return !this._isEditOutDisabled(flags);
	}

};