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

package es.upm.fiware.rss.model;

// Generated 24-abr-2012 17:09:13 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BmProductVsObId generated by hbm2java.
 */
@Embeddable
public class BmProductVsObId implements java.io.Serializable {

    private long nuProductId;
    private long nuDeploymentId;

    /**
     * Constructor.
     */
    public BmProductVsObId() {
    }

    /**
     * Constructor.
     * 
     * @param nuProductId
     * @param nuDeploymentId
     */
    public BmProductVsObId(long nuProductId, long nuDeploymentId) {
        this.nuProductId = nuProductId;
        this.nuDeploymentId = nuDeploymentId;
    }

    @Column(name = "NU_PRODUCT_ID", nullable = false, precision = 10, scale = 0)
    public long getNuProductId() {
        return this.nuProductId;
    }

    public void setNuProductId(long nuProductId) {
        this.nuProductId = nuProductId;
    }

    @Column(name = "NU_DEPLOYMENT_ID", nullable = false, precision = 10, scale = 0)
    public long getNuDeploymentId() {
        return this.nuDeploymentId;
    }

    public void setNuDeploymentId(long nuDeploymentId) {
        this.nuDeploymentId = nuDeploymentId;
    }

    /**
     * Overriden.
     * 
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof BmProductVsObId)) {
            return false;
        }
        BmProductVsObId castOther = (BmProductVsObId) other;

        return (this.getNuProductId() == castOther.getNuProductId())
            && (this.getNuDeploymentId() == castOther.getNuDeploymentId());
    }

    /**
     * Overriden.
     * 
     * @return int
     */
    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getNuProductId();
        result = 37 * result + (int) this.getNuDeploymentId();
        return result;
    }

}
