package com.example.dotconnect.Features.Profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dotconnect.Features.PostUpload.PostUploadActivity;
import com.example.dotconnect.Model.GeneralResponse;
import com.example.dotconnect.Model.Profile;
import com.example.dotconnect.Model.ProfileResponse;
import com.example.dotconnect.R;
import com.example.dotconnect.data.BitmapToFileConverter;
import com.example.dotconnect.data.LoginViewModel;
import com.example.dotconnect.data.PostUploadViewModel;
import com.example.dotconnect.data.Remote.ApiClient;
import com.example.dotconnect.data.Remote.ProfileViewModel;
import com.example.dotconnect.utils.ViewModelFactory;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Url;

public class ProfileActivity extends AppCompatActivity {

    private ImageView cover_Image;
    private ImageView profile_image;
    private Button custom_button;
    private Toolbar title_toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private String current_state="";
    private String uid;
    private int position;
    PostUploadViewModel profile_update_viewModel;
    BitmapToFileConverter bitmapToFileConverter;
    private File compressed_file;
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

        bitmapToFileConverter= new BitmapToFileConverter(this);

        profile_update_viewModel= new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory())
                .get(PostUploadViewModel.class);

        ActivityResultLauncher<Intent> resultLauncher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data= result.getData();
                            Uri uri= data.getData();
                            try {
                                Bitmap bitmap=MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(),
                                        uri);
                                compressed_file=bitmapToFileConverter.ConvertToFile(bitmap,getFileName(uri));
                                Update_profile(position, compressed_file.getName());

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });


        profileViewModel= new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory())
                .get(ProfileViewModel.class);

        uid=getIntent().getStringExtra("uid");

        HashMap<String,String> query_map= new HashMap<>();
        if(Objects.equals(uid, FirebaseAuth.getInstance().getCurrentUser().getUid())){
            current_state="5";
            query_map.put("current_state",current_state);
        }

        query_map.put("uid",uid);
        query_map.put("current_uid",FirebaseAuth.getInstance().getCurrentUser().getUid());



        profileViewModel.load_profile_info(query_map)
                .observe(this, new Observer<ProfileResponse>() {
                    @Override
                    public void onChanged(ProfileResponse profileResponse) {
                        Profile data= profileResponse.getProfile();
                        collapsingToolbar.setTitle(data.getName());
                        current_state=profileResponse.getState();

                        try {
                            Log.d("Glide", "onChanged: "+data.getProfileUrl());
                            URL profile_url= new URL(ApiClient.BaseUrl+data.getProfileUrl());
                            Uri profile_uri= Uri.parse(profile_url.toURI().toString());


                            Glide.with(ProfileActivity.this).load(profile_uri)
                                    .into(profile_image);

                            URL cover_url= new URL(ApiClient.BaseUrl+data.getCoverUrl());
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
        switch (current_state){
            case "1":
                custom_button.setText("Unfriend");
                break;
            case "2":
                custom_button.setText("Requested");
                break;
            case "3":
                custom_button.setText("Accept");
                break;
            case "4":
                custom_button.setText("Add Friend");
                break;
            case "5":
                custom_button.setText("Edit Profile");
                break;
        }
        custom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.equals(current_state, "5")) {
                    custom_button.setEnabled(false);
                    CharSequence options[] = new CharSequence[]{"Edit Profile Image", "Edit Cover Image"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Choose option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            position = which;
                            select_image(resultLauncher);
                            custom_button.setEnabled(true);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });
    }
    public void select_image(ActivityResultLauncher<Intent> resultLauncher){
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);

    }
    public void Update_profile(int position,String file_name){
        MultipartBody.Builder builder= new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        builder.addFormDataPart("position",position+"");
        builder.addFormDataPart("file",
                file_name,
                RequestBody.create(MediaType.parse("multipart/form-data"), compressed_file));
        MultipartBody multipartBody= builder.build();
        profile_update_viewModel.upload_post(multipartBody,true).observe(ProfileActivity.this, new Observer<GeneralResponse>() {
            @Override
            public void onChanged(GeneralResponse generalResponse) {
                if (generalResponse !=null) {
                    if (generalResponse.getStatus() == 200) {
                        Toast.makeText(ProfileActivity.this, generalResponse.getMessage()
                                , Toast.LENGTH_SHORT);
                        try {

                            Log.d("Glide", "onChanged: "+generalResponse.getExtra());
                            if(position==0) {
                                URL profile_url = new URL(ApiClient.BaseUrl + generalResponse.getExtra());
                                Uri profile_uri = Uri.parse(profile_url.toURI().toString());


                                Glide.with(ProfileActivity.this).load(profile_uri)
                                        .into(profile_image);
                            }
                            else {
                                URL cover_url = new URL(ApiClient.BaseUrl + generalResponse.getExtra());
                                Uri cover_uri = Uri.parse(cover_url.toURI().toString());

                                Glide.with(ProfileActivity.this).load(cover_uri)
                                        .into(cover_Image);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    }
                }
        });
    }
    public String getFileName(@NonNull Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    if(cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    }
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}