package com.example.news.network;

import com.example.news.models.NewsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("v2/everything")
    Call<NewsApiResponse> getRelatedNews(
            @Query("q") String query,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize
    );
}
