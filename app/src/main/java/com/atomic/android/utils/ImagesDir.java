

package com.atomic.android.utils;
/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class ImagesDir {

    private static final String TEMP_IMAGES_PATH = "images/temp";
    private static File imagesTempDir;

    public static File getTempImagesDir(Context context) {
        if (imagesTempDir == null) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                imagesTempDir = context.getExternalFilesDir(TEMP_IMAGES_PATH);
            } else {
                imagesTempDir = context.getCacheDir();
            }
        }

        if (imagesTempDir != null && !imagesTempDir.exists()) {
            imagesTempDir.mkdirs();
        }

        return imagesTempDir;
    }

}
