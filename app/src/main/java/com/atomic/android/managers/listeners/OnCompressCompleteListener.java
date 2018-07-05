
package com.atomic.android.managers.listeners;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import com.google.firebase.storage.UploadTask;

public interface OnCompressCompleteListener {

    void onCompressComplete(UploadTask obj);


}