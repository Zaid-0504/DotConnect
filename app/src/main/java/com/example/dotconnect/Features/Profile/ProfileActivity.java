package com.example.dotconnect.Features.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dotconnect.Model.Profile;
import com.example.dotconnect.Model.ProfileResponse;
import com.example.dotconnect.R;
import com.example.dotconnect.data.LoginViewModel;
import com.example.dotconnect.data.Remote.ProfileViewModel;
import com.example.dotconnect.utils.ViewModelFactory;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.http.Url;

public class ProfileActivity extends AppCompatActivity {

    private ImageView cover_Image;
    private ImageView profile_image;
    private AppCompatButton custom_button;
    private Toolbar title_toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private String current_state;
    private String uid;
    private ProfileViewModel profileViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cover_Image=findViewById(R.id.profile_cover);
        profile_image=findViewById(R.id.profile_picture);
        custom_button=findViewById(R.id.profile_custom_button);
        title_toolbar=findViewById(R.id.profile_name_toolbar);
        collapsingToolbar=findViewById(R.id.profile_collapsing_toolbar);


        profileViewModel= new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory())
                .get(ProfileViewModel.class);
        uid=getIntent().getStringExtra("uid");

        if(Objects.equals(uid, FirebaseAuth.getInstance().getCurrentUser().getUid())){
            current_state="5";
        }
        else {
            // find current state from backend
        }
        HashMap<String,String> query_map= new HashMap<>();
        query_map.put("uid",uid);
        query_map.put("current_state",current_state);

        profileViewModel.load_profile_info(query_map)
                .observe(this, new Observer<ProfileResponse>() {
                    @Override
                    public void onChanged(ProfileResponse profileResponse) {
                        Profile data= profileResponse.getProfile();
                        collapsingToolbar.setTitle(data.getName());

                        try {
                            URL profile_url= new URL(data.getProfileUrl());
                            Uri profile_uri= Uri.parse(profile_url.toURI().toString());

                            Glide.with(ProfileActivity.this).load(profile_uri)
                                    .into(profile_image);

                            URL cover_url= new URL(data.getCoverUrl());
                            Uri cover_uri= Uri.parse(cover_url.toURI().toString());

                            Glide.with(ProfileActivity.this).load(cover_uri)
                                    .into(cover_Image);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}