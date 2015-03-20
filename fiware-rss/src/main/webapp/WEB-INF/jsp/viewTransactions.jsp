<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
<%@include file="/jspf/appResources.jsp"%>
<title>FI-WARE RSS - Settlement</title>
</head>
<body>

	<%@  include file="/jspf/header.jsp"%> 
    <%@  include file="/jspf/footer.jspf"%> <br/> <br/> <br/><br/><br/>

	<div style="margin-left:40px;">
	  <table border="1">
        <tr>
          <th>Provider ID</th>
          <th>User ID</th>
          <th>Tx Type</th>
          <th>App Id</th>
          <th>Request Time</th>
          <th>Ref Code</th>
          <th>Amount</th>
          <th>Tax Amount</th>
          <th>Total Amount</th>
          <th>Description</th>
        </tr>
		<c:forEach var="trans" items="${transactions}">
		  <tr>
		    <td>${trans.txAppProvider}</td>
          	<td>${trans.txEndUserId}</td>
            <td>${trans.tcTransactionType}</td>
            <td>${trans.txApplicationId}</td>
            <td>${trans.tsRequest}</td>
            <td>${trans.txReferenceCode}</td>
            <td>${trans.ftRequestAmount}</td>
            <td>${trans.ftRequestTaxAmount}</td>
            <td>${trans.ftRequestTotalAmount}</td>
            <td>${trans.txRequestAmountDesc}</td>
		  </tr>
		</c:forEach>
		</table>
	</div>		
    <div style="margin-left:40px;"><a href="${contextPath}/settlement/settlement.html">Back</a></div>

</body>
</html>