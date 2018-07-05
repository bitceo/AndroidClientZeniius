
package com.atomic.android.enums;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

public enum ProfileStatus {
    PROFILE_CREATED(0), NOT_AUTHORIZED(1), NO_PROFILE(2);

    int status;

    ProfileStatus(int status) {
        this.status = status;
    }

}
