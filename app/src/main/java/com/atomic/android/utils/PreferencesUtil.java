
package com.atomic.android.utils;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

    private static final String SHARED_PREFERENCES_NAME = "com.atom.zeniius";
    private static final String PREF_PARAM_IS_PROFILE_CREATED = "isProfileCreated";
    private static final String PREF_PARAM_IS_POSTS_WAS_LOADED_AT_LEAST_ONCE = "isPostsWasLoadedAtLeastOnce";
    private static final String PREF_PARAM_USER_LANGUAGE = "userLanguage";

    private static SharedPreferences getSharedPreferences(Context context) {
        if(context != null){
            return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        }else{
            return null;
        }

    }

    public static Boolean isProfileCreated(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_PARAM_IS_PROFILE_CREATED, false);
    }

    public static Boolean isPostWasLoadedAtLeastOnce(Context context) {
        if(context != null){
            return getSharedPreferences(context).getBoolean(PREF_PARAM_IS_POSTS_WAS_LOADED_AT_LEAST_ONCE, false);
        }else{
            return false;
        }

    }

    public static void setProfileCreated(Context context, Boolean isProfileCreated) {
        if(context != null) {
            getSharedPreferences( context ).edit().putBoolean( PREF_PARAM_IS_PROFILE_CREATED, isProfileCreated ).commit();
        }
    }

    public static void setLanguage(Context context, String lang) {
        if(context != null) {
            getSharedPreferences( context ).edit().putString( PREF_PARAM_USER_LANGUAGE, lang ).commit();
        }
    }

    public static String getLanguage(Context context) {
        return getSharedPreferences(context).getString(PREF_PARAM_USER_LANGUAGE, "en");
    }


    public static void setPostWasLoadedAtLeastOnce(Context context, Boolean isPostWasLoadedAtLeastOnce) {
        if (context != null) {
            getSharedPreferences( context ).edit().putBoolean( PREF_PARAM_IS_POSTS_WAS_LOADED_AT_LEAST_ONCE, isPostWasLoadedAtLeastOnce ).commit();
        }
    }

}
