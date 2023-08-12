package com.example.dotconnect.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.dotconnect.Model.UploadPostResponse;

import okhttp3.MultipartBody;

public class PostUploadViewModel extends ViewModel {

    private Repository repository;

    public PostUploadViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<UploadPostResponse> upload_post(MultipartBody multipartBody){
        return repository.upload_post(multipartBody);
    }
}
