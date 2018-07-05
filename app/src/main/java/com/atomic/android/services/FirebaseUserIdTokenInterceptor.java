package com.atomic.android.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class FirebaseUserIdTokenInterceptor implements Interceptor {

    // Custom header for passing ID token in request.
    private static final String X_FIREBASE_ID_TOKEN = "Authorization";
    private String token;


    @Override
    public Response intercept(final Chain chain) throws IOException {

        final Request request = chain.request();
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {

                throw new Exception("User is not logged in.");
            } else {
                String tokenWithBearer = "Bearer "+ token;
                Request modifiedRequest = request.newBuilder()
                        .addHeader(X_FIREBASE_ID_TOKEN, tokenWithBearer)
                        .build();

                return chain.proceed(modifiedRequest);

            }

        } catch (Exception e) {
            throw new IOException(e.getMessage());

        }
    }

    public void setToken(String token) {
        this.token = token;
    }


}