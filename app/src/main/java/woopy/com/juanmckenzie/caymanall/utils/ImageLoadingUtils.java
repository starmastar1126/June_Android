package woopy.com.juanmckenzie.caymanall.utils;

import android.graphics.Bitmap;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

/**
 * @author cubycode
 * @since 05/08/2018
 * All rights reserved
 */
public class ImageLoadingUtils {

    private static final int DISPLAY_IMAGE_SIZE = 800;

    public static void loadImage(ParseObject obj, String columnName, final OnImageLoadListener onImageLoadListener) {
        ParseFile fileObject = obj.getParseFile(columnName);
        if (fileObject == null) {
            onImageLoadListener.onImageLoadingError();
            return;
        }

        fileObject.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException error) {
                if (error == null) {
                    Bitmap bitmap = FileUtils.decodeByteArray(data, DISPLAY_IMAGE_SIZE);

                    if (bitmap == null) {
                        onImageLoadListener.onImageLoadingError();
                    } else {
                        onImageLoadListener.onImageLoaded(bitmap);
                    }
                } else {
                    onImageLoadListener.onImageLoadingError();
                }
            }
        });
    }

    public static void saveImage(Bitmap bitmap, ParseObject parseObj, String columnName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile imageFile = new ParseFile("image.jpg", byteArray);
        parseObj.put(columnName, imageFile);
    }

    public interface OnImageLoadListener {

        void onImageLoaded(Bitmap bitmap);

        void onImageLoadingError();
    }
}
