package com.atomic.android.activities;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.atomic.android.R;
import com.atomic.android.managers.listeners.OnClickPhotoBottomDialogListener;

public class AddPhotoBottomDialogFragment extends BottomSheetDialogFragment{

    private static LinearLayout camera_layout,photo_layout,cancel_layout;
    String [] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE ,  Manifest.permission.WRITE_EXTERNAL_STORAGE };
    OnClickPhotoBottomDialogListener onClickListener;
    boolean enabledPhoto = true;
    boolean enabledCamera = true;
    private static final int PERMISSION_ALL = 123;

    @SuppressLint("ValidFragment")
    public AddPhotoBottomDialogFragment (OnClickPhotoBottomDialogListener onClickListener, boolean enabledPhoto, boolean enabledCamera) {
        this.onClickListener = onClickListener;
        this.enabledCamera = enabledCamera;
        this.enabledPhoto = enabledPhoto;

    }

    public AddPhotoBottomDialogFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.photo_bottom_sheet, container,
                false);

        camera_layout = view.findViewById( R.id.camera_layout );
        photo_layout = view.findViewById( R.id.photo_layout );
        cancel_layout = view.findViewById( R.id.cancel_layout );

        if( this.enabledCamera){
            camera_layout.setVisibility(View.VISIBLE);
            camera_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraClick();
                }
            });
        }else{
            camera_layout.setVisibility(View.GONE);
        }

        if( this.enabledPhoto){
            photo_layout.setVisibility(View.VISIBLE);
            photo_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoClick();
                }
            });
        }else{
            photo_layout.setVisibility(View.GONE);
        }
        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClick();
            }
        });

        return view;

    }
    public boolean hasPermissions(Context context) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    public void cameraClick()
    {
        if(!hasPermissions( getActivity() )){
            ActivityCompat.requestPermissions(getActivity(), permissions, PERMISSION_ALL);
        }else{
            if(this.onClickListener != null){
                dismiss();
                onClickListener.onUploadCameraClick();
            }
        }

    }
   

    public void photoClick()
    {
        if(!hasPermissions( getActivity() )){
            ActivityCompat.requestPermissions(getActivity(), permissions, PERMISSION_ALL);
        }else {
            if (this.onClickListener != null) {
                dismiss();
                onClickListener.onUploadPhotoClick();
            }
        }
    }
    public void cancelClick()
    {
        dismiss();
    }


}