package com.atomic.android.activities.profile;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.AddPhotoBottomDialogFragment;
import com.atomic.android.activities.GalleryViewActivity;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnClickPhotoBottomDialogListener;
import com.atomic.android.managers.listeners.OnCompanyCreatedListener;
import com.atomic.android.model.Company;
import com.atomic.android.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
public class ViewCompanyProfileDetail_Fragment extends Fragment implements OnClickListener, OnCompanyCreatedListener {
	public static final String USERID = "ViewUserProfileDetail_Fragment.USERID";
	private static View view;
	private FirebaseAuth mAuth;
	private static TextView name,email,phone,about, facebook, address, masothue, website,imageNumberTextView
	,editGeneralTextView,editContactTextView,uploadProfilePicture,editCareerTextView,editGPKDTextView, loadingText;
	private String userId;
	private static ProgressBar progressBar;
	private static ImageView imageView, gpkdImageView1, gpkdImageView2;
	private ProfileManager profileManager;
	private Company companyProfile;
	private LinearLayout careerLayout;
	private Uri imageUri;
	private ArrayList<Uri> listImageUris;
	public ViewCompanyProfileDetail_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_viewcompanyprofiledetail, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.ViewCompanyProfileDetailFragment_title));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		userId = getArguments().getString(USERID);
		mAuth = FirebaseAuth.getInstance();
		initViews();


		return view;
	}


	private void initViews() {

		name = view.findViewById( R.id.name );
		about = view.findViewById( R.id.about );
		email = view.findViewById( R.id.email );
		phone = view.findViewById( R.id.phone );
		address = view.findViewById( R.id.address );
		facebook = view.findViewById( R.id.facebook );
		masothue = view.findViewById( R.id.masothue );
		website = view.findViewById( R.id.website );

		progressBar = view.findViewById( R.id.progressBar );
		imageView = view.findViewById( R.id.imageView );
		gpkdImageView1 = view.findViewById( R.id.gpkdImageView1 );
		gpkdImageView2 = view.findViewById( R.id.gpkdImageView2 );
		careerLayout = view.findViewById( R.id.careerLayout );
		imageNumberTextView = view.findViewById( R.id.imageNumberTextView );
		editContactTextView = view.findViewById( R.id.editContactTextView );
		editGeneralTextView = view.findViewById( R.id.editGeneralTextView );
		editCareerTextView = view.findViewById( R.id.editCareerTextView );
		uploadProfilePicture = view.findViewById( R.id.uploadProfilePicture );
		editGPKDTextView = view.findViewById( R.id.editGPKDTextView );
		loadingText = view.findViewById( R.id.loadingText );
		mAuth = FirebaseAuth.getInstance();

		if(mAuth.getCurrentUser().getUid().equals( userId )){
			editContactTextView.setVisibility(View.VISIBLE);
			editGeneralTextView.setVisibility(View.VISIBLE);
			editCareerTextView.setVisibility(View.VISIBLE);
			editGPKDTextView.setVisibility(View.VISIBLE);
			//uploadNameCardTop.setVisibility(View.VISIBLE);
			//uploadNameCardBot.setVisibility(View.VISIBLE);
			uploadProfilePicture.setVisibility(View.VISIBLE);
		}else{
			editContactTextView.setVisibility(View.GONE);
			editGeneralTextView.setVisibility(View.GONE);
			editCareerTextView.setVisibility(View.GONE);
			editGPKDTextView.setVisibility(View.GONE);
			//uploadNameCardTop.setVisibility(View.GONE);
			//uploadNameCardBot.setVisibility(View.GONE);
			uploadProfilePicture.setVisibility(View.GONE);
		}

		loadProfile();


		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openImageDetailScreen(1);
			}
		});

		gpkdImageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openImageDetailScreen(2);
			}
		});
		gpkdImageView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openImageDetailScreen(2);
			}
		});

		editGeneralTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceEditGeneralCompanyProfile();
			}
		});
		editContactTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceEditContactCompanyProfile();
			}
		});
		editCareerTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceEditCareerCompanyProfile();
			}
		});
		uploadProfilePicture.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {
				AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
						new AddPhotoBottomDialogFragment( new OnClickPhotoBottomDialogListener() {
							@Override
							public void onUploadCameraClick() {

								ContentValues values = new ContentValues();
								values.put(MediaStore.Images.Media.TITLE, "New Picture");
								values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
								imageUri = getActivity().getContentResolver().insert(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values );
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
								startActivityForResult(intent, Utils.CAPTURE_IMAGE_PROFILEPICTURE_REQUEST_CODE);

							}

							@Override
							public void onUploadPhotoClick() {
								Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(i, Utils.IMAGE_PICKER_PROFILEPICTURE_REQUEST_CODE);
							}
						},true, true );
				addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
						"add_photo_dialog_fragment");


			}


		});


		editGPKDTextView.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {
				AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
						new AddPhotoBottomDialogFragment( new OnClickPhotoBottomDialogListener() {
							@Override
							public void onUploadCameraClick() {
								ContentValues values = new ContentValues();
								values.put(MediaStore.Images.Media.TITLE, "New Picture");
								values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
								imageUri = getActivity().getContentResolver().insert(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values );
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
								startActivityForResult(intent, Utils.CAPTURE_IMAGE_GPKD_REQUEST_CODE);
							}

							@Override
							public void onUploadPhotoClick() {
								Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
								intent.setType("image/*");
								intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
								startActivityForResult(Intent.createChooser(intent, "Select Picture"), Utils.IMAGE_PICKER_GPKD_REQUEST_CODE); //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult

							}
						},true,true );
				addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
						"add_photo_dialog_fragment");


			}


		});


	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Utils.IMAGE_PICKER_PROFILEPICTURE_REQUEST_CODE
				&& resultCode == getActivity().RESULT_OK) {
			imageUri = data.getData();
			if (imageUri != null) {
				progressBar.setVisibility(View.VISIBLE);
				imageView.setVisibility(View.INVISIBLE);
				ProfileManager.getInstance(getActivity()).createOrUpdateCompany(companyProfile,  imageUri, this);

			}
		}else if (requestCode == Utils.IMAGE_PICKER_GPKD_REQUEST_CODE
				&& resultCode == getActivity().RESULT_OK) {
			if(data.getClipData() != null) {
				listImageUris = new ArrayList<Uri>();
				int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
				for(int i = 0; i < count; i++){
					Uri item = data.getClipData().getItemAt(i).getUri();
					listImageUris.add( item );

				}
				loadingText.setVisibility(View.VISIBLE);
				gpkdImageView1.setVisibility(View.GONE);
				gpkdImageView2.setVisibility(View.GONE);
				ProfileManager.getInstance(getActivity()).createOrUpdateCompanyWithGPKDUrls(companyProfile,listImageUris, this);


			}else if(data.getData() != null) {
				imageUri = data.getData();
				listImageUris = new ArrayList<Uri>();
				listImageUris.add( imageUri );
				loadingText.setVisibility(View.VISIBLE);
				gpkdImageView1.setVisibility(View.GONE);
				gpkdImageView2.setVisibility(View.GONE);
				ProfileManager.getInstance(getActivity()).createOrUpdateCompanyWithGPKDUrls(companyProfile,listImageUris, this);
			}
		}
		else if (requestCode == Utils.CAPTURE_IMAGE_PROFILEPICTURE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {

				try {
					progressBar.setVisibility(View.VISIBLE);
					imageView.setVisibility(View.INVISIBLE);
					ProfileManager.getInstance(getActivity()).createOrUpdateCompany(companyProfile,  imageUri, this);
				} catch (Exception e) {
					progressBar.setVisibility(View.GONE);
					imageView.setVisibility(View.VISIBLE);
					((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);

				}

			}
		}else if (requestCode == Utils.CAPTURE_IMAGE_GPKD_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {

				try {
					listImageUris = new ArrayList<Uri>();
					listImageUris.add( imageUri );
					loadingText.setVisibility(View.VISIBLE);
					gpkdImageView1.setVisibility(View.GONE);
					gpkdImageView2.setVisibility(View.GONE);
					ProfileManager.getInstance(getActivity()).createOrUpdateCompanyWithGPKDUrls(companyProfile,listImageUris, this);

				} catch (Exception e) {

					loadingText.setVisibility(View.GONE);
					gpkdImageView1.setVisibility(View.VISIBLE);
					gpkdImageView2.setVisibility(View.VISIBLE);

					((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);

				}

			}
		}
	}





	private void replaceEditGeneralCompanyProfile() {

		((UserProfileDetailActivity)getActivity()).replaceGeneralCompanyProfileFragment();
	}

	private void replaceEditContactCompanyProfile() {

		((UserProfileDetailActivity)getActivity()).replaceContactCompanyProfileFragment();
	}
	private void replaceEditCareerCompanyProfile() {

		((UserProfileDetailActivity)getActivity()).replaceCareerCompanyProfileFragment();
	}

	private void openImageDetailScreen(Integer type) {

		if(companyProfile != null){
			if (type == 1) { // open avatar image
				if(companyProfile.getProfilePicture() != null && ! companyProfile.getProfilePicture() .isEmpty()){
					Intent intent = new Intent(getActivity(), GalleryViewActivity.class);
					String[] arrayImages = {companyProfile.getProfilePicture() };
					intent.putExtra(GalleryViewActivity.IMAGE_URLS_EXTRA_KEY,arrayImages);
					startActivity(intent);

				}

			}else if (type == 2) { // open avatar image
				if(companyProfile.getGpkdUrls() != null && ! companyProfile.getGpkdUrls() .isEmpty()){
					Intent intent = new Intent(getActivity(), GalleryViewActivity.class);
					String[] arrayImages =new String[companyProfile.getGpkdUrls().size()];
					int index = 0;
					for(String url: companyProfile.getGpkdUrls()){
						arrayImages[index] = url;
						index++;
					}

					intent.putExtra(GalleryViewActivity.IMAGE_URLS_EXTRA_KEY,arrayImages);
					startActivity(intent);

				}

			}
		}



	}
	private void loadProfile() {
		profileManager = ProfileManager.getInstance(getActivity());
		companyProfile = profileManager.getProfileAndCompany(userId ).getCompany();
		fillUIFields();
	}



	private void fillUIFields() {
		if (companyProfile != null) {

			name.setText(companyProfile.getName());


			if(companyProfile.getAbout() != null && !companyProfile.getAbout().isEmpty()){
				about.setText(companyProfile.getAbout());
			}else{
				about.setText(getResources().getString(R.string.General_NoInformation));
			}

			if(companyProfile.getFacebook() != null && !companyProfile.getFacebook().isEmpty()){
				facebook.setText(companyProfile.getFacebook());
			}else{
				facebook.setText(getResources().getString(R.string.General_NoInformation));
			}

			if(companyProfile.getEmail() != null && !companyProfile.getEmail().isEmpty()){
				email.setText(companyProfile.getEmail());
			}else{
				email.setText(getResources().getString(R.string.General_NoInformation));
			}

			if(companyProfile.getPhone() != null && !companyProfile.getPhone().isEmpty()){
				phone.setText(companyProfile.getPhone());
			}else{
				phone.setText(getResources().getString(R.string.General_NoInformation));
			}

			if((companyProfile.getMasothue() != null && !companyProfile.getMasothue().isEmpty())){
				masothue.setText(companyProfile.getMasothue());
			}else{
				masothue.setText(getResources().getString(R.string.General_NoInformation));
			}
			if((companyProfile.getWebsite() != null && !companyProfile.getWebsite().isEmpty())){
				website.setText(companyProfile.getWebsite());
			}else{
				website.setText(getResources().getString(R.string.General_NoInformation));
			}

			String addressText = "";
			if((companyProfile.getAddress() != null && !companyProfile.getAddress().isEmpty())){
				addressText = companyProfile.getAddress();
			}
			if((companyProfile.getDistrict() != null && !companyProfile.getDistrict().isEmpty())){
				if(addressText.equals( "" )){
					addressText +=  companyProfile.getDistrict();
				}else{
					addressText += ", " + companyProfile.getDistrict();
				}

			}
			if((companyProfile.getCity() != null && !companyProfile.getCity().isEmpty())){

				if(addressText.equals( "" )){
					addressText +=  companyProfile.getCity();
				}else{
					addressText += ", " + companyProfile.getCity();
				}
			}
			if((companyProfile.getCountry() != null && !companyProfile.getCountry().isEmpty())){

				if(addressText.equals( "" )){
					addressText +=  companyProfile.getCountry();
				}else{
					addressText += ", " + companyProfile.getCountry();
				}
			}

			if(addressText.equals( "" )){
				address.setText(getResources().getString(R.string.General_NoInformation));
			} else{
				address.setText(addressText);
			}
			if (companyProfile.getProfilePicture() != null) {
				Glide.with(this)
						.load(companyProfile.getProfilePicture())
						.diskCacheStrategy( DiskCacheStrategy.SOURCE)
						.crossFade()
						.error(R.drawable.ic_stub)
						.listener(new RequestListener<String, GlideDrawable>() {
							@Override
							public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
								scheduleStartPostponedTransition(imageView);
								progressBar.setVisibility(View.GONE);
								return false;
							}

							@Override
							public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
								scheduleStartPostponedTransition(imageView);
								progressBar.setVisibility(View.GONE);
								return false;
							}
						})
						.into(imageView);
			} else {
				progressBar.setVisibility(View.GONE);
				imageView.setImageResource(R.drawable.ic_stub);
			}

			if(companyProfile.getCareers() != null){
				for (String key : companyProfile.getCareers().keySet()) {
					View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_careerdetail,null);
					TextView careerNameTextView = view.findViewById(R.id.careerNameTextView);
					TextView careerAbout= view.findViewById(R.id.careerAbout);

					String careerName =getResources().getString(R.string.General_NoInformation);
					String careerAboutText =getResources().getString(R.string.General_NoInformation);
					if(ProfileManager.getInstance( getActivity()).getListCareerMap().containsKey( key )){
						careerName = ProfileManager.getInstance( getActivity()).getListCareerMap().get( key ).getName();
						if(companyProfile.getCareers().get(key  ).getAbout() != null){
							careerAboutText = companyProfile.getCareers().get(key ).getAbout();
						}

					}
					careerNameTextView.setText(careerName);
					careerAbout.setText(careerAboutText);
					careerLayout.addView(view);

				}

			}

			if(companyProfile.getGpkdUrls() != null){
				int index = 0;
				for (String url : companyProfile.getGpkdUrls()) {
					if(url != null) {
						if (index == 0) {
							loadImageView1( url );
							gpkdImageView2.setVisibility( View.GONE );
							imageNumberTextView.setVisibility( View.GONE );
						} else if (index == 1) {
							gpkdImageView2.setVisibility( View.VISIBLE );
							loadImageView2( url);
						} else {
							imageNumberTextView.setText( "+" + String.valueOf( companyProfile.getGpkdUrls().size() - 1 ) );
							imageNumberTextView.setVisibility( View.VISIBLE );
						}
						index++;
					}
				}

			}else{
				gpkdImageView2.setVisibility(View.GONE);
				imageNumberTextView.setVisibility( View.GONE );
				gpkdImageView1.setImageDrawable(getResources().getDrawable(R.drawable.namecard_profiledetail));
			}
		}
	}

	private void loadImageView1(String url){
		Glide.with(this)
				.load(url)
				.diskCacheStrategy( DiskCacheStrategy.SOURCE)
				.crossFade()
				.error(R.drawable.namecard_profiledetail)
				.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
						scheduleStartPostponedTransition(gpkdImageView1);

						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
						scheduleStartPostponedTransition(gpkdImageView1);

						return false;
					}
				})
				.into(gpkdImageView1);
	}

	private void loadImageView2(String url){
		Glide.with(this)
				.load(url)
				.diskCacheStrategy( DiskCacheStrategy.SOURCE)
				.crossFade()
				.error(R.drawable.namecard_profiledetail)
				.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
						scheduleStartPostponedTransition(gpkdImageView2);

						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
						scheduleStartPostponedTransition(gpkdImageView2);

						return false;
					}
				})
				.into(gpkdImageView2);
	}

	private void scheduleStartPostponedTransition(final ImageView imageView) {
		imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				imageView.getViewTreeObserver().removeOnPreDrawListener(this);
				getActivity().supportStartPostponedEnterTransition();
				return true;
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method

                //new PostActivity().replaceSignUp_Profile_Fragment();
				//((AuthenticationActivity)getActivity()).startCreateProfileActivity();
                break;


        }


    }


	@Override
	public void onCompanyCreated(boolean success) {
		progressBar.setVisibility(View.GONE);
		loadingText.setVisibility(View.GONE);
		imageView.setVisibility(View.VISIBLE);

		if (success) {
			((UserProfileDetailActivity)getActivity()).showSnackBarSuccess(getResources().getString(R.string.General_Success));

		} else {
			((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);
		}

		if(companyProfile.getProfilePicture() != null){
			Glide.with(getActivity())
					.load(companyProfile.getProfilePicture())
					.diskCacheStrategy(DiskCacheStrategy.NONE)
					.skipMemoryCache(true)
					.error(R.drawable.ic_stub)
					.fitCenter()
					.into(imageView);
		}else{
			imageView.setImageResource(R.drawable.ic_stub);
		}
		if(companyProfile.getGpkdUrls() != null){
			int index = 0;
			for (String url : companyProfile.getGpkdUrls()) {
				if(url != null) {
					if (index == 0) {
						gpkdImageView1.setVisibility( View.VISIBLE );
						loadImageView1( url );
						gpkdImageView2.setVisibility( View.GONE );
						imageNumberTextView.setVisibility( View.GONE );
					} else if (index == 1) {
						gpkdImageView2.setVisibility( View.VISIBLE );
						loadImageView2( url);
					} else {
						imageNumberTextView.setText( "+" + String.valueOf( companyProfile.getGpkdUrls().size() - 1 ) );
						imageNumberTextView.setVisibility( View.VISIBLE );
					}
					index++;
				}
			}

		}else{
			gpkdImageView1.setVisibility( View.VISIBLE );
			gpkdImageView2.setVisibility(View.GONE);
			imageNumberTextView.setVisibility( View.GONE );
			gpkdImageView1.setImageDrawable(getResources().getDrawable(R.drawable.namecard_profiledetail));
		}
	}
}
