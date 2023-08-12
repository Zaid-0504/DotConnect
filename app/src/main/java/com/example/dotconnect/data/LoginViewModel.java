package com.example.dotconnect.data;

import androidx.lifecycle.LiveData;

import com.example.dotconnect.Model.AuthResponse;
import com.example.dotconnect.Model.ProfileResponse;
import com.example.dotconnect.Model.Users;

import java.util.HashMap;

public class LoginViewModel extends androidx.lifecycle.ViewModel {

    private Repository repository;
    private LiveData<AuthResponse> authResponse;

    public LoginViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<AuthResponse> login(Users user){
        authResponse=repository.login(user);
        return authResponse;
    }

}
