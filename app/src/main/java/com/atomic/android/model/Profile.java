
package com.atomic.android.model;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Profile implements Serializable {

    private String firstName;
    private String lastName;
    private String middleName;
    private long birthday;
    private String phone;
    private String id;
    private String about;
    private String email;
    private String facebook;
    private String profilePicture;
    private long likesCount;
    private String registrationToken;
    private String nameCardBot;
    private String nameCardTop;
    private long registerDate;
    private long enabled;
    private long needCounter;
    private long offerCounter;
    private long eventCounter;
    private long questionCounter;
    private String os;


    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public Profile(String id) {
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

    public String getprofilePicture() {
        return profilePicture;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getFullName() {
        String fullname = lastName;
        if( middleName == null || middleName.equals("") || middleName.length() == 0)
        {
        }else{
            fullname += " " + middleName;
        }
        fullname += " " + firstName;
        return fullname;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getMiddleName() {
        return this.middleName;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return this.phone;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }
    public long getBirthday() {
        return this.birthday;
    }
    public void setprofilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getNameCardBot() {
        return nameCardBot;
    }

    public void setNameCardBot(String nameCardBot) {
        this.nameCardBot = nameCardBot;
    }

    public String getNameCardTop() {
        return nameCardTop;
    }

    public void setNameCardTop(String nameCardTop) {
        this.nameCardTop = nameCardTop;
    }

    public long getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(long registerDate) {
        this.registerDate = registerDate;
    }

    public long getEnabled() {
        return enabled;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public long getNeedCounter() {
        return needCounter;
    }

    public long getOfferCounter() {
        return offerCounter;
    }

    public long getEventCounter() {
        return eventCounter;
    }

    public long getQuestionCounter() {
        return questionCounter;
    }

}
