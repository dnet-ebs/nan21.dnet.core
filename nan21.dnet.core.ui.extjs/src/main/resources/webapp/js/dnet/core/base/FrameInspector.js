/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Utility to display information about an application frame.
 */

Ext
		.define(
				"dnet.core.base.FrameInspector",
				{
					extend : "Ext.Window",

					title : Dnet
							.translate("appmenuitem", "frameInspector__lbl"),
					border : true,
					height : 300,
					width : 500,
					layout : 'fit',
					resizable : true,

					bodyPadding : 20,
					closable : true,
					constrain : true,
					buttonAlign : "center",
					modal : true,
					autoScroll : true,

					initComponent : function() {

						var _t = [
								'<h1 style="padding-top: 10px;padding-bottom: 10px;">Frame: {frameName}</h1>',
								'<p style=" padding-bottom: 5px;"> Class: {frameFqn} </p>',
								'<h3 style="padding-top: 10px;padding-bottom: 10px;"><u>Data-controls</u></h3>',
								'<tpl for="dc">',
								'<p style=" padding-bottom: 5px;">{#}. <i>Key:</i> {alias} <br> <i>Class:</i> {name} <br>',
								'<i>Data-source: </i> ',
								'<a href="#" onclick="javascript:var w=window.open( Dnet.dsAPI(\'{dsAlias}\',\'html\').info ,\'DataSourceInfo\',\'width=600,height=500,scrollbars=yes\');w.focus();" >{ds}</a>',
								'</p></tpl>',
								'<h3 style="padding-top: 10px;padding-bottom: 10px;"><u>Toolbars</u></h3> ',
								'<tpl for="tlb">',
								'<p style=" padding-bottom: 5px;">{#}.  <i>Key:</i>  {alias}, <i>Title:</i> `{title}`</p></tpl>',
								'<h3 style="padding-top: 10px;padding-bottom: 10px;"><u>Assignments</u></h3> ',
								'<p style=" padding-bottom: 5px;">Assignment components are visible only after they have been opened in the frame.</p>',
								'<tpl for="asgn">',
								'<p style=" padding-bottom: 5px;">{#}. <i>Title:</i> `{title}` <br> <i>Class:</i>  {className}</p></tpl>' ];

						this.tpl = new Ext.XTemplate(_t);

						var frame = getApplication().getActiveFrameInstance();
						if (frame) {
							var dc = null, dcs = [], tlb = null, tlbs = [], asgns = [];
							if (frame._dcs_ != null) {
								frame._dcs_.each(function(item, idx, length) {
									var dc = {
										name : item.$className,
										alias : item._instanceKey_,
										ds : item.recordModel,
										dsAlias : item.recordModel.substring(
												item.recordModel
														.lastIndexOf('.') + 1,
												item.recordModel.length)
									};
									dcs[dcs.length] = dc;
								}, this);
							}

							if (frame._tlbs_ != null) {
								frame._tlbs_
										.eachKey(
												function(key, item) {
													var tlb = {
														alias : key,
														title : ((frame._tlbtitles_) ? frame._tlbtitles_
																.get(key)
																|| "-"
																: "-")
													};
													tlbs[tlbs.length] = tlb;
												}, this);
							}

							if (frame._asgns_ != null) {
								frame._asgns_.eachKey(function(key, item) {
									var asgn = {
										className : item.className,
										title : item.title
									}
									asgns[asgns.length] = asgn;
								}, this);
							}

							this.data = {
								frameFqn : frame.$className,
								frameName : frame.$className.substring(
										frame.$className.lastIndexOf('.') + 1,
										frame.$className.length),
								dc : dcs,
								tlb : tlbs,
								asgn : asgns
							};
						} else {
							this.html = "<br><br>Frame inspector works only for "
									+ " application frames not for the home panel !";
						}
						this.callParent(arguments);
					}

				});