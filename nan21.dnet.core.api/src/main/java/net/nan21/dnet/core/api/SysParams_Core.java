/**
 * DNet eBusiness Suite
 * Copyright: 2010-2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api;

import java.util.Collection;

import net.nan21.dnet.core.api.descriptor.AbstractSysParams;
import net.nan21.dnet.core.api.descriptor.ISysParamDefinition;
import net.nan21.dnet.core.api.descriptor.SysParamDefinition;
 /*
 Set default values in application properties file:


# Application logo URL - Extjs
# Link to the company logo to be displayed in the Extjs based application header.
sysparam.CORE_LOGO_URL_EXTJS=http://dnet.nan21.net/static-demo-resources/client-logo.png

# Report logo URL
# Link to the company logo to be displayed in the reports.
sysparam.CORE_LOGO_URL_REPORT=http://dnet.nan21.net/static-demo-resources/client-logo-report.png

# Check request IP
# Check if the remote client IP of the request is the same as the one used at login time. Possible values: true, false.
sysparam.CORE_SESSION_CHECK_IP=false

# Check request user-agent
# Check if the remote client user-agent of the request is the same as the one used at login time. Possible values: true, false.
sysparam.CORE_SESSION_CHECK_USER_AGENT=false

# Css file location for HTML export
# Css used to style the HTML exports from grid.
sysparam.CORE_EXP_HTML_CSS=http://localhost:8081/dnet-ebs/extjs.themes/webapp/resources/css/export-html.css

# Template file location FreeMarker
# Default template used when printing in html format with FreeMarker.
sysparam.CORE_PRINT_HTML_TPL=

# Default language
# Default language to be used if no language preference set by user.
sysparam.CORE_DEFAULT_LANGUAGE=en

# Default theme Extjs
# Default theme to be used in Extjs based user interface if no theme preference set by user.
sysparam.CORE_DEFAULT_THEME_EXTJS=gray

# Scheduled job user
# Generic user to execute scheduled jobs
sysparam.CORE_JOB_USER=JOB_PROCESS

Declare bean in spring xml: 

<bean name="sysparams" class="net.nan21.dnet.core.api.SysParams_Core">
	<property name="defaultValues">
		<props>
			<prop key="CORE_LOGO_URL_EXTJS">${sysparam.CORE_LOGO_URL_EXTJS}</prop>
			<prop key="CORE_LOGO_URL_REPORT">${sysparam.CORE_LOGO_URL_REPORT}</prop>
			<prop key="CORE_SESSION_CHECK_IP">${sysparam.CORE_SESSION_CHECK_IP}</prop>
			<prop key="CORE_SESSION_CHECK_USER_AGENT">${sysparam.CORE_SESSION_CHECK_USER_AGENT}</prop>
			<prop key="CORE_EXP_HTML_CSS">${sysparam.CORE_EXP_HTML_CSS}</prop>
			<prop key="CORE_PRINT_HTML_TPL">${sysparam.CORE_PRINT_HTML_TPL}</prop>
			<prop key="CORE_DEFAULT_LANGUAGE">${sysparam.CORE_DEFAULT_LANGUAGE}</prop>
			<prop key="CORE_DEFAULT_THEME_EXTJS">${sysparam.CORE_DEFAULT_THEME_EXTJS}</prop>
			<prop key="CORE_JOB_USER">${sysparam.CORE_JOB_USER}</prop>
		</props>
	</property>
</bean>
 */
public class SysParams_Core extends AbstractSysParams {

	public static final String CORE_LOGO_URL_EXTJS = "CORE_LOGO_URL_EXTJS";
	public static final String CORE_LOGO_URL_REPORT = "CORE_LOGO_URL_REPORT";
	public static final String CORE_SESSION_CHECK_IP = "CORE_SESSION_CHECK_IP";
	public static final String CORE_SESSION_CHECK_USER_AGENT = "CORE_SESSION_CHECK_USER_AGENT";
	public static final String CORE_EXP_HTML_CSS = "CORE_EXP_HTML_CSS";
	public static final String CORE_PRINT_HTML_TPL = "CORE_PRINT_HTML_TPL";
	public static final String CORE_DEFAULT_LANGUAGE = "CORE_DEFAULT_LANGUAGE";
	public static final String CORE_DEFAULT_THEME_EXTJS = "CORE_DEFAULT_THEME_EXTJS";
	public static final String CORE_JOB_USER = "CORE_JOB_USER";

	protected void initParams(Collection<ISysParamDefinition> params) {
		
		params.add(new SysParamDefinition(CORE_LOGO_URL_EXTJS,
			"Application logo URL - Extjs",
			"Link to the company logo to be displayed in the Extjs based application header.",
			SysParamDefinition.TYPE_STRING,
			getDefaultValue(CORE_LOGO_URL_EXTJS), null));
		
		params.add(new SysParamDefinition(CORE_LOGO_URL_REPORT,
			"Report logo URL",
			"Link to the company logo to be displayed in the reports.",
			SysParamDefinition.TYPE_STRING,
			getDefaultValue(CORE_LOGO_URL_REPORT), null));
		
		params.add(new SysParamDefinition(CORE_SESSION_CHECK_IP,
			"Check request IP",
			"Check if the remote client IP of the request is the same as the one used at login time. Possible values: true, false.",
			SysParamDefinition.TYPE_BOOLEAN,
			getDefaultValue(CORE_SESSION_CHECK_IP), null));
		
		params.add(new SysParamDefinition(CORE_SESSION_CHECK_USER_AGENT,
			"Check request user-agent",
			"Check if the remote client user-agent of the request is the same as the one used at login time. Possible values: true, false.",
			SysParamDefinition.TYPE_BOOLEAN,
			getDefaultValue(CORE_SESSION_CHECK_USER_AGENT), null));
		
		params.add(new SysParamDefinition(CORE_EXP_HTML_CSS,
			"Css file location for HTML export",
			"Css used to style the HTML exports from grid.",
			SysParamDefinition.TYPE_STRING,
			getDefaultValue(CORE_EXP_HTML_CSS), null));
		
		params.add(new SysParamDefinition(CORE_PRINT_HTML_TPL,
			"Template file location FreeMarker",
			"Default template used when printing in html format with FreeMarker.",
			SysParamDefinition.TYPE_STRING,
			getDefaultValue(CORE_PRINT_HTML_TPL), null));
		
		params.add(new SysParamDefinition(CORE_DEFAULT_LANGUAGE,
			"Default language",
			"Default language to be used if no language preference set by user.",
			SysParamDefinition.TYPE_STRING,
			getDefaultValue(CORE_DEFAULT_LANGUAGE), null));
		
		params.add(new SysParamDefinition(CORE_DEFAULT_THEME_EXTJS,
			"Default theme Extjs",
			"Default theme to be used in Extjs based user interface if no theme preference set by user.",
			SysParamDefinition.TYPE_STRING,
			getDefaultValue(CORE_DEFAULT_THEME_EXTJS), null));
		
		params.add(new SysParamDefinition(CORE_JOB_USER,
			"Scheduled job user",
			"Generic user to execute scheduled jobs",
			SysParamDefinition.TYPE_STRING,
			getDefaultValue(CORE_JOB_USER), null));
	}
}
