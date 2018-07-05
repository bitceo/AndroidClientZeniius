package com.atomic.android.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.enums.NotificationStatus;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.model.Notification;
import com.atomic.android.model.Profile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

public class NotificationItemView extends FrameLayout {

    private TextView notiTimeTextView;
    private TextView notiContentTextView;
    private ImageView notiIconImageView;
    private ImageView notiImageView;
    private LinearLayout intro;
    private Context context;
    public NotificationItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initViews();
    }

    public NotificationItemView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public void bind(Notification notification, boolean isToggled) {
        if (notification != null) {

            long lastTimestamp = 0;
            String lastKey = "";
            for (String key : notification.getListSubjectId().keySet()) {
                if(lastTimestamp < notification.getListSubjectId().get(key)){
                    lastKey = key;
                    lastTimestamp = notification.getListSubjectId().get(key);
                }
            }

            Profile profile = ProfileManager.getInstance(context).getProfile( lastKey );

            if (notification.getContent() != null && profile != null && profile.getFullName() != null) {

                String notiContent =  String.format("<b> %s </b></font>", profile.getFullName());

                String content = notification.getContent();
                String actiontype = notification.getActionType();
                String opportunity = getResources().getString(R.string.General_Demand);
                String action = getResources().getString(R.string.General_JoinAction);
                String titleObject = "";

                if(content.contains( "Thảo luận" )){
                    opportunity = getResources().getString(R.string.General_Discussion);
                    String segments[] = content.split("Thảo luận");
                    titleObject = segments[segments.length - 1];
                }else if (content.contains( "Nhu cầu" )){
                    opportunity = getResources().getString(R.string.General_Demand);
                    String segments[] = content.split("Nhu cầu");
                    titleObject = segments[segments.length - 1];
                }else if (content.contains( "Năng lực" )){
                    opportunity = getResources().getString(R.string.General_Ability);
                    String segments[] = content.split("Năng lực");
                    titleObject = segments[segments.length - 1];
                }else if (content.contains( "Sự kiện" )){
                    opportunity = getResources().getString(R.string.General_Event);
                    String segments[] = content.split("Sự kiện");
                    titleObject = segments[segments.length - 1];
                }
                if(actiontype != null && actiontype.equals( "1" )){
                    action = getResources().getString(R.string.General_CommentAction);
                }else  if(actiontype != null && actiontype.equals( "2" )){
                    action = getResources().getString(R.string.General_LikeAction);
                }else  if(actiontype != null && actiontype.equals( "3" )){
                    action = getResources().getString(R.string.General_JoinAction);
                }else  if(actiontype != null && actiontype.equals( "4" )){
                    action = getResources().getString(R.string.General_LikeCommentAction);
                }else{
                    action = getResources().getString(R.string.General_LikeAction);
                }
                String totalContent = action + " "+ opportunity + " " + titleObject;
                if(notification.getListSubjectId().size() - 1 > 0)
                {
                    notiContent += " and ";
                    notiContent += String.valueOf(notification.getListSubjectId().size() - 1);
                    notiContent += " others";
                }
                notiContent += " " + totalContent;
                Spanned text = Html.fromHtml(notiContent );
                notiContentTextView.setText(text);

                if(notification.getStatus() != null && notification.getStatus().equals( NotificationStatus.Unread.name())){
                    intro.setBackgroundColor(getResources().getColor(R.color.unread_noti_color));
                }else{
                    intro.setBackgroundColor(getResources().getColor(R.color.white));
                }



            }
            if(profile != null && profile.getprofilePicture() != null){
                Glide.with(context)
                        .load(profile.getprofilePicture())
                        .centerCrop()
                        //.override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_stub)
                        .crossFade()
                        .into(notiImageView);

            }

            notiTimeTextView.setText(getDate(lastTimestamp *1000));

            String uri = "@drawable/like";  // where myresource (without the extension) is the file
            if(notification.getActionType().equals("1"  ) )
            {
                uri = "@drawable/comment";
            }else if(notification.getActionType().equals("2"))
            {
                uri = "@drawable/like";
            }else if(notification.getActionType().equals("3"))
            {
                uri = "@drawable/join";
            }else if(notification.getActionType().equals("4"))
            {
                uri = "@drawable/like";
            }
            int imageResource = getResources().getIdentifier(uri, null, context.getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            notiIconImageView.setImageDrawable(res);

        }


    }

    private void initViews() {
        View view = inflate(getContext(), R.layout.notification_item_view, this);
        notiTimeTextView = view.findViewById(R.id.notiTimeTextView);
        notiContentTextView = view.findViewById(R.id.notiContentTextView);
        notiIconImageView = view.findViewById(R.id.notiIconImageView);
        notiImageView = view.findViewById(R.id.notiImageView);
        intro = view.findViewById(R.id.intro);
    }

    private String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd/MM/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

}
