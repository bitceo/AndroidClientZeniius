package com.atomic.android.activities.more;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.BaseActivity;
import com.atomic.android.activities.CustomToast;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnPasswordChangedListener;

public class ChangePasswordActivity extends BaseActivity implements OnClickListener, OnPasswordChangedListener {


	private static EditText oldpassword, newpassword, retypenewpassword;
	private static TextView submit, back;
	private static ProgressBar progressBar;
	public ChangePasswordActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepassword_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initViews();
		setListeners();
	}


	// Initialize the views
	private void initViews() {
		oldpassword = findViewById(R.id.oldpassword);
		newpassword = findViewById(R.id.newpassword);
		retypenewpassword = findViewById(R.id.retypenewpassword);
		submit = findViewById(R.id.forgot_button);
		back = findViewById(R.id.backToLoginBtn);
		progressBar = findViewById(R.id.progressBar);


	}

	// Set Listeners over buttons
	private void setListeners() {
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backToLoginBtn:

			finish();
			break;

		case R.id.forgot_button:

			// Call Submit button task
			submitButtonTask();
			break;

		}

	}

	// Check Validation Method
	private boolean checkValidation(String oldPassword, String newPassword, String retypeNewPassword){

		// Check for both field is empty or not
		if (oldPassword.equals("") || oldPassword.length() == 0
				|| newPassword.equals("") || newPassword.length() == 0
				|| retypeNewPassword.equals("") || retypeNewPassword.length() == 0) {
			//loginLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(this, findViewById(android.R.id.content),
					getResources().getString(R.string.General_EnterBothCredentials));
			return false;

		}else if(! newPassword.equals( retypeNewPassword )){
			new CustomToast().Show_Toast(this, findViewById(android.R.id.content),
					getResources().getString(R.string.General_TwoPassDoesNotMatch));
			return false;
		}

		return true;

	}

	private void submitButtonTask() {
		if (hasInternetConnection()) {
			String newPass = newpassword.getText().toString();
			String oldPass = oldpassword.getText().toString();
			String retypePass = retypenewpassword.getText().toString();
			if(checkValidation(oldPass, newPass, retypePass)){
				progressBar.setVisibility(View.VISIBLE);
				back.setVisibility(View.GONE);
				submit.setVisibility(View.GONE);
				ProfileManager.getInstance( this ).changePasssword(oldPass,newPass, this  );
			}

		} else {
			showSnackBar(R.string.internet_connection_failed);
		}

	}

	@Override
	public void onPasswordChanged(boolean success) {
		progressBar.setVisibility(View.GONE);
		back.setVisibility(View.VISIBLE);
		submit.setVisibility(View.VISIBLE);
		if(success){
			showSnackBarSuccess(getResources().getString(R.string.General_Success));
			finish();

		}else{
			new CustomToast().Show_Toast(this, findViewById(android.R.id.content),
					getResources().getString(R.string.General_WrongOldPass));
		}
	}
}