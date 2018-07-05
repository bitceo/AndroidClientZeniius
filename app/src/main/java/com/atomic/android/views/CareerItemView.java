package com.atomic.android.views;
/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.model.Career;

import java.util.Locale;


public class CareerItemView extends FrameLayout {

    private TextView careerDescription;
    private TextView mNameTextView;
    private TextView mTickIconView;

    public CareerItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public CareerItemView(Context context) {
        super(context);
        initViews();
    }

    public void bind(Career sample, boolean isToggled) {
        if (sample != null) {

            Locale current = getResources().getConfiguration().locale;
            String lang = current.getLanguage();
            if(sample.getLanguages().containsKey( lang ) && sample.getLanguages().get(lang).getDescription() != null){
                careerDescription.setText(sample.getLanguages().get(lang).getDescription());
            }else{
                careerDescription.setText(sample.getDescription());
            }

            if(sample.getLanguages().containsKey( lang ) && sample.getLanguages().get(lang).getName() != null){
                mNameTextView.setText(sample.getLanguages().get(lang).getName());
            }else{
                mNameTextView.setText(sample.getName());
            }

        }

        mTickIconView.setVisibility(isToggled ? VISIBLE : GONE);
    }

    private void initViews() {
        View view = inflate(getContext(), R.layout.career_item_view, this);
        careerDescription = view.findViewById(R.id.careerDescription);
        mNameTextView = view.findViewById(R.id.sample_item_name_text_view);
        mTickIconView = view.findViewById(R.id.sample_item_name_tick_view);
    }

}
