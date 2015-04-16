/**
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

(function(){

    var endpointManager = new EndpointManager();

    var makeRequest = function makeRequest(url, callback) {
        // Get aggregators
        $.ajax({
            url: url,
            dataType: 'json'
        }).done(callback);
    };

    var paintProviders = function paintProviders(providers) {
        var i = 0;

        $('#owner-provider').empty();
        // Create select form
        for (i = 0; i < providers.length; i++) {
            var provider = providers[i];

            $('<option></option>')
                    .val(provider.providerId)
                    .text(provider.providerName)
                    .appendTo('#owner-provider');
        }
    };

    var getProviders = function getProviders(aggregatorId) {
        makeRequest(
            endpointManager.getEndpoint('PROVIDER_COLLECTION') + '?aggregatorId=' + aggregatorId,
            paintProviders
        );
    };

    var paintAggregators = function paintAggregators(aggregators) {
        var i = 0;

        // Create select form
        for (i = 0; i < aggregators.length; i++) {
            var aggregator = aggregators[i];

            $('<option></option>')
                    .val(aggregator.aggregatorId)
                    .text(aggregator.aggregatorName)
                    .appendTo('#rs-aggregator');
        }
        // Create get providers events
        $('#rs-aggregator').change(function () {
            getProviders($(this).val());
        });

        // Get providers for the default aggregator
        getProviders($('#rs-aggregator').val());
        
    };

    var getAggregators = function getAggregators() {
        // Get aggregators
        makeRequest(
            endpointManager.getEndpoint('AGGREGATOR_COLLECTION'),
            paintAggregators
        );
    };

    var paintAlagorithms = function paintAlagorithms(algorithms) {
        var i = 0;
        for (i = 0; i < algorithms.length; i++) {
            var algorithm = algorithms[i];

            $('<option></option>')
                    .val(algorithm.algorithmId)
                    .text(algorithm.algorithmId)
                    .appendTo('#algorithm-type');
        }
        getAggregators();
    };

    var getAlgorithms = function getAlgorithms() {
        makeRequest(
            endpointManager.getEndpoint('ALGORITHM_COLLECTION'),
            paintAlagorithms
        );
    };

    var buildForm = function buildForm() {
        getAlgorithms();
    };

    $(document).ready(buildForm);
})();

