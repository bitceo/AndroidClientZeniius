
package com.atomic.android;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.atomic.android.listeners.OnGetFirebaseTokenListener;
import com.atomic.android.managers.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;


public class ApplicationHelper {

    private static DatabaseHelper databaseHelper;


    public static final String API_DEV = "";
    public static final String API_PRODUCTION = "";

    public static final String STORAGE_LINK_DEV = "gs://example-zeniius.appspot.com";
    public static final String STORAGE_LINK_PRODUCTION = "gs://example-zeniius.appspot.com";

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public static void initDatabaseHelper(android.app.Application application) {
        databaseHelper = DatabaseHelper.getInstance(application);
        databaseHelper.init();
    }

    public static void getFireBaseToken(final OnGetFirebaseTokenListener onGetFirebaseTokenListener){
        if(FirebaseAuth.getInstance() != null && FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override

                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                onGetFirebaseTokenListener.onGetFinish( idToken );
                                // Send token to your backend via HTTPS
                                // ...
                            } else {
                                onGetFirebaseTokenListener.onGetFinish( null );
                                // Handle error -> task.getException();
                            }
                        }
                    });
            }
    }

    public static boolean isProductionMode(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int flags = packageInfo.applicationInfo.flags;
            return (flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0;
        } catch (PackageManager.NameNotFoundException e) {
            return false;

        }

    }
    public static String getBaseUrl(Context context){
        String API = "";
        if(isProductionMode( context )){
            API = String.format(API_PRODUCTION);
        }else{
            //API = String.format(API_PRODUCTION);
            API = String.format(API_DEV);
        }
        return API;
    }

    public static String getStorageLink(Context context){
        String storage = "";
        if(isProductionMode( context )){
            storage = String.format(STORAGE_LINK_PRODUCTION);
        }else{
            storage = String.format(STORAGE_LINK_DEV);

        }
        return storage;
    }


}
