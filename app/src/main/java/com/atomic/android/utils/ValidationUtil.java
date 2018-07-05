
package com.atomic.android.utils;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;

import com.atomic.android.Constants;


public class ValidationUtil {
    private static final String [] IMAGE_TYPE = new String[]{"jpg", "png", "jpeg", "bmp", "jp2", "psd", "tif", "gif"};

    public static boolean isNameValid(String name) {
        return name.length() <= Constants.Profile.MAX_NAME_LENGTH;
    }

    public static boolean isImage(Uri uri, Context context) {
        String mimeType = context.getContentResolver().getType(uri);

        if (mimeType != null) {
            return mimeType.contains("image");
        } else {
            String filenameArray[] = uri.getPath().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];

            if (extension != null) {
                for (String type : IMAGE_TYPE) {
                    if (type.toLowerCase().equals(extension.toLowerCase())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean checkImageMinSize(Rect rect) {
        return rect.height() >= Constants.Profile.MIN_AVATAR_SIZE && rect.width() >= Constants.Profile.MIN_AVATAR_SIZE;
    }
}
