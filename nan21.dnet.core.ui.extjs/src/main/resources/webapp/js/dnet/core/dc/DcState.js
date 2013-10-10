/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Collection of flags to describe the state of a data-control.
 * 
 */
Ext.define("dnet.core.dc.DcState", {

	mixins : {
		observable : 'Ext.util.Observable'
	},

	isReadOnly : false,

	isStoreDirty : false,

	isDirty : false,

	isAnyChildDirty : false,

	recordIsNull : true,

	isCurrentRecordDirty : false,

	multiEdit : false,

	selectedRecordsLength : 0,

	storeCount : 0,

	hasParent : false,

	parentIsNull : false,

	parentIsNew : false,

	parentIsReadOnly : false,

	parentIsDirty : false,

	constructor : function(config) {
		this.addEvents("flagchanged");
		this.mixins.observable.constructor.call(this);
	},

	set : function(flag, nv, silent) {
		var me = this;
		var ov = me[flag];
		// console.log("DcState, flag=" + flag + ", ov=" + ov + ", nv=" + nv);
		if (ov === undefined) {
			alert("Invalid flag name `" + flag + "` provided to DcState! ");
		}
		if (ov !== nv) {
			me[flag] = nv;
			if (silent !== true) {
				me.fireEvent("flagchanged", {
					flag : flag,
					ov : ov,
					nv : nv
				});
			}
			return true;
		}
		return false;
	},

	run : function(dc) {
		var me = this;
		var runManager = false;

		var flags = {
			isReadOnly : dc.isReadOnly(),
			isStoreDirty : dc.isStoreDirty(),
			isDirty : dc.isDirty(),
			isAnyChildDirty : dc.isAnyChildDirty(),
			recordIsNull : Ext.isEmpty(dc.record),
			isCurrentRecordDirty : dc.isCurrentRecordDirty(),
			multiEdit : dc.multiEdit,
			selectedRecordsLength : dc.getSelectedRecords().length,
			storeCount : dc.store.getCount()
		};

		flags.hasParent = (dc.getParent() != null);

		if (flags.hasParent) {

			var _parent = dc.getParent();

			if (!flags.parentIsNull && _parent.isReadOnly()) {
				flags.parentIsReadOnly = true;
			} else {
				flags.parentIsReadOnly = false;
			}

			if (!_parent.getRecord()) {
				flags.parentIsNull = true;
			} else {
				flags.parentIsNull = false;

				if (_parent.record.phantom) {
					flags.parentIsNew = true;
				} else {
					flags.parentIsNew = false;

					if (_parent.isCurrentRecordDirty()) {
						flags.parentIsDirty = true;
					} else {
						flags.parentIsDirty = false;
					}
				}
			}
		}

		for ( var flag in flags) {
			if (this.set(flag, flags[flag], true) == true) {
				runManager = true;
			}
		}

		if (runManager) {
			// console.log("DcState.run for " + dc.$className);
			dnet.core.dc.DcActionsStateManager.applyStates(dc);
		}
		return runManager;
	},

	isRecordChangeAllowed : function() {
		if (this.multiEdit) {
			return !this.isAnyChildDirty;
		} else {
			return !(this.isCurrentRecordDirty || this.isAnyChildDirty);
		}
	}

});