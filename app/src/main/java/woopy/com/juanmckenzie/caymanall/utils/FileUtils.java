package woopy.com.juanmckenzie.caymanall.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author cubycode
 * @since 11/08/2018
 * All rights reserved
 */
public class FileUtils {

    public static Bitmap getPictureFromPath(String currentPhotoPath, int requiredSize) {
        try {
            // Get the dimensions of the bitmap
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(currentPhotoPath, o);

            int scale = 1;

            while (o.outWidth / scale > requiredSize || o.outHeight / scale > requiredSize) {
                scale *= 2;
            }

            // Decode the image file into a Bitmap sized to fill the View
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            return BitmapFactory.decodeFile(currentPhotoPath, o2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeIntentData(Uri data, int requiredSize) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            ContentResolver cr = UIUtils.getAppContext().getContentResolver();
            BitmapFactory.decodeStream(cr.openInputStream(data), null, o);

            o.inSampleSize = calculateInSampleSize(o, requiredSize, requiredSize);
            o.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(cr.openInputStream(data), null, o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap decodeByteArray(byte[] data, int requiredSize) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        ContentResolver cr = UIUtils.getAppContext().getContentResolver();
        BitmapFactory.decodeByteArray(data, 0, data.length, o);

        o.inSampleSize = calculateInSampleSize(o, requiredSize, requiredSize);
        o.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(data, 0, data.length, o);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static File createCachedFileToShare(Bitmap bitmap) {
        File imagesFolder = new File(UIUtils.getAppContext().getCacheDir(), "images");
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Uri getUri(File file) {
        Context context = UIUtils.getAppContext();
        if (file != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                return Uri.fromFile(file);
            }
            return FileProvider.getUriForFile(context,
                    context.getPackageName() + ".provider", file);
        }
        return null;
    }

    public static File createEmptyFile(String fileName, String extension) {
        File root = UIUtils.getAppContext().getExternalCacheDir();
        File myDir = null;
        try {
            myDir = File.createTempFile(fileName, extension, root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myDir;
    }

    public static Bitmap processExif(String photoPath, Bitmap bitmap) {
        ExifInterface ei;

        try {
            ei = new ExifInterface(photoPath);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
        } catch (IOException e) {
            Log.d("FileUtils", "Exif not found");
            e.printStackTrace();
        }

        return bitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
