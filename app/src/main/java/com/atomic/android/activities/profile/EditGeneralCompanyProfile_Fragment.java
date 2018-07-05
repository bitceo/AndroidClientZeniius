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
import com.atomic.android.managers.listeners.OnCompanyCreatedListener;
import com.atomic.android.model.Company;
public class EditGeneralCompanyProfile_Fragment extends Fragment  implements OnCompanyCreatedListener {
	public static final String USERID = "EditGeneralCompanyProfile_Fragment.USERID";
	private static View view;
    private static EditText name, tax, position,about;
	private String userId;
	private ProfileManager profileManager;
	private Company companyProfile;

	public EditGeneralCompanyProfile_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_editgeneralcompanyprofile, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.General_updateCompany));
		userId = getArguments().getString(USERID);
		setHasOptionsMenu(true);
		initViews();
		return view;
	}


	private void initViews() {

		name = view.findViewById( R.id.name );
		tax = view.findViewById( R.id.tax );
		position = view.findViewById( R.id.position );
		about = view.findViewById( R.id.about );

		loadProfile();

	}


	private void loadProfile() {
		profileManager = ProfileManager.getInstance(getActivity());
		companyProfile = profileManager.getProfileAndCompany(userId ).getCompany();
		fillUIFields();
	}


	private void fillUIFields() {

		if (companyProfile != null) {

			if (companyProfile.getAbout() != null && !companyProfile.getAbout().isEmpty()) {
				about.setText( companyProfile.getAbout() );
			}

			if (companyProfile.getPos() != null && !companyProfile.getPos().isEmpty()) {
				position.setText( companyProfile.getPos() );
			}

			if (companyProfile.getName() != null && !companyProfile.getName().isEmpty()) {
				name.setText( companyProfile.getName() );
			}

			if (companyProfile.getMasothue() != null && !companyProfile.getMasothue().isEmpty()) {
				tax.setText( companyProfile.getMasothue() );
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
				updateEditGeneralCompany();
				break;
		}
		return true;

	}

	private void updateEditGeneralCompany(){


		if(name.getText() != null && !name.getText().equals( "" )){
			companyProfile.setName( name.getText().toString());
		}
		if(tax.getText() != null && !tax.getText().equals( "" )){
			companyProfile.setMasothue( tax.getText().toString());
		}
		if(position.getText() != null && !position.getText().equals( "" )){
			companyProfile.setPos( position.getText().toString());
		}
		if(about.getText() != null && !about.getText().equals( "" )){
			companyProfile.setAbout( about.getText().toString());
		}

		ProfileManager.getInstance(getActivity()).createOrUpdateCompany(companyProfile,  null, this);
	}


	@Override
	public void onCompanyCreated(boolean success) {
		// Check if no view has focus:
		View view = this.getActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService( Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		if (success) {
			((UserProfileDetailActivity)getActivity()).replaceCompanyDetailFragmentNoBackStack();
			((UserProfileDetailActivity)getActivity()).showSnackBarSuccess(getResources().getString(R.string.General_Success));

		} else {
			((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);
		}
	}
}
