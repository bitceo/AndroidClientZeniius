package com.atomic.android.activities.conversation;


/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.atomic.android.R;
import com.atomic.android.activities.MainActivity;

public class Conversations_Fragment extends Fragment  {
	private static View view;

	public Conversations_Fragment() {

	}


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tab_chat, container, false);
		((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.ConversationFragment_title));

		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.conversations_menu, menu);
		super.onCreateOptionsMenu(menu,inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			default:
				return super.onOptionsItemSelected(item);
		}
	}






}
