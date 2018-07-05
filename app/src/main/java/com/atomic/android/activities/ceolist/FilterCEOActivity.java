package com.atomic.android.activities.ceolist;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.atomic.android.R;
import com.atomic.android.activities.BaseActivity;
import com.atomic.android.activities.CareerActivity;
import com.atomic.android.model.Career;

import java.util.ArrayList;

public class FilterCEOActivity extends BaseActivity implements OnClickListener {

	public static final String LIST_FILTER_CAREER = "FilterCEOActivity.LIST_FILTER_CAREER";
	private Button careerBtn;
    private ArrayList<Career> newListCareer;
	private LinearLayout careerLayout;
	public FilterCEOActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_ceo);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initViews();

	}
	// Initialize the views
	private void initViews() {

		careerBtn = findViewById(R.id.careerBtn);

		careerLayout = findViewById( R.id.careerLayout );

		careerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openCarrerView();
			}
		});


	}

	private void openCarrerView() {
		try {
			Intent k = new Intent(FilterCEOActivity.this, CareerActivity.class);
			startActivityForResult(k,555);

		} catch(Exception e) {
			e.printStackTrace();
		}


	}

	@Override
	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// handle result of pick image chooser
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 555) {
			if(resultCode == CareerActivity.RESULT_OK){
				newListCareer = data.getParcelableArrayListExtra(CareerActivity.LIST_CAREER);
				Intent returnIntent = new Intent();
				returnIntent.putParcelableArrayListExtra(LIST_FILTER_CAREER, newListCareer );
				setResult(CareerActivity.RESULT_OK,returnIntent);
				finish();

			}
			else if (resultCode == CareerActivity.RESULT_CANCELED) {

			}
		}
		else{

		}

	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backToLoginBtn:

				finish();
				break;


		}
	}



}