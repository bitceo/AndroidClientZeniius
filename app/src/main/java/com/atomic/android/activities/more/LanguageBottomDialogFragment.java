package com.atomic.android.activities.more;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.atomic.android.R;
import com.atomic.android.managers.listeners.OnClickLanguageBottomDialogListener;

public class LanguageBottomDialogFragment extends BottomSheetDialogFragment{

    private static LinearLayout english_layout,vietnamese_layout,cancel_layout;
    OnClickLanguageBottomDialogListener onClickListener;
    @SuppressLint("ValidFragment")
    public LanguageBottomDialogFragment(OnClickLanguageBottomDialogListener onClickListener) {
        this.onClickListener = onClickListener;

    }

    public LanguageBottomDialogFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.language_bottom_sheet, container,
                false);

        english_layout = view.findViewById( R.id.english_layout );
        vietnamese_layout = view.findViewById( R.id.vietnamese_layout );
        cancel_layout = view.findViewById( R.id.cancel_layout );

        english_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    englishClick();
                }
            });


        vietnamese_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vietnameseClick();
                }
            });

        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClick();
            }
        });

        return view;

    }

    public void englishClick()
    {
        if(this.onClickListener != null){
            dismiss();
            onClickListener.onEnglishClick();
        }
    }

    public void vietnameseClick()
    {
        if(this.onClickListener != null){
            dismiss();
            onClickListener.onVietnameseClick();
        }
    }
    public void cancelClick()
    {
        dismiss();
    }


}