<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<%@include file="/jspf/appResources.jsp"%>
<title>FIWARE RSS - Error</title>
</head>
<body>
   <%@  include file="/jspf/header.jsp"%> 
    <%@  include file="/jspf/footer.jspf"%> <br/> <br/> <br/><br/><br/>
    <div style="margin-left:40px;">
		<h3>Error:</h3>
    	<p>${message}</p>
    </div>
</body>
</html>