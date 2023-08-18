package com.example.dotconnect.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.dotconnect.data.LoginViewModel;
import com.example.dotconnect.data.PostUploadViewModel;
import com.example.dotconnect.data.Remote.ApiClient;
import com.example.dotconnect.data.Remote.ApiClientService;
import com.example.dotconnect.data.Remote.ProfileViewModel;
import com.example.dotconnect.data.Remote.SearchViewModel;
import com.example.dotconnect.data.Repository;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Repository repository;

    public ViewModelFactory() {

        ApiClientService apiClientService= ApiClient.getRetrofit().create(ApiClientService.class);
        repository=Repository.getInstance(apiClientService);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelClass.isAssignableFrom(LoginViewModel.class))
            return (T) new LoginViewModel(repository);
        else if (modelClass.isAssignableFrom(ProfileViewModel.class))
            return (T) new ProfileViewModel(repository);
        else if (modelClass.isAssignableFrom(PostUploadViewModel.class))
            return (T) new PostUploadViewModel(repository);
        else if (modelClass.isAssignableFrom(SearchViewModel.class))
            return (T) new SearchViewModel(repository);
        throw new IllegalArgumentException("View Model not found !");


    }
}
