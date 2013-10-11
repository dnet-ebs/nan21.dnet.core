/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.base.WfTaskFormWindowExtjs", {
	extend : "dnet.core.base.WfAbstractFormWindowExtjs",

	_taskId_ : null,
	_taskName_ : null,
	_taskDescription_ : null,

	initComponent : function() {

		this.items = {
			xtype : "form",
			url : Dnet.wfTaskAPI(this._taskId_).complete,
			bodyPadding : 10,
			layout : 'anchor',
			fieldDefaults : {
				labelAlign : "right",
				labelWidth : 130
			},
			frame : true,
			defaults : {
				anchor : '100%'
			},
			items : this._buildForm_(),

			buttons : [
					{
						text : 'Submit',
						formBind : true,
						disabled : true,
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								Dnet.working();
								form.submit({
									success : function(form, action) {
										Ext.Msg.hide();
										Ext.Msg.alert('Success',
												"Task completed successfully");
										this.up('window').close();
									},
									failure : function(form, action) {
										Ext.Msg.hide();
										Ext.Msg.alert('Failed',
												action.response.responseText);
									},
									scope : this
								});
							}
						}
					}, {
						text : 'Reset',
						handler : function() {
							this.up('form').getForm().reset();
						}
					} ]
		}
		this.callParent(arguments);
	},

	_buildForm_ : function() {
		var fields = [];

		fields[0] = {
			xtype : "hidden",
			name : "taskId",
			value : this._taskId_
		}
		fields[1] = {
			xtype : "container",
			padding : 15,
			html : this._taskDescription_
		};

		this._buildFormFields_(fields);
		return fields;
	}

});