package com.heitorzanetti.chamatest.home.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heitorzanetti.chamatest.R;
import com.heitorzanetti.chamatest.utils.GithubProfile;
import com.heitorzanetti.chamatest.utils.GithubRepository;
import com.heitorzanetti.chamatest.widgets.OpenSansTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by heitorzc on 18/01/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM   = 1;

    private Context context;
    private ArrayList<Object> items = new ArrayList<>();


    class ViewHolderProfile extends RecyclerView.ViewHolder {
        @Bind(R.id.tvUserName)    OpenSansTextView tvUsername;
        @Bind(R.id.tvFullName)    OpenSansTextView tvFullName;
        @Bind(R.id.tvRepoCount)   OpenSansTextView tvRepoCount;

        private ViewHolderProfile(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public View getView(){
            return itemView;
        }
    }


    class ViewHolderRepo extends RecyclerView.ViewHolder {
        @Bind(R.id.tvRepoName)      OpenSansTextView tvRepoName;
        @Bind(R.id.tvRepoDate)      OpenSansTextView tvRepoDate;

        private ViewHolderRepo(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }



    public HomeAdapter(Context context, ArrayList<GithubProfile> items) {
        this.context = context;

        for (GithubProfile profile : items){
            this.items.add(profile);
            this.items.addAll(profile.getRepos());
        }

    }



    private String formatDate(String dateToFormat){

        Date date = null;
        String dateFormat = context.getString(R.string.repo_date_format);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());

        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (date != null) ? simpleDateFormat.format(date) : dateToFormat;

    }


    private View.OnClickListener onClickListener(final String url){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        };
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list_header, parent, false);
            return new ViewHolderProfile(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list_item, parent, false);
            return new ViewHolderRepo(v);
        }

    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderProfile){

            ViewHolderProfile vHolder = (ViewHolderProfile) holder;

            final GithubProfile p = (GithubProfile) items.get(position);

            vHolder.tvUsername.setText(p.getLogin());
            vHolder.tvFullName.setText(p.getName());
            vHolder.tvRepoCount.setText(String.valueOf(p.getPublic_repos()));

            //Set an OnClickListener to take the user to the GitHubProfile's website.
            vHolder.itemView.setOnClickListener(onClickListener(p.getHtml_url()));

        }
        else {

            ViewHolderRepo vHolder = (ViewHolderRepo) holder;

            final GithubRepository r = (GithubRepository) items.get(position);

            vHolder.tvRepoName.setText(r.getName());

            String date = formatDate(r.getCreated_at());
            vHolder.tvRepoDate.setText(date);

            //Set an OnClickListener to take the user to the GitHubRepository's website.
            vHolder.itemView.setOnClickListener(onClickListener(r.getHtml_url()));

        }

    }


    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof GithubProfile){
            return TYPE_HEADER;
        }

        return TYPE_ITEM;

    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}
