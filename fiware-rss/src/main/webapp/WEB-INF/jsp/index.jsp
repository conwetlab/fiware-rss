<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="validUser" value="${param.validUser}"/>
<c:set var="error" value="${param.error}"/>

<html>
<head>
<%@include file="/jspf/appResources.jsp"%>
<title>FIWARE RSS - Settlement</title>
<script type="text/javascript">
   function init() {
	   if (null != "${error}" && "${error}" != '') {
		   alert("Error: ${error}");
	   } else if ("${validUser}" == 'false') {
	   		alert("Your user is not allowed to administrate RSS aplication.");
       }
   }

</script>
</head>
<body onload="javascript:init();" >

<%@  include file="/jspf/header.jsp"%>
<%@  include file="/jspf/footer.jspf"%> <br/> <br/> <br/><br/><br/>

	
	
	<div><p style="margin-left:40px;">It is necessary being logged to access to the page. Please  login in accounting page.
	      <a href="${contextPath}/settlement/settlement">Login</a></p></div>

</body>
</html>