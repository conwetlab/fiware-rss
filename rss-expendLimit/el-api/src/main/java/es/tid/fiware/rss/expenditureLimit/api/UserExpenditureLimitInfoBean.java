/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
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

/**
 * 
 */
package es.tid.fiware.rss.expenditureLimit.api;

import java.util.List;

/**
 * Information related to the expenditure control for an user.
 * 
 * 
 */
public class UserExpenditureLimitInfoBean {
    private String service;
    private String appProvider;
    private List<LimitBean> generalUserLimits;
    private List<LimitBean> appProvidersLimits;
    private List<LimitBean> serviceLimits;

    /**
     * 
     * 
     * @return
     */
    public List<LimitBean> getGeneralUserLimits() {
        return generalUserLimits;
    }

    /**
     * 
     * 
     * @param accumulativeLimits
     */
    public void setGeneralUserLimits(List<LimitBean> accumulativeLimits) {
        this.generalUserLimits = accumulativeLimits;
    }

    /**
     * 
     * 
     * @return
     */
    public List<LimitBean> getAppProvidersLimits() {
        return appProvidersLimits;
    }

    /**
     * 
     * 
     * @param appProvidersLimits
     */
    public void setAppProvidersLimits(List<LimitBean> appProvidersLimits) {
        this.appProvidersLimits = appProvidersLimits;
    }

    /**
     * 
     * 
     * @return
     */
    public List<LimitBean> getServiceLimits() {
        return serviceLimits;
    }

    /**
     * 
     * 
     * @param serviceLimits
     */
    public void setServiceLimits(List<LimitBean> serviceLimits) {
        this.serviceLimits = serviceLimits;
    }

    /**
     * 
     * @return
     */
    public String getService() {
        return service;
    }

    /**
     * 
     * @param service
     */
    public void setService(String service) {
        this.service = service;
    }

    public String getAppProvider() {
        return appProvider;
    }

    public void setAppProvider(String appProvider) {
        this.appProvider = appProvider;
    }

}
