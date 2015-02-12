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
    
    <div style="margin-left:40px;">
     <table border="1">
        <tr>
          <th>Provider ID</th>
          <th>Revenue Share %</th>
          <th>Product Class</th>
        </tr>
		<c:forEach var="rs" items="${rsList}">
			<tr>
          	<td>${rs.id.txAppProviderId}</td>
          	<td>${rs.nuPercRevenueShare}</td>
          	<td>${rs.id.productClass}</td>
          </tr>
		</c:forEach>
		</table>
	</div>
    <div style="margin-left:40px;"><a href="${contextPath}/settlement/settlement.html">Back</a></div>

</body>
</html>