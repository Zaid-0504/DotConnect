package com.example.dotconnect.data.Remote;

import com.example.dotconnect.Model.AuthResponse;
import com.example.dotconnect.Model.GeneralResponse;
import com.example.dotconnect.Model.ProfileResponse;
import com.example.dotconnect.Model.GeneralResponse;
import com.example.dotconnect.Model.SearchResponse;
import com.example.dotconnect.Model.Users;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiClientService {

    @POST("login")
    Call<AuthResponse> login_user(@Body Users users);

    @POST("uploadpost")
    Call<GeneralResponse> upload_post(@Body MultipartBody multipartBody);

    @POST("update_profile")
    Call<GeneralResponse> update_profile(@Body MultipartBody multipartBody);

    @GET("loadprofileinfo")
    Call<ProfileResponse> profile_info(@QueryMap(encoded = true) HashMap<String,String> query_map);

    @GET("search")
    Call<SearchResponse> Search_info(@Query(value = "keyword") String keyword);
}
