<%@page
	import="net.nan21.dnet.core.web.controller.ui.extjs.AbstractUiExtjsController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="net.nan21.dnet.core.api.Constants, java.util.Map"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${title}</title>
<script type="text/javascript">
		__ITEM__ = "${item}";  	
		__LANGUAGE__ = "${shortLanguage}";
		__THEME__ = "${theme}"; 
		${constantsJsFragment}
	</script>
<script type="text/javascript" src="${ urlUiExtjsCore }/js/globals.js"></script>
<script type="text/javascript">
	  checkAndStart(); 
	  __checkAuthToken();
	</script>
<!-- Extjs 

<link rel="stylesheet" type="text/css"
	href="${urlUiExtjsThemes}/resources/ext-theme-${theme}/ext-theme-${theme}-all.css" /> -->
	
	
<link rel="stylesheet" type="text/css"
	href="http://localhost/my-costm-theme/my-custom-theme-all.css" />
</head>
<body>

	<%@ include file="_loading_mask.jspf"%>

	<script type="text/javascript">
    
    	if(document &&  document.getElementById('n21-loading-msg')) {
        	document.getElementById('n21-loading-msg').innerHTML = 'Loading...';
        }
	</script>
	<c:if test="${sysCfg_workingMode == 'dev'}">
		<%@ include file="_includes_dev.jspf"%>
	</c:if>
	<c:if test="${sysCfg_workingMode == 'prod'}">
		<%@ include file="_includes_prod.jspf"%>
	</c:if>

	<%@ include file="_dnet_params.jspf"%>

	${extensions}

	<script type="text/javascript">
	
  		if(document && document.getElementById('n21-loading-msg')) {
  	  		document.getElementById('n21-loading-msg').innerHTML = Dnet.translate("msg", "initialize")+' ${item}...';
  	  	}
	</script>

	<script>

    Ext.onReady(function(){

    	<%@ include file="_on_ready.jspf" %>

		dnet.core.base.Session.user.code = '${user.code}';
		dnet.core.base.Session.user.loginName = '${user.loginName}';
		dnet.core.base.Session.user.name = '${user.name}';
		dnet.core.base.Session.user.systemUser = ${user.systemUser};
		
		dnet.core.base.Session.client.id = '${user.client.id}';
		dnet.core.base.Session.client.code = '${user.client.code}';
		dnet.core.base.Session.client.name = '${user.client.name}';
		dnet.core.base.Session.locked = false;
		dnet.core.base.Session.roles = [${userRolesStr}];
		
		${extensionsContent}
        
      	var tr = dnet.core.base.TemplateRepository;

		__application__ = dnet.core.base.Application;
		__application__.menu = new dnet.core.base.ApplicationMenu({ region:"north" });
		__application__.view = new Ext.Viewport({
			 layout:'card', 
			 activeItem:0 ,
			 forceLayout:true,
			 items:[
				  { html:"" } ,
			   	  {  padding:0,
			    	layout:'border',
				    forceLayout:true,
				    items:[
				   		{	region:"center",
					   		enableTabScroll : true,
					   		xtype:"tabpanel",
						   	deferredRender:false,
						   	activeTab:0,
						   	plain : true,
						   	cls: "dnet-home",
						   	plugins: Ext.create('Ext.ux.TabCloseMenu', {}),
				   			id:"dnet-application-view-body",
				    	    items:[{	
				    	    		xtype:"dnetHomePanel",
									id:"dnet-application-view-home"
								 }    
							]
						},				    	        
				    	__application__.menu 				    	          								    	        
				    ]
				}   
				 
			]			 			  
      	});   
		
		__application__.run();
	 
		var map = new Ext.util.KeyMap({
			target : document.body,
			eventName : 'keydown',
			processEvent : function(event, source, options) {
				return event;
			},
			binding : [ Ext.apply(Dnet.keyBindings.app.gotoNextTab, {
				fn : function(keyCode, e) {
					e.stopEvent();
					getApplication().gotoNextTab();
				},
				scope : this
			}),Ext.apply(Dnet.keyBindings.dc.doClearQuery, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doClearQuery();
					}
				},
				scope : this
			}),Ext.apply(Dnet.keyBindings.dc.doEnterQuery, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doEnterQuery();
					}
				},
				scope : this
			}),Ext.apply(Dnet.keyBindings.dc.doQuery, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doQuery();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doNew, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doNew();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doCancel, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doCancel();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doSave, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doSave();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doCopy, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doCopy();
						ctrl.doEditIn();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doEditIn, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doEditIn();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.doEditOut, {
				fn : function(keyCode, e) {
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.doEditOut();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.nextRec, {
				fn : function(keyCode, e) {
					console.log("indexMain.nextRec");
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.setNextAsCurrent();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.prevRec, {
				fn : function(keyCode, e) {
					console.log("indexMain.prevRec");
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.setPreviousAsCurrent();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.nextPage, {
				fn : function(keyCode, e) {
					console.log("indexMain.nextPage");
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.nextPage();
					}
				},
				scope : this
			}), Ext.apply(Dnet.keyBindings.dc.prevPage, {
				fn : function(keyCode, e) {
					console.log("indexMain.prevPage");
					e.stopEvent();
					var frame = __application__.getActiveFrameInstance();
					if (frame) {
						ctrl = frame._getRootDc_();
						ctrl.previousPage();
					}
				},
				scope : this
			}) ]
		});
		 
      
    });

    <%@ include file="_loading_mask_remove.jspf" %> 
    
    	var dont_confirm_leave = 0;  
        var leave_message = 'You sure you want to leave?'
        function goodbye(e) 
        {
            if(dont_confirm_leave!==1)
            {
                if(!e) e = window.event;                
                e.cancelBubble = true;
                e.returnValue = leave_message;
                if (e.stopPropagation) 
                {
                    e.stopPropagation();
                    e.preventDefault();
                }
                return leave_message;
            }
        }   
    window.onbeforeunload=goodbye;
    
    
  </script>
</body>
</html>