package com.example.dotconnect.Features.Homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.dotconnect.Features.Homepage.Friends.FriendsFragment;
import com.example.dotconnect.Features.Homepage.PostFeed.PostFragment;
import com.example.dotconnect.Features.PostUpload.PostUploadActivity;
import com.example.dotconnect.Features.Profile.ProfileActivity;
import com.example.dotconnect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FriendsFragment friendsFragment;
    PostFragment postFragment;
    NavigationBarView bottomNavigationView;
    FloatingActionButton create_post_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.Bottom_nav_mainActivity);
        create_post_button=findViewById(R.id.create_post_button);
        friendsFragment= new FriendsFragment();
        postFragment=new PostFragment();
        set_Fragment(postFragment);
        set_BottomNavigation();

        create_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostUploadActivity.class));
            }
        });
    }

    private void set_Fragment(Fragment fragment){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Fragment_mainActivity,fragment);
        fragmentTransaction.commit();
    }
    private void set_BottomNavigation(){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.Bottom_Nav_Home:
                        set_Fragment(postFragment);
                        return true;
                    case R.id.Bottom_Nav_Friends:
                        set_Fragment(friendsFragment);
                        return true;
                    case R.id.Bottom_Nav_Profile:

                        startActivity(new Intent(MainActivity.this, ProfileActivity.class)
                                .putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()));
                        return false;
                }
                return true;
            }
        });
    }
}