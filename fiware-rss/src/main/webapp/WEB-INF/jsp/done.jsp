<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
<%@include file="/jspf/appResources.jsp"%>
<title>FI-WARE RSS - Settlement</title>
</head>
<body>

	<%@  include file="/jspf/header.jsp"%>
    <%@  include file="/jspf/pie.jspf"%> <br/> <br/> <br/><br/><br/>
	
	<div style="margin-left:40px;"><p>${operation} done.</p></div>
	
    <div style="margin-left:40px;"><a href="${contextPath}/settlement/settlement.html">Back</a></div>

</body>
</html>