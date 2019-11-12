package de.heuremo.commons.common.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DropOffInformation {

    @SerializedName("deliveryDateDeviating")
    @Expose
    private Boolean deliveryDateDeviating;
    @SerializedName("desiredDeliveryDate")
    @Expose
    private String desiredDeliveryDate;
    @SerializedName("streetAddress")
    @Expose
    private String streetAddress;

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Boolean getDeliveryDateDeviating() {
        return deliveryDateDeviating;
    }

    public void setDeliveryDateDeviating(Boolean deliveryDateDeviating) {
        this.deliveryDateDeviating = deliveryDateDeviating;
    }

    public String getDesiredDeliveryDate() {
        return desiredDeliveryDate;
    }

    public void setDesiredDeliveryDate(String desiredDeliveryDate) {
        this.desiredDeliveryDate = desiredDeliveryDate;
    }
}
