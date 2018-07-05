

package com.atomic.android.utils;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.support.v4.app.FragmentActivity;

import com.atomic.android.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleApiHelper {

    public static GoogleApiClient createGoogleApiClient(FragmentActivity fragmentActivity) {
        GoogleApiClient.OnConnectionFailedListener failedListener;

        if (fragmentActivity instanceof GoogleApiClient.OnConnectionFailedListener) {
            failedListener = (GoogleApiClient.OnConnectionFailedListener) fragmentActivity;
        } else {
            throw new IllegalArgumentException(fragmentActivity.getClass().getSimpleName() + " should implement OnConnectionFailedListener");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(fragmentActivity.getResources().getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        return null;
    }
}
