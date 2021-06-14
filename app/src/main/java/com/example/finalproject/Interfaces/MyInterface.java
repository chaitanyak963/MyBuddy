package com.example.finalproject.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInterface {
    @GET("top-headlines?sources=bbc-news&apiKey=426c9cb4088e4f5881c342a4ac9a5a71")
    Call<String> value();
}
