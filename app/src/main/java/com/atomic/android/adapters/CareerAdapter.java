package com.atomic.android.adapters;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.atomic.android.model.Career;
import com.atomic.android.views.CareerItemView;
import com.github.stephenvinouze.advancedrecyclerview.core.adapters.RecyclerAdapter;

public class CareerAdapter extends RecyclerAdapter<Career> {

    public CareerAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View onCreateItemView(@NonNull ViewGroup parent, int viewType) {
        return new CareerItemView(getContext());
    }

    @Override
    protected void onBindItemView(@NonNull View view, int position) {
        CareerItemView sampleItemView = (CareerItemView) view;
        sampleItemView.bind(getItems().get(position), isItemViewToggled(position));
    }

}
