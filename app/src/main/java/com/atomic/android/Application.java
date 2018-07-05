package com.atomic.android;
/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Context;
import android.support.multidex.MultiDex;

public class Application extends android.app.Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationHelper.initDatabaseHelper(this);

    }


}

