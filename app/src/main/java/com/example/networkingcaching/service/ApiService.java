package com.example.networkingcaching.service;

import com.example.networkingcaching.model.ApiResponse;
import com.example.networkingcaching.model.access.AccessResponse;
import com.example.networkingcaching.model.ikwento.IkwentoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Author : Arvindo Mondal
 * Created on 05-02-2020
 */
public interface ApiService {

    @GET("/photos")
    Call<List<ApiResponse>> getAllPhotos();

    @GET("api/v1/book-restore")
    Call<AccessResponse> restoreApp(
            @Header("Authorization") String token
    );

    @POST("api/v4/aboutus")
    Call<IkwentoResponse> getAboutUs(
            @Body String orderId
    );

}
