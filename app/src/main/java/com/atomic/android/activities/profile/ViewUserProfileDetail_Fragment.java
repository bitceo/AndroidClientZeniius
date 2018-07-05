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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.AddPhotoBottomDialogFragment;
import com.atomic.android.activities.GalleryViewActivity;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnClickPhotoBottomDialogListener;
import com.atomic.android.managers.listeners.OnProfileCreatedListener;
import com.atomic.android.model.Company;
import com.atomic.android.model.Profile;
import com.atomic.android.utils.FormatterUtil;
import com.atomic.android.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;

public class ViewUserProfileDetail_Fragment extends Fragment implements OnProfileCreatedListener {
	public static final String USERID = "ViewUserProfileDetail_Fragment.USERID";
	private static View view;
	private FirebaseAuth mAuth;
	private static TextView fullName,email,phone,birthday,about, facebook,
			uploadNameCardTop,uploadNameCardBot,positionTextView, nameCompany,editGeneralTextView,editContactTextView,uploadProfilePicture;
	private String userId;
	private Uri imageUri;
	private static ProgressBar progressBar,progressBarTop,progressBarBot;
	private static ImageView imageView, nameCardBot, nameCardTop;
	private ProfileManager profileManager;
	private Profile userProfile;
	public ViewUserProfileDetail_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_viewuserprofiledetail, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor( R.color.background_primary));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.ViewUserProfileDetailFragment_title));
		((UserProfileDetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		userId = getArguments().getString(USERID);
		initViews();


		return view;
	}


	private void initViews() {

		fullName = view.findViewById( R.id.fullName );
		about = view.findViewById( R.id.about );
		email = view.findViewById( R.id.email );
		phone = view.findViewById( R.id.phone );
		facebook = view.findViewById( R.id.facebook );
		birthday = view.findViewById( R.id.birthday );
		progressBar = view.findViewById( R.id.progressBar );
		progressBarTop = view.findViewById( R.id.progressBarTop );
		progressBarBot = view.findViewById( R.id.progressBarBot );
		imageView = view.findViewById( R.id.imageView );
		nameCardBot = view.findViewById( R.id.nameCardBot );
		nameCardTop = view.findViewById( R.id.nameCardTop );
		positionTextView = view.findViewById( R.id.positionTextView );
		nameCompany = view.findViewById( R.id.nameCompany );
		editContactTextView = view.findViewById( R.id.editContactTextView );
		editGeneralTextView = view.findViewById( R.id.editGeneralTextView );
		uploadProfilePicture = view.findViewById( R.id.uploadProfilePicture );
		uploadNameCardTop = view.findViewById( R.id.uploadNameCardTop );
		uploadNameCardBot = view.findViewById( R.id.uploadNameCardBot );

		mAuth = FirebaseAuth.getInstance();

		if(mAuth.getCurrentUser().getUid().equals( userId )){
			editContactTextView.setVisibility(View.VISIBLE);
			editGeneralTextView.setVisibility(View.VISIBLE);
			uploadNameCardTop.setVisibility(View.VISIBLE);
			uploadNameCardBot.setVisibility(View.VISIBLE);
			uploadProfilePicture.setVisibility(View.VISIBLE);
		}else{
			editContactTextView.setVisibility(View.GONE);
			editGeneralTextView.setVisibility(View.GONE);
			uploadNameCardTop.setVisibility(View.GONE);
			uploadNameCardBot.setVisibility(View.GONE);
			uploadProfilePicture.setVisibility(View.GONE);
		}

		loadProfile();

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openImageDetailScreen(1);
			}
		});
		nameCardBot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openImageDetailScreen(3);
			}
		});
		nameCardTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openImageDetailScreen(2);
			}
		});

		editGeneralTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceEditGeneralUserProfile();
			}
		});
		editContactTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceContactGeneralUserProfile();
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
								values.put( MediaStore.Images.Media.TITLE, "New Picture");
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

		uploadNameCardBot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
						new AddPhotoBottomDialogFragment( new OnClickPhotoBottomDialogListener() {
							@Override
							public void onUploadCameraClick() {
								ContentValues values = new ContentValues();
								values.put( MediaStore.Images.Media.TITLE, "New Picture");
								values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
								imageUri = getActivity().getContentResolver().insert(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values );
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
								startActivityForResult(intent, Utils.CAPTURE_IMAGE_NAMECARD_BOT_REQUEST_CODE);

							}

							@Override
							public void onUploadPhotoClick() {
								Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(i, Utils.IMAGE_PICKER_NAMECARD_BOT_REQUEST_CODE);
							}
						},true,true );
				addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
						"add_photo_dialog_fragment");
			}

		});

		uploadNameCardTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
						new AddPhotoBottomDialogFragment( new OnClickPhotoBottomDialogListener() {
							@Override
							public void onUploadCameraClick() {
								ContentValues values = new ContentValues();
								values.put( MediaStore.Images.Media.TITLE, "New Picture");
								values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
								imageUri = getActivity().getContentResolver().insert(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values );
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
								startActivityForResult(intent, Utils.CAPTURE_IMAGE_NAMECARD_TOP_REQUEST_CODE);

							}

							@Override
							public void onUploadPhotoClick() {
								Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(i, Utils.IMAGE_PICKER_NAMECARD_TOP_REQUEST_CODE);
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
				ProfileManager.getInstance(getActivity()).createOrUpdateProfile(userProfile,  imageUri, this);

			}
		}else if (requestCode == Utils.IMAGE_PICKER_NAMECARD_TOP_REQUEST_CODE
				&& resultCode == getActivity().RESULT_OK) {
			imageUri = data.getData();
			if (imageUri != null) {
				progressBarTop.setVisibility(View.VISIBLE);
				nameCardTop.setVisibility(View.GONE);
				ProfileManager.getInstance(getActivity()).createOrUpdateProfileNameCardTop(userProfile,  imageUri, this);
			}
		}else if (requestCode == Utils.IMAGE_PICKER_NAMECARD_BOT_REQUEST_CODE
				&& resultCode == getActivity().RESULT_OK) {
			imageUri = data.getData();
			if (imageUri != null) {
				progressBarBot.setVisibility(View.VISIBLE);
				nameCardBot.setVisibility(View.GONE);
				ProfileManager.getInstance(getActivity()).createOrUpdateProfileNameCardBot(userProfile,  imageUri, this);
			}
		}
		else if (requestCode == Utils.CAPTURE_IMAGE_PROFILEPICTURE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {

				try {
					progressBar.setVisibility(View.VISIBLE);
					imageView.setVisibility(View.INVISIBLE);
					ProfileManager.getInstance(getActivity()).createOrUpdateProfile(userProfile,  imageUri, this);

				} catch (Exception e) {
					progressBar.setVisibility(View.GONE);
					imageView.setVisibility(View.VISIBLE);
					((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);

				}

			}
		}else if (requestCode == Utils.CAPTURE_IMAGE_NAMECARD_TOP_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {

				try {
					progressBarTop.setVisibility(View.VISIBLE);
					nameCardTop.setVisibility(View.GONE);
					ProfileManager.getInstance(getActivity()).createOrUpdateProfileNameCardTop(userProfile,  imageUri, this);

				} catch (Exception e) {
					((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);

				}

			}
		}else if (requestCode == Utils.CAPTURE_IMAGE_NAMECARD_BOT_REQUEST_CODE) {

			if (resultCode == Activity.RESULT_OK) {
				try {
					progressBarBot.setVisibility(View.VISIBLE);
					nameCardBot.setVisibility(View.GONE);
					ProfileManager.getInstance(getActivity()).createOrUpdateProfileNameCardBot(userProfile,  imageUri, this);

				} catch (Exception e) {
					((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);

				}

			}
		}
	}


	private void replaceEditGeneralUserProfile() {

		((UserProfileDetailActivity)getActivity()).replaceGeneralUserProfileFragment();
	}

	private void replaceContactGeneralUserProfile() {

		((UserProfileDetailActivity)getActivity()).replaceContactUserProfileFragment();
	}

	private void openImageDetailScreen(Integer type) {

		if(userProfile != null){
			if (type == 1) { // open avatar image
				if(userProfile.getprofilePicture() != null && ! userProfile.getprofilePicture() .isEmpty()){
					Intent intent = new Intent(getActivity(), GalleryViewActivity.class);
					String[] arrayImages = {userProfile.getprofilePicture() };
					intent.putExtra(GalleryViewActivity.IMAGE_URLS_EXTRA_KEY,arrayImages);
					startActivity(intent);

				}

			}else if (type == 2){// open nameCardTop
				if(userProfile.getNameCardTop() != null && ! userProfile.getNameCardTop().isEmpty()){
					Intent intent = new Intent(getActivity(), GalleryViewActivity.class);
					String[] arrayImages = {userProfile.getNameCardTop()};
					intent.putExtra(GalleryViewActivity.IMAGE_URLS_EXTRA_KEY,arrayImages);
					startActivity(intent);

				}
			}
			else if (type == 3){// open nameCardBot
				if(userProfile.getNameCardBot() != null && ! userProfile.getNameCardBot().isEmpty()){
					Intent intent = new Intent(getActivity(), GalleryViewActivity.class);
					String[] arrayImages = {userProfile.getNameCardBot()};
					intent.putExtra(GalleryViewActivity.IMAGE_URLS_EXTRA_KEY,arrayImages);
					startActivity(intent);

				}

			}
		}



	}
	private void loadProfile() {

		profileManager = ProfileManager.getInstance(getActivity());
		userProfile = profileManager.getProfileAndCompany(userId ).getProfile();
		fillUIFields();

	}



	private void fillUIFields() {

		if(profileManager.getProfileAndCompany(userId ) != null && profileManager.getProfileAndCompany(userId).getCompany() != null)
		{
			Company company = profileManager.getProfileAndCompany(userId).getCompany();
			if(company.getPos() != null && !company.getPos() .isEmpty()){
				positionTextView.setText(company.getPos());
			}else{
				positionTextView.setText(getResources().getString(R.string.General_NoInformation));
			}
			if(company.getName() != null && !company.getName().isEmpty()){
				nameCompany.setText(company.getName());
			}else{
				nameCompany.setText(getResources().getString(R.string.General_NoInformation));
			}
		}
		if (userProfile != null) {

			fullName.setText(userProfile.getFullName());
			email.setText(userProfile.getEmail());
			phone.setText(userProfile.getPhone());

			if(userProfile.getAbout() != null && !userProfile.getAbout().isEmpty()){
				about.setText(userProfile.getAbout());
			}else{
				about.setText(getResources().getString(R.string.General_NoInformation));
			}

			if(userProfile.getFacebook() != null && !userProfile.getFacebook().isEmpty()){
				facebook.setText(userProfile.getFacebook());
			}else{
				facebook.setText(getResources().getString(R.string.General_NoInformation));
			}

			if(userProfile.getEmail() != null && !userProfile.getEmail().isEmpty()){
				if(mAuth.getCurrentUser().getUid().equals( userId )) {
					email.setText( userProfile.getEmail() );
				}else{
					email.setText( "****** " + getResources().getString(R.string.General_PrivateInformation)) ;
					//email.setText( userProfile.getEmail() );
				}
			}else{
				email.setText(getResources().getString(R.string.General_NoInformation));
			}

			if(userProfile.getPhone() != null && !userProfile.getPhone().isEmpty()){
				if(mAuth.getCurrentUser().getUid().equals( userId )) {
					phone.setText(userProfile.getPhone());
				}else{
					phone.setText( "****** " + getResources().getString(R.string.General_PrivateInformation)) ;
				}

			}else{
				phone.setText(getResources().getString(R.string.General_NoInformation));
			}

			if(userProfile.getBirthday() != 0){
				String date = FormatterUtil.getDateWithDateFormat(userProfile.getBirthday()*1000);
				birthday.setText(date);
			}else{
				birthday.setText(getResources().getString(R.string.General_NoInformation));
			}


			if (userProfile.getprofilePicture() != null ) {
				Glide.with(this)
						.load(userProfile.getprofilePicture())
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



			if (userProfile.getNameCardTop() != null) {
				Glide.with(this)
						.load(userProfile.getNameCardTop())
						.diskCacheStrategy( DiskCacheStrategy.SOURCE)
						.crossFade()
						.error(R.drawable.namecard_profiledetail)
						.listener(new RequestListener<String, GlideDrawable>() {
							@Override
							public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
								scheduleStartPostponedTransition(nameCardTop);
								return false;
							}

							@Override
							public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
								scheduleStartPostponedTransition(nameCardTop);
								return false;
							}
						})
						.into(nameCardTop);
			} else {

				nameCardTop.setImageResource(R.drawable.namecard_profiledetail);
			}

			if (userProfile.getNameCardBot() != null) {
				Glide.with(this)
						.load(userProfile.getNameCardBot())
						.diskCacheStrategy( DiskCacheStrategy.SOURCE)
						.crossFade()
						.error(R.drawable.namecard_profiledetail)
						.listener(new RequestListener<String, GlideDrawable>() {
							@Override
							public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
								scheduleStartPostponedTransition(nameCardBot);
								return false;
							}

							@Override
							public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
								scheduleStartPostponedTransition(nameCardBot);
								return false;
							}
						})
						.into(nameCardBot);
			} else {

				nameCardBot.setImageResource(R.drawable.namecard_profiledetail);
			}


		}
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
	public void onProfileCreated(boolean success) {
		if (success) {
			//((UserProfileDetailActivity)getActivity()).replaceUserDetailFragmentNoBackStack();
			((UserProfileDetailActivity)getActivity()).showSnackBarSuccess(getResources().getString(R.string.General_Success));

		} else {
			((UserProfileDetailActivity)getActivity()).showSnackBar(R.string.error_fail_create_profile);
		}
		imageView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		progressBarBot.setVisibility(View.GONE);
		nameCardBot.setVisibility(View.VISIBLE);
		progressBarTop.setVisibility(View.GONE);
		nameCardTop.setVisibility(View.VISIBLE);
		if(userProfile.getprofilePicture() != null){
			Glide.with(getActivity())
					.load(userProfile.getprofilePicture())
					.diskCacheStrategy(DiskCacheStrategy.NONE)
					.skipMemoryCache(true)
					.error(R.drawable.ic_stub)
					.fitCenter()
					.into(imageView);
		}else{
			imageView.setImageResource(R.drawable.ic_stub);
		}

		if(userProfile.getNameCardBot() != null){
			Glide.with(getActivity())
					.load(userProfile.getNameCardBot())
					.diskCacheStrategy(DiskCacheStrategy.NONE)
					.skipMemoryCache(true)
					.error(R.drawable.ic_stub)
					.fitCenter()
					.into(nameCardBot);
		}else{
			nameCardBot.setImageResource(R.drawable.ic_stub);
		}

		if(userProfile.getNameCardTop() != null){
			Glide.with(getActivity())
					.load(userProfile.getNameCardTop())
					.diskCacheStrategy(DiskCacheStrategy.NONE)
					.skipMemoryCache(true)
					.error(R.drawable.ic_stub)
					.fitCenter()
					.into(nameCardTop);
		}else{
			nameCardTop.setImageResource(R.drawable.ic_stub);
		}

	}
}
