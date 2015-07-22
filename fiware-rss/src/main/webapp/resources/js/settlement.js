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

    var setProvidersSettlement = function (providers) {
        $("#providerSettlement").empty();
        $("#providerSettlement").append('<option value="">---</option>');
        populateProviders(providers, $("#providerSettlement"));
    };

    var setAggregatorsSettlement = function (aggregators) {
        $("#aggregatorSettlement").empty();
        $("#aggregatorSettlement").append('<option value="">---</option>');
        populateAggregators(aggregators, $("#aggregatorSettlement"));

        // Create providers population handler
        $("#aggregatorSettlement").change(function () {
            var agg = $(this).val();
            if (agg) {
                getActors("PROVIDER_COLLECTION", [setProvidersSettlement], "?aggregatorId=" + agg);
            } else {
                $("#providerSettlement").empty();
            }
        });
    }

    launchSettlement = function () {
        var aggregator = $('#aggregatorSettlement').val();
        var provider = $('#providerSettlement').val();
        var query;

        if (aggregator) {
            query = "?aggregatorId=" + aggregator;
            if (provider) {
                query += "&providerId=" + provider;
            }
        }
        getActors("SETTLEMENT_COLLECTION", [function () {
            $('#msg-container .modal-header h3').text('Launched');
            $('#msg-container .modal-body').empty();
            $('#msg-container .modal-body').append('<p> The settlement process has been launched </p>');
            $('#msg-container').modal('show');
        }], query, true);
    };
    
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

    var populateProviders = function (providers, container) {
        var cnt =  $('#providerTransaction');

        if (container) {
            cnt = container;
        }

        for (var i = 0; i < providers.length; i++) {
            // Build option node
            var option = "<option value=" + providers[i].providerId + ">" + providers[i].providerName + "</option>";
            cnt.append(option);
        }
    };

    var populateAggregators = function (aggregators, container) {
        var cnt = $('#aggregator');
        if (container) {
            cnt = container;
        }
        for (var i = 0; i < aggregators.length; i++) {
            var option = "<option value=" + aggregators[i].aggregatorId + ">" + aggregators[i].aggregatorName + "</option>";
            cnt.append(option);
        }
    };

    var getActors = function (ep, callbacks, query, notType) {
        var url = endpointManager.getEndpoint(ep);

        if (query) {
            url += query;
        }

        var params = {
            url: url,
            error: function (xhr) {
                var resp = xhr.responseJSON;
                $('#msg-container .modal-body').empty();
                $('#msg-container .modal-body').append('<p>' + resp.exceptionText + '</p>');
                $('#msg-container').modal('show');
            }
        };

        if (!notType) {
            params.dataType = 'json';
        }

        $.ajax(params).done(function (data) {
            for (var i = 0; i < callbacks.length; i++) {
                callbacks[i](data);
            }
        });
    }

    $(document).ready(function () {
        var agCall = [setAggregatorsSettlement];

        if (IS_ADMIN) {
            agCall.push(populateAggregators);
            // Populate providers
            getActors('PROVIDER_COLLECTION', [populateProviders]);
        }
        // Populate aggregators
        getActors("AGGREGATOR_COLLECTION", agCall);
    });
})();
