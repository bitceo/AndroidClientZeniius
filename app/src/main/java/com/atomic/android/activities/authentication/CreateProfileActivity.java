
package com.atomic.android.activities.authentication;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.AddPhotoBottomDialogFragment;
import com.atomic.android.activities.CareerActivity;
import com.atomic.android.activities.CustomToast;
import com.atomic.android.activities.GalleryViewActivity;
import com.atomic.android.activities.PickImageActivity;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnClickPhotoBottomDialogListener;
import com.atomic.android.managers.listeners.OnCompanyCreatedListener;
import com.atomic.android.managers.listeners.OnGetObjectListener;
import com.atomic.android.managers.listeners.OnProfileCreatedListener;
import com.atomic.android.model.Career;
import com.atomic.android.model.CareerOfUser;
import com.atomic.android.model.Company;
import com.atomic.android.model.Profile;
import com.atomic.android.utils.FormatterUtil;
import com.atomic.android.utils.LogUtil;
import com.atomic.android.utils.LogoutHelper;
import com.atomic.android.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateProfileActivity extends PickImageActivity implements OnProfileCreatedListener, OnCompanyCreatedListener {
    private static final String TAG = CreateProfileActivity.class.getSimpleName();


    private ImageView imageView;
    private ProgressBar progressBar;
    private Button careerBtn;
    private Profile userProfile;
    private Company companyProfile;
    private String userId;
    private FirebaseAuth mAuth;
    private static EditText firstName,middleName, lastName, email, phone, companyName, birthday;
    private static TextView logoutProfile, uploadProfilePicture;
    private static LinearLayout careerLayout;
    private HashMap<String, CareerOfUser> newListCareer;

    Calendar myCalendar = Calendar.getInstance();


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_profile);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initViews();
        loadProfile();
    }


    private void initViews() {
        careerBtn = findViewById(R.id.careerBtn);
        logoutProfile = findViewById(R.id.logoutProfile);
        uploadProfilePicture = findViewById(R.id.uploadProfilePicture);
        lastName = findViewById(R.id.lastName);
        middleName = findViewById(R.id.middleName);
        firstName = findViewById(R.id.firstName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        companyName = findViewById(R.id.companyName);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        birthday = findViewById(R.id.birthday);
        careerLayout = findViewById( R.id.careerLayout );


        birthday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageDetailScreen();
            }
        });
        uploadProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
                        new AddPhotoBottomDialogFragment( new OnClickPhotoBottomDialogListener() {
                            @Override
                            public void onUploadCameraClick() {
                                ContentValues values = new ContentValues();
                                values.put( MediaStore.Images.Media.TITLE, "New Picture");
                                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                                imageUri = getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values );
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, Utils.CAPTURE_IMAGE_PROFILEPICTURE_REQUEST_CODE);

                            }

                            @Override
                            public void onUploadPhotoClick() {
                                Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, Utils.IMAGE_PICKER_PROFILEPICTURE_REQUEST_CODE);
                            }
                        },true,true );
                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
            }

        });

        logoutProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        careerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openCarrerView();
            }
        });


    }

    private void openImageDetailScreen() {

        if(userProfile != null){
            if(userProfile.getprofilePicture() != null && ! userProfile.getprofilePicture() .isEmpty()){
                Intent intent = new Intent(this, GalleryViewActivity.class);
                String[] arrayImages = {userProfile.getprofilePicture() };
                intent.putExtra(GalleryViewActivity.IMAGE_URLS_EXTRA_KEY,arrayImages);
                startActivity(intent);

            }


        }

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthday.setText(sdf.format(myCalendar.getTime()));
    }

    private void loadProfile() {

        Context context = this;
        ProfileManager.getInstance(this).getProfileFromDataBase(userId, new OnGetObjectListener<Profile>() {
            @Override
            public void onGetObjectFinish(Profile profile) {

                ProfileManager.getInstance( context ).getCompanyProfileFromDataBase( userId, new OnGetObjectListener<Company>() {
                    @Override
                    public void onGetObjectFinish(Company company) {
                        companyProfile = company;
                        userProfile = profile;
                        if (userProfile != null) {
                            fillUserProfileField();
                        }else{
                            email.setText(mAuth.getCurrentUser().getEmail());
                            userProfile = ProfileManager.getInstance(context).buildProfile(mAuth.getCurrentUser());
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.ic_stub);
                        }
                        if (companyProfile != null) {
                            newListCareer = companyProfile.getCareers();
                            setListSelectedCareer(companyProfile.getCareers());
                            fillCompanyField();
                            fillCareerFields();
                        }else{
                            companyProfile = ProfileManager.getInstance(context).buildCompany(mAuth.getCurrentUser());
                        }
                    }

                } );

            }

        } );

    }

    private void setListSelectedCareer(HashMap<String, CareerOfUser> newListCareer) {

        if(newListCareer != null){
            CareerActivity.listSelectedCareer = new ArrayList<Career> ();
            for (String key : newListCareer.keySet()) {
                if(ProfileManager.getInstance( this).getListCareerMap().containsKey( key )){
                    CareerActivity.listSelectedCareer.add( ProfileManager.getInstance( this).getListCareerMap().get( key ) );

                }

            }

        }

    }

    private void fillCompanyField()
    {
        if(companyProfile.getName() != null){
            companyName.setText(companyProfile.getName());
        }

    }

    private void fillCareerFields() {

        careerLayout.removeAllViews();
        Locale current = getResources().getConfiguration().locale;
        String lang = current.getLanguage();
        if(newListCareer != null){
            for (String key : newListCareer.keySet()) {
                View view = LayoutInflater.from(this).inflate(R.layout.layout_editcareerdetail,null);
                TextView careerNameTextView = view.findViewById(R.id.careerNameTextView);
                EditText careerAbout= view.findViewById(R.id.careerAbout);
                careerAbout.setTag(key);
                String careerName =getResources().getString(R.string.General_NoInformation);
                String careerAboutText ="";
                if(ProfileManager.getInstance( this).getListCareerMap().containsKey( key )
                        && ProfileManager.getInstance( this).getListCareerMap().get( key ).getLanguages().get( lang ) != null ){
                    careerName = ProfileManager.getInstance( this).getListCareerMap().get( key ).getLanguages().get( lang ).getName();
                    if(newListCareer.get(key).getAbout() != null){
                        careerAboutText = newListCareer.get(key ).getAbout();
                    }

                }

                careerNameTextView.setText(careerName);
                careerAbout.setText(careerAboutText);
                careerLayout.addView(view);

            }

        }


    }

    private void fillUserProfileField()
    {
        if (userProfile.getprofilePicture() != null) {
            Glide.with(this)
                    .load(userProfile.getprofilePicture())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .error(R.drawable.ic_stub)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        }else {
            progressBar.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.ic_stub);
        }

        if(userProfile.getFirstName() != null)
        {
            firstName.setText(userProfile.getFirstName() );
        }
        if(userProfile.getLastName() != null)
        {
            lastName.setText(userProfile.getLastName() );
        }
        if(userProfile.getMiddleName() != null)
        {
            middleName.setText(userProfile.getMiddleName() );
        }
        if(userProfile.getPhone() != null)
        {
            phone.setText(userProfile.getPhone() );
        }
        if(userProfile.getEmail() != null)
        {
            email.setText(userProfile.getEmail() );
        }
        if(userProfile.getBirthday() != 0){
            String date = FormatterUtil.getDateWithDateFormat(userProfile.getBirthday()*1000);
            birthday.setText(date);
        }

    }




    @Override
    public ProgressBar getProgressView() {
        return progressBar;
    }

    @Override
    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public void onImagePikedAction() {
        startCropImageActivity();
    }

    private void attemptCreateProfile() {
        // Get all edittext texts

        String getFirstName = firstName.getText().toString();
        String getLastName = lastName.getText().toString();
        String getMiddleName = middleName.getText().toString();
        String getEmailId = email.getText().toString();
        String getMobileNumber = phone.getText().toString();
        String getCompanyName = companyName.getText().toString();
        String birthdayText = birthday.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFirstName.equals("") || getFirstName.length() == 0
                || getLastName.equals("") || getLastName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getCompanyName.equals("") || getCompanyName.length() == 0
                || birthdayText.equals("") || birthdayText.length() == 0
                || newListCareer == null || newListCareer.isEmpty()
                ) {
            new CustomToast().Show_Toast(this, this.findViewById(android.R.id.content), getResources().getString(R.string.General_AllFieldRequired));
            //showSnackBar(R.string.internet_connection_failed);
            return;
            // Check if email id valid or not

        }else if ( (userProfile == null && imageUri == null) || (userProfile.getprofilePicture() == null && imageUri == null )){
            new CustomToast().Show_Toast(this, this.findViewById(android.R.id.content), getResources().getString(R.string.General_ChoosePhoto));
            //showSnackBar(R.string.internet_connection_failed);
            return;
        }
        else if (!m.find()) {
            new CustomToast().Show_Toast(this,  this.findViewById(android.R.id.content),
                    getResources().getString(R.string.General_InvalidEmail));

            return;
        }
        // Else do signup or do your stuff
        else {
            showProgress();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date parsedDate = dateFormat.parse(birthday.getText().toString());
                long seconds = parsedDate.getTime()/1000;
                userProfile.setBirthday(seconds);

            } catch(Exception e) {
                hideProgress();
                new CustomToast().Show_Toast(this, this.findViewById(android.R.id.content), getResources().getString(R.string.General_InvalidBirthday));
                return;
                // look the origin of excption
            }
            userProfile.setFirstName(getFirstName);
            userProfile.setLastName(getLastName);
            if(!getMiddleName.equals("") && !(getMiddleName.length() == 0)){
                userProfile.setMiddleName(getMiddleName);
            }
            userProfile.setPhone(getMobileNumber);
            userProfile.setEmail(getEmailId);
            for (String key : newListCareer.keySet()) {
                EditText editText = this.findViewById(android.R.id.content).findViewWithTag(key);
                newListCareer.get( key ).setAbout( editText.getText().toString());

            }
            if(userProfile.getRegisterDate() == 0){
                userProfile.setRegisterDate( Calendar.getInstance().getTimeInMillis()/1000 ) ;
            }
            companyProfile.setCareers( newListCareer );
            companyProfile.setName(getCompanyName);
            ProfileManager.getInstance(this).createOrUpdateProfile(userProfile,  imageUri, this);


        }

    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 555) {
            if(resultCode == CareerActivity.RESULT_OK){
                CareerActivity.listSelectedCareer = data.getParcelableArrayListExtra(CareerActivity.LIST_CAREER);
                newListCareer = new HashMap<String, CareerOfUser>();
                for (Career element : CareerActivity.listSelectedCareer) {
                    CareerOfUser careerOfUser = new CareerOfUser();
                    careerOfUser.setId( element.getId() );
                    careerOfUser.setAbout( getCareerAboutText(element.getId()) );
                    newListCareer.put( element.getId(),careerOfUser );

                }
                fillCareerFields();
            }
            if (resultCode == CareerActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }else if (requestCode == Utils.IMAGE_PICKER_PROFILEPICTURE_REQUEST_CODE
                && resultCode == RESULT_OK) {
            imageUri = data.getData();
            if (imageUri != null) {
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(CreateProfileActivity.this)
                        .load(imageUri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .fitCenter()
                        .listener(new RequestListener<Uri, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                LogUtil.logDebug(TAG, "Glide Success Loading image from uri : " + imageUri.getPath());
                                return false;
                            }
                        })
                        .into(imageView);

            }
        }else if (requestCode == Utils.CAPTURE_IMAGE_PROFILEPICTURE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    progressBar.setVisibility(View.VISIBLE);
                    Glide.with(CreateProfileActivity.this)
                            .load(imageUri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .fitCenter()
                            .listener(new RequestListener<Uri, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    LogUtil.logDebug(TAG, "Glide Success Loading image from uri : " + imageUri.getPath());
                                    return false;
                                }
                            })
                            .into(imageView);

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    showSnackBar(R.string.error_fail_create_profile);

                }

            }
        }
        else{
            //handleCropImageResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onProfileCreated(boolean success) {
        hideProgress();

        if (success) {
            ProfileManager.getInstance(this).createOrUpdateCompany(companyProfile,  null, this);
        } else {
            showSnackBar(R.string.General_SomethingWrong);
        }
    }

    @Override
    public void onCompanyCreated(boolean success) {
        hideProgress();
        if (success) {
            setResult(RESULT_OK);
            finish();
            //showSnackBar(getResources().getString(R.string.General_Success));
        } else {
            showSnackBar(R.string.General_SomethingWrong);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.continueButton:
                if (hasInternetConnection()) {
                    attemptCreateProfile();
                } else {
                    showSnackBar(R.string.internet_connection_failed);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        LogoutHelper.signOut(null, this);
        setResult(RESULT_CANCELED);
        finish();


    }
    private void openCarrerView() {
        try {
            Intent k = new Intent(CreateProfileActivity.this, CareerActivity.class);
            k.putExtra( CareerActivity.USERID,userId );
            startActivityForResult(k,555);

        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    private String getCareerAboutText(String key){
        String careerAboutText ="";
        if(companyProfile.getCareers() != null && companyProfile.getCareers().get(key) != null
                && companyProfile.getCareers().get(key).getAbout() != null
                && !companyProfile.getCareers().get(key).getAbout().isEmpty()){
            careerAboutText = companyProfile.getCareers().get(key ).getAbout();
        }
        return careerAboutText;
    }

    @Override
    public void onBackPressed() {
        logout();

    }




}
