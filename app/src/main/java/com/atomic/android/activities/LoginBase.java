package com.atomic.android.activities;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
public interface LoginBase extends GoogleApiClient.OnConnectionFailedListener, OnClickListener {
    @Override
    void onClick(View v);

    @Override
    void onConnectionFailed(@NonNull ConnectionResult connectionResult) ;

}
