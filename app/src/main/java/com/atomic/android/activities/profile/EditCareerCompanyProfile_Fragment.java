package com.atomic.android.activities.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.CareerActivity;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnCompanyCreatedListener;
import com.atomic.android.model.Career;
import com.atomic.android.model.CareerOfUser;
import com.atomic.android.model.Company;

import java.util.ArrayList;
import java.util.HashMap;

public class EditCareerCompanyProfile_Fragment extends Fragment  implements OnCompanyCreatedListener {
	public static final String USERID = "EditCareerCompanyProfile_Fragment.USERID";
	private static View view;
    private String userId;
	private ProfileManager profileManager;
	private Company companyProfile;
	private LinearLayout careerLayout;
	private Button careerBtn;

	private HashMap<String, CareerOfUser> newListCareer;
	public EditCareerCompanyProfile_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_editcareercompanyprofile, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.EditCareerCompanyProfileFragment_title));
		userId = getArguments().getString(USERID);
		setHasOptionsMenu(true);
		initViews();
		return view;
	}


	private void initViews() {

		careerLayout = view.findViewById( R.id.careerLayout );
		careerBtn = view.findViewById( R.id.careerBtn);
		careerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openCarrerView();
			}
		});
		loadProfile();

	}


	private void loadProfile() {
		profileManager = ProfileManager.getInstance(getActivity());
		companyProfile = profileManager.getProfileAndCompany(userId ).getCompany();
		if (companyProfile != null) {
			newListCareer = companyProfile.getCareers();
			setListSelectedCareer(companyProfile.getCareers());
			fillUIFields();
		}
	}

	private void setListSelectedCareer(HashMap<String, CareerOfUser> newListCareer) {

		if(newListCareer != null){
			CareerActivity.listSelectedCareer = new ArrayList<Career> ();
			for (String key : newListCareer.keySet()) {
				if(ProfileManager.getInstance( getActivity()).getListCareerMap().containsKey( key )){
					CareerActivity.listSelectedCareer.add( ProfileManager.getInstance( getActivity()).getListCareerMap().get( key ) );

				}

			}

		}


	}

	private void fillUIFields() {

		careerLayout.removeAllViews();
		if(newListCareer != null){
			for (String key : newListCareer.keySet()) {
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_editcareerdetail,null);
				TextView careerNameTextView = view.findViewById(R.id.careerNameTextView);
				EditText careerAbout= view.findViewById(R.id.careerAbout);
				careerAbout.setTag(key);
				String careerName =getResources().getString(R.string.General_NoInformation);
				String careerAboutText ="";
				if(ProfileManager.getInstance( getActivity()).getListCareerMap().containsKey( key )){
					careerName = ProfileManager.getInstance( getActivity()).getListCareerMap().get( key ).getName();
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
				updateEditCareerCompany();
				break;
		}
		return true;

	}

	private void updateEditCareerCompany(){

		for (String key : newListCareer.keySet()) {
			EditText editText = view.findViewWithTag(key);
			newListCareer.get( key ).setAbout( editText.getText().toString());

		}
		companyProfile.setCareers( newListCareer );
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

	private void openCarrerView() {
		try {
			Intent k = new Intent(getActivity(), CareerActivity.class);
			k.putExtra( CareerActivity.USERID,userId );

			startActivityForResult(k,555);
		} catch(Exception e) {
			e.printStackTrace();
		}


	}

	@Override
	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
				fillUIFields();


			}
			if (resultCode == CareerActivity.RESULT_CANCELED) {
				//Write your code if there's no result
			}
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


}
