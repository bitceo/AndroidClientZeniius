package com.atomic.android.activities.more;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.MainActivity;
import com.atomic.android.activities.ProfileActivity;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnClickLanguageBottomDialogListener;
import com.atomic.android.model.Profile;
import com.atomic.android.utils.LogoutHelper;
import com.atomic.android.views.CircularImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class More_Fragment extends Fragment {
	private static View view;
	private FirebaseAuth mAuth;
	private static TextView nameTextView, chinhsachTextView,thoathuanTextView,languageTextView ;
	private static TextView aboutZeniiusTextView;
	private static TextView changePasswordTextView;
	private static TextView logoutTextView;
	private static RelativeLayout profileLayout;
	private String userId;
	private static CircularImageView ceoImageView;
	private static RelativeLayout languageLayout;
	private ProfileManager profileManager;
	private static ImageView languageFlag;
	Profile userProfile;
	public More_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_more, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
		((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.MoreFragment_title));
		mAuth = FirebaseAuth.getInstance();
		userId = mAuth.getUid();
		initViews();
		return view;
	}


	private void initViews() {

		nameTextView = view.findViewById( R.id.nameTextView );
		aboutZeniiusTextView = view.findViewById( R.id.aboutZeniiusTextView );
		changePasswordTextView = view.findViewById( R.id.changePasswordTextView );
		chinhsachTextView = view.findViewById( R.id.chinhsachTextView );
		thoathuanTextView = view.findViewById( R.id.thoathuanTextView );
		logoutTextView = view.findViewById( R.id.logoutTextView );
		languageTextView = view.findViewById( R.id.languageTextView );
		ceoImageView = view.findViewById( R.id.ceoImageView );
		profileLayout = view.findViewById( R.id.profileLayout );
		languageFlag = view.findViewById(R.id.languageFlag);
		languageLayout = view.findViewById(R.id.languageLayout);
		profileLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openProfileDetail();
			}
		});

		logoutTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logout();
			}
		});

		changePasswordTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changePassword();
			}
		});

		aboutZeniiusTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aboutZeniius();
			}
		});

		Locale current = getResources().getConfiguration().locale;

		if(current.getLanguage() == "en") {
			languageFlag.setBackgroundResource(R.drawable.us);

		}else if(current.getLanguage() == "vi") {
			languageFlag.setBackgroundResource(R.drawable.vn);
		}else {
			languageFlag.setBackgroundResource(R.drawable.vn);
			vietnameseClick();
		}

		languageLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LanguageBottomDialogFragment addPhotoBottomDialogFragment =
						new LanguageBottomDialogFragment( new OnClickLanguageBottomDialogListener() {

							@Override
							public void onEnglishClick() {
								englishClick();

							}

							@Override
							public void onVietnameseClick() {
								vietnameseClick();
							}
						} );
				addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
						"language_dialog_fragment");
			}

		});
		loadProfile();
	}

	private void englishClick()
	{


		Locale current = getResources().getConfiguration().locale;
		if(current.getLanguage() != "en"){

			((MainActivity)getActivity()).changeLocale("en","US");
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.setCustomAnimations( android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out )
                    .detach(this).attach(this).commit();
		}
	}

	private void vietnameseClick()
	{

		Locale current = getResources().getConfiguration().locale;
		if(current.getLanguage() != "vi"){
			((MainActivity)getActivity()).changeLocale("vi","VN");
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.setCustomAnimations( android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out )
                    .detach(this).attach(this).commit();
		}
	}

	private void logout() {
		LogoutHelper.signOut(null, this);
		((MainActivity)getActivity()).startAuthenticationActivity();
	}

	private void changePassword() {
		((MainActivity)getActivity()).startChangePasswordActivity();
	}

	private void aboutZeniius() {
		((MainActivity)getActivity()).startAboutZeniiusActivity();
	}



	private void openProfileDetail() {

		if(userProfile != null){
			Intent intent = new Intent(getActivity(), ProfileActivity.class);
			intent.putExtra(ProfileActivity.USER_ID_EXTRA_KEY, userId);
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {
				View authorImageView = view.findViewById(R.id.ceoImageView);
				ActivityOptions options = ActivityOptions.
						makeSceneTransitionAnimation(getActivity(),
								new android.util.Pair<>(authorImageView, getString(R.string.post_author_image_transition_name)));
				startActivity(intent, options.toBundle());
			} else {
				startActivity(intent);
			}

		}

	}


	private void loadProfile() {
		profileManager = ProfileManager.getInstance(getActivity());
		if(profileManager.getProfileAndCompany( userId ) != null){
			userProfile = profileManager.getProfileAndCompany( userId ).getProfile();
			fillUIFields();
		}

	}



	private void fillUIFields() {
		if (userProfile != null) {
			if(userProfile.getFullName() != null && !userProfile.getFullName().isEmpty()){
				nameTextView.setText(userProfile.getFullName());
			}else{
				nameTextView.setText(getResources().getString(R.string.General_NoInformation));
			}

			if (userProfile.getprofilePicture() != null) {
				Glide.with(this)
						.load(userProfile.getprofilePicture())
						.diskCacheStrategy( DiskCacheStrategy.SOURCE)
						.crossFade()
						.error(R.drawable.ic_stub)
						.into(ceoImageView);
			} else {
				ceoImageView.setImageResource(R.drawable.ic_stub);
			}


		}
	}

}
