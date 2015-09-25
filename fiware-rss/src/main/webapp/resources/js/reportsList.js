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

(function () {
    var fillModels = function (models) {
        for (var i = 0; i < models.length; i++) {
            // Build table for RS model
            var tableTmpl = '<table class="table table-bordered table-responsive table-condensed big-margin">';
            tableTmpl += '<tr><th>Algorithm</th><th>Product Class</th><th>Store</th>';
            tableTmpl += '<th>Store Value</th><th>Provider</th><th>Provider Value</th><th>Currency</th><th>Timestamp</th></tr>';
            tableTmpl += '</table>';

            var table = $(tableTmpl);

            // Include basic values
            var row = $('<tr></tr>');
            row.append('<td>' + models[i].algorithmType + '</td>');
            row.append('<td>' + models[i].productClass + '</td>');
            row.append('<td>' + models[i].aggregatorId + '</td>');
            row.append('<td>' + models[i].aggregatorValue + '</td>');
            row.append('<td>' + models[i].ownerProviderId + '</td>');
            row.append('<td>' + models[i].ownerValue + '</td>');
            row.append('<td>' + models[i].currency + '</td>');
            row.append('<td>' + models[i].timestamp + '</td>');

            table.append(row);

            // Include stakeholders
            if(models[i].stakeholders.length > 0) {
                var stTitle = '<tr><th colspan="4">Stakeholder</th><th colspan="4">Stakeholder Value</th></tr>';
                table.append(stTitle);
                for (var j = 0; j < models[i].stakeholders.length; j++) {
                    var row = $('<tr></tr>');
                    row.append('<td colspan="4">' + models[i].stakeholders[j].stakeholderId + '</td>');
                    row.append('<td colspan="4">' + models[i].stakeholders[j].modelValue + '</td>');
                    table.append(row);
                }
            }
            table.appendTo('#reports-container');
        }
    };

    $(document).ready(function () {
        var endpointManager = new EndpointManager();
        var url = endpointManager.getEndpoint('REPORTS_COLLECTION');

        $.ajax({
            url: url,
            data: 'json'
        }).done(fillModels);
    });
})();


