package com.atomic.android.activities.more;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.BaseActivity;
public class AboutZeniiusActivity extends BaseActivity  {


	private static TextView aboutTextView;

	public AboutZeniiusActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_zeniius);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initViews();

	}
	// Initialize the views
	private void initViews() {
		aboutTextView = findViewById(R.id.aboutTextView);

		try {
			PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			aboutTextView.setText(getResources().getString(R.string.MoreFragment_Version) +": " + version);
		} catch (PackageManager.NameNotFoundException e) {
			aboutTextView.setText(getResources().getString(R.string.MoreFragment_UnknownVersion));
			e.printStackTrace();
		}


	}




}