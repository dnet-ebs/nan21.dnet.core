<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${title}</title>
<script type="text/javascript">
	__ITEM__ = "${item}";
	__LANGUAGE__ = "${shortLanguage}";
	__THEME__ = "${theme}";
</script>
<script type="text/javascript">${constantsJsFragment}</script>
<script type="text/javascript" src="${urlUiExtjsCore}/js/globals.js"></script>
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
		if (document && document.getElementById('n21-loading-msg')) {
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

	<c:if test="${sysCfg_workingMode == 'dev'}">
		<c:forEach var="_include" items="${frameDependenciesTrl}">
			<script type="text/javascript" src="${_include}"></script>
		</c:forEach>
		<c:forEach var="_include" items="${frameDependenciesCmp}">
			<script type="text/javascript" src="${_include}"></script>
		</c:forEach>
	</c:if>
	<c:if test="${sysCfg_workingMode == 'prod'}">
		<script type="text/javascript"
			src="${deploymentUrl}/ui-extjs/frame/${bundle}/${shortLanguage}/${itemSimpleName}.js"></script>
		<script type="text/javascript"
			src="${deploymentUrl}/ui-extjs/frame/${bundle}/${itemSimpleName}.js"></script>
	</c:if>

	${extensions}

	<script type="text/javascript">
		if (document && document.getElementById('n21-loading-msg')) {
			document.getElementById('n21-loading-msg').innerHTML = Dnet
					.translate("msg", "initialize")
					+ ' ${item}...';
		}
	</script>

	<script>
		var theFrameInstance = null;
		var __theViewport__ = null;
		Ext.onReady(function() {
			if (getApplication().getSession().useFocusManager) {
				Ext.FocusManager.enable(true);
			}
	<%@ include file="_on_ready.jspf" %>
		var frameReports = [];

			${extensionsContent}

			var cfg = {
				layout : "fit",
				xtype : "container",
				items : [ {
					xtype : "${itemSimpleName}",
					border : false,
					_reports_ : frameReports,
					listeners : {
						afterrender : {
							fn : function(p) {
								theFrameInstance = this;
							}
						}
					}
				} ]
			};
			__theViewport__ = new Ext.Viewport(cfg);

			var map = new Ext.util.KeyMap({
				target : document.body,
				eventName : 'keydown',
				processEvent : function(event, source, options) {
					return event;
				},
				binding : [ Ext.apply(Dnet.keyBindings.dc.doClearQuery, {
					fn : function(keyCode, e) {
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doClearQuery();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.doEnterQuery, {
					fn : function(keyCode, e) {
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doEnterQuery();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.doQuery, {
					fn : function(keyCode, e) {
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doQuery();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.doNew, {
					fn : function(keyCode, e) {
						//console.log("indexFrame.doNew");
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doNew();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.doCancel, {
					fn : function(keyCode, e) {
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doCancel();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.doSave, {
					fn : function(keyCode, e) {
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doSave();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.doCopy, {
					fn : function(keyCode, e) {
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doCopy();
						ctrl.doEditIn();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.doEditIn, {
					fn : function(keyCode, e) {
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doEditIn();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.doEditOut, {
					fn : function(keyCode, e) {
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.doEditOut();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.nextRec, {
					fn : function(keyCode, e) {
						//console.log("indexFrame.nextRec");
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.setNextAsCurrent();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.prevRec, {
					fn : function(keyCode, e) {
						//console.log("indexFrame.prevRec");
						e.stopEvent();
						var ctrl = theFrameInstance._getRootDc_();
						ctrl.setPreviousAsCurrent();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.nextPage, {
					fn : function(keyCode, e) {
						//console.log("indexFrame.nextPage");
						e.stopEvent();
						theFrameInstance._getRootDc_().nextPage();
					},
					scope : this
				}), Ext.apply(Dnet.keyBindings.dc.prevPage, {
					fn : function(keyCode, e) {
						//console.log("indexFrame.prevPage");
						e.stopEvent();
						theFrameInstance._getRootDc_().previousPage();
					},
					scope : this
				}) ]
			});

		});
	<%@ include file="_loading_mask_remove.jspf" %>
		
	</script>
</body>
</html>