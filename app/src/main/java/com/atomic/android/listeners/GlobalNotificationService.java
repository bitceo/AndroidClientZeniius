

package com.atomic.android.listeners;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GlobalNotificationService {

    @GET("/api/globalnotification/create/global-notification")
    Call<ResponseBody> getResultGlobalNotification(
            @Query("subject") String subject,
            @Query("object") String object,
            @Query("actiontype") int actiontype,
            @Query("parentobject") String parentobject);

}
