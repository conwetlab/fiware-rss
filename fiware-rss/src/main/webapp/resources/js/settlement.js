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

(function () {

    var endpointManager = new EndpointManager();
    // Get csrf token
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

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
    
    createAggregatorProvider = function (aggregatorId) {
    	newProviderId = $("#newProviderId").val();
    	providerName = $("#providerName").val();
        createProvider(newProviderId, providerName, aggregatorId);
    }
    
    createAdminProvider = function () {
    	newProviderId = $("#providerAdminId").val();
    	providerName =  $("#providerAdminName").val();
    	aggregatorId = $("#aggregator").val();
        createProvider(newProviderId, providerName, aggregatorId);
    }    	  
    
    var createProvider = function (newProviderId, providerName, aggregatorId) {    	    	
    	if(newProviderId == null || newProviderId == ""){
    		dialogMessage("Provider Id must be introduced");
    		return;
    	}
    	if(providerName == null || providerName == ""){
    		dialogMessage("Provider Name must be introduced");
    		return;
    	} 	

        var providerData = {
            'providerId': newProviderId,
            'providerName': providerName,
            'aggregatorId': aggregatorId
        };

    	url = endpointManager.getEndpoint('PROVIDER_COLLECTION');
        $.ajax({
            url: url,
            contentType: "application/json",
            method: 'POST',
            data: JSON.stringify(providerData),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        }).done(function () {
            location.reload();
        });
    }
    
    createAggregator = function () {
        var url = endpointManager.getEndpoint("AGGREGATOR_COLLECTION");

        var aggregatorData = {
            'aggregatorId': $("#aggregatorId").val(),
            'aggregatorName': $("#aggregatorName").val()
        }

    	if(aggregatorData.aggregatorId == null || aggregatorData.aggregatorId == ""){
    		dialogMessage("Aggregator Id must be introduced");
    		return;
    	}
    	if(aggregatorData.aggregatorName == null || aggregatorData.aggregatorName == ""){
    		dialogMessage("Aggregator Name must be introduced");
    		return;
    	}

        $.ajax({
            url: url,
            contentType: "application/json",
            method: 'POST',
            data: JSON.stringify(aggregatorData),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        }).done(function () {
            location.reload();
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

    var populateProviders = function (providers) {
        for (var i = 0; i < providers.length; i++) {
            // Build option node
            var option = "<option value=" + providers[i].providerId + ">" + providers[i].providerName + "</option>";

            // Include providers in settlement
            $('#providerSettlement').append(option);

            // Include providers in delete transactions if possible
            if (IS_ADMIN) {
                $('#providerTransaction').append(option);
            }
        }
    };

    var populateAggregators = function (aggregators) {
        for (var i = 0; i < aggregators.length; i++) {
            var option = "<option value=" + aggregators[i].aggregatorId + ">" + aggregators[i].aggregatorName + "</option>";
            $('#aggregator').append(option);
        }
    };

    $(document).ready(function () {
        // Populate providers
        var url = endpointManager.getEndpoint('PROVIDER_COLLECTION');
        $.ajax({
            url: url,
            dataType: 'json'
        }).done(populateProviders);

        // Populate aggregators
        if (IS_ADMIN) {
            url = endpointManager.getEndpoint("AGGREGATOR_COLLECTION");
            $.ajax({
                url: url,
                dataType: 'json'
            }).done(populateAggregators);
        }
    })
})(); 


