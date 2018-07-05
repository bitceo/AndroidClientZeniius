package com.atomic.android.model;
/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;


public class Career implements Parcelable {

    private String id;
    private int rate;
    private String name;
    private String description;
    private HashMap<String,CareerValue> languages;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Career(){}
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rate);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeMap(languages);
    }

    public Career(Parcel in) {
        rate = in.readInt();
        id = in.readString();
        name = in.readString();
        description = in.readString();
        languages = in.readHashMap(CareerValue.class.getClassLoader());

    }

    public static final Parcelable.Creator<Career> CREATOR = new Parcelable.Creator<Career>()
    {
        public Career createFromParcel(Parcel in)
        {
            return new Career(in);
        }
        public Career[] newArray(int size)
        {
            return new Career[size];
        }
    };

    public HashMap<String, CareerValue> getLanguages() {
        return languages;
    }

    public void setLanguages(HashMap<String, CareerValue> languages) {
        this.languages = languages;
    }
}
