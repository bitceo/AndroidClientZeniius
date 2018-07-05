package com.atomic.android.model;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class CareerOfUser implements Serializable {

    private String id;
    private String about;

    public CareerOfUser() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
