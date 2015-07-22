<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
    <head>
        <%@include file="/jspf/appResources.jsp"%>
        <title>FIWARE RSS - Settlement</title>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/EndpointManager.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/providers.js"></script>
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
            <div class="col-md-6 col-md-offset-3">
                <table class="table table-bordered table-responsive
                       table-condensed big-margin" id="providers">
                    <tr>
                        <th>Store</th>
                        <th>Provider ID</th>
                        <th>Provider Name</th>
                    </tr>
		</table>
            </div>
        </div>
    </body>
</html>