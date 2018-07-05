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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atomic.android.R;
import com.atomic.android.activities.CustomToast;
import com.atomic.android.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ForgotPassword_Fragment extends Fragment implements
		OnClickListener {
	private static View view;

	private static EditText emailId;
	private static TextView submit, back;

	public ForgotPassword_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.forgotpassword_layout, container,
				false);
		initViews();
		setListeners();
		return view;
	}

	// Initialize the views
	private void initViews() {
		emailId = view.findViewById(R.id.registered_emailid);
		submit = view.findViewById(R.id.forgot_button);
		back = view.findViewById(R.id.backToLoginBtn);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			back.setTextColor(csl);
			submit.setTextColor(csl);

		} catch (Exception e) {
		}

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

			// Replace Login Fragment on Back Presses
			((AuthenticationActivity)getActivity()).replaceLoginFragment();
			break;

		case R.id.forgot_button:

			// Call Submit button task
			submitButtonTask();
			break;

		}

	}

	private void submitButtonTask() {
		String getEmailId = emailId.getText().toString();

		// Pattern for email id validation
		Pattern p = Pattern.compile(Utils.regEx);

		// Match the pattern
		Matcher m = p.matcher(getEmailId);

		// First check if email id is not null else show error toast
		if (getEmailId == null || getEmailId.equals("") || getEmailId.length() == 0){
            new CustomToast().Show_Toast(getActivity(), view,
					getResources().getString(R.string.General_EmailEmpty));

            return;
        }
		else if (!m.find()){
            new CustomToast().Show_Toast(getActivity(), view,
					getResources().getString(R.string.General_InvalidEmail));
            return;
        }else
        {
            ((AuthenticationActivity)getActivity()).showProgress();
            FirebaseAuth.getInstance().sendPasswordResetEmail(getEmailId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ((AuthenticationActivity)getActivity()).hideProgress();
                            if (task.isSuccessful()) {

                                Toast.makeText(getActivity(), getResources().getString(R.string.General_CheckEmail),
                                        Toast.LENGTH_SHORT).show();
                                //new MainActivity().replaceLoginFragment();
                            }else{

                                new CustomToast().Show_Toast(getActivity(), view,
										getResources().getString(R.string.General_SomethingWrong));
                            }
                        }
                    });

        }


	}
}