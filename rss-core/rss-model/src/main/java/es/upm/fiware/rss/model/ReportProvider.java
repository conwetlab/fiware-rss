/**
 * Copyright (C) 2015 CoNWeT Lab., Universidad Politécnica de Madrid
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
package es.upm.fiware.rss.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author fdelavega
 */
@Entity
@Table(name = "report_provider")
public class ReportProvider implements Serializable{
    
    private ReportProviderId id;
    private BigDecimal modelValue;

    public ReportProvider() {
    }

    @EmbeddedId
    public ReportProviderId getId() {
        return id;
    }

    public void setId(ReportProviderId id) {
        this.id = id;
    }

    @Column(name = "PROVIDER_MODEL_VALUE", nullable = false, precision = 5, scale = 0)
    public BigDecimal getModelValue() {
        return this.modelValue;
    }

    public void setModelValue(BigDecimal modelValue) {
        this.modelValue = modelValue;
    }

    @Transient
    public SharingReport getReport() {
        return this.getId().getReport();
    }
    
    public void setReport(SharingReport model) {
        this.getId().setReport(model);
    }

    @Transient
    public DbeAppProvider getStakeholder() {
        return this.getId().getStakeholder();
    }

    public void setStakeholder(DbeAppProvider stakeholder) {
        this.getId().setStakeholder(stakeholder);
    }
}
