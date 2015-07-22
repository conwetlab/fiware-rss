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
    var fillProviders = function (providers) {
        for (var i = 0; i < providers.length; i++) {
            var row = $('<tr></tr>');
            $('<td></td>').text(providers[i].aggregatorId).appendTo(row);
            $('<td></td>').text(providers[i].providerId).appendTo(row);
            $('<td></td>').text(providers[i].providerName).appendTo(row);
            row.appendTo('#providers');
        }
    };

    $(document).ready(function () {
        var endpointManager = new EndpointManager();
        var url = endpointManager.getEndpoint('PROVIDER_COLLECTION');

        $.ajax({
            url: url,
            data: 'json'
        }).done(fillProviders);
    });
})();


