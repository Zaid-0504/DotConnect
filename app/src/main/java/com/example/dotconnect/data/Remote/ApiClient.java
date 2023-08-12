package com.example.dotconnect.data.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String BaseUrl="http://192.168.1.102/DotConnect/public/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null){

            retrofit= new Retrofit.Builder().baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }

        return retrofit;

    }
}
