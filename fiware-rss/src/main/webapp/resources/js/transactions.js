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
    var fillTransactions = function (txs) {
        for (var i = 0; i < txs.length; i++) {
            var row = $('<tr></tr>');
            $('<td></td>').text(txs[i].productClass).appendTo(row);
            $('<td></td>').text(txs[i].appProvider).appendTo(row);
            $('<td></td>').text(txs[i].customerId).appendTo(row);
            $('<td></td>').text(txs[i].transactionType).appendTo(row);
            $('<td></td>').text(txs[i].application).appendTo(row);
            $('<td></td>').text(txs[i].timestamp).appendTo(row);
            $('<td></td>').text(txs[i].referenceCode).appendTo(row);
            $('<td></td>').text(txs[i].chargedAmount).appendTo(row);
            $('<td></td>').text(txs[i].chargedTaxAmount).appendTo(row);
            $('<td></td>').text(txs[i].currency).appendTo(row);
            $('<td></td>').text(txs[i].description).appendTo(row);
            row.appendTo('#txs');
        }
    };

    $(document).ready(function () {
        var endpointManager = new EndpointManager();
        var url = endpointManager.getEndpoint('CDR_COLLECTION');

        $.ajax({
            url: url,
            data: 'json'
        }).done(fillTransactions);
    });
})();

