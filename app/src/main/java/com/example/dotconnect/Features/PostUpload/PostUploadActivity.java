package com.example.dotconnect.Features.PostUpload;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotconnect.Features.Homepage.MainActivity;
import com.example.dotconnect.Model.GeneralResponse;
import com.example.dotconnect.R;
import com.example.dotconnect.data.BitmapToFileConverter;
import com.example.dotconnect.data.PostUploadViewModel;
import com.example.dotconnect.utils.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PostUploadActivity extends AppCompatActivity {

    AppCompatSpinner spinner;
    private TextView post_button;
    private TextInputEditText post_edit_text;
    private ImageView add_image;
    private ImageView preview_image;
    private Boolean is_image_set;
    private File compressed_file;
    BitmapToFileConverter bitmapToFileConverter;
    PostUploadViewModel postUploadViewModel;
    private int privacy=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        spinner=findViewById(R.id.post_privacy_spinner);
        post_button= findViewById(R.id.post_button);
        post_edit_text=findViewById(R.id.post_editText);
        add_image=findViewById(R.id.add_image_icon);
        preview_image=findViewById(R.id.preview_image);

        bitmapToFileConverter= new BitmapToFileConverter(this);
        postUploadViewModel= new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory())
                .get(PostUploadViewModel.class);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedText= (TextView) view;
                if(selectedText!=null){
                    selectedText.setTextColor(Color.WHITE);
                    selectedText.setTypeface(null, Typeface.BOLD);
                }
                privacy=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status= post_edit_text.getText().toString();
                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(status.length()>0){
                    MultipartBody.Builder builder= new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM);
                    builder.addFormDataPart("post",status);
                    builder.addFormDataPart("postUserId",userId);
                    builder.addFormDataPart("privacy",privacy+"");

                    if(is_image_set){
                        builder.addFormDataPart("file",
                                compressed_file.getName(),
                                RequestBody.create(MediaType.parse("multipart/form-data"), compressed_file));
                    }

                    MultipartBody multipartBody= builder.build();

                    postUploadViewModel.upload_post(multipartBody,false).observe(PostUploadActivity.this,
                            new Observer<GeneralResponse>() {
                                @Override
                                public void onChanged(GeneralResponse generalResponse) {
                                    if (generalResponse !=null){
                                        if(generalResponse.getStatus()==200) {
                                            Toast.makeText(PostUploadActivity.this, generalResponse.getMessage()
                                                    , Toast.LENGTH_SHORT);
                                            startActivity(new Intent(PostUploadActivity.this, MainActivity.class));
                                            finish();
                                    }
                                    }else {
                                        Log.d("Retrofit", "onChanged: "+ System.currentTimeMillis()/1000);
                                    }
                                }
                            });
                }
            }
        });
        ActivityResultLauncher<Intent> resultLauncher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data= result.getData();
                            Uri uri= data.getData();
                            is_image_set=true;
                            try {
                                Bitmap bitmap=MediaStore.Images.Media.getBitmap(PostUploadActivity.this.getContentResolver(),
                                        uri);
                                add_image.setVisibility(View.GONE);
                                preview_image.setVisibility(View.VISIBLE);
                                compressed_file=bitmapToFileConverter.ConvertToFile(bitmap,getFileName(uri));
                                preview_image.setImageBitmap(bitmap);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                resultLauncher.launch(intent);
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