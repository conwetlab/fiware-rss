<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
    <head>
        <%@include file="/jspf/appResources.jsp"%>
        <title>FIWARE RSS - Settlement</title>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/EndpointManager.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/transactions.js"></script>
        <script type="text/javascript">
            CONTEXT_PATH="<%=request.getContextPath()%>";
        </script>
    </head>
    <body>
        <%@  include file="/jspf/header.jsp"%> 
        <%@  include file="/jspf/footer.jspf"%>

        <a class="btn btn-default back" href="${contextPath}">
            <span class="glyphicon glyphicon-arrow-left"></span>
            Back
        </a>

        <div class="container-fluid">
            <div class="col-md-8 col-md-offset-2">
                <table class="table table-bordered table-responsive
                       table-condensed big-margin" id="txs">
                    <tr>
                        <th>Product Class</th>
                        <th>Provider ID</th>
                        <th>User ID</th>
                        <th>Tx Type</th>
                        <th>App Id</th>
                        <th>Request Time</th>
                        <th>Ref Code</th>
                        <th>Amount</th>
                        <th>Tax Amount</th>
                        <th>Currency</th>
                        <th>Description</th>
                    </tr>
		</table>
            </div>
        </div>
    </body>
</html>