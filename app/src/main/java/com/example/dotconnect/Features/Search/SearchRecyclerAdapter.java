package com.example.dotconnect.Features.Search;

import android.content.Context;
import android.content.Intent;
import android.hardware.lights.LightState;
import android.net.Uri;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dotconnect.Features.Profile.ProfileActivity;
import com.example.dotconnect.Model.SearchResultItem;
import com.example.dotconnect.Model.Users;
import com.example.dotconnect.R;
import com.example.dotconnect.data.Remote.ApiClient;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder> {

   Context context;
   List<SearchResultItem> Users;

    public SearchRecyclerAdapter(Context context, List<SearchResultItem> users) {
        this.context = context;
        this.Users = users;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_rcy_layout,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        if(Users!= null) {
            SearchResultItem user = Users.get(position);

            Uri profile_uri = null;
            try {
                URL profile_url = new URL(ApiClient.BaseUrl + user.getProfileUrl());
                profile_uri = Uri.parse(profile_url.toURI().toString());
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
            }


            Glide.with(context).load(profile_uri)
                    .into(holder.profile_img);
            holder.userName.setText(user.getName());
        }
    }

    @Override
    public int getItemCount() {
        if(Users != null) {
            return Users.size();
        }else{
            return 0;
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView profile_img;
        TextView userName;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
             profile_img= itemView.findViewById(R.id.profile_picture_search);
             userName= itemView.findViewById(R.id.search_username);

             itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(context,ProfileActivity.class).putExtra("uid",
                    Users.get(getAdapterPosition()).getUid()));
        }
    }
}
