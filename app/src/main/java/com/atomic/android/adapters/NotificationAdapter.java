package com.atomic.android.adapters;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.atomic.android.model.Notification;
import com.atomic.android.views.NotificationItemView;
import com.github.stephenvinouze.advancedrecyclerview.core.adapters.RecyclerAdapter;

public class NotificationAdapter extends RecyclerAdapter<Notification> {

    public NotificationAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View onCreateItemView(@NonNull ViewGroup parent, int viewType) {
        return new NotificationItemView(getContext());
    }

    @Override
    protected void onBindItemView(@NonNull View view, int position) {
        NotificationItemView sampleItemView = (NotificationItemView) view;
        sampleItemView.bind(getItems().get(position), isItemViewToggled(position));

    }


}
