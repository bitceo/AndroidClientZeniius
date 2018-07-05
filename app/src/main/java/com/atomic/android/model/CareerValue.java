package com.atomic.android.model;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class CareerValue implements Serializable {

    private String id;
    private String description;
    private String keywords;
    private String name;

    public CareerValue() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }
}
