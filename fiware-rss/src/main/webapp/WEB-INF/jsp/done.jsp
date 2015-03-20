<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
<%@include file="/jspf/appResources.jsp"%>
<title>FIWARE RSS - Settlement</title>
</head>
<body>

	<%@  include file="/jspf/header.jsp"%>
    <%@  include file="/jspf/footer.jspf"%> <br/> <br/> <br/><br/><br/>
	
	<div style="margin-left:40px;"><p>${operation} done.</p></div>
	
    <div style="margin-left:40px;"><a href="${contextPath}/settlement/settlement">Back</a></div>

</body>
</html>