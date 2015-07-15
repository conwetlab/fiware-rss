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
    var currentProviders = [];
    var stakeholders = [];

    var makeRequest = function makeRequest(url, callback) {
        // Get aggregators
        $.ajax({
            url: url,
            dataType: 'json'
        }).done(callback);
    };

    var removeProvider = function removeProvider(list, idField, provider) {
        var newList = [];
        for (var i = 0; i < list.length; i++) {
            if (list[i][idField] !== provider) {
                newList.push(list[i]);
            }
        }
        return newList;
    };

    var fillProviders = function fillProviders(providers, container) {
        // Create select form
        for (i = 0; i < providers.length; i++) {
            var provider = providers[i];

            $('<option></option>')
                    .val(provider.providerId)
                    .text(provider.providerName)
                    .appendTo(container);
        }
    };

    var fillStakeholderList = function fillStakeholderList() {
        $('#select-stakeholder').empty();
        currentProviders = removeProvider(currentProviders, 'providerId', $('#owner-provider').val());
        fillProviders(currentProviders, '#select-stakeholder');
    };

    var paintProviders = function paintProviders(providers) {
        var i = 0;
        $('#stakeholder-container').empty();
        currentProviders = providers.slice();

        $('#owner-provider').empty();

        fillProviders(providers, '#owner-provider');

        $('#owner-provider').off();
        $('#owner-provider').change(function () {
            currentProviders = providers.slice();
            fillStakeholderList();
        });
        fillStakeholderList();
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
        // Set add stakeholder listener
        $('#add-stakeholder').click(function () {
            if (currentProviders.length > 0) {
                var name = $('#select-stakeholder').val();
                var displayName = $('#select-stakeholder option:selected').text();
                var value = $.trim($('#sel-stakeholder-val').val());

                if (name && value && $.isNumeric(value)) {
                    var stHtml = '<div class="selected-stakeholder embedded-input">';
                    stHtml += '<span class="glyphicon glyphicon-user"></span>';
                    stHtml += '<span>' + displayName + '</span>';
                    stHtml += '<span class="st-value"><b>' + value + '</b></span></div>';

                    var stDiv = $(stHtml);
                    stDiv.appendTo('#stakeholder-container');
                    stakeholders.push({
                        stakeholderId: name,
                        modelValue: value
                    });

                    $('<a class="btn btn-default remove-st"><span class="glyphicon glyphicon-remove"></span></a>')
                            .appendTo('#stakeholder-container')
                            .click((function (id, dispName){
                                return function () {
                                    currentProviders.push({
                                        providerId: id,
                                        providerName: dispName
                                    });
                                    $(this).remove();
                                    stDiv.remove();
                                    fillStakeholderList();
                                    stakeholders = removeProvider(stakeholders, 'stakeholderId', id);
                                };
                            })(name, displayName));
                    currentProviders = removeProvider(currentProviders, 'providerId', name);
                    fillStakeholderList();
                }
            }
        });

        // Set create model listener
        $('#create-model').click(function (evnt) {
            evnt.preventDefault();
            evnt.stopPropagation();
            // Make create RS model request
            var data = {
                'ownerProviderId': $('#owner-provider').val(),
                'ownerValue': $.trim($('#owner-value').val()),
                'productClass': $.trim($('#product-class').val()),
                'algorithmType': $('#algorithm-type').val(),
                'aggregatorId': $('#rs-aggregator').val(), 
                'aggregatorValue': $.trim($('#store-value').val()),
                'stakeholders': stakeholders
            };

            var url = endpointManager.getEndpoint('RSMODEL_COLLECTION');
            $.ajax({
                method: 'POST',
                url: url,
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(data),
                error: function (xhr) {
                    var resp = xhr.responseJSON;
                    $('#msg-container .modal-header h3').text('Error');
                    $('#msg-container .modal-body').empty();
                    $('#msg-container .modal-body').append('<p>' + resp.exceptionText + '</p>');
                    $('#msg-container').modal('show');
                }
            }).done(function () {
                $('#msg-container .modal-header h3').text('Created');
                $('#msg-container .modal-body').empty();
                $('#msg-container .modal-body').append('<p> The model has been created </p>');
                $('#msg-container').modal('show');
            });
        });

        getAlgorithms();
    };

    $(document).ready(buildForm);
})();

