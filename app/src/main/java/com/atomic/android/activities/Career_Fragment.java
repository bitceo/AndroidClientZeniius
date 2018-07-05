package com.atomic.android.activities;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.atomic.android.R;
import com.atomic.android.adapters.CareerAdapter;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.model.Career;
import com.github.stephenvinouze.advancedrecyclerview.core.enums.ChoiceMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public class Career_Fragment extends AbstractRecyclerFragment implements OnQueryTextListener {

    public CareerAdapter adapter;

    public Career_Fragment() {

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CareerAdapter(getContext());
        searchView.setQueryHint(getResources().getString(R.string.General_CareerHint));
        //adapter.setItems(Career.mockItems());

        ArrayList<Career> careerList = ProfileManager.getInstance( this.getActivity() ).getCareerList();
        adapter.setItems(careerList);
        if(((CareerActivity)getActivity()).isMultiselect())
        {
            adapter.setChoiceMode(ChoiceMode.MULTIPLE);
        }else{
            adapter.setChoiceMode(ChoiceMode.SINGLE);
        }
        if(((CareerActivity)getActivity()).getUserId() != null)
        {
            if(CareerActivity.listSelectedCareer != null){
                for (Career ca : CareerActivity.listSelectedCareer) {
                    int index = 0;
                    for (Career career :careerList){
                        if(career.getId().equals( ca.getId() )){
                            adapter.toggleItemView(index);
                            break;
                        }
                        index++;
                    }
                }

            }

        }

        adapter.setOnClick( new Function2<View, Integer, Unit>() {
            @Override
            public Unit invoke(View view1, Integer position) {
                Career sample = adapter.getItems().get( position );
                Locale current = Career_Fragment.this.getResources().getConfiguration().locale;
                String lang = current.getLanguage();
                if (sample.getLanguages().containsKey( lang ) && sample.getLanguages().get( lang ).getName() != null) {
                    Toast.makeText( Career_Fragment.this.getActivity(), Career_Fragment.this.getResources().getString( R.string.General_ItemClicked ) + " " + sample.getLanguages().get( lang ).getName() + " (" + adapter.getSelectedItemViewCount() +
                            " " + Career_Fragment.this.getResources().getString( R.string.General_Selected ) + ")", Toast.LENGTH_SHORT ).show();

                }
                return null;

            }
        } );

        recyclerView.setAdapter(adapter);
    }

    public List<Career> getListSelectCareer()
    {
        List<Career> listcareer = adapter.getItems();
        List<Career> listSelectedCareer = new ArrayList<Career> ();
        for (Integer element : adapter.getSelectedItemViews()) {
            Career career = listcareer.get( element );
            listSelectedCareer.add( career );
        }
        return listSelectedCareer;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(TextUtils.isEmpty(query)){
            adapter.setItems(ProfileManager.getInstance( this.getActivity() ).getCareerList());


        }else {
            adapter.setItems(ProfileManager.getInstance( this.getActivity() ).getCareerListQuery(query));

        }
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(TextUtils.isEmpty(newText)){
            adapter.setItems(ProfileManager.getInstance( this.getActivity() ).getCareerList());
        }else {
            adapter.setItems(ProfileManager.getInstance( this.getActivity() ).getCareerListQuery(newText));
            //adapter.getFilter().filter(newText.toString());
        }
        return true;

    }








}

