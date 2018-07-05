package com.atomic.android.activities.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.atomic.android.R;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnProfileCreatedListener;
import com.atomic.android.model.Profile;
import com.atomic.android.utils.FormatterUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditGeneralUserProfile_Fragment extends Fragment implements OnProfileCreatedListener {
	public static final String USERID = "EditGeneralUserProfile_Fragment.USERID";
	private static View view;
    private static EditText firstName, lastName, middleName,birthday,about;
	private String userId;
	private ProfileManager profileManager;
	private Profile userProfile;
	Calendar myCalendar = Calendar.getInstance();
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			// TODO Auto-generated method stub
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateBirthdayEditText();
		}

	};

	private void updateBirthdayEditText() {
		String myFormat = "dd/MM/yyyy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		birthday.setText(sdf.format(myCalendar.getTime()));
	}



	public EditGeneralUserProfile_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_editgeneraluserprofile, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.General_updateProfile));
		userId = getArguments().getString(USERID);
		setHasOptionsMenu(true);
		initViews();
		return view;
	}


	private void initViews() {

		firstName = view.findViewById( R.id.firstName );
		lastName = view.findViewById( R.id.lastName );
		middleName = view.findViewById( R.id.middleName );
		about = view.findViewById( R.id.about );
		birthday = view.findViewById( R.id.birthday );

		loadProfile();

		birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(getActivity(), date, myCalendar
						.get( Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});



	}


	private void loadProfile() {
		profileManager = ProfileManager.getInstance(getActivity());
		userProfile = profileManager.getProfileAndCompany(userId ).getProfile();
		fillUIFields();

	}



	private void fillUIFields() {

		if (userProfile != null) {

			if(userProfile.getAbout() != null && !userProfile.getAbout().isEmpty()){
				about.setText(userProfile.getAbout());
			}

			if(userProfile.getFirstName() != null && !userProfile.getFirstName().isEmpty()){
				firstName.setText(userProfile.getFirstName());
			}

			if(userProfile.getMiddleName() != null && !userProfile.getMiddleName().isEmpty()){
				middleName.setText(userProfile.getMiddleName());
			}

			if(userProfile.getLastName() != null && !userProfile.getLastName().isEmpty()){
				lastName.setText(userProfile.getLastName());
			}

			if(userProfile.getBirthday() != 0){
				String date = FormatterUtil.getDateWithDateFormat(userProfile.getBirthday()*1000);
				birthday.setText(date);
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
				updateEditGeneralUserProfile();
				break;
		}
		return true;

	}
	private void updateEditGeneralUserProfile(){


		if(firstName.getText() != null && !firstName.getText().equals( "" )){
			userProfile.setFirstName( firstName.getText().toString());
		}
		if(middleName.getText() != null && !middleName.getText().equals( "" )){
			userProfile.setMiddleName( middleName.getText().toString());
		}
		if(lastName.getText() != null && !lastName.getText().equals( "" )){
			userProfile.setLastName( lastName.getText().toString());
		}
		if(about.getText() != null && !about.getText().equals( "" )){
			userProfile.setAbout( about.getText().toString());
		}
		if(birthday.getText() != null && !birthday.getText().equals( "" )){

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date parsedDate = dateFormat.parse(birthday.getText().toString());
				long seconds = parsedDate.getTime()/1000;
				userProfile.setBirthday(seconds);

			} catch(Exception e) { //this generic but you can control another types of exception
				return;
				// look the origin of excption
			}
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
