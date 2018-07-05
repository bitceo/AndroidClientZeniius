package com.atomic.android.activities.authentication;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.CustomToast;
import com.atomic.android.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Fragment extends Fragment implements OnClickListener  {
	private static View view;
	private static EditText  emailId,
			password, confirmPassword;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	private FirebaseAuth mAuth;
	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
		initViews();
		setListeners();
		return view;
	}


    // Initialize all views
	private void initViews() {
		emailId = view.findViewById(R.id.userEmailId);
		password = view.findViewById(R.id.password);
		confirmPassword = view.findViewById(R.id.confirmPassword);
		signUpButton = view.findViewById(R.id.signUpBtn);
		login = view.findViewById(R.id.already_user);
		terms_conditions = view.findViewById(R.id.terms_conditions);


		mAuth = FirebaseAuth.getInstance();
		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}


	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.signUpBtn:

				checkValidationAndCreateAccount();
                break;

            case R.id.already_user:
				((AuthenticationActivity)getActivity()).replaceLoginFragment();
                break;
        }


    }



	// Check Validation Method
	private void checkValidationAndCreateAccount(){

		// Get all edittext texts

		String getEmailId = emailId.getText().toString();

		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();

		// Pattern match for email id
		Pattern p = Pattern.compile(Utils.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0){
			new CustomToast().Show_Toast(getActivity(), view,
					getResources().getString(R.string.General_AllFieldRequired));
		}
		else if (!m.find()){
			new CustomToast().Show_Toast(getActivity(), view,
					getResources().getString(R.string.General_InvalidEmail));
		}
		else if (!getConfirmPassword.equals(getPassword)){
			new CustomToast().Show_Toast(getActivity(), view,
					getResources().getString(R.string.General_TwoPassDoesNotMatch));
		}
		else if (!terms_conditions.isChecked()){
			new CustomToast().Show_Toast(getActivity(), view,
					getResources().getString(R.string.General_CheckedTerms));
		}
		else {
			((AuthenticationActivity)getActivity()).showProgress();
			mAuth.createUserWithEmailAndPassword(getEmailId, getPassword)
					.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {

							if (task.isSuccessful()) {
								FirebaseUser user = mAuth.getCurrentUser();
								((AuthenticationActivity)getActivity()).checkIsProfileExist(user.getUid());
								//getActivity().finish();
							} else {
								// If sign in fails, display a message to the user.
								((AuthenticationActivity)getActivity()).hideProgress();
								new CustomToast().Show_Toast(getActivity(), view,
										getResources().getString(R.string.General_SomethingWrong));

							}

						}
					});

		}


	}

}
