package de.heuremo.commons.common.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ColliList {
    @SerializedName("boundingHeight")
    @Expose
    private Integer boundingHeight;
    @SerializedName("boundingLength")
    @Expose
    private Integer boundingLength;
    @SerializedName("boundingWidth")
    @Expose
    private Integer boundingWidth;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    public ColliList(Integer boundingHeight, Integer boundingLength, Integer boundingWidth, Integer count, String imageURL) {
        this.boundingHeight = boundingHeight;
        this.boundingLength = boundingLength;
        this.boundingWidth = boundingWidth;
        this.count = count;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getBoundingHeight() {
        return boundingHeight;
    }

    public void setBoundingHeight(Integer boundingHeight) {
        this.boundingHeight = boundingHeight;
    }

    public Integer getBoundingLength() {
        return boundingLength;
    }

    public void setBoundingLength(Integer boundingLength) {
        this.boundingLength = boundingLength;
    }

    public Integer getBoundingWidth() {
        return boundingWidth;
    }

    public void setBoundingWidth(Integer boundingWidth) {
        this.boundingWidth = boundingWidth;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
