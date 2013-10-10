<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<title>DNet eBusiness Suite</title>
<head>
<link rel="stylesheet" type="text/css" href="${loginPageCss}" />
</head>
<body onload="javascript:onDocumentReady();">
	<br />
	<br />
	<br />
	<br />
	<br />
	<table align=center style='vertical-align: middle; align: center'>
		<tr>
			<td style='border-top: 1px solid gray;' colspan=2>&nbsp;</td>
		</tr>
		<tr>
			<td colspan=2 style='padding: 10px;'>
				<table width='100%'>
					<tr>
						<td align=center class="text"><IMG
							style="border: 0px solid #ccc;" src="${loginPageLogo}"></td>
						<td
							style='padding-left: 20px; font-family: arial, verdana; font-size: 11px;'>
							<form name="login" action="doLogin" method="post"
								onsubmit="return doLogin()">
								<input type="hidden" name="lang" value="en" />
								<table cellspacing=5 cellpadding=0>
									<tr>
										<td class='label'>User</td>
										<td><input name='user' class='field' autocomplete="off"></td>
									</tr>
									<tr>
										<td class='label'>Password</td>
										<td><input name='pswd' class='field' autocomplete="off"
											type="password"></td>
									</tr>
									<tr>
										<td class='label'>Client</td>
										<td><input name='client' class='field' autocomplete="off"></td>
									</tr>
									<tr>
										<td></td>
										<td><input name="save" class='button' type="submit"
											value="Sign in"></td>
									</tr>
								</table>
							</form>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan=2 style="text-align: center; color: red;">&nbsp;
				${error } &nbsp;</td>
		</tr>
		<tr>
			<td
				style='border-top: 1px solid gray; font-family: arial, verdana; font-size: 11px;'
				colspan=2 align=center><br> Copyright &copy; 2010-<%=Calendar.getInstance().get(Calendar.YEAR)%>
				Nan21 Electronics SRL. All Rights Reserved.</td>
		</tr>
	</table>
	<script>
		function doLogin() {
			if (checkFields()) {
				document.forms['login'].save.disabled = true;
				document.forms['login'].submit();
			}
			return false;
		}
		function checkFields() {
			if (document.forms['login'].user.value == '') {
				alert('Enter your username.');
				document.forms['login'].user.focus();
				return false;
			}
			if (document.forms['login'].pswd.value == '') {
				alert('Enter your password.');
				document.forms['login'].pswd.focus();
				return false;
			}
			return true;
		}
		function onDocumentReady() {
			document.forms['login'].user.focus();
		}
	</script>
</body>
</html>