
package com.atomic.android.activities.post;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.MainActivity;
import com.atomic.android.activities.ProfileActivity;
import com.atomic.android.enums.ProfileStatus;
import com.atomic.android.managers.ProfileManager;
import com.google.firebase.auth.FirebaseAuth;

public class PostActivity extends Fragment {

    private static View view;
    private ProfileManager profileManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_post, container, false);
        setHasOptionsMenu(true);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.PostFragment_title));
        view.setBackgroundColor(getActivity().getResources().getColor( R.color.primary_dark));
        profileManager = ProfileManager.getInstance(getActivity());
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void openProfileActivity(String userId) {
        openProfileActivity(userId, null);
    }

    private void openProfileActivity(String userId, View view) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_ID_EXTRA_KEY, userId);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {

            View authorImageView = view.findViewById(R.id.authorImageView);

            ActivityOptions options = ActivityOptions.
                    makeSceneTransitionAnimation(getActivity(),
                            new android.util.Pair<>(authorImageView, getString(R.string.post_author_image_transition_name)));
            startActivityForResult(intent, ProfileActivity.CREATE_POST_FROM_PROFILE_REQUEST, options.toBundle());
        } else {
            startActivityForResult(intent, ProfileActivity.CREATE_POST_FROM_PROFILE_REQUEST);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                ProfileStatus profileStatus = profileManager.checkProfile();

                if (profileStatus.equals(ProfileStatus.PROFILE_CREATED)) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    openProfileActivity(userId);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
