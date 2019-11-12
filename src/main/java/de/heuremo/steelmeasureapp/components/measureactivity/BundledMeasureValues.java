package de.heuremo.steelmeasureapp.components.measureactivity;

public class BundledMeasureValues {

    private float width;
    private float height;
    private float length;
    private String imageRef;


    public BundledMeasureValues() {
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private float width;
        private float height;
        private float length;
        private String imageRef;


        public Builder() {
        }

        public Builder setWidth(float width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(float height) {
            this.height = height;
            return this;

        }

        public Builder setLength(float length) {
            this.length = length;
            return this;

        }

        public Builder setImageRef(String imageRef) {
            this.imageRef = imageRef;
            return this;

        }

        public BundledMeasureValues build() {
            BundledMeasureValues bundledMeasureValues = new BundledMeasureValues();
            bundledMeasureValues.setHeight(this.height);
            bundledMeasureValues.setWidth(this.width);
            bundledMeasureValues.setLength(this.length);
            bundledMeasureValues.setImageRef(this.imageRef);
            return bundledMeasureValues;

        }
    }
}
