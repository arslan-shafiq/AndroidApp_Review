package de.heuremo.commons.common.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliveryNote {

    @SerializedName("fileRef")
    @Expose
    private List<FileRef> fileRef = null;

    public List<FileRef> getFileRef() {
        return fileRef;
    }

    public void setFileRef(List<FileRef> fileRef) {
        this.fileRef = fileRef;
    }
}
