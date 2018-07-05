

package com.atomic.android.managers;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.atomic.android.ApplicationHelper;
import com.atomic.android.Constants;
import com.atomic.android.managers.listeners.OnCompanyCreatedListener;
import com.atomic.android.managers.listeners.OnCompressCompleteListener;
import com.atomic.android.managers.listeners.OnGetObjectListener;
import com.atomic.android.managers.listeners.OnObjectChangedListener;
import com.atomic.android.managers.listeners.OnPasswordChangedListener;
import com.atomic.android.managers.listeners.OnProfileCreatedListener;
import com.atomic.android.model.Career;
import com.atomic.android.model.CareerValue;
import com.atomic.android.model.Company;
import com.atomic.android.model.Profile;
import com.atomic.android.model.ProfileAndCompany;
import com.atomic.android.utils.LogUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatabaseHelper {

    public static final String TAG = DatabaseHelper.class.getSimpleName();
    private static DatabaseHelper instance;

    private Context context;
    private FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;

    private Map<ValueEventListener, DatabaseReference> activeListeners = new HashMap<>();
    private Map<ChildEventListener, DatabaseReference> activeChildListeners = new HashMap<>();



    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    public DatabaseHelper(Context context) {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void init() {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(false);
        storage = FirebaseStorage.getInstance();

//        Sets the maximum time to retry upload operations if a failure occurs.
        storage.setMaxUploadRetryTimeMillis(Constants.Database.MAX_UPLOAD_RETRY_MILLIS);
    }

    public DatabaseReference getDatabaseReference() {
        return database.getReference();
    }

    public void closeListener(ValueEventListener listener) {
        if (activeListeners.containsKey(listener)) {
            DatabaseReference reference = activeListeners.get(listener);
            reference.removeEventListener(listener);
            activeListeners.remove(listener);
            LogUtil.logDebug(TAG, "closeListener(), listener was removed: " + listener);
        } else {
            LogUtil.logDebug(TAG, "closeListener(), listener not found :" + listener);
        }
    }

    public void closeChildListener(ChildEventListener listener) {
        if (activeChildListeners.containsKey(listener)) {
            DatabaseReference reference = activeChildListeners.get(listener);
            reference.removeEventListener(listener);
            activeChildListeners.remove(listener);
            LogUtil.logDebug(TAG, "closeListener(), listener was removed: " + listener);
        } else {
            LogUtil.logDebug(TAG, "closeListener(), listener not found :" + listener);
        }
    }

    public void createOrUpdateProfile(final Profile profile, final OnProfileCreatedListener onProfileCreatedListener) {
        DatabaseReference databaseReference = ApplicationHelper.getDatabaseHelper().getDatabaseReference();
        Task<Void> task = databaseReference.child("user_profiles").child(profile.getId()).setValue(profile);

        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onProfileCreatedListener.onProfileCreated(task.isSuccessful());
                addRegistrationToken(FirebaseInstanceId.getInstance().getToken(), profile.getId());
                LogUtil.logDebug(TAG, "createOrUpdateProfile, success: " + task.isSuccessful());
            }
        });
    }

    public void createOrUpdateCompany(final Company company, final OnCompanyCreatedListener onCompanyCreatedListener) {
        DatabaseReference databaseReference = ApplicationHelper.getDatabaseHelper().getDatabaseReference();
        Task<Void> task = databaseReference.child("company_profiles").child(company.getId()).setValue(company);

        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onCompanyCreatedListener.onCompanyCreated(task.isSuccessful());
                LogUtil.logDebug(TAG, "createOrUpdateProfile, success: " + task.isSuccessful());
            }
        });
    }


    public void getCareerList( final HashMap<String,Career> listCareerMap ,final OnGetObjectListener onGetObjectListener)
    {
        DatabaseReference databaseReference = ApplicationHelper.getDatabaseHelper().getDatabaseReference();
        databaseReference.child("career").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Career> careerList = new ArrayList<Career>();
                for (DataSnapshot careerSnapshot: dataSnapshot.getChildren()) {
                    Career career = careerSnapshot.getValue(Career.class);
                    career.setId( careerSnapshot.getKey());
                    if(careerSnapshot.hasChild( "languages" )){
                        HashMap<String, CareerValue> languages = new  HashMap<String, CareerValue>();
                        DataSnapshot languageSnapshot = careerSnapshot.child("languages");
                        for (DataSnapshot item: languageSnapshot.getChildren()) {
                            CareerValue careerValue = item.getValue(CareerValue.class);
                            careerValue.setId( item.getKey() );
                            languages.put(item.getKey(), careerValue );
                        }
                        career.setLanguages( languages );

                    }
                    listCareerMap.put( career.getId(),career );
                    careerList.add( career );
                }
                onGetObjectListener.onGetObjectFinish(careerList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetObjectListener.onGetObjectFinish(null);
            }

        });

    }


    public void getProfileAndCompanyList(final CopyOnWriteArrayList<ProfileAndCompany> listEnabledProfile,
                                         final HashMap<String,ProfileAndCompany> mapProfile, final OnGetObjectListener onGetObjectListener)
    {

        final ArrayList<ProfileAndCompany> profileAndCompanyList = new ArrayList<ProfileAndCompany>();
        final ArrayList<Profile> profileList = new  ArrayList<Profile>();
        final DatabaseReference databaseReference = ApplicationHelper.getDatabaseHelper().getDatabaseReference();

        databaseReference.child("user_profiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot profileSnapshot: dataSnapshot.getChildren()) {
                    Profile profile = profileSnapshot.getValue(Profile.class);
                    profile.setId( profileSnapshot.getKey());
                    profileList.add( profile );
                }
                databaseReference.child("company_profiles").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot data2Snapshot) {
                        HashMap<String, Company> companyMap = new HashMap<String, Company>();
                        for (DataSnapshot companySnapshot: data2Snapshot.getChildren()) {
                            Company company = companySnapshot.getValue(Company.class);
                            company.setId( companySnapshot.getKey());
                            companyMap.put(companySnapshot.getKey(), company);
                        }

                        for (Profile profile:profileList )
                        {
                            ProfileAndCompany profileAndCompany = new ProfileAndCompany(profile.getId());
                            profileAndCompany.setProfile(profile  );
                            if(companyMap.containsKey(profile.getId()))
                            {
                                profileAndCompany.setCompany( companyMap.get(profile.getId()  ) );
                            }
                            profileAndCompanyList.add( profileAndCompany );
                            mapProfile.put( profile.getId(), profileAndCompany);
                            if(profile.getEnabled() == 1){
                                listEnabledProfile.add( profileAndCompany );
                            }

                        }
                        onGetObjectListener.onGetObjectFinish(profileAndCompanyList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });




    }



    public void getNotificationList(String userId, final OnGetObjectListener onGetObjectListener)
    {
        onGetObjectListener.onGetObjectFinish(null);



    }




    public void updateRegistrationToken(final String token) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            final String currentUserId = firebaseUser.getUid();

            getProfileSingleValue(currentUserId, new OnObjectChangedListener<Profile>() {
                @Override
                public void onObjectChanged(Profile obj) {
                    if(obj != null) {
                        addRegistrationToken(token, currentUserId);
                    } else {
                        LogUtil.logError(TAG, "updateRegistrationToken",
                                new RuntimeException("Profile is not found"));
                    }
                }
            });
        }
    }



    public void addRegistrationToken(String token, String userId) {

    }

    public void removeRegistrationToken(String token, String userId) {

    }



    public void uploadImage(Uri uri, String imageTitle, String folder, OnCompressCompleteListener onCompressCompleteListener ) {

        Uri compressUri = compressImage(uri);
        if(compressUri != null){
            //StorageReference storageRef = storage.getReferenceFromUrl(context.getResources().getString(R.string.storage_link));
            StorageReference storageRef = storage.getReferenceFromUrl(ApplicationHelper.getStorageLink( context ));
            StorageReference riversRef = storageRef.child(folder  + imageTitle);
            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCacheControl("max-age=7776000, Expires=7776000, public, must-revalidate")
                    .build();

            //return riversRef.putFile(uri, metadata);
            onCompressCompleteListener.onCompressComplete( riversRef.putFile(compressUri, metadata) );
        }else{
            onCompressCompleteListener.onCompressComplete( null) ;
        }

    }

    private Uri compressImage(Uri uri){

        return uri;


    }


    public void getProfileSingleValue(String id, final OnObjectChangedListener<Profile> listener) {
        DatabaseReference databaseReference = getDatabaseReference().child("user_profiles").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                listener.onObjectChanged(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtil.logError(TAG, "getProfileSingleValue(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });
    }

    public ValueEventListener getProfile(String id, final OnObjectChangedListener<Profile> listener) {
        DatabaseReference databaseReference = getDatabaseReference().child("user_profiles").child(id);
        ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                listener.onObjectChanged(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtil.logError(TAG, "getProfile(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });
        activeListeners.put(valueEventListener, databaseReference);
        return valueEventListener;
    }


    public void changePassword(final String oldPass, final String newPass, final OnPasswordChangedListener onPasswordChangedListener)
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPass);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        onPasswordChangedListener.onPasswordChanged(true);
                                        Log.d(TAG, "Password updated");
                                    } else {
                                        onPasswordChangedListener.onPasswordChanged(false);
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            onPasswordChangedListener.onPasswordChanged(false);
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });
    }
}
