

package com.atomic.android.model;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@IgnoreExtraProperties
public class Company implements Serializable {

    private String name;
    private String about;
    private String address;
    private String city;
    private String country;
    private String district;
    private String id;
    private String masothue;
    private String email;
    private String phone;
    private String website;
    private String facebook;
    private String profilePicture;
    private String pos;
    private ArrayList<String> gpkdUrls;
    private  HashMap <String, CareerOfUser> careers;



    public Company() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public Company(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getMasothue() {
        return masothue;
    }

    public void setMasothue(String masothue) {
        this.masothue = masothue;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public HashMap<String, CareerOfUser> getCareers() {
        return careers;
    }

    public void setCareers(HashMap<String, CareerOfUser> careers) {
        this.careers = careers;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }


    public ArrayList<String> getGpkdUrls() {
        return gpkdUrls;
    }

    public void setGpkdUrls(ArrayList<String> gpkdUrls) {
        this.gpkdUrls = gpkdUrls;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
