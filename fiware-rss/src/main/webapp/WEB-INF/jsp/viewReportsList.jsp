<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<core:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
<%@include file="/jspf/appResources.jsp"%>
<title>FI-WARE RSS - View files</title>
</head>

<body>


<%@  include file="/jspf/cabecera.jsp"%> 
<%@  include file="/jspf/pie.jspf"%> <br/> <br/> <br/><br/><br/>

<div style="margin-left:40px;">
<table width="90%" align="center" cellpadding="2" cellspacing="0" border="0">
	<tr>
		<th align="left">&nbsp;</th>
		<th align="left">RSS Files</th>
	</tr>
	
	<core:forEach var="RSSFile" items="${RSSFilesList}">	
	<tr>
		<td align="left" colspan="2">
		<core:if test="${RSSFile.txUrl!=''}">
			<a href="${contextPath}/settlement/viewFile.html?rssname=${RSSFile.txUrl}"> ${RSSFile.txName} </a>
		</core:if>
		<core:if test="${RSSFile.txUrl==''}">
			${RSSFile.txName}
		</core:if>
		</td>
	</tr>
	</core:forEach>
	
	<tr><td>&nbsp;</td></tr>
	
</table>
</div>

<div style="margin-left:40px;"><a href="${contextPath}/settlement/settlement.html">Back</a></div>

</body>
</html>