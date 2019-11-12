package de.heuremo.commons.common.model.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDTO implements Parcelable {

    public static final Creator<OrderDTO> CREATOR = new Creator<OrderDTO>() {
        @Override
        public OrderDTO createFromParcel(Parcel in) {
            return new OrderDTO(in);
        }

        @Override
        public OrderDTO[] newArray(int size) {
            return new OrderDTO[size];
        }
    };
    @SerializedName("colliList")
    @Expose
    private List<ColliList> colliList = null;
    @SerializedName("commission")
    @Expose
    private String commission;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("deliveryNote")
    @Expose
    private DeliveryNote deliveryNote;
    @SerializedName("departmentFilter")
    @Expose
    private String departmentFilter;
    @SerializedName("dropOffInformation")
    @Expose
    private DropOffInformation dropOffInformation;
    @SerializedName("estimatedWeight")
    @Expose
    private Integer estimatedWeight;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lastUpdatedAt")
    @Expose
    private String lastUpdatedAt;
    @SerializedName("pickupInformation")
    @Expose
    private PickupInformation pickupInformation;

    public OrderDTO() {

    }

    protected OrderDTO(Parcel in) {
        commission = in.readString();
        createdAt = in.readString();
        departmentFilter = in.readString();
        if (in.readByte() == 0) {
            estimatedWeight = null;
        } else {
            estimatedWeight = in.readInt();
        }
        id = in.readString();
        lastUpdatedAt = in.readString();
    }

    public List<ColliList> getColliList() {
        return colliList;
    }

    public void setColliList(List<ColliList> colliList) {
        this.colliList = colliList;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public DeliveryNote getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getDepartmentFilter() {
        return departmentFilter;
    }

    public void setDepartmentFilter(String departmentFilter) {
        this.departmentFilter = departmentFilter;
    }

    public DropOffInformation getDropOffInformation() {
        return dropOffInformation;
    }

    public void setDropOffInformation(DropOffInformation dropOffInformation) {
        this.dropOffInformation = dropOffInformation;
    }

    public Integer getEstimatedWeight() {
        return estimatedWeight;
    }

    public void setEstimatedWeight(Integer estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public PickupInformation getPickupInformation() {
        return pickupInformation;
    }

    public void setPickupInformation(PickupInformation pickupInformation) {
        this.pickupInformation = pickupInformation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commission);
        dest.writeString(createdAt);
        dest.writeString(departmentFilter);
        if (estimatedWeight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(estimatedWeight);
        }
        dest.writeString(id);
        dest.writeString(lastUpdatedAt);
    }
}
