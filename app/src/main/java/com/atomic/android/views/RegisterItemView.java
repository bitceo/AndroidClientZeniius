package com.atomic.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.model.ProfileAndCompany;
import com.atomic.android.utils.FormatterUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

public class RegisterItemView extends FrameLayout {


    private TextView timeTextView;
    private ImageView ceoImageView;
    private TextView mNameTextView;
    private TextView numberTextView;
    private Context context;
    public RegisterItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initViews();
    }

    public RegisterItemView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public void bind(ProfileAndCompany profileAndCompany, boolean isToggled) {
        if (profileAndCompany != null) {

            String dateString = FormatterUtil.getDate((profileAndCompany.getTimestamp()*1000 ));
            timeTextView.setText(dateString);
            numberTextView.setText(profileAndCompany.getRank().toString());


            String fullname = profileAndCompany.getProfile().getLastName();
            if(profileAndCompany.getProfile().getMiddleName() == null || profileAndCompany.getProfile().getMiddleName().equals("") || profileAndCompany.getProfile().getMiddleName().length() == 0)
            {
            }else{
                fullname += " " + profileAndCompany.getProfile().getMiddleName();
            }
            fullname += " " + profileAndCompany.getProfile().getFirstName();
            mNameTextView.setText(fullname);
            if (profileAndCompany.getProfile().getprofilePicture() != null) {
                Glide.with( this.context )
                        .load( profileAndCompany.getProfile().getprofilePicture() )
                        .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                        .crossFade()
                        .error( R.drawable.ic_stub )
                        .listener( new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                return false;
                            }
                        } )
                        .into( ceoImageView );
            }else{
                ceoImageView.setImageResource(R.drawable.ic_stub);

            }
        }

    }

    private void initViews() {
        View view = inflate(getContext(), R.layout.queue_item_view, this);
       // profileDescription = view.findViewById(R.id.profileDescription);
        timeTextView = view.findViewById(R.id.timeTextView);
        ceoImageView = view.findViewById(R.id.ceoImageView);
        mNameTextView = view.findViewById(R.id.mNameTextView);
        numberTextView = view.findViewById(R.id.numberTextView);
    }

}
