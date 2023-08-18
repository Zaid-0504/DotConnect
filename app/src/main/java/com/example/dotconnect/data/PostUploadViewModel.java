package com.example.dotconnect.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.dotconnect.Model.GeneralResponse;

import okhttp3.MultipartBody;

public class PostUploadViewModel extends ViewModel {

    private Repository repository;

    public PostUploadViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<GeneralResponse> upload_post(MultipartBody multipartBody, Boolean is_profile_update){
        return repository.upload_post_profile(multipartBody,is_profile_update);
    }
}
