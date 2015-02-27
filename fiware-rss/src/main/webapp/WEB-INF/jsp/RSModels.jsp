<%-- 
    Document   : RSModels
    Created on : 27-feb-2015, 15:40:41
    Author     : Francisco de la Vega
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<c:set var="aggregatorId" value="${userSession.aggregatorId}"/>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/jspf/appResources.jsp"%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FIWARE RSS - Settlement</title>

        <script type="text/javascript">
            function createModel() {    	
                providerId = $( "#providerId option:selected" ).val();
                productClass = document.getElementById("productClass").value;
                revenue = document.getElementById("revenue");
               if(revenue.value == null || revenue.value == "" || ! isNumber(revenue)){
                    dialogMessage("Revenue number must be introduced");
                  return;
                }    	
                url = "${contextPath}/settlement/createRSModel.json?providerId=" + providerId 
                +"&productClass=" + productClass + "&revenue=" + revenue.value;

                $.getJSON(url, function(json){
                    if(json.success){
                        dialogMessage(json.message, "info");
                    }else {
                        dialogMessage(json.message);
                    }
                });    	
            } 
        </script>
    </head>
    <body>
        <%@  include file="/jspf/header.jsp"%> 
        <%@  include file="/jspf/footer.jspf"%>

        <br /><br /><br /><br /><br />
        <div style="margin-left:40px;"><a href="${contextPath}/settlement/settlement">Back</a></div>
        <div style="margin-left:40px;">
           <td><b>Create RS model:</b>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		   <td>Provider</td>
		   <td> 
			 <select id="providerId" name="providerId" style="width:150px">
				<c:forEach var="provider" items="${providers}">			
	               <option value="${provider.txAppProviderId}">${provider.txName}</option>
	      	    </c:forEach>					
			 </select>
		   </td> 		      
		   <td>Revenue %</td>
		   <td>
			  <input type="text" id="revenue" name="revenue" value='' size="2" maxlength="2"/>			  
		   </td>  
		   <td>Product Class:</td>
		   <td>
			  <input type="text" id="productClass" name="productClass" value='' size="40"/>	  		   
           </td> 
           <td><input type="submit" value="Create" onclick="javascript:createModel();"/> </td>	
        </div> 
    </body>
</html>
