package com.atomic.android.activities;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.atomic.android.R;
import com.atomic.android.model.Career;
import com.atomic.android.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class CareerActivity extends BaseActivity {
    public static final String LIST_CAREER = "CareerActivity.LIST_CAREER";
    public static final String MULTISELECT = "CareerActivity.MULTISELECT";
    public static final String USERID = "CareerActivity.USERID";
    public static ArrayList<Career> listSelectedCareer = new ArrayList<Career> ();
    private boolean multiselect = true;
    private String userId;
    public Career_Fragment career_Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity );

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        multiselect = getIntent().getBooleanExtra(CareerActivity.MULTISELECT, true);

        userId = getIntent().getStringExtra(CareerActivity.USERID);

        career_Fragment = new Career_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.generalframe_container, career_Fragment,
                        Utils.Career_Fragment).commit();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_career_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.finishButton:
                Intent returnIntent = new Intent();
                List<Career> listCareer=  career_Fragment.getListSelectCareer();
                returnIntent.putParcelableArrayListExtra(CareerActivity.LIST_CAREER, (ArrayList<Career>) listCareer );
                setResult(CareerActivity.RESULT_OK,returnIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean isMultiselect() {
        return multiselect;
    }

    public String getUserId() {
        return userId;
    }

}
