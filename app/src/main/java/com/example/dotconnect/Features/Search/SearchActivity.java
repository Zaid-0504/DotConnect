package com.example.dotconnect.Features.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotconnect.Model.SearchResponse;
import com.example.dotconnect.Model.SearchResultItem;
import com.example.dotconnect.Model.Users;
import com.example.dotconnect.R;
import com.example.dotconnect.data.Remote.SearchViewModel;
import com.example.dotconnect.utils.ViewModelFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView default_text;
    SearchView searchView;
    SearchViewModel searchViewModel;
    ArrayList<SearchResultItem> usersList;
    SearchRecyclerAdapter searchRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar=findViewById(R.id.toolbar_search);
        recyclerView=findViewById(R.id.search_recycler);
        default_text=findViewById(R.id.default_text);
        searchView=findViewById(R.id.search_user);

        usersList= new ArrayList<>();
        searchRecyclerAdapter= new SearchRecyclerAdapter(SearchActivity.this,usersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchRecyclerAdapter);

        searchViewModel= new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory())
                .get(SearchViewModel.class);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.super.onBackPressed();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDB(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>2){
                    searchDB(newText);
                }else {
                    default_text.setVisibility(View.VISIBLE);
                    if(usersList!=null)
                        usersList.clear();
                    searchRecyclerAdapter.notifyDataSetChanged();

                }
                return true;
            }
        });
    }

    public void searchDB(String Query){
        searchViewModel.search_user(Query).observe(SearchActivity.this, new Observer<SearchResponse>() {
            @Override
            public void onChanged(SearchResponse searchResponse) {
                if(searchResponse.getStatus()== 200){
                    default_text.setVisibility(View.GONE);
                    if(usersList!=null)
                        usersList.clear();
                    Log.d("Retrofit", "onChanged: "+searchResponse.getResult().get(0).getName() );
                    usersList.addAll(searchResponse.getResult());
                    searchRecyclerAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(SearchActivity.this,searchResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    default_text.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}