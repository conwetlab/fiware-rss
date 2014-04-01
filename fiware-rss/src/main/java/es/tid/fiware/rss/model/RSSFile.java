/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014,  Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.tid.fiware.rss.model;

import java.io.Serializable;

public class RSSFile  implements Serializable {
	private static final long serialVersionUID = -4172378703613378099L;
	
	//Attributes of RSS File
	private String txName;
	private String txUrl;
		

	public String getTxName() {
		return txName;
	}
	public void setTxName(String txName) {
		this.txName = txName;
	}
	
	public String getTxUrl() {
		return txUrl;
	}
	public void setTxUrl(String txUrl) {
		this.txUrl = txUrl;
	}	
}
