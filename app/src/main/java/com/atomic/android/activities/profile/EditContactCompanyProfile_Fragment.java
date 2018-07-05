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
import com.atomic.android.utils.FormatterUtil;

import org.angmarch.views.NiceSpinner;
public class EditContactCompanyProfile_Fragment extends Fragment implements OnCompanyCreatedListener {
	public static final String USERID = "EditContactCompanyProfile_Fragment.USERID";
	private static View view;
    private static EditText address, phone, email, facebook,website, city, district ;
	private String userId;
	private ProfileManager profileManager;
	private Company companyProfile;
	protected NiceSpinner country;
	public EditContactCompanyProfile_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_editcontactcompanyprofile, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.General_updateCompany));
		userId = getArguments().getString(USERID);
		setHasOptionsMenu(true);
		initViews();
		return view;
	}


	private void initViews() {

		address = view.findViewById( R.id.address );
		phone = view.findViewById( R.id.phone );
		email = view.findViewById( R.id.email );
		facebook = view.findViewById( R.id.facebook );
		website = view.findViewById( R.id.website );
		city = view.findViewById( R.id.city );
		district = view.findViewById( R.id.district );
		country = view.findViewById(R.id.country);

		country.attachDataSource( FormatterUtil.getListCountry(getActivity()));

		loadProfile();

	}


	private void loadProfile() {
		profileManager = ProfileManager.getInstance(getActivity());
		companyProfile = profileManager.getProfileAndCompany(userId ).getCompany();
		fillUIFields();
	}


	private void fillUIFields() {

		if (companyProfile != null) {
			if (companyProfile.getCountry() != null && !companyProfile.getCountry().isEmpty()) {
				if(FormatterUtil.getListCountry(getActivity()).contains( companyProfile.getCountry() )){
					int position = FormatterUtil.getListCountry(getActivity()).indexOf( companyProfile.getCountry());
					country.setSelectedIndex( position );
				}else{
					country.setSelectedIndex( 0 );
				}

			}else{
				country.setSelectedIndex( 0 );
			}

			if (companyProfile.getAddress() != null && !companyProfile.getAddress().isEmpty()) {
				address.setText( companyProfile.getAddress() );
			}
			if (companyProfile.getCity() != null && !companyProfile.getCity().isEmpty()) {
				city.setText( companyProfile.getCity() );
			}

			if (companyProfile.getDistrict() != null && !companyProfile.getDistrict().isEmpty()) {
				district.setText( companyProfile.getDistrict() );
			}

			if (companyProfile.getPhone() != null && !companyProfile.getPhone().isEmpty()) {
				phone.setText( companyProfile.getPhone() );
			}

			if (companyProfile.getEmail() != null && !companyProfile.getEmail().isEmpty()) {
				email.setText( companyProfile.getEmail() );
			}

			if (companyProfile.getFacebook() != null && !companyProfile.getFacebook().isEmpty()) {
				facebook.setText( companyProfile.getFacebook() );
			}
			if (companyProfile.getWebsite() != null && !companyProfile.getWebsite().isEmpty()) {
				website.setText( companyProfile.getWebsite() );
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

		if(country.getSelectedIndex() != 0)
		{
			String countryText = FormatterUtil.getListCountry(getActivity()).get(country.getSelectedIndex() );
			companyProfile.setCountry( countryText );

		}
		if(city.getText() != null && !city.getText().equals( "" )){
			companyProfile.setCity( city.getText().toString());
		}
		if(district.getText() != null && !district.getText().equals( "" )){
			companyProfile.setDistrict( district.getText().toString());
		}
		if(facebook.getText() != null && !facebook.getText().equals( "" )){
			companyProfile.setFacebook( facebook.getText().toString());
		}
		if(website.getText() != null && !website.getText().equals( "" )){
			companyProfile.setWebsite( website.getText().toString());
		}
		if(address.getText() != null && !address.getText().equals( "" )){
			companyProfile.setAddress( address.getText().toString());
		}
		if(phone.getText() != null && !phone.getText().equals( "" )){
			companyProfile.setPhone( phone.getText().toString());
		}
		if(email.getText() != null && !email.getText().equals( "" )){
			companyProfile.setEmail( email.getText().toString());
		}
		ProfileManager.getInstance(getActivity()).createOrUpdateCompany(companyProfile,  null, this);
	}


	@Override
	public void onCompanyCreated(boolean success) {
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
