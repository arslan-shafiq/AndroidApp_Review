package de.heuremo.steelmeasureapp.components.measureactivity.image;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ImageConverter {

    public static byte[] convertSessionImage(Image image) {

        if (image.getFormat() != ImageFormat.YUV_420_888) {
            throw new IllegalArgumentException("Illegal image format");
        }

        return ImageConverter.NV21toJPEG(ImageConverter.YUV_420_888toNV21(image), image.getWidth(), image.getHeight());
    }


    private static byte[] NV21toJPEG(byte[] nv21, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);
        return out.toByteArray();
    }

    public static byte[] YUV_420_888toNV21(Image image) {

        if (image.getFormat() != ImageFormat.YUV_420_888) {
            throw new IllegalArgumentException("Illegal image format");
        }

        byte[] nv21;
        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer();
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        nv21 = new byte[ySize + uSize + vSize];

        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        return nv21;
    }

    public static byte[] convertBitmapToByteArray(Bitmap bp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bp.recycle();
        return byteArray;
    }
}
