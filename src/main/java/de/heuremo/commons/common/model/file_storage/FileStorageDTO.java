package de.heuremo.commons.common.model.file_storage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileStorageDTO {

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("lastUpdatedAt")
    @Expose
    private String lastUpdatedAt;
    @SerializedName("departmentFilter")
    @Expose
    private Object departmentFilter;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("filePath")
    @Expose
    private String filePath;
    @SerializedName("version")
    @Expose
    private Integer version;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Object getDepartmentFilter() {
        return departmentFilter;
    }

    public void setDepartmentFilter(Object departmentFilter) {
        this.departmentFilter = departmentFilter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}