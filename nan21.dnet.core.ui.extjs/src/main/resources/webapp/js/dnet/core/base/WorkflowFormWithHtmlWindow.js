/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
Ext.define("dnet.core.base.WorkflowFormWithHtmlWindow", {
	extend : "Ext.Window",

	constructor : function(config) {
		var cfg = {
			border : true,
			width : 500,
			height : 500,
			resizable : true,
			closable : true,
			constrain : true,
			layout : "fit",
			modal : true,
			buttonAlign : "center",
			buttons : [ {
				xtype : "button",
				text : Dnet.translate("tlbitem", "save__lbl"),
				scope : this,
				handler : this.doSave
			} ]
		};
		var resolved = false;
		var sh = null;
		var eh = null;
		if (config._wfConfig_.type == "startform") {
			var submitUrl = Dnet.wfProcessInstanceAPI().start;
			sh = "<form id='dnet-workflow-form' action='";
			sh += submitUrl;
			sh += "'>";
			sh += "<input type='hidden' name='processDefinitionId' value='";
			sh += config._wfConfig_.processDefinitionId;
			sh += "'>";
			sh += "<input type='hidden' name='processDefinitionKey' value=''>";
			sh += "<input type='hidden' name='businessKey' value=''>";
			eh = "</form>";
			resolved = true;
		}

		if (config._wfConfig_.type == "taskform") {
			var submitUrl = Dnet.wfTaskAPI(config._wfConfig_.taskId).complete;
			sh = "<form id='dnet-workflow-form' action='" + submitUrl + "'>";
			eh = "</form>";
			resolved = true;
		}

		if (!resolved) {
			alert ("Invalid value for _wfConfig_.type"
					+ " in WorkflowFormWithHtmlWindow.");
		}
		config.html = sh + config.html + eh;
		Ext.apply(cfg, config);
		dnet.core.base.WorkflowFormWithHtmlWindow.superclass.constructor.call(
				this, cfg);

	},

	initComponent : function() {
		dnet.core.base.WorkflowFormWithHtmlWindow.superclass.initComponent
				.call(this);
	},

	getButton : function() {
		return this.items.get(0).buttons[0];
	},

	doSave : function(btn, evnt) {
		var frm = document.getElementById("dnet-workflow-form");
		var p = {};
		var elements = frm.elements;
		var len = elements.length;
		for ( var i = 0; i < len; i++) {
			p[elements[i].name] = elements[i].value;
		}
		Ext.Ajax.request({
			url : frm.action,
			method : "POST",
			params : p,
			success : this.onSaveSuccess,
			failure : this.onSaveFailure,
			scope : this
		});
	},

	onSaveSuccess : function(response, options) {
		this.close();
	},

	onSaveFailure : function(response, options) {
		Ext.MessageBox.hide();
		var msg, withDetails = false;
		if (response.responseText) {
			if (response.responseText.length > 2000) {
				msg = response.responseText.substr(0, 2000);
				withDetails = true;
			} else {
				msg = response.responseText;
			}
		} else {
			msg = "No response received from server.";
		}
		var alertCfg = {
			msg : msg,
			scope : this,
			icon : Ext.MessageBox.ERROR,
			buttons : Ext.MessageBox.OK
		}
		if (withDetails) {
			alertCfg.buttons['cancel'] = 'Details';
			alertCfg['detailedMessage'] = response.responseText;
		}
		Ext.Msg.show(alertCfg);
	}
});