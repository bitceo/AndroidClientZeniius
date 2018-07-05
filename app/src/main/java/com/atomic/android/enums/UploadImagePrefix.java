
package com.atomic.android.enums;


/**
 * Created by Hung Hoang on 09/07/2017.
 */

public enum UploadImagePrefix {

    PROFILE("profile_"), POST("post_"), COMPANY("company_"), NAMECARDTOP("namecardtop_"), NAMECARDBOT("namecardbot_")
    ,GPKD("gpkd_"), COMMENT("comment");

    String prefix;

    UploadImagePrefix(String prefix) {
        this.prefix = prefix;
    }

    private String getPrefix() {
        return prefix;
    }

    @Override
    public String toString() {
        return getPrefix();
    }
}