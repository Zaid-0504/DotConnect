package com.example.dotconnect.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dotconnect.Model.AuthResponse;
import com.example.dotconnect.Model.ProfileResponse;
import com.example.dotconnect.Model.GeneralResponse;
import com.example.dotconnect.Model.SearchResponse;
import com.example.dotconnect.Model.Users;
import com.example.dotconnect.data.Remote.ApiClientService;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private static Repository instance;
    private ApiClientService apiClientService;

    public Repository(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public static Repository getInstance(ApiClientService apiClientService) {

        if(instance== null)
            instance=new Repository(apiClientService);

        return instance;
    }

    public LiveData<AuthResponse> login(Users user){

        MutableLiveData<AuthResponse> authResponse= new MutableLiveData<>();
        Call<AuthResponse> call= apiClientService.login_user(user);
        Log.d("Retrofit", "onResponse: Success");
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Retrofit", "onResponse: Success from complete");
                    authResponse.postValue(response.body());}
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                Log.d("Retrofit", "onResponse:"+ throwable);
            }
        });

        return authResponse;
    }

    public LiveData<ProfileResponse> load_profile_info(HashMap<String,String> query_map){

        MutableLiveData<ProfileResponse> profileResponse= new MutableLiveData<>();
        Call<ProfileResponse> call= apiClientService.profile_info(query_map);
        Log.d("Retrofit", "onResponse: Success");
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Retrofit", "onResponse: Success from complete");
                    Log.d("Retrofit", "onResponse: " + response.errorBody()+"  "+ response.code());
                    profileResponse.postValue(response.body());}
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                Log.d("Retrofit", "onResponse:"+ throwable);
            }
        });

        return profileResponse;
    }

    public LiveData<GeneralResponse> upload_post_profile(MultipartBody multipartBody, Boolean is_profile_update){

        MutableLiveData<GeneralResponse> generalResponseMutableLiveData= new MutableLiveData<>();
        Call<GeneralResponse> call=null;
        if (is_profile_update){
             call= apiClientService.update_profile(multipartBody);}
        else{
             call= apiClientService.upload_post(multipartBody);}
        Log.d("Retrofit", "onResponse: " + "Done at "+ System.currentTimeMillis()/1000);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("Retrofit", "onResponse: " + "Done at "+ System.currentTimeMillis()/1000);
                    generalResponseMutableLiveData.postValue(response.body());
                }else {
                    Log.d("Retrofit", "onResponse: " + response.errorBody()+"  "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Log.d("Retrofit", "onResponse:" + t);
            }
        });

        return generalResponseMutableLiveData;
    }

    public LiveData<SearchResponse> Search_user(String Query){
        MutableLiveData<SearchResponse> searchResponse = new MutableLiveData<>();
        Call<SearchResponse> call= apiClientService.Search_info(Query);
        Log.d("Retrofit", "onResponse: " + "Done at "+ System.currentTimeMillis()/1000);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("Retrofit", "onResponse: " + "Done at "+ System.currentTimeMillis()/1000);
                    searchResponse.postValue(response.body());
                }else {
                    Log.d("Retrofit", "onResponse: " + response.errorBody()+"  "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d("Retrofit", "onResponse:"+ t);
            }
        });
        return searchResponse;
    }
}
