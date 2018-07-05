package com.atomic.android.activities.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.atomic.android.R;
import com.atomic.android.activities.BaseActivity;
import com.atomic.android.utils.Utils;


public class UserProfileDetailActivity extends BaseActivity {
    public static final String USERID = "UserProfileDetailActivity.USERID";
    public static final String TYPE_DETAIL = "UserProfileDetailActivity.TYPE_DETAIL";
    private String userId;
    private int typeDetail;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.userprofiledetail_activity );
        userId = getIntent().getStringExtra( USERID );
        typeDetail = getIntent().getIntExtra( TYPE_DETAIL,1 );
        fragmentManager = getSupportFragmentManager();
        if(typeDetail == 1){ // open userprofile detail
            replaceUserDetailFragment();
        }else if(typeDetail == 2){ // open companyprofile detail
            replaceCompanyDetailFragment();
        }


    }

    public void replaceUserDetailFragment(){
        Bundle bundle = new Bundle();
        bundle.putString( ViewUserProfileDetail_Fragment.USERID, userId );

        ViewUserProfileDetail_Fragment fragobj = new ViewUserProfileDetail_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( R.anim.left_enter, R.anim.right_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.ViewUserProfileDetail_Fragment )
                .addToBackStack(Utils.ViewUserProfileDetail_Fragment).commit();
    }

    public void replaceUserDetailFragmentNoBackStack(){
        Bundle bundle = new Bundle();
        bundle.putString( ViewUserProfileDetail_Fragment.USERID, userId );

        ViewUserProfileDetail_Fragment fragobj = new ViewUserProfileDetail_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.ViewUserProfileDetail_Fragment )
                .addToBackStack(null).commit();
    }

    public void replaceGeneralUserProfileFragment(){
        Bundle bundle = new Bundle();
        bundle.putString( EditGeneralUserProfile_Fragment.USERID, userId );

        EditGeneralUserProfile_Fragment fragobj = new EditGeneralUserProfile_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( R.anim.left_enter, R.anim.right_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.EditGeneralUserProfile_Fragment )
                .addToBackStack(Utils.EditGeneralUserProfile_Fragment).commit();
    }

    public void replaceContactUserProfileFragment(){
        Bundle bundle = new Bundle();
        bundle.putString( EditContactUserProfile_Fragment.USERID, userId );

        EditContactUserProfile_Fragment fragobj = new EditContactUserProfile_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( R.anim.left_enter, R.anim.right_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.EditContactUserProfile_Fragment )
                .addToBackStack(Utils.EditContactUserProfile_Fragment).commit();
    }

    public void replaceGeneralCompanyProfileFragment(){
        Bundle bundle = new Bundle();
        bundle.putString( EditGeneralCompanyProfile_Fragment.USERID, userId );

        EditGeneralCompanyProfile_Fragment fragobj = new EditGeneralCompanyProfile_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( R.anim.left_enter, R.anim.right_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.EditGeneralCompanyProfile_Fragment )
                .addToBackStack(Utils.EditGeneralCompanyProfile_Fragment).commit();
    }

    public void replaceContactCompanyProfileFragment(){
        Bundle bundle = new Bundle();
        bundle.putString( EditContactCompanyProfile_Fragment.USERID, userId );

        EditContactCompanyProfile_Fragment fragobj = new EditContactCompanyProfile_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( R.anim.left_enter, R.anim.right_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.EditContactCompanyProfile_Fragment )
                .addToBackStack(Utils.EditContactCompanyProfile_Fragment).commit();
    }

    public void replaceCareerCompanyProfileFragment(){
        Bundle bundle = new Bundle();
        bundle.putString( EditCareerCompanyProfile_Fragment.USERID, userId );

        EditCareerCompanyProfile_Fragment fragobj = new EditCareerCompanyProfile_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( R.anim.left_enter, R.anim.right_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.EditCareerCompanyProfile_Fragment )
                .addToBackStack(Utils.EditCareerCompanyProfile_Fragment).commit();
    }





    public void replaceCompanyDetailFragment()
    {
        Bundle bundle = new Bundle();
        bundle.putString( ViewCompanyProfileDetail_Fragment.USERID, userId );

        fragmentManager = getSupportFragmentManager();
        ViewCompanyProfileDetail_Fragment fragobj = new ViewCompanyProfileDetail_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( R.anim.left_enter, R.anim.right_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.ViewCompanyProfileDetail_Fragment )
                .addToBackStack(Utils.ViewCompanyProfileDetail_Fragment).commit();
    }

    public void replaceCompanyDetailFragmentNoBackStack()
    {
        Bundle bundle = new Bundle();
        bundle.putString( ViewCompanyProfileDetail_Fragment.USERID, userId );

        fragmentManager = getSupportFragmentManager();
        ViewCompanyProfileDetail_Fragment fragobj = new ViewCompanyProfileDetail_Fragment();
        fragobj.setArguments( bundle );
        fragmentManager.beginTransaction().setCustomAnimations( android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out )
                .replace( R.id.userprofiledetail_container, fragobj,
                        Utils.ViewCompanyProfileDetail_Fragment )
                .addToBackStack(null).commit();
    }


    @Override
    public void onBackPressed() {
        fragmentManager = getSupportFragmentManager();

        EditGeneralUserProfile_Fragment editGeneralUserProfile_Fragment = (EditGeneralUserProfile_Fragment) fragmentManager.findFragmentByTag( Utils.EditGeneralUserProfile_Fragment );
        EditContactUserProfile_Fragment editContactUserProfile_Fragment = (EditContactUserProfile_Fragment) fragmentManager.findFragmentByTag( Utils.EditContactUserProfile_Fragment );
        EditGeneralCompanyProfile_Fragment editGeneralCompanyProfile_Fragment = (EditGeneralCompanyProfile_Fragment) fragmentManager.findFragmentByTag( Utils.EditGeneralCompanyProfile_Fragment );
        EditContactCompanyProfile_Fragment editContactCompanyProfile_Fragment = (EditContactCompanyProfile_Fragment) fragmentManager.findFragmentByTag( Utils.EditContactCompanyProfile_Fragment );
        EditCareerCompanyProfile_Fragment editCareerCompanyProfile_Fragment = (EditCareerCompanyProfile_Fragment) fragmentManager.findFragmentByTag( Utils.EditCareerCompanyProfile_Fragment );

        if (editGeneralUserProfile_Fragment != null && editGeneralUserProfile_Fragment.isVisible()) {
            replaceUserDetailFragmentNoBackStack();

        } else if (editContactUserProfile_Fragment != null && editContactUserProfile_Fragment.isVisible()) {
            replaceUserDetailFragmentNoBackStack();

        }else if (editGeneralCompanyProfile_Fragment != null && editGeneralCompanyProfile_Fragment.isVisible()) {
            replaceCompanyDetailFragmentNoBackStack();

        }
        else if (editContactCompanyProfile_Fragment != null && editContactCompanyProfile_Fragment.isVisible()) {
            replaceCompanyDetailFragmentNoBackStack();

        }else if (editCareerCompanyProfile_Fragment != null && editCareerCompanyProfile_Fragment.isVisible()) {
            replaceCompanyDetailFragmentNoBackStack();

        }else {
            super.onBackPressed();
            finish();
        }
    }













}
