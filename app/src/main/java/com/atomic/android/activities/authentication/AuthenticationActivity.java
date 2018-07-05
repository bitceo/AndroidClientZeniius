package com.atomic.android.activities.authentication;


/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;

import com.atomic.android.R;
import com.atomic.android.activities.BaseActivity;
import com.atomic.android.activities.CustomToast;
import com.atomic.android.activities.MainActivity;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnGetObjectListener;
import com.atomic.android.managers.listeners.OnProfileAndCompanyGetListener;
import com.atomic.android.model.Company;
import com.atomic.android.model.Profile;
import com.atomic.android.utils.PreferencesUtil;
import com.atomic.android.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.Locale;
public class AuthenticationActivity extends BaseActivity {
	private static FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
	private static final int PERMISSION_ALL = 123;
	private Context context;
	String [] permissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE ,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE };
	private NewtonCradleLoading newtonCradleLoading;
	private LinearLayout loadingLayout;
	private Login_Fragment login_Fragment;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		fragmentManager = getSupportFragmentManager();
		mAuth = FirebaseAuth.getInstance();
		setContentView(R.layout.activity_authentication );

		newtonCradleLoading = findViewById(R.id.newton_cradle_loading);
		loadingLayout = findViewById(R.id.loadingLayout);

		changeLocaleNotRefesh("en","US");

		if (mAuth.getCurrentUser() != null) {
			loadingLayout.setVisibility(View.VISIBLE);
			newtonCradleLoading.start();
			checkIsProfileExist(mAuth.getCurrentUser().getUid());
		}else {
			replaceLoginFragment();
			if(!hasPermissions(AuthenticationActivity.this)){
				ActivityCompat.requestPermissions(AuthenticationActivity.this, permissions, PERMISSION_ALL);
			}
		}


	}
	public boolean hasPermissions(Context context) {
		if (context != null && permissions != null) {
			for (String permission : permissions) {
				if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
					return false;
				}
			}
		}
		return true;
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//No call for super(). Bug on API Level > 11.
	}


	public void changeLocaleNotRefesh(String localeText,String localeCountry )
	{
		Locale locale = new Locale(localeText,localeCountry);
		Locale.setDefault(locale);
		PreferencesUtil.setLanguage( this,localeText );
		Configuration config = getBaseContext().getResources().getConfiguration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());



	}

	public void changeLocale(String localeText,String localeCountry )
	{
		Locale locale = new Locale(localeText,localeCountry);
		Locale.setDefault(locale);
		PreferencesUtil.setLanguage( this,localeText );
		Configuration config = getBaseContext().getResources().getConfiguration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

		// Refresh main activity upon close of dialog box
		Intent refresh = new Intent(this, AuthenticationActivity.class);
		startActivity(refresh);
		this.finish();

	}

	public void signInWithEmailPassword(String getEmailId, String getPassword) {
		if (hasInternetConnection()) {
			showProgress();
			mAuth.signInWithEmailAndPassword(getEmailId, getPassword).addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {

					if (task.isSuccessful()) {

						checkIsProfileExist(task.getResult().getUser().getUid());

					} else {
						//loginLayout.startAnimation(shakeAnimation);
						hideProgress();
						new CustomToast().Show_Toast(AuthenticationActivity.this.getApplicationContext(), findViewById(android.R.id.content),
								"Invalid Email or Password");
					}
				}
			});

		} else {
			showSnackBar(R.string.internet_connection_failed);
		}
	}





	public void checkIsProfileExist(final String userId) {

		Context context = this;

		ProfileManager.getInstance(this).getProfileFromDataBase(userId, new OnGetObjectListener<Profile>() {
			@Override
			public void onGetObjectFinish(Profile profile) {

				ProfileManager.getInstance( context ).getCompanyProfileFromDataBase( userId, new OnGetObjectListener<Company>() {
					@Override
					public void onGetObjectFinish(Company company) {

						AsyncTask.execute( new Runnable() {
							@Override
							public void run() {
								//TODO your background code

								EmojiManager.install(new IosEmojiProvider());
								ProfileManager.getInstance( context ).preGetCareerList( new OnGetObjectListener() {
									@Override
									public void onGetObjectFinish(Object obj) {

									}
								});

							}
						});

						if(profile == null || company == null) {
							stopLoading();
							startCreateProfileActivity();
						}else if (profile.getFirstName() ==  null || profile.getFirstName().equals( "" ) ) {
							stopLoading();
							startCreateProfileActivity();
						}else if (profile.getLastName() ==  null || profile.getLastName().equals( "" ) ) {
							stopLoading();
							startCreateProfileActivity();
						}else if (profile.getEmail() ==  null || profile.getEmail().equals( "" ) ) {
							stopLoading();
							startCreateProfileActivity();
						}else if (profile.getPhone() ==  null || profile.getPhone().equals( "" ) ) {
							stopLoading();
							startCreateProfileActivity();
						}else if (profile.getprofilePicture() ==  null || profile.getprofilePicture().equals( "" ) ) {
							stopLoading();
							startCreateProfileActivity();
						}else if (profile.getBirthday() == 0) {
							stopLoading();
							startCreateProfileActivity();
						}else if (company.getName() ==  null ||company.getName().equals( "" ) ) {
							stopLoading();
							startCreateProfileActivity();
						}else if (company.getCareers() ==  null ||company.getCareers().isEmpty() ) {
							stopLoading();
							startCreateProfileActivity();
						}else {

								PreferencesUtil.setProfileCreated(AuthenticationActivity.this, true);
								preLoadDataAndStartMainActivity(userId, profile);


						}


					}

				} );

			}

		} );
	}
	private void stopLoading(){
		loadingLayout.setVisibility(View.GONE);
		newtonCradleLoading.stop();
		hideProgress();
	}

	public void preLoadDataAndStartMainActivity(final String userId, Profile profile){


		ProfileManager.getInstance( context ).preGetListProfileAndCompany( new OnProfileAndCompanyGetListener() {
			@Override
			public void onProfileAndCompanyGet(boolean success) {

				stopLoading();
				startMainActivity();

			}

		});

	}

	public void startCreateProfileActivity() {

		Intent intent = new Intent(AuthenticationActivity.this, CreateProfileActivity.class);
		startActivityForResult(intent, Utils.CREATE_PROFILE_ACTIVITY_REQUEST_CODE);

	}

	protected void startMainActivity() {

		Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
		startActivity(intent);

	}

	public void replaceLoginFragment() {
		login_Fragment = new Login_Fragment();
		fragmentManager
				.beginTransaction()
				.setCustomAnimations( android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out )
				.replace(R.id.frameAuthenticationContainer, login_Fragment,
						Utils.Login_Fragment).commit();
	}





	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// getIntent() should always return the most recent
		int i = intent.getIntExtra("FLAG", 0);
		if(i == 0)
			finish();
	}

	@Override
	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// handle result of pick image chooser
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Utils.CREATE_PROFILE_ACTIVITY_REQUEST_CODE){
			if( resultCode == RESULT_OK) {
				checkIsProfileExist(mAuth.getCurrentUser().getUid());

			}else if( resultCode == RESULT_CANCELED) {
				replaceLoginFragment();
			}

		}
	}

	@Override
	public void onBackPressed() {
		fragmentManager = getSupportFragmentManager();

		SignUp_Fragment signUp_Fragment = (SignUp_Fragment) fragmentManager.findFragmentByTag( Utils.SignUp_Fragment );
		ForgotPassword_Fragment forgotPassword_Fragment = (ForgotPassword_Fragment) fragmentManager.findFragmentByTag( Utils.ForgotPassword_Fragment );

		if (signUp_Fragment != null && signUp_Fragment.isVisible()) {
			replaceLoginFragmentNoBackStack();

		}else if (forgotPassword_Fragment != null && forgotPassword_Fragment.isVisible()) {
			replaceLoginFragmentNoBackStack();

		}else {
			super.onBackPressed();
			finish();
		}
	}





	public void replaceLoginFragmentNoBackStack(){

		Login_Fragment fragobj = new Login_Fragment();
		fragmentManager.beginTransaction().setCustomAnimations( android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out )
				.replace( R.id.frameAuthenticationContainer, fragobj,
						Utils.Login_Fragment )
				.addToBackStack(null).commit();
	}




}


