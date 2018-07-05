package com.atomic.android.activities.notification;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Context;

import com.atomic.android.ApplicationHelper;
import com.atomic.android.listeners.GlobalNotificationService;
import com.atomic.android.listeners.OnGetFirebaseTokenListener;
import com.atomic.android.services.FirebaseUserIdTokenInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationService {
    public static void requestNotification(Context context, String subjectId, String objectId, long actionType, String parentObjectId){
        ApplicationHelper.getFireBaseToken( new OnGetFirebaseTokenListener() {
            @Override
            public void onGetFinish(String token) {
                if(token != null){

                    FirebaseUserIdTokenInterceptor interceptor = new FirebaseUserIdTokenInterceptor();
                    interceptor.setToken(token );

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .build();


                    String url = ApplicationHelper.getBaseUrl( context);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .client(okHttpClient)
                            .build();
                    GlobalNotificationService service = retrofit.create(GlobalNotificationService.class);
                    service.getResultGlobalNotification( subjectId,objectId,2,"").enqueue( new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    } );

                }
            }
        } );
    }
}
