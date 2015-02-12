<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<c:set var="aggregatorId" value="${userSession.aggregatorId}"/>

<html>
<head>
<%@include file="/jspf/appResources.jsp"%>
<title>FI-WARE RSS - Settlement</title>
<script type="text/javascript">
    function launchSettlement(aggregatorId){
    	dateFrom = document.getElementById("dateFrom").value;
    	dateTo = document.getElementById("dateTo").value;
    	intervalMethod = true;
    	if ((dateFrom == null || dateFrom == "") && ( dateTo == null || dateTo == "" )) {
    		//Default way of working. current month    		
    		intervalMethod = false;   		    		
    	}else {
    		if ((dateFrom == null || dateFrom == "") && ( dateTo != null || dateTo != "" )) {
    			dialogMessage("Init Month can not be null");
				return;
    		
    		}else if ((dateFrom != null || dateFrom != "") && ( dateTo == null || dateTo == "" )) {
    			dialogMessage("End Month can not be null");
				return;    		
    		} else  if ( dateGreaorEqualThan(dateFrom,dateTo) ) {
    			dialogMessage("Month end must be greater than month init");
    			return;
    		}   
    	}
    	providerId = $( "#providerSettlement option:selected" ).val();
    	var url = "${contextPath}/settlement/doSettlement.json?aggregatorId=" + aggregatorId + "&providerId=" + providerId;
    	if (intervalMethod) {
    		url = url +"&dateFrom=" + dateFrom + "&dateTo=" + dateTo;  	
    	} 
    	$.getJSON(url, function(json){
    		if(json.success){
    			dialogMessage(json.message, "info");
	    	  }else {
	    		  dialogMessage(json.message);
	    	  }
  		  });    	
    }
    
    function createAggregatorProvider (aggregatorId) {
    	newProviderId = document.getElementById("newProviderId").value;
    	providerName = document.getElementById("providerName").value;
        createProvider(newProviderId,providerName,aggregatorId);
    }
    
    function createAdminProvider () {
    	newProviderId = document.getElementById("providerAdminId").value;
    	providerName = document.getElementById("providerAdminName").value;
    	aggregatorId = $( "#aggregator option:selected" ).val();
        createProvider(newProviderId,providerName,aggregatorId);
    }    	  
    
    function createProvider(newProviderId,providerName,aggregatorId) {    	    	
    	if(newProviderId == null || newProviderId == ""){
    		dialogMessage("Provider Id must be introduced");
    		return;
    	}
    	if(providerName == null || providerName == ""){
    		dialogMessage("Provider Name must be introduced");
    		return;
    	} 	
    	
    	url = "${contextPath}/settlement/createProvider.json?providerName=" + providerName 
		 +"&providerId=" + newProviderId +"&aggregatorId=" + aggregatorId;
    	
    	$.getJSON(url, function(json){
    		if(json.success){
    			dialogMessage(json.message, "info");
	    	  }else {
	    		  dialogMessage(json.message);
	    	  }
  		});     	
    }
    
    function createAggregator() {    	
    	aggregatorId = document.getElementById("aggregatorId").value;
    	aggregatorName = document.getElementById("aggregatorName").value;
    	if(aggregatorId == null || aggregatorId == ""){
    		dialogMessage("Aggregator Id must be introduced");
    		return;
    	}
    	if(aggregatorName == null || aggregatorName == ""){
    		dialogMessage("Aggregator Name must be introduced");
    		return;
    	} 	
    	
    	url = "${contextPath}/settlement/createAggregator.json?aggregatorName=" + aggregatorName 
		 +"&aggregatorId=" + aggregatorId;
    	
    	$.getJSON(url, function(json){
    		if(json.success){
    			dialogMessage(json.message, "info");
	    	  }else {
	    		  dialogMessage(json.message);
	    	  }
  		});     	
    }
    
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
    
    function deletingProvider() {    	
    	providerId = $( "#name option:selected" ).val();
    	providerId = document.getElementById("name").value;    	
    	url = "${contextPath}/settlement/clean.json?name=" + providerId; 
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
    <%@  include file="/jspf/footer.jspf"%> <br/> <br/> <br/><br/><br/>
	
 	<div style="margin-left:40px;"><a href="${contextPath}/rss/">RSS API</a></div> 
 
    <div style="margin-left:40px;">
           <td><b>Settlement:</b>&nbsp;&nbsp;&nbsp;&nbsp;</td>
           <td>Provider</td>
           <td> 
			 <select id="providerSettlement" name="providerSettlement" style="width:150px">
			    <option></option>
				<c:forEach var="provider" items="${providers}">			
	               <option value="${provider.txAppProviderId}">${provider.txName}</option>
	      	    </c:forEach>					
			 </select>
		   </td>
		   <td>From</td>
		   <td>
			  <input type="text" id="dateFrom" name="dateFrom" value='' size="10" onchange="validMonthDate(this)"  
			  	     onclick="displayCalendar(MM_findObj('dateFrom'),'mm/yyyy',this)"/>
			  <img src="<%=request.getContextPath()%>/dhtmlcalendar/dhtmlgoodies_calendar/images/calendar_icon5.gif"
				 class="pointer" onclick="displayCalendar(MM_findObj('dateFrom'),'mm/yyyy',this)" />
				 &nbsp;To&nbsp; 
			  <input type="text" id="dateTo" name="dateTo" value='' size="10" class="formInput1" onchange="validMonthDate(this)"
			          onclick="displayCalendar(MM_findObj('dateTo'),'mm/yyyy',this)"/>
			  <img src="<%=request.getContextPath()%>/dhtmlcalendar/dhtmlgoodies_calendar/images/calendar_icon5.gif"
			  class="pointer" onclick="displayCalendar(MM_findObj('dateTo'),'mm/yyyy',this)" />
			  </td>    
			  <td><input type="submit" value="Launch" onclick="javascript:launchSettlement('${aggregatorId}');"/> </td>	
    </div>
 
 	<div style="margin-left:40px;"><a href="${contextPath}/settlement/viewFiles?aggregatorId=${aggregatorId}">View files</a></div>
 
	<div style="margin-left:40px;"><a href="${contextPath}/settlement/viewTransactions?aggregatorId=${aggregatorId}">View transactions in database</a></div>
 
 	<div style="margin-left:40px;"><a href="${contextPath}/settlement/viewRS?aggregatorId=${aggregatorId}">View RS models in database</a></div>
 	
 	<div style="margin-left:40px;"><a href="${contextPath}/settlement/viewProviders?aggregatorId=${aggregatorId}">View Providers in database</a></div>
 	
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
    
 	<c:if  test="${userSession.role != 'Provider'}"> 
 	<div style="margin-left:40px;">
           <td><b>Register Provider:</b>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		   <td>Provider Id</td>		   
		   <td>
			  <input type="text" id="newProviderId" name="newProviderId" value='' size="20"/>			  
		   </td>    
		   <td>Provider Name</td>
		   <td> <input type="text" id="providerName" name="providerName" value='' size="40"/>	 </td>  	
		   <td><input type="submit" value="Create" onclick="javascript:createAggregatorProvider('${aggregatorId}');"/> </td>	   
    </div>
 	</c:if>
 	<c:if  test="${userSession.role == 'Provider'}">  	
 	<div style="margin-left:40px;">
           <td><b>Register Provider:</b>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		   <td>Provider Id</td>		   
		   <td>
			  <input type="text" id="providerAdminId" name="providerAdminId" value='' size="20"/>			  
		   </td>    
		   <td>Provider Name</td>
		   <td> <input type="text" id="providerAdminName" name="providerAdminName" value='' size="40"/></td> 
		   <td>Store</td>
		   <td> 
			 <select id="aggregator" name="aggregator" style="width:200px">
			    <option value=""></option>
				<c:forEach var="aggregator" items="${aggregators}">			
	               <option value="${aggregator.txEmail}">${aggregator.txName}</option>
	      	    </c:forEach>					
			 </select>
		   </td>  	
		   <td><input type="submit" value="Create" onclick="createAdminProvider();"/> </td>	   
    </div>
    
    <div style="margin-left:40px;">
           <td><b>Register Store:</b>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		   <td>Store Admin email</td>		   
		   <td>
			  <input type="text" id="aggregatorId" name="aggregatorId" value='' size="30"/>			  
		   </td>    
		   <td>Store Name</td>
		   <td> <input type="text" id="aggregatorName" name="aggregatorName" value='' size="30"/>	 </td>  	
		   <td><input type="submit" value="Create" onclick="javascript:createAggregator();"/> </td>	   
    </div>
    
 	<div style="margin-left:40px;">
 	 	  <td><b>Delete transactions:</b>&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 	 	  <td>Provider</td>
 	 	  <td> 
			 <select id="name" name="name" style="width:150px">
				<c:forEach var="provider" items="${providers}">			
	               <option value="${provider.txAppProviderId}">${provider.txName}</option>
	      	    </c:forEach>					
			 </select>
		   </td>  	
 	 	  <td><input type="submit" value="Delete" onclick="javascript:deletingProvider();"/> </td>
 	</div>
 	
 	</c:if>
 	 
 	<div style="margin-left:40px;"><a href="${pentahoReportsUrl}" target="_blank">View Reports</a></div>
 	 
 	<div style="margin-left:40px;"><a href="${contextPath}/settlement/logout">logout</a></div>
 	 
</body>
</html>