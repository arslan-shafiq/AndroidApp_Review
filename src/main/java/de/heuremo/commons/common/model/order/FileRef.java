package de.heuremo.commons.common.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileRef {

    @SerializedName("filePath")
    @Expose
    private String filePath;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
}
