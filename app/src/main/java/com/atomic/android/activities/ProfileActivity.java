
package com.atomic.android.activities;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.activities.profile.UserProfileDetailActivity;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnObjectChangedListener;
import com.atomic.android.model.Profile;
import com.atomic.android.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    public static final int CREATE_POST_FROM_PROFILE_REQUEST = 22;
    public static final String USER_ID_EXTRA_KEY = "ProfileActivity.USER_ID_EXTRA_KEY";

    // UI references.
    private TextView nameEditText;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView postsCounterTextView;
    private TextView postsLabelTextView;
    private ProgressBar postsProgressBar;
    private Button chatBtn;
    private RelativeLayout infoContent, companyContent;
    private String currentUserId;
    private String userID;

    private SwipeRefreshLayout swipeContainer;
    private TextView likesCountersTextView;
    private ProfileManager profileManager;
    private TextView needCounterTextView,offerCounterTextView, questionCounterTextView,eventCounterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.ProfileActivity_title));

        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        userID = getIntent().getStringExtra(USER_ID_EXTRA_KEY);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
        }

        // Set up the login form.
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        nameEditText = findViewById(R.id.nameEditText);
        postsCounterTextView = findViewById(R.id.postsCounterTextView);
        likesCountersTextView = findViewById(R.id.likesCountersTextView);
        postsLabelTextView = findViewById(R.id.postsLabelTextView);
        postsProgressBar = findViewById(R.id.postsProgressBar);
        infoContent = findViewById(R.id.infoContent);
        companyContent  = findViewById(R.id.companyContent);
        swipeContainer = findViewById(R.id.swipeContainer);
        needCounterTextView = findViewById(R.id.needCounterTextView);
        offerCounterTextView = findViewById(R.id.offerCounterTextView);
        questionCounterTextView = findViewById(R.id.questionCounterTextView);
        eventCounterTextView = findViewById(R.id.eventCounterTextView);
        chatBtn = findViewById(R.id.chatBtn);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshAction();
            }
        });

        supportPostponeEnterTransition();

        infoContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserProfileDetailActivity(1);
            }
        });

        companyContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserProfileDetailActivity(2);
            }
        });

        if(currentUserId.equals( userID )){
            chatBtn.setVisibility(View.GONE);

        }else{
            chatBtn.setVisibility(View.VISIBLE);
            chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openChatDetailActivity();
                }
            });
        }

    }



    @Override
    public void onStart() {
        super.onStart();
        loadProfile();

    }

    @Override
    public void onStop() {
        super.onStop();
        profileManager.closeListeners(this);


    }


    private void onRefreshAction() {

    }

    private void openChatDetailActivity(){

    }


    private Spannable buildCounterSpannable(String label, int value) {
        SpannableStringBuilder contentString = new SpannableStringBuilder();
        contentString.append(String.valueOf(value));
        contentString.append("\n");
        int start = contentString.length();
        contentString.append(label);
        contentString.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance_Second_Light), start, contentString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return contentString;
    }



    public void startUserProfileDetailActivity(int type) {
        Intent intent = new Intent(ProfileActivity.this, UserProfileDetailActivity.class);
        intent.putExtra( UserProfileDetailActivity.USERID, this.userID);
        intent.putExtra( UserProfileDetailActivity.TYPE_DETAIL, type);
        startActivity(intent);
    }

    private void loadProfile() {
        profileManager = ProfileManager.getInstance(this);
        profileManager.getProfileValue(ProfileActivity.this, userID, createOnProfileChangedListener());
    }

    private OnObjectChangedListener<Profile> createOnProfileChangedListener() {
        return new OnObjectChangedListener<Profile>() {
            @Override
            public void onObjectChanged(Profile obj) {
                fillUIFields(obj);
            }
        };
    }

    private void fillUIFields(Profile profile) {
        if (profile != null) {
            nameEditText.setText(profile.getFullName());

            if (profile.getprofilePicture() != null) {
                Glide.with(this)
                        .load(profile.getprofilePicture())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
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

            int likesCount = (int) profile.getLikesCount();
            String likesLabel = getResources().getQuantityString(R.plurals.likes_counter_format, likesCount, likesCount);
            likesCountersTextView.setText(buildCounterSpannable(likesLabel, likesCount));

            int needCount = (int) profile.getNeedCounter();
            String needLabel = getResources().getQuantityString(R.plurals.need_counter_format, needCount, needCount);
            needCounterTextView.setText(buildCounterSpannable(needLabel, needCount));

            int offerCount = (int) profile.getOfferCounter();
            String offerLabel = getResources().getQuantityString(R.plurals.offer_counter_format, offerCount, offerCount);
            offerCounterTextView.setText(buildCounterSpannable(offerLabel, offerCount));

            int questionCount = (int) profile.getQuestionCounter();
            String questionLabel = getResources().getQuantityString(R.plurals.question_counter_format, questionCount, questionCount);
            questionCounterTextView.setText(buildCounterSpannable(questionLabel, questionCount));

            int eventCount = (int) profile.getEventCounter();
            String eventLabel = getResources().getQuantityString(R.plurals.event_counter_format, eventCount, eventCount);
            eventCounterTextView.setText(buildCounterSpannable(eventLabel, eventCount));

        }
    }



    private void scheduleStartPostponedTransition(final ImageView imageView) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtil.logDebug(TAG, "onConnectionFailed:" + connectionResult);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        ProfileActivity.this.overridePendingTransition(android.R.animator.fade_in,
                android.R.animator.fade_out);
        super.onBackPressed();
        finish();
    }
}
