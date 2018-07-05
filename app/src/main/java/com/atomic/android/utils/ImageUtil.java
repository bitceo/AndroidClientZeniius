
package com.atomic.android.utils;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import com.atomic.android.enums.UploadImagePrefix;

import java.util.Date;


public class ImageUtil {

    public static String generateImageTitle(UploadImagePrefix prefix, String parentId) {
        if (parentId != null) {
            return prefix.toString() + parentId;
        }

        return prefix.toString() + new Date().getTime();
    }

    public static String generateImageTitleWithIndex(UploadImagePrefix prefix, String parentId, Integer index) {
        if (parentId != null && index != null) {
            return prefix.toString() + parentId  + "_" + Integer.toString(index);
        }

        return prefix.toString()  + new Date().getTime();
    }
}
