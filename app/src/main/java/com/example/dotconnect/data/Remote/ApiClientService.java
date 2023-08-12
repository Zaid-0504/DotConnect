package com.example.dotconnect.data.Remote;

import com.example.dotconnect.Model.AuthResponse;
import com.example.dotconnect.Model.ProfileResponse;
import com.example.dotconnect.Model.UploadPostResponse;
import com.example.dotconnect.Model.Users;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiClientService {

    @POST("login")
    Call<AuthResponse> login_user(@Body Users users);

    @POST("uploadpost")
    Call<UploadPostResponse> upload_post(@Body MultipartBody multipartBody);

    @GET("loadprofileinfo")
    Call<ProfileResponse> profile_info(@QueryMap(encoded = true) HashMap<String,String> query_map);
}
