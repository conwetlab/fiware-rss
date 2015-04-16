<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<c:set var="aggregatorId" value="${userSession.aggregatorId}"/>

<html>
    <head>
        <%@include file="/jspf/appResources.jsp"%>
        <title>FIWARE RSS - Settlement</title>
        <script type="text/javascript">
            CONTEXT_PATH="<%=request.getContextPath()%>";
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/settlement.js"></script>
    </head>

    <body>
        <%@  include file="/jspf/header.jsp"%>
        <%@  include file="/jspf/footer.jspf"%> <br/> <br/> <br/><br/><br/>
	
        <div style="margin-left:40px;"><a href="${contextPath}/rss/">RSS API</a></div>
 
        <div class="container-fluid">
            <div class="col-md-8">
                <table class="table table-bordered table-responsive
                       table-condensed">
                    <th>
                        Settlement
                    </th>
                    <tr>
                        <td>
                            Launch Settlement: 
                            <select id="providerSettlement" name="providerSettlement">
                                <c:forEach var="provider" items="${providers}">
                                    <option value="${provider.txAppProviderId}">${provider.txName}</option>
                                </c:forEach>
                            </select>
                            From
                            <input type="text" id="dateFrom" name="dateFrom" value='' size="10" onchange="validMonthDate(this)"
                                onclick="displayCalendar(MM_findObj('dateFrom'),'mm/yyyy',this)"/>

                            <img src="<%=request.getContextPath()%>/dhtmlcalendar/dhtmlgoodies_calendar/images/calendar_icon5.gif"
                                class="pointer" onclick="displayCalendar(MM_findObj('dateFrom'),'mm/yyyy',this)" />
                            &nbsp;To&nbsp;
                            <input type="text" id="dateTo" name="dateTo" value='' size="10" class="formInput1" onchange="validMonthDate(this)"
                                onclick="displayCalendar(MM_findObj('dateTo'),'mm/yyyy',this)"/>
                            <img src="<%=request.getContextPath()%>/dhtmlcalendar/dhtmlgoodies_calendar/images/calendar_icon5.gif"
                                class="pointer" onclick="displayCalendar(MM_findObj('dateTo'),'mm/yyyy',this)" />
                            <input type="submit" class="btn btn-default" value="Launch" onclick="javascript:launchSettlement('${aggregatorId}');"/>
                        </td>
                    </tr>
                    <c:if  test="${userSession.role == 'Provider'}">
                    <tr>
                        <td>
                            Delete transactions: 
                            Provider
                            <select id="name" name="name">
                                <c:forEach var="provider" items="${providers}">
                                    <option value="${provider.txAppProviderId}">${provider.txName}</option>
                                </c:forEach>
                            </select>
                            <input type="submit" value="Delete" class="btn btn-default" onclick="javascript:deletingProvider();"/>
                        </td>
                    </tr>
                    </c:if>
                    <tr>
                        <td>
                            <a href="${contextPath}/settlement/viewFiles?aggregatorId=${aggregatorId}">View files</a>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <a href="${contextPath}/settlement/viewTransactions?aggregatorId=${aggregatorId}">View transactions in database</a>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="container-fluid">
            <div class="col-md-8">
                <table class="table table-bordered table-responsive
                       table-condensed">
                    <th>
                        Revenue Sharing models
                    </th>
                    <tr>
                        <td>
                            <a href="${contextPath}/settlement/viewRS?aggregatorId=${aggregatorId}">View RS models in database</a>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <a href="${contextPath}/settlement/RSModels">Create RS Model</a>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="container-fluid">
            <div class="col-md-8">
                <table class="table table-bordered table-responsive
                       table-condensed">
                    <th>
                        Providers Management
                    </th>
                    <tr>
                        <td>
                            <c:if  test="${userSession.role != 'Provider'}">
                                Register Provider: 
                                Provider Id
                                <input type="text" id="newProviderId" name="newProviderId" value='' size="20"/>
                                Provider Name
                                <input type="text" id="providerName" name="providerName" value='' size="40"/>
                                <input type="submit" class="btn btn-default" value="Create" onclick="javascript:createAggregatorProvider('${aggregatorId}');"/>
                            </c:if>
                            <c:if  test="${userSession.role == 'Provider'}">
                                Register Provider:
                                Provider Id
                                <input type="text" id="providerAdminId" name="providerAdminId" value='' size="20"/>
                                Provider Name
                                <input type="text" id="providerAdminName" name="providerAdminName" value='' size="40"/>
                                Store
                                <select id="aggregator" name="aggregator">
                                    <c:forEach var="aggregator" items="${aggregators}">
                                        <option value="${aggregator.txEmail}">${aggregator.txName}</option>
                                    </c:forEach>
                                </select>
                                <input type="submit" class="btn btn-default" value="Create" onclick="createAdminProvider();"/>
                            </c:if>
                        </td>
                    </tr>
                    <c:if  test="${userSession.role == 'Provider'}">
                    <tr>
                        <td>
                            Register Store: 
                            Store Admin email
                            <input type="text" id="aggregatorId" name="aggregatorId" value='' size="30"/>
                            Store Name
                            <input type="text" id="aggregatorName" name="aggregatorName" value='' size="30"/>
                            <input type="submit" class="btn btn-default" value="Create" onclick="javascript:createAggregator();"/>
                        </td>
                    </tr>
                    </c:if>
                    <tr>
                        <td>
                            <a href="${contextPath}/settlement/viewProviders?aggregatorId=${aggregatorId}">View Providers in database</a>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div style="margin-left:40px;"><a href="${pentahoReportsUrl}" target="_blank">View Reports</a></div>
        <div style="margin-left:40px;"><a href="${contextPath}/settlement/logout">logout</a></div>
    </body>
</html>