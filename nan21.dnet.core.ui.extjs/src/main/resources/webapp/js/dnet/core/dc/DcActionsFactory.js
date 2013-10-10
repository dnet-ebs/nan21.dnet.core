/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Factory class used to create the Ext.Action instances for the registered
 * action names. To create other actions register them here, create a command
 * executor and implement the state management rules in DcActionStateManager.
 */
dnet.core.dc.DcActionsFactory = {

	RUN_QUERY : "Query",
	CLEAR_QUERY : "ClearQuery",
	CREATE : "New",
	COPY : "Copy",
	SAVE : "Save",
	DELETE : "Delete",
	CANCEL : "Cancel",
	EDIT_IN : "EditIn",
	EDIT_OUT : "EditOut",
	PREV_REC : "PrevRec",
	NEXT_REC : "NextRec",
	RELOAD_REC : "ReloadRec",

	BUTTON_UI : "default",
	
	/**
	 * List with the names of the registered actions. See the appropriate create
	 * method for the meaning of each of the actions.
	 */
	actionNames : function() {
		return [ this.RUN_QUERY, this.CLEAR_QUERY, this.CREATE, this.COPY,
				this.SAVE, this.DELETE, this.CANCEL, this.EDIT_IN,
				this.EDIT_OUT, this.PREV_REC, this.NEXT_REC, this.RELOAD_REC ];
	},

	/**
	 * Create an object of actions for the given data-control and list of action
	 * names.
	 */
	createActions : function(dc, names) {
		var result = {};
		for ( var i = 0, l = names.length; i < l; i++) {
			var n = names[i];
			result["do" + n] = this["create" + n + "Action"](dc);
		}
		return result;
	},

	/**
	 * Create the action to execute a query.
	 */
	createQueryAction : function(dc) {
		return new Ext.Action({
			name : "doQuery",
			ui: this.BUTTON_UI,
			iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-fetch"
					: null,
			disabled : false,
			text : Dnet.translate("tlbitem", "load__lbl"),
			tooltip : Dnet.translate("tlbitem", "load__tlp") + " | "
					+ Dnet.keyBindingToString(Dnet.keyBindings.dc.doQuery),
			scope : dc,			
			handler : dc.doQuery
		});
	},

	/**
	 * Create the action to clear the filter values.
	 */
	createClearQueryAction : function(dc) {
		return new Ext.Action(
				{
					name : "doClearQuery",
					ui: this.BUTTON_UI,
					iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-fetch"
							: null,
					disabled : false,
					text : Dnet.translate("tlbitem", "clearquery__lbl"),
					tooltip : Dnet.translate("tlbitem", "clearquery__tlp")
							+ " | "
							+ Dnet
									.keyBindingToString(Dnet.keyBindings.dc.doClearQuery),
					scope : dc,
					handler : dc.doClearQuery
				});
	},

	/**
	 * Create the action to create a new record.
	 */
	createNewAction : function(dc) {
		return new Ext.Action({
			name : "doNew",
			ui: this.BUTTON_UI,
			iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-new"
					: null,
			disabled : false,
			text : Dnet.translate("tlbitem", "new__lbl"),
			tooltip : Dnet.translate("tlbitem", "new__tlp") + " | "
					+ Dnet.keyBindingToString(Dnet.keyBindings.dc.doNew),
			scope : dc,
			handler : dc.doNew
		});
	},

	/**
	 * Create the action to copy a record in client.
	 */
	createCopyAction : function(dc) {
		return new Ext.Action({
			name : "doCopy",
			ui: this.BUTTON_UI,
			iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-copy"
					: null,
			disabled : true,
			text : Dnet.translate("tlbitem", "copy__lbl"),
			tooltip : Dnet.translate("tlbitem", "copy__tlp") + " | "
					+ Dnet.keyBindingToString(Dnet.keyBindings.dc.doCopy),
			scope : dc,
			handler : dc.doCopy
		});
	},

	/**
	 * Create the action to save changes.
	 */
	createSaveAction : function(dc) {
		return new Ext.Action({
			name : "doSave",
			ui: this.BUTTON_UI,
			iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-save"
					: null,
			disabled : true,
			text : Dnet.translate("tlbitem", "save__lbl"),
			tooltip : Dnet.translate("tlbitem", "save__tlp") + " | "
					+ Dnet.keyBindingToString(Dnet.keyBindings.dc.doSave),
			scope : dc,
			handler : dc.doSave
		});
	},

	/**
	 * Create the action to delete records.
	 */
	createDeleteAction : function(dc) {
		return new Ext.Action(
				{
					name : "deleteSelected",
					ui: this.BUTTON_UI,
					iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-delete"
							: null,
					disabled : true,
					text : Dnet.translate("tlbitem", "delete_selected__lbl"),
					tooltip : Dnet.translate("tlbitem", "delete_selected__tlp")
							+ " | "
							+ Dnet
									.keyBindingToString(Dnet.keyBindings.dc.doDelete),
					scope : dc,
					handler : dc.doDelete
				});
	},

	/**
	 * Create the action to cancel changes made to the data-model.
	 */
	createCancelAction : function(dc) {
		return new Ext.Action(
				{
					name : "doCancel",
					ui: this.BUTTON_UI,
					iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-rollback"
							: null,
					disabled : true,
					text : Dnet.translate("tlbitem", "cancel__lbl"),
					tooltip : Dnet.translate("tlbitem", "cancel__tlp")
							+ " | "
							+ Dnet
									.keyBindingToString(Dnet.keyBindings.dc.doCancel),
					scope : dc,
					handler : dc.doCancel
				});
	},

	/**
	 * Create the action to enter edit-mode.
	 */
	createEditInAction : function(dc) {
		return new Ext.Action({
			name : "doEditIn",
			ui: this.BUTTON_UI,
			iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-edit"
					: null,
			disabled : true,
			text : Dnet.translate("tlbitem", "edit__lbl"),
			tooltip : Dnet.translate("tlbitem", "edit__tlp") + " | "
					+ Dnet.keyBindingToString(Dnet.keyBindings.dc.doEditIn),
			scope : dc,
			handler : dc.doEditIn
		});
	},

	/**
	 * Create the action to exit edit-mode.
	 */
	createEditOutAction : function(dc) {
		return new Ext.Action({
			name : "doEditOut",
			ui: this.BUTTON_UI,
			iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-back"
					: null,
			disabled : false,
			text : Dnet.translate("tlbitem", "back__lbl"),
			tooltip : Dnet.translate("tlbitem", "back__tlp") + " | "
					+ Dnet.keyBindingToString(Dnet.keyBindings.dc.doEditOut),
			scope : dc,
			handler : dc.doEditOut
		});
	},

	/**
	 * Create the action to load the previous available record in store as
	 * current record.
	 */
	createPrevRecAction : function(dc) {
		return new Ext.Action(
				{
					name : "doPrevRec",
					ui: this.BUTTON_UI,
					iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-previous"
							: null,
					disabled : false,
					text : "<",
					tooltip : Dnet.translate("tlbitem", "prev_rec__tlp")
							+ " | "
							+ Dnet
									.keyBindingToString(Dnet.keyBindings.dc.prevRec),
					scope : dc,
					handler : dc.setPreviousAsCurrent
				});
	},

	/**
	 * Create the action to load the next available record in store as current
	 * record.
	 */
	createNextRecAction : function(dc) {
		return new Ext.Action({
			name : "doNextRec",
			ui: this.BUTTON_UI,
			iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-next"
					: null,
			disabled : false,
			text : ">",
			tooltip : Dnet.translate("tlbitem", "next_rec__tlp") + " | "
					+ Dnet.keyBindingToString(Dnet.keyBindings.dc.nextRec),
			scope : dc,
			handler : dc.setNextAsCurrent
		});
	},

	/**
	 * Create the action to reload the current record from server.
	 */
	createReloadRecAction : function(dc) {
		return new Ext.Action(
				{
					name : "doReloadRec",
					ui: this.BUTTON_UI,
					iconCls : (Dnet.viewConfig.USE_TOOLBAR_ICONS) ? "icon-action-refresh"
							: null,
					disabled : false,
					text : Dnet.translate("tlbitem", "reload_rec__lbl"),
					tooltip : Dnet.translate("tlbitem", "reload_rec__tlp"),
					scope : dc,
					handler : dc.reloadCurrentRecord
				});
	}
};