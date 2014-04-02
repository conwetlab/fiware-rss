package es.tid.fiware.rss.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SetRevenueShareConfId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String txAppProviderId;
    private Long nuObId;
    private Long countryId;
    private String productClass;

    /**
     * @return the txAppProviderId
     */
    @Column(name = "TX_APPPROVIDER_ID", nullable = false, length = 50)
    public String getTxAppProviderId() {
        return txAppProviderId;
    }

    /**
     * @param txAppProviderId
     *            the txAppProviderId to set
     */
    public void setTxAppProviderId(String txAppProviderId) {
        this.txAppProviderId = txAppProviderId;
    }

    /**
     * @return the nuObId
     */
    @Column(name = "NU_OB_ID", nullable = false, precision = 10, scale = 0)
    public Long getNuObId() {
        return nuObId;
    }

    /**
     * @param nuObId
     *            the nuObId to set
     */
    public void setNuObId(Long nuObId) {
        this.nuObId = nuObId;
    }

    /**
     * @return the countryId
     */
    @Column(name = "NU_COUNTRY_ID", nullable = false, precision = 10, scale = 0)
    public Long getCountryId() {
        return countryId;
    }

    /**
     * @param countryId
     *            the countryId to set
     */
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    /**
     * @return the productClass
     */
    @Column(name = "TX_PRODUCT_CLASS", nullable = true, length = 40)
    public String getProductClass() {
        return productClass;
    }

    /**
     * @param productClass
     *            the productClass to set
     */
    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
        result = prime * result + ((nuObId == null) ? 0 : nuObId.hashCode());
        result = prime * result + ((productClass == null) ? 0 : productClass.hashCode());
        result = prime * result + ((txAppProviderId == null) ? 0 : txAppProviderId.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SetRevenueShareConfId other = (SetRevenueShareConfId) obj;
        if (countryId == null) {
            if (other.countryId != null) {
                return false;
            }
        } else if (!countryId.equals(other.countryId)) {
            return false;
        }
        if (nuObId == null) {
            if (other.nuObId != null) {
                return false;
            }
        } else if (!nuObId.equals(other.nuObId)) {
            return false;
        }
        if (productClass == null) {
            if (other.productClass != null) {
                return false;
            }
        } else if (!productClass.equals(other.productClass)) {
            return false;
        }
        if (txAppProviderId == null) {
            if (other.txAppProviderId != null) {
                return false;
            }
        } else if (!txAppProviderId.equals(other.txAppProviderId)) {
            return false;
        }
        return true;
    }

}
