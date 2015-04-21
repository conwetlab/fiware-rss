/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * Copyright (C) 2015, CoNWeT Lab., Universidad Politécnica de Madrid
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
    	var url = CONTEXT_PATH + "/settlement/doSettlement.json?aggregatorId=" + aggregatorId + "&providerId=" + providerId;
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
    	
    	url = CONTEXT_PATH + "/settlement/createProvider.json?providerName=" + providerName 
		 +"&providerId=" + newProviderId +"&aggregatorId=" + aggregatorId;
    	
    	$.getJSON(url, function(json){
            if(json.success){
                location.reload();
            }});
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
    	
    	url = CONTEXT_PATH + "/settlement/createAggregator.json?aggregatorName=" + aggregatorName 
		 +"&aggregatorId=" + aggregatorId;
    	
    	$.getJSON(url, function(json){
    		if(json.success){
                    location.reload();
	    	  }
  		});     	
    }
    
    function deletingProvider() {    	
    	providerId = $( "#name option:selected" ).val();
    	providerId = document.getElementById("name").value;    	
    	url = CONTEXT_PATH + "/settlement/clean.json?name=" + providerId; 
   	    $.getJSON(url, function(json){
   		if(json.success){
   			dialogMessage(json.message, "info");
	    	  }else {
	    		  dialogMessage(json.message);
	    	  }
 		  });    	
    } 


