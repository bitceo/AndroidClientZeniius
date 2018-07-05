package com.atomic.android.activities.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.atomic.android.R;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnProfileCreatedListener;
import com.atomic.android.model.Profile;

public class EditContactUserProfile_Fragment extends Fragment implements OnProfileCreatedListener {
	public static final String USERID = "EditContactUserProfile_Fragment.USERID";
	private static View view;
    private static EditText phone, facebook, email;
	private String userId;
	private ProfileManager profileManager;
	private Profile userProfile;

	public EditContactUserProfile_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_editcontactuserprofile, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.General_updateProfile));
		userId = getArguments().getString(USERID);
		setHasOptionsMenu(true);
		initViews();
		return view;
	}


	private void initViews() {

		phone = view.findViewById( R.id.phone );
		email = view.findViewById( R.id.email );
		facebook = view.findViewById( R.id.facebook );


		loadProfile();

	}


	private void loadProfile() {

		profileManager = ProfileManager.getInstance(getActivity());
		userProfile = profileManager.getProfileAndCompany(userId ).getProfile();
		fillUIFields();

	}



	private void fillUIFields() {

		if (userProfile != null) {

			if(userProfile.getPhone() != null && !userProfile.getPhone().isEmpty()){
				phone.setText(userProfile.getPhone());
			}

			if(userProfile.getEmail() != null && !userProfile.getEmail().isEmpty()){
				email.setText(userProfile.getEmail());
			}

			if(userProfile.getFacebook() != null && !userProfile.getFacebook().isEmpty()){
				facebook.setText(userProfile.getFacebook());
			}



		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Do something that differs the Activity's menu here
		inflater.inflate(R.menu.save_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.saveButton:
				updateEditContactCompany();
				break;
		}
		return true;

	}

	private void updateEditContactCompany(){


		if(phone.getText() != null && !phone.getText().equals( "" )){
			userProfile.setPhone( phone.getText().toString());
		}
		if(facebook.getText() != null && !facebook.getText().equals( "" )){
			userProfile.setFacebook( facebook.getText().toString());
		}
		if(email.getText() != null && !email.getText().equals( "" )){
			userProfile.setEmail( email.getText().toString());
		}

		ProfileManager.getInstance(getActivity()).createOrUpdateProfile(userProfile,  null, this);
	}


	@Override
	public void onProfileCreated(boolean success) {
		View view = this.getActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService( Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		if (success) {
			((UserProfileDetailActivity)getActivity()).replaceUserDetailFragmentNoBackStack();
			((UserProfileDetailActivity)getActivity()).showSnackBarSuccess(getResources().getString(R.string.General_Success));

		} else {
			((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);
		}
	}
}
