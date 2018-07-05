package com.atomic.android.activities.authentication;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.LoginBase;
import com.atomic.android.activities.more.LanguageBottomDialogFragment;
import com.atomic.android.managers.listeners.OnClickLanguageBottomDialogListener;
import com.atomic.android.utils.LogUtil;
import com.atomic.android.utils.PreferencesUtil;
import com.atomic.android.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Login_Fragment extends Fragment implements LoginBase {
    private static final String TAG = Login_Fragment.class.getSimpleName();
    private static FragmentManager fragmentManager;

    private static View view;
    FirebaseAuth mAuth;

    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp, language;
    private static ImageView languageFlag;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    public Login_Fragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login, container, false);
        initViews();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        setListeners();

        return view;
    }




    private void initViews()
    {
        fragmentManager = getActivity().getSupportFragmentManager();
        emailid = view.findViewById(R.id.login_emailid);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.createAccount);
        show_hide_password = view.findViewById(R.id.show_hide_password);
        loginLayout = view.findViewById(R.id.login_layout);
        language = view.findViewById(R.id.language);
        languageFlag = view.findViewById(R.id.languageFlag);


        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            signUp.setTextColor(csl);
            language.setTextColor(csl);
        } catch (Exception e) {
        }

        String lang = PreferencesUtil.getLanguage(getActivity());

        if(lang.equals( "en" )) {
            languageFlag.setBackgroundResource(R.drawable.us);

        }else if(lang.equals( "vi" )) {
            languageFlag.setBackgroundResource(R.drawable.vn);
        }else {
            languageFlag.setBackgroundResource(R.drawable.us);
            englishClick();
        }

        languageFlag.setOnClickListener(new View.OnClickListener() {
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

        language.setOnClickListener(new View.OnClickListener() {
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


    }

    private void englishClick()
    {
        Locale current = getResources().getConfiguration().locale;
        if(current.getLanguage() != "en"){
            ((AuthenticationActivity)getActivity()).changeLocale("en","US");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations( android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out )
                    .detach(this).commit();
        }
    }

    private void vietnameseClick()
    {
        Locale current = getResources().getConfiguration().locale;
        if(current.getLanguage() != "vi"){
            ((AuthenticationActivity)getActivity()).changeLocale("vi","VN");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations( android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out )
                    .detach(this).commit();
        }
    }





    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Configure firebase auth
        mAuth = FirebaseAuth.getInstance();
        // Set check listener over checkbox for showing and hiding password
        show_hide_password
            .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton button,
                                             boolean isChecked) {

                    // If it is checkec then show password else hide
                    // password
                    if (isChecked) {

                        show_hide_password.setText(R.string.General_hidepassword);// change
                        // checkbox
                        // text

                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        password.setTransformationMethod(HideReturnsTransformationMethod
                                .getInstance());// show password
                    } else {
                        show_hide_password.setText(R.string.General_showpassword);// change
                        // checkbox
                        // text

                        password.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        password.setTransformationMethod(PasswordTransformationMethod
                                .getInstance());// hide password

                    }

                }
            });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                signInWithEmailPassword();
                break;

            case R.id.forgot_password:
                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameAuthenticationContainer,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:
                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameAuthenticationContainer, new SignUp_Fragment(),
                                Utils.SignUp_Fragment).commit();
                break;
        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        LogUtil.logDebug(TAG, "onConnectionFailed:" + connectionResult);

    }


    private void signInWithEmailPassword() {
        if(checkValidation())
        {
            ((AuthenticationActivity)getActivity()).signInWithEmailPassword(emailid.getText().toString(),password.getText().toString());
        }

    }

    // Check Validation Method
    private boolean checkValidation(){

        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();


        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            //loginLayout.startAnimation(shakeAnimation);
            ((AuthenticationActivity)getActivity()).showSnackBar(getResources().getString(R.string.General_EnterBothCredentials));
            return false;

        }

        // Check if email id is valid or not
        else if (!m.find()){
            ((AuthenticationActivity)getActivity()).showSnackBar(getResources().getString(R.string.General_InvalidEmail));

            //new CustomToast().Show_Toast(getActivity(), view.findViewById(android.R.id.content),getResources().getString(R.string.General_InvalidEmail));
            return false;
        }
        return true;

    }


}

