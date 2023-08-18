package com.example.dotconnect.data.Remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.dotconnect.Model.ProfileResponse;
import com.example.dotconnect.Model.SearchResponse;
import com.example.dotconnect.data.Repository;

import java.util.HashMap;

public class SearchViewModel extends ViewModel {

    private Repository repository;

    public SearchViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<SearchResponse> search_user(String Query){
        return repository.Search_user(Query);
    }
}
