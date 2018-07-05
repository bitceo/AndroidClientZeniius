
package com.atomic.android.utils;
/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.atomic.android.managers.DatabaseHelper;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
public class LogoutHelper {

    private static final String TAG = LogoutHelper.class.getSimpleName();
    private static ClearImageCacheAsyncTask clearImageCacheAsyncTask;

    public static void signOut(GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseHelper.getInstance(fragmentActivity.getApplicationContext())
                    .removeRegistrationToken(FirebaseInstanceId.getInstance().getToken(), user.getUid());

            logoutFirebase(fragmentActivity.getApplicationContext());
        }

        if (clearImageCacheAsyncTask == null) {
            clearImageCacheAsyncTask = new ClearImageCacheAsyncTask(fragmentActivity.getApplicationContext());
            clearImageCacheAsyncTask.execute();
        }
    }

    public static void signOut(GoogleApiClient mGoogleApiClient, Fragment fragment) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseHelper.getInstance(fragment.getContext())
                    .removeRegistrationToken(FirebaseInstanceId.getInstance().getToken(), user.getUid());

            logoutFirebase(fragment.getContext());
        }

        if (clearImageCacheAsyncTask == null) {
            clearImageCacheAsyncTask = new ClearImageCacheAsyncTask(fragment.getContext());
            clearImageCacheAsyncTask.execute();
        }
    }


    private static void logoutFirebase(Context context) {
        FirebaseAuth.getInstance().signOut();
        PreferencesUtil.setProfileCreated(context, false);
    }

    private static void logoutFacebook(Context context) {
        FacebookSdk.sdkInitialize(context);
        LoginManager.getInstance().logOut();
    }

    private static void logoutGoogle(GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiHelper.createGoogleApiClient(fragmentActivity);
        }

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        final GoogleApiClient finalMGoogleApiClient = mGoogleApiClient;
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (finalMGoogleApiClient.isConnected()) {

                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                LogUtil.logDebug(TAG, "Google API Client Connection Suspended");
            }
        });
    }

    private static class ClearImageCacheAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        public ClearImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Glide.get(context.getApplicationContext()).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
            clearImageCacheAsyncTask = null;
            Glide.get(context.getApplicationContext()).clearMemory();
        }
    }
}
