

package com.atomic.android.managers;
/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.atomic.android.ApplicationHelper;
import com.atomic.android.enums.ProfileStatus;
import com.atomic.android.enums.UploadImagePrefix;
import com.atomic.android.managers.listeners.OnCompanyCreatedListener;
import com.atomic.android.managers.listeners.OnCompressCompleteListener;
import com.atomic.android.managers.listeners.OnGetObjectListener;
import com.atomic.android.managers.listeners.OnObjectChangedListener;
import com.atomic.android.managers.listeners.OnPasswordChangedListener;
import com.atomic.android.managers.listeners.OnProfileAndCompanyGetListener;
import com.atomic.android.managers.listeners.OnProfileCreatedListener;
import com.atomic.android.model.Career;
import com.atomic.android.model.Company;
import com.atomic.android.model.Profile;
import com.atomic.android.model.ProfileAndCompany;
import com.atomic.android.utils.FormatterUtil;
import com.atomic.android.utils.ImageUtil;
import com.atomic.android.utils.LogUtil;
import com.atomic.android.utils.PreferencesUtil;
import com.atomic.android.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;


public class ProfileManager extends FirebaseListenersManager {

    private ArrayList<ProfileAndCompany> listProfile = new ArrayList<ProfileAndCompany>();

    final private HashMap<String,ProfileAndCompany> mapProfile = new HashMap<String,ProfileAndCompany>();
    private HashMap<String,Boolean> mapListenProfile = new HashMap<String,Boolean>();
    private ArrayList<Career> careerList = new ArrayList<Career>();
    private CopyOnWriteArrayList<ProfileAndCompany> listEnabledProfile = new CopyOnWriteArrayList<>(); // contacts in memory
    private  HashMap<String,Career> listCareerMap = new HashMap<String,Career>();
    private static final String TAG = ProfileManager.class.getSimpleName();
    private static ProfileManager instance;
    private Context context;
    private DatabaseHelper databaseHelper;


    public void preGetListProfileAndCompany(final OnProfileAndCompanyGetListener onProfileAndCompanyGet){
        if(careerList != null && careerList.size() > 0){
            onProfileAndCompanyGet.onProfileAndCompanyGet(true);
        }else{
            DatabaseHelper.getInstance(context).getProfileAndCompanyList( listEnabledProfile,mapProfile, new OnGetObjectListener (){
                @Override
                public void onGetObjectFinish(Object obj) {
                    listProfile= (ArrayList<ProfileAndCompany>) obj;
                    onProfileAndCompanyGet.onProfileAndCompanyGet(true);

                    listEnabledProfile.sort(new Comparator<ProfileAndCompany>() {
                        @Override
                        public int compare(ProfileAndCompany profileAndCompany2, ProfileAndCompany profileAndCompany1) {

                            return Long.valueOf( profileAndCompany1.getProfile().getRegisterDate() ).compareTo( Long.valueOf(profileAndCompany2.getProfile().getRegisterDate()));

                        }
                    });

                }
            });

        }
    }

    public void preGetCareerList( final OnGetObjectListener onGetCareerObjectListener) {
        if(careerList == null || careerList.size() == 0 ){
            listCareerMap.clear();
            databaseHelper.getCareerList( listCareerMap ,new OnGetObjectListener (){
                @Override
                public void onGetObjectFinish(Object obj) {
                    careerList= (ArrayList<Career>) obj;
                    onGetCareerObjectListener.onGetObjectFinish(careerList);
                }
            });
        }else{
            onGetCareerObjectListener.onGetObjectFinish(careerList);
        }

    }



