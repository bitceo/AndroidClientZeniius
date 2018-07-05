
package com.atomic.android;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
public class Constants {

    public static class Profile {
        public static final int MAX_AVATAR_SIZE = 1280; //px, side of square
        public static final int MIN_AVATAR_SIZE = 100; //px, side of square
        public static final int MAX_NAME_LENGTH = 120;
    }



    public static class Database {
        public static final int MAX_UPLOAD_RETRY_MILLIS = 60000; //1 minute
    }

    public static class PushNotification {
        public static final int LARGE_ICONE_SIZE = 256; //px
    }
}
