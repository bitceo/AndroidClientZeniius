package com.atomic.android.activities;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.atomic.android.R;
import com.atomic.android.activities.authentication.AuthenticationActivity;
import com.atomic.android.activities.ceolist.CeoList_Fragment;
import com.atomic.android.activities.conversation.Conversations_Fragment;
import com.atomic.android.activities.more.AboutZeniiusActivity;
import com.atomic.android.activities.more.ChangePasswordActivity;
import com.atomic.android.activities.more.More_Fragment;
import com.atomic.android.activities.notification.NotificationList_Fragment;
import com.atomic.android.activities.post.PostActivity;
import com.atomic.android.managers.BottomNavigationViewHelper;
import com.atomic.android.utils.PreferencesUtil;
import com.atomic.android.utils.Utils;

import java.util.Locale;

public class MainActivity extends BaseActivity {
	private static FragmentManager fragmentManager;
    private BottomNavigationView navigation;

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_home:
					loadFeedListFragment();
					return true;
				case R.id.navigation_ceo:
					loadCEOListFragment();
					return true;
				case R.id.navigation_chat:
					loadConversationFragment();
					return true;

				case R.id.navigation_notifications:
					loadNotificationFragment();
					return true;
				case R.id.navigation_more:
					loadMoreFragment();
					return true;

			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main );
		navigation =  findViewById( R.id.navigation );
		BottomNavigationViewHelper.removeShiftMode(navigation);
		navigation.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );

		loadFeedListFragment();
	}

	protected void loadFeedListFragment() {
		fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.frameContainer, new PostActivity(),
						Utils.FeedList).commit();
		//loadingScreen.setVisibility( View.GONE);
	}

    protected void loadCEOListFragment() {
		fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.frameContainer, new CeoList_Fragment(),
						Utils.CeoList_Fragment).commit();
	}

	protected void loadNotificationFragment() {
		fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.frameContainer, new NotificationList_Fragment(),
						Utils.NotificationList_Fragment).commit();
	}

	protected void loadConversationFragment() {
		fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.frameContainer, new Conversations_Fragment(),
						Utils.Conversation_Fragment).commit();
	}

	protected void loadMoreFragment() {
		fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.frameContainer, new More_Fragment(),
						Utils.More_Fragment).commit();
	}



	public void startAuthenticationActivity() {
		Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void startChangePasswordActivity() {
		Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
		startActivity(intent);
	}
	public void startAboutZeniiusActivity() {
		Intent intent = new Intent(MainActivity.this, AboutZeniiusActivity.class);
		startActivity(intent);
	}




	@Override
	public void onBackPressed() {

		Intent intent = new Intent(this, AuthenticationActivity.class);
		intent.putExtra("FLAG", 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);


	}

	public void changeLocale(String localeText,String localeCountry )
	{

		Locale locale = new Locale(localeText,localeCountry);
		Locale.setDefault(locale);
		PreferencesUtil.setLanguage( this,localeText );
		Configuration config = getBaseContext().getResources().getConfiguration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());



	}





}
