package com.atomic.android.activities.notification;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.AbstractRecyclerFragment;
import com.atomic.android.activities.MainActivity;
import com.atomic.android.adapters.NotificationAdapter;
import com.atomic.android.enums.NotificationStatus;
import com.atomic.android.managers.DatabaseHelper;
import com.atomic.android.managers.listeners.OnGetObjectListener;
import com.atomic.android.model.Notification;
import com.github.stephenvinouze.advancedrecyclerview.core.enums.ChoiceMode;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public class NotificationList_Fragment extends AbstractRecyclerFragment {

    private FirebaseAuth mAuth;
    public NotificationAdapter adapter;


   // private RecyclerView.Adapter mAdapter;
    public NotificationList_Fragment() {

    }

    ArrayList<Notification> notiList = new ArrayList<Notification>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        adapter = new NotificationAdapter(getContext());
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.NotificationFragment_title));
        mAuth = FirebaseAuth.getInstance();
        TextView news_TextView = view.findViewById( R.id.news_TextView );
        news_TextView.setVisibility(View.VISIBLE);
        view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
        this.searchLayout.setVisibility(View.GONE);


    }




}