    public static ProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileManager(context);
        }

        return instance;
    }

    private ProfileManager(Context context) {
        this.context = context;
        databaseHelper = ApplicationHelper.getDatabaseHelper();
    }


    public Profile buildProfile(FirebaseUser firebaseUser) {
        Profile profile = new Profile(firebaseUser.getUid());
        profile.setEmail(firebaseUser.getEmail());
       
        return profile;
    }

    public Company buildCompany(FirebaseUser firebaseUser) {
        Company company = new Company(firebaseUser.getUid());
        return company;
    }

    public void getProfileFromDataBase(String id, final OnGetObjectListener<Profile> onGetObjectListener) {
        DatabaseReference databaseReference = databaseHelper.getDatabaseReference().child("user_profiles").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    profile.setId( dataSnapshot.getKey() );
                    onGetObjectListener.onGetObjectFinish( profile );
                }else{
                    onGetObjectListener.onGetObjectFinish( null );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetObjectListener.onGetObjectFinish( null );
            }
        });
    }

    public void getCompanyProfileFromDataBase(String id, final OnGetObjectListener<Company> onGetObjectListener) {
        DatabaseReference databaseReference = databaseHelper.getDatabaseReference().child("company_profiles").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Company company = dataSnapshot.getValue(Company.class);
                    company.setId( dataSnapshot.getKey() );
                    onGetObjectListener.onGetObjectFinish( company );
                }else{
                    onGetObjectListener.onGetObjectFinish( null );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetObjectListener.onGetObjectFinish( null );
            }
        });
    }


    public void changePasssword (final String oldPass, final String newPass, final OnPasswordChangedListener onPasswordChangedListener){
        databaseHelper.changePassword(oldPass, newPass, onPasswordChangedListener);
    }


    public void createOrUpdateProfile(final Profile profile, Uri imageUri, final OnProfileCreatedListener onProfileCreatedListener) {
        if (imageUri == null) {
            databaseHelper.createOrUpdateProfile(profile, onProfileCreatedListener);
            return;
        }

        String imageTitle = ImageUtil.generateImageTitle(UploadImagePrefix.PROFILE, profile.getId());
        databaseHelper.uploadImage(imageUri, imageTitle, Utils.Storage_Profile_Path + profile.getId() +"/" , new OnCompressCompleteListener() {
            @Override
            public void onCompressComplete(UploadTask obj) {
                UploadTask uploadTask = obj;
                if (uploadTask != null) {
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult().getDownloadUrl();
                                LogUtil.logDebug(TAG, "successful upload image, image url: " + String.valueOf(downloadUrl));

                                profile.setprofilePicture(downloadUrl.toString());
                                databaseHelper.createOrUpdateProfile(profile, onProfileCreatedListener);

                            } else {
                                onProfileCreatedListener.onProfileCreated(false);
                                LogUtil.logDebug(TAG, "fail to upload image");
                            }

                        }
                    });
                } else {
                    onProfileCreatedListener.onProfileCreated(false);
                    LogUtil.logDebug(TAG, "fail to upload image");
                }
            }
        } );


    }


    public void createOrUpdateProfileNameCardTop(final Profile profile, Uri imageUri, final OnProfileCreatedListener onProfileCreatedListener) {

        String imageTitle = ImageUtil.generateImageTitle(UploadImagePrefix.NAMECARDTOP, profile.getId());
        databaseHelper.uploadImage(imageUri, imageTitle,Utils.Storage_Profile_Path + profile.getId() +"/", new OnCompressCompleteListener() {
            @Override
            public void onCompressComplete(UploadTask obj) {
                UploadTask uploadTask = obj;
                if (uploadTask != null) {
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult().getDownloadUrl();
                                LogUtil.logDebug(TAG, "successful upload image, image url: " + String.valueOf(downloadUrl));

                                profile.setNameCardTop(downloadUrl.toString());
                                databaseHelper.createOrUpdateProfile(profile, onProfileCreatedListener);

                            } else {
                                onProfileCreatedListener.onProfileCreated(false);
                                LogUtil.logDebug(TAG, "fail to upload image");
                            }

                        }
                    });
                } else {
                    onProfileCreatedListener.onProfileCreated(false);
                    LogUtil.logDebug(TAG, "fail to upload image");
                }
            }
        } );


    }

    public void createOrUpdateProfileNameCardBot(final Profile profile, Uri imageUri, final OnProfileCreatedListener onProfileCreatedListener) {

        String imageTitle = ImageUtil.generateImageTitle(UploadImagePrefix.NAMECARDBOT, profile.getId());
        databaseHelper.uploadImage(imageUri, imageTitle,Utils.Storage_Profile_Path + profile.getId() +"/", new OnCompressCompleteListener() {
            @Override
            public void onCompressComplete(UploadTask obj) {
                UploadTask uploadTask =obj;
                if (uploadTask != null) {
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult().getDownloadUrl();
                                LogUtil.logDebug(TAG, "successful upload image, image url: " + String.valueOf(downloadUrl));

                                profile.setNameCardBot(downloadUrl.toString());
                                databaseHelper.createOrUpdateProfile(profile, onProfileCreatedListener);

                            } else {
                                onProfileCreatedListener.onProfileCreated(false);
                                LogUtil.logDebug(TAG, "fail to upload image");
                            }

                        }
                    });
                } else {
                    onProfileCreatedListener.onProfileCreated(false);
                    LogUtil.logDebug(TAG, "fail to upload image");
                }
            }
        } );


    }






    public void createOrUpdateCompany(final Company company, Uri imageUri, final OnCompanyCreatedListener onCompanyCreatedListener) {
        if (imageUri == null) {
            databaseHelper.createOrUpdateCompany(company, onCompanyCreatedListener);
            return;
        }

        String imageTitle = ImageUtil.generateImageTitle(UploadImagePrefix.COMPANY, company.getId());
        databaseHelper.uploadImage(imageUri, imageTitle,Utils.Storage_Profile_Path + company.getId() +"/", new OnCompressCompleteListener() {
            @Override
            public void onCompressComplete(UploadTask obj) {
                UploadTask uploadTask = obj;
                if (uploadTask != null) {
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult().getDownloadUrl();
                                LogUtil.logDebug(TAG, "successful upload image, image url: " + String.valueOf(downloadUrl));

                                company.setProfilePicture(downloadUrl.toString());
                                databaseHelper.createOrUpdateCompany(company, onCompanyCreatedListener);

                            } else {
                                onCompanyCreatedListener.onCompanyCreated(false);
                                LogUtil.logDebug(TAG, "fail to upload image");
                            }

                        }
                    });
                } else {
                    onCompanyCreatedListener.onCompanyCreated(false);
                    LogUtil.logDebug(TAG, "fail to upload image");
                }
            }
        } );


    }


    public void createOrUpdateCompanyWithGPKDUrls(final Company company,List<Uri> imageUris, final OnCompanyCreatedListener onCompanyCreatedListener) {


        final ArrayList<String> listImagePath = new ArrayList<String>();
        final ArrayList<String> listImageTitle = new ArrayList<String>();
        int index = 0;
        final Integer count = imageUris.size();
        for(Uri element : imageUris)
        {
            final String imageTitle = ImageUtil.generateImageTitleWithIndex(UploadImagePrefix.GPKD, company.getId(), index);
            databaseHelper.uploadImage(element, imageTitle,Utils.Storage_Profile_Path + company.getId() +"/", new OnCompressCompleteListener() {
                @Override
                public void onCompressComplete(UploadTask obj) {
                    UploadTask uploadTask = obj;
                    if (uploadTask != null) {
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                onCompanyCreatedListener.onCompanyCreated(false);

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                LogUtil.logDebug(TAG, "successful upload image, image url: " + String.valueOf(downloadUrl));
                                listImagePath.add( String.valueOf(downloadUrl) );
                                // post.setImagePath(String.valueOf(downloadUrl));
                                listImageTitle.add(imageTitle  );
                                //post.setImageTitle(imageTitle);
                                if(listImagePath.size() == count) {
                                    company.setGpkdUrls( listImagePath );
                                    databaseHelper.createOrUpdateCompany(company, onCompanyCreatedListener);
                                    onCompanyCreatedListener.onCompanyCreated( true );
                                }

                            }
                        });
                    }
                }
            } );
            index++;

        }

    }



    public void getProfileValue(Context activityContext, String id, final OnObjectChangedListener<Profile> listener) {
        ValueEventListener valueEventListener = databaseHelper.getProfile(id, listener);
        addListenerToMap(activityContext, valueEventListener);
    }

    public void getProfileSingleValue(String id, final OnObjectChangedListener<Profile> listener) {
        databaseHelper.getProfileSingleValue(id, listener);
    }

    public ProfileStatus checkProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return ProfileStatus.NOT_AUTHORIZED;
        } else if (!PreferencesUtil.isProfileCreated(context)){
            return ProfileStatus.NO_PROFILE;
        } else {
            return ProfileStatus.PROFILE_CREATED;
        }
    }



    public CopyOnWriteArrayList<ProfileAndCompany> getListEnabledProfile() {
        return listEnabledProfile;

    }



    public ProfileAndCompany getProfileAndCompany(String id) {
        return mapProfile.get( id );
    }

    public Profile getProfile(String id) {
        if(mapProfile.get( id ) != null){
            return mapProfile.get( id ).getProfile();
        }else{
            return null;
        }

    }


    public ArrayList<Career> getCareerList() {
        return careerList;
    }

    public ArrayList<Career> getCareerListQuery(String filterText) {
        ArrayList<Career> listFilter = new  ArrayList<Career>();

        String filterTextNotAccents = FormatterUtil.stripAccents(filterText ).toLowerCase();
        for (Career career : careerList) {
            String nameNotAccents = null;
            String descriptionNotAccents = null;

            Locale current = context.getResources().getConfiguration().locale;
            String lang = current.getLanguage();
            if(career.getLanguages().containsKey( lang ) && career.getLanguages().get( lang ).getName() != null) {

                nameNotAccents = FormatterUtil.stripAccents( career.getLanguages().get( lang ).getName() ).toLowerCase();
            }
            if(career.getLanguages().containsKey( lang ) && career.getLanguages().get( lang ).getDescription() != null){
                descriptionNotAccents = FormatterUtil.stripAccents( career.getLanguages().get( lang ).getDescription()).toLowerCase();
            }
            if(nameNotAccents != null && nameNotAccents.contains(filterTextNotAccents  )) {
                listFilter.add( career );
            }else  if(descriptionNotAccents != null && descriptionNotAccents.contains(filterTextNotAccents  )) {
                listFilter.add( career );
            }
        }
        return listFilter;
    }

    public HashMap<String, Career> getListCareerMap() {
        return listCareerMap;
    }

}
