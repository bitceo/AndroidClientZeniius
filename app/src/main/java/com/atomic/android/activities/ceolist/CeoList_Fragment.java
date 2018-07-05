package com.atomic.android.activities.ceolist;


/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.atomic.android.R;
import com.atomic.android.activities.AbstractRecyclerFragment;
import com.atomic.android.activities.CareerActivity;
import com.atomic.android.activities.MainActivity;
import com.atomic.android.activities.ProfileActivity;
import com.atomic.android.adapters.ProfileAdapter;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnProfileClickListener;
import com.atomic.android.model.ProfileAndCompany;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.atomic.android.utils.Utils.CAREER_REQUEST_CODE;

public class CeoList_Fragment extends AbstractRecyclerFragment implements OnProfileClickListener {

    public ProfileAdapter adapter;
    CopyOnWriteArrayList<ProfileAndCompany> list;

    private MenuItem filterActionMenuItem;
    private MenuItem filterDeleteActionMenuItem;

    public CeoList_Fragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        view.setBackgroundColor( getActivity().getResources().getColor( R.color.primary_dark ) );
        setHasOptionsMenu( true );
        searchView.setQueryHint( getResources().getString( R.string.CEOListFragment_searchHint ) );
        list = ProfileManager.getInstance( getActivity() ).getListEnabledProfile();

        if (list != null) {
            ((MainActivity) getActivity()).getSupportActionBar()
                    .setTitle( getResources().getString( R.string.CEOListFragment_title ) + "(" + list.size() + ")" );
        } else {
            list = new CopyOnWriteArrayList<ProfileAndCompany>();
            ((MainActivity) getActivity()).getSupportActionBar().setTitle( getResources().getString( R.string.CEOListFragment_title ) );
        }

        adapter = new ProfileAdapter( getContext(), list );
        adapter.setOnContactClickListener( this );
        recyclerView.setAdapter( adapter );


    }

   



    private void openProfileActivity(String userId) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_ID_EXTRA_KEY, userId);
        startActivityForResult(intent, ProfileActivity.CREATE_POST_FROM_PROFILE_REQUEST);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }



    @Override
    public void onContactClicked(ProfileAndCompany contact, int position) {
        if(contact.getProfile() != null && contact.getProfile().getId() != null)
        CeoList_Fragment.this.openProfileActivity( contact.getProfile().getId() );

    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        searchView.clearFocus();
        inflater.inflate(R.menu.filter_menu, menu);
        filterActionMenuItem = menu.findItem(R.id.filter);
        filterDeleteActionMenuItem = menu.findItem(R.id.filter_delete);
        updateOptionMenuVisibility();

        searchView.setOnQueryTextListener( new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (adapter != null)
                    //adapter.getListProfileQuery( query );
                 adapter.getFilter().filter(query);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (adapter != null) {
                    adapter.getFilter().filter(query);

                }
                return false;
            }


        } );


        super.onCreateOptionsMenu(menu,inflater);
    }

    private void updateOptionMenuVisibility() {

        if(ProfileAdapter.listFilterCareer == null || ProfileAdapter.listFilterCareer.isEmpty()){
            filterActionMenuItem.setVisible(true);
            filterDeleteActionMenuItem.setVisible(false);
        }else{
            filterActionMenuItem.setVisible(false);
            filterDeleteActionMenuItem.setVisible(true);
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter:

                Intent intent = new Intent(getActivity(), FilterCEOActivity.class);
                startActivityForResult(intent,CAREER_REQUEST_CODE);
                //intent.putExtra(PostDetailsActivity.POST_ID_EXTRA_KEY, post.getId());

                return true;
            case R.id.filter_delete:

                ProfileAdapter.listFilterCareer = null;
                updateOptionMenuVisibility();
                filterCareer();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAREER_REQUEST_CODE) {
            if(resultCode == CareerActivity.RESULT_OK){
                ProfileAdapter.listFilterCareer = data.getParcelableArrayListExtra(FilterCEOActivity.LIST_FILTER_CAREER);
                updateOptionMenuVisibility();
                filterCareer();
            }
            else if (resultCode == CareerActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }


    }

    private void filterCareer(){
        CharSequence charSequence = searchView.getQuery();
        //adapter.setFilterCareerList(charSequence);
        if (adapter != null)
            //adapter.getListProfileQuery( query );
            adapter.getFilter().filter(charSequence);

    }




}

