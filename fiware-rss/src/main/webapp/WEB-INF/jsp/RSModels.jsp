<%-- 
    Document   : RSModels
    Created on : 27-feb-2015, 15:40:41
    Author     : Francisco de la Vega
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<c:set var="aggregatorId" value="${aggregatorId}"/>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/jspf/appResources.jsp"%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FIWARE RSS - Settlement</title>

        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/EndpointManager.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/models.js"></script>
        <script type="text/javascript">
            CONTEXT_PATH="<%=request.getContextPath()%>"
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
            <div id="msg-container" class="modal fade">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3>Error</h3>
                        </div>
                        <div class="modal-body">
                        </div>
                        <div class="modal-footer">
                            <a href="#" class="btn btn-primary" data-dismiss="modal">Accept</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-md-offset-3">
                <h3>Create Revenue Sharing model</h3>
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="algorithm-type" class="col-sm-2 control-label">Algorithm Type</label>
                        <div class="col-sm-10">
                            <select class="form-control" id="algorithm-type">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rs-aggregator" class="col-sm-2 control-label">Store</label>
                        <div class="col-sm-10">
                            <select class="form-control" id="rs-aggregator">
                            </select>
                            <input type="text" id="store-value" class="form-control embedded-input" placeholder="Store value" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="owner-provider" class="col-sm-2 control-label">Owner provider</label>
                        <div class="col-sm-10">
                            <select class="form-control" id="owner-provider">
                            </select>
                            <input type="text" id="owner-value" class="form-control embedded-input" placeholder="Owner provider value" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="product-class" class="col-sm-2 control-label">Product Class</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="product-class" placeholder="Product Class" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add-stakeholder" class="col-sm-2 control-label">Stakeholders</label>
                        <div class="col-sm-10">
                            <select class="form-control" id="select-stakeholder">
                            </select>
                            <input type="text" class="form-control embedded-input" id="sel-stakeholder-val" placeholder="Stakeholder value" />
                            <a class="btn btn-default embedded-input" id="add-stakeholder" >
                                <span class="glyphicon glyphicon-plus"></span>
                                Add the Stakeholder
                            </a>
                            <div class="form-divider"></div>
                            <div id="stakeholder-container"></div>
                            <div class="form-divider"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit" class="btn btn-default" id="create-model">Create model</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
