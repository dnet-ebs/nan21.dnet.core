/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * General purpose templates.
 * 
 * @type
 */
dnet.core.base.TemplateRepository = {

	/**
	 * Application footer displayed in main page
	 * 
	 * @type
	 */
	APP_FOOTER : "<div style='width:100%;'>"
			+ "<span style='float:left;'> {product.name} &copy "
			+ (new Date()).getFullYear() + " {product.vendor} </span>"
			+ "</div>",

	/**
	 * About box content
	 * 
	 * @type
	 */
	APP_ABOUT : "<table width='100%' border='0' align=center cellpadding='0' cellspacing='0' style='font-family: arial, sans-serif;line-height: 100%;font-size:10pt; '>"
			+ "<tr><td><h2 style='text-align: center;font-family: arial, sans-serif;font-size:16pt;margin-top: 20px;' > {product.name} </h2> </td></tr>"
			+ "<tr><td style='text-align: center;'>"
			+ "  <h5 style='text-align: center;font-family: verdana, sans-serif;font-size:10pt;' >{product.description}</h5>"
			+ "  </td></tr>"
			+ "<tr><td><h5 style='text-align: center;font-family: arial, sans-serif;font-size:10pt; ' > Version: {product.version} </h5> </td></tr>"
			+ " <tr><td  style=' text-align: center; '> {product.url} </td> </tr>"
			+ " <tr><td  style=' text-align: center; '><br> &copy; "
			+ (new Date()).getFullYear()
			+ " {product.vendor} </td> </tr>"
			+ "  </td></tr>" + "<tr><td></td></tr></table>",

	/**
	 * Return a compiled template
	 * 
	 * @param {}
	 *            t - template name
	 * @return {}
	 */
	get : function(t) {
		return new Ext.Template(t, {
			compiled : true
		});
	}
};
