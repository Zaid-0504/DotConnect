package com.example.dotconnect.data.Remote;

import androidx.lifecycle.LiveData;

import com.example.dotconnect.Model.ProfileResponse;
import com.example.dotconnect.data.Repository;

import java.util.HashMap;

public class ProfileViewModel  extends androidx.lifecycle.ViewModel{

    private Repository repository;

    public ProfileViewModel(Repository repository) {
        this.repository = repository;
    }
    public LiveData<ProfileResponse> load_profile_info(HashMap<String,String> query_map){
        return repository.load_profile_info(query_map);
    }
}
