package com.atomic.android.utils;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;


public class Utils {


    public static final int CAPTURE_IMAGE_PROFILEPICTURE_REQUEST_CODE = 11111;
    public static final int CAPTURE_IMAGE_NAMECARD_TOP_REQUEST_CODE = 11112;
    public static final int CAPTURE_IMAGE_NAMECARD_BOT_REQUEST_CODE = 11113;
    public static final int CAPTURE_IMAGE_GPKD_REQUEST_CODE = 11114;


    public static final int IMAGE_PICKER_PROFILEPICTURE_REQUEST_CODE = 21111;
    public static final int IMAGE_PICKER_NAMECARD_TOP_REQUEST_CODE = 21112;
    public static final int IMAGE_PICKER_NAMECARD_BOT_REQUEST_CODE = 21113;
    public static final int IMAGE_PICKER_GPKD_REQUEST_CODE = 21114;


    public static final int CREATE_PROFILE_ACTIVITY_REQUEST_CODE = 31111;

    public static final int CAREER_REQUEST_CODE = 51111;



    public static final String Storage_Profile_Path  = "user_profiles/";



    public static int getDisplayWidth(Context context) {
        return getSize(context).x;
    }

    private static Point getSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
    //Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    //Fragments Tags
    public static final String Login_Fragment = "Login_Fragment";
    public static final String SignUp_Fragment = "SignUp_Fragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";
    public static final String FeedList = "FeedList";
    public static final String Career_Fragment = "Career_Fragment";
    public static final String More_Fragment = "More_Fragment";
    public static final String Conversation_Fragment = "Conversation";
    public static final String CeoList_Fragment ="CeoList_Fragment";
    public static final String NotificationList_Fragment = "NotificationList_Fragment";
    public static final String ViewUserProfileDetail_Fragment = "ViewUserProfileDetail_Fragment";
    public static final String ViewCompanyProfileDetail_Fragment = "ViewCompanyProfileDetail_Fragment";
    public static final String EditGeneralUserProfile_Fragment = "EditGeneralUserProfile_Fragment";
    public static final String EditContactUserProfile_Fragment = "EditContactUserProfile_Fragment";
    public static final String EditGeneralCompanyProfile_Fragment = "EditGeneralCompanyProfile_Fragment";
    public static final String EditContactCompanyProfile_Fragment = "EditContactCompanyProfile_Fragment";
    public static final String EditCareerCompanyProfile_Fragment = "EditCareerCompanyProfile_Fragment";


}
