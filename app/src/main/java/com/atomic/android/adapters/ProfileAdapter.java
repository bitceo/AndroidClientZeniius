package com.atomic.android.adapters;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.atomic.android.R;
import com.atomic.android.managers.ProfileManager;
import com.atomic.android.managers.listeners.OnProfileClickListener;
import com.atomic.android.model.Career;
import com.atomic.android.model.CareerOfUser;
import com.atomic.android.model.ProfileAndCompany;
import com.atomic.android.utils.FormatterUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> implements Filterable {

    private List<ProfileAndCompany> contactListFiltered;
    private List<ProfileAndCompany> contactList;
    Context context;
    private OnProfileClickListener onProfileClickListener;
    public static ArrayList<Career> listFilterCareer ;
    public ProfileAdapter(Context context, List<ProfileAndCompany> contactList) {

        this.contactList = contactList;
        contactListFiltered = contactList;
        this.context = context;

    }

    public void setOnContactClickListener(OnProfileClickListener onContactClickListener) {
        this.onProfileClickListener = onContactClickListener;
    }


    public OnProfileClickListener getOnProfileClickListener() {
        return onProfileClickListener;
    }

    @Override
    public ProfileAdapter.ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item_view, parent, false);
        return new ProfileAdapter.ProfileViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ProfileAdapter.ProfileViewHolder holder, final int position) {

        final ProfileAndCompany profileAndCompany = contactListFiltered.get(position);
        if (profileAndCompany != null) {
            if(profileAndCompany.getProfile() != null){
                String fullname = profileAndCompany.getProfile().getFullName();
                if(fullname != null){
                    holder.mNameTextView.setText(fullname);
                }else{
                    holder.mNameTextView.setText(context.getResources().getString(R.string.General_NoInformation));
                }
                if(profileAndCompany.getProfile().getprofilePicture()  != null){
                    Glide.with( this.context )
                            .load( profileAndCompany.getProfile().getprofilePicture() )
                            .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                            .crossFade()
                            .error( R.drawable.ic_stub )
                            .into( holder.ceoImageView );
                }else{
                    holder.ceoImageView.setImageResource(R.drawable.ic_stub);
                }

            }else{
                holder.mNameTextView.setText(context.getResources().getString(R.string.General_NoInformation));
                holder.ceoImageView.setImageResource(R.drawable.ic_stub);
            }

            if(profileAndCompany.getCompany() != null){
                if(profileAndCompany.getCompany().getName() != null)
                {
                    holder.profileDescription.setText(profileAndCompany.getCompany().getName());
                }else{
                    holder.profileDescription.setText(context.getResources().getString(R.string.General_NoInformation));
                }
                if(profileAndCompany.getCompany().getCareers() != null){
                    HashMap<String,CareerOfUser> mapCareer  = profileAndCompany.getCompany().getCareers();
                    String careerDescriptionText ="";
                    Locale current = context.getResources().getConfiguration().locale;
                    String lang = current.getLanguage();
                    if(mapCareer != null){
                        for (String key : mapCareer.keySet()) {
                            if(ProfileManager.getInstance( context).getListCareerMap().containsKey( key )){
                                String job = ProfileManager.getInstance( context).getListCareerMap().get( key ).getLanguages().get(lang).getName();
                                if(careerDescriptionText.equals("")){
                                    careerDescriptionText += job;
                                }else{
                                    careerDescriptionText += ", "+job;
                                }
                            }
                        }
                        holder.careerDescription.setText(careerDescriptionText);
                    }else{
                        holder.careerDescription.setText(context.getResources().getString(R.string.General_NoInformation));
                    }
                }else{
                    holder.careerDescription.setText(context.getResources().getString(R.string.General_NoInformation));
                }
            }else{
                holder.careerDescription.setText(context.getResources().getString(R.string.General_NoInformation));
                holder.profileDescription.setText(context.getResources().getString(R.string.General_NoInformation));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getOnProfileClickListener().onContactClicked(profileAndCompany, position);
                }
            });


        }else{
            holder.profileDescription.setText(context.getResources().getString(R.string.General_NoInformation));
            holder.careerDescription.setText(context.getResources().getString(R.string.General_NoInformation));
            holder.mNameTextView.setText(context.getResources().getString(R.string.General_NoInformation));
            holder.ceoImageView.setImageResource(R.drawable.ic_stub);
        }


    }




    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if ((charString.isEmpty() || charString.equals( "" )) && (listFilterCareer == null  || listFilterCareer.isEmpty())) {
                    contactListFiltered = contactList;
                }else if((charString.isEmpty() || charString.equals( "" )) && !(listFilterCareer == null  || listFilterCareer.isEmpty())){
                    doFilterOnlyCareer();
                }else if(!(charString.isEmpty() || charString.equals( "" )) && (listFilterCareer == null  || listFilterCareer.isEmpty())){
                    doFilterOnlyText(charString);
                }else{
                    doFilterBotCareerAndText(charString);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (CopyOnWriteArrayList<ProfileAndCompany>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void doFilterOnlyCareer(){
        List<ProfileAndCompany> filteredList = new CopyOnWriteArrayList<>();
        for (ProfileAndCompany profileAndCompany : contactList) {
            if(profileAndCompany.getCompany() != null && profileAndCompany.getCompany().getCareers() != null){
                HashMap<String,CareerOfUser> userMapCareer = profileAndCompany.getCompany().getCareers();
                boolean isContainCareer = false;
                for (Career career: listFilterCareer){

                    if(userMapCareer.containsKey( career.getId() )){
                        isContainCareer = true;
                        break;
                    }
                }
                if(isContainCareer){
                    filteredList.add( profileAndCompany );

                }
            }
        }
        contactListFiltered = filteredList;
    }

    public void doFilterOnlyText(String charString){
        List<ProfileAndCompany> filteredList = new CopyOnWriteArrayList<>();
        String filterTextNotAccents = FormatterUtil.stripAccents(charString ).toLowerCase();
        for (ProfileAndCompany row : contactList) {
            String fullNameNotAccents = null;
            String phoneNotAccents = null;
            if(row.getProfile() != null && row.getProfile().getFullName() != null) {
                fullNameNotAccents = FormatterUtil.stripAccents( row.getProfile().getFullName() ).toLowerCase();
            }
            if(row.getProfile() != null && row.getProfile().getPhone()!= null) {
                phoneNotAccents = FormatterUtil.stripAccents( row.getProfile().getPhone() ).toLowerCase();
            }

            if(fullNameNotAccents != null && fullNameNotAccents.contains(filterTextNotAccents  )) {
                filteredList.add( row );
            }
            else if(phoneNotAccents != null && phoneNotAccents.contains(filterTextNotAccents  )) {
                filteredList.add( row );
            }
        }
        contactListFiltered = filteredList;
    }

    public void doFilterBotCareerAndText(String charString){
        List<ProfileAndCompany> filteredList = new CopyOnWriteArrayList<>();
        for (ProfileAndCompany profileAndCompany : contactList) {
            boolean isContainCareer = false;
            if(profileAndCompany.getCompany() != null && profileAndCompany.getCompany().getCareers() != null){
                HashMap<String,CareerOfUser> userMapCareer = profileAndCompany.getCompany().getCareers();
                for (Career career: listFilterCareer){
                    if(userMapCareer.containsKey( career.getId() )){
                        isContainCareer = true;
                        break;
                    }
                }
            }
            if(isContainCareer){
                String filterTextNotAccents = FormatterUtil.stripAccents(charString ).toLowerCase();
                String fullNameNotAccents = null;
                String phoneNotAccents = null;
                if(profileAndCompany.getProfile() != null && profileAndCompany.getProfile().getFullName() != null) {
                    fullNameNotAccents = FormatterUtil.stripAccents( profileAndCompany.getProfile().getFullName() ).toLowerCase();
                }
                if(profileAndCompany.getProfile() != null && profileAndCompany.getProfile().getPhone()!= null) {
                    phoneNotAccents = FormatterUtil.stripAccents( profileAndCompany.getProfile().getPhone() ).toLowerCase();
                }

                if(fullNameNotAccents != null && fullNameNotAccents.contains(filterTextNotAccents  )) {
                    filteredList.add( profileAndCompany );
                }
                else if(phoneNotAccents != null && phoneNotAccents.contains(filterTextNotAccents  )) {
                    filteredList.add( profileAndCompany );
                }

            }
        }
        contactListFiltered = filteredList;
    }


    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView profileDescription;
        private TextView mNameTextView;
        private ImageView ceoImageView;
        private TextView careerDescription;


        ProfileViewHolder(View itemView) {
            super(itemView);
            profileDescription = itemView.findViewById(R.id.profileDescription);
            mNameTextView = itemView.findViewById(R.id.mNameTextView);
            ceoImageView = itemView.findViewById(R.id.ceoImageView);
            careerDescription = itemView.findViewById(R.id.careerDescription);

        }
    }

}
