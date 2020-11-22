package com.example.networkingcaching.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author : Arvindo Mondal
 * Created on 05-02-2020
 */
public class RetrofitClient3 {
    private static Retrofit retrofit;
    private static Retrofit retrofit2;
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final String BASE_URL_2 = "http://13.251.29.198/";


    public static final String token = "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjdlNzJhMGIzYmI2YjMzZDU4NWE1NmM1ZGU5ODNhOTczNDgzNDA1MDUzZmI2MDQ1NjcxMTRlNzExYzQ1NDUwYTYzMjBhMjQwZWNmNWFlNDhiIn0.eyJhdWQiOiIxIiwianRpIjoiN2U3MmEwYjNiYjZiMzNkNTg1YTU2YzVkZTk4M2E5NzM0ODM0MDUwNTNmYjYwNDU2NzExNGU3MTFjNDU0NTBhNjMyMGEyNDBlY2Y1YWU0OGIiLCJpYXQiOjE1NzgzNzkzODEsIm5iZiI6MTU3ODM3OTM4MSwiZXhwIjoxNjEwMDAxNzgxLCJzdWIiOiI4Iiwic2NvcGVzIjpbXX0.OyVGR37FUszAAhknQnxwVfsVPKl388dMkzRl3uXF--4o5Z7sVoLhBSaQWvUp5Q6RuTPr2z0JQHAxbc19bMDQl399BvxQTlW4Mzbnprq9o7M2xICneKoo6dc5nTSj_P2f9_9TK6Y77FMtQYhlKD_IZYwL6JDzP9oH4i2XxMbSf4RuU5xWTra6EshSkmc5BDnB_lMF72VfORi-08T-P_rS9jLzDHwtAjwDsnT7QCnDDalBeAs6US4Efe9HdILVjocWeEWiR7Tn4VNZFkoWMHJ1Mqk6QSWAXGdsPvESvBtjJpqGYA_UtpCfiPT6-MCKitUaCpFZ0N6RU29k4HMB2Hh2bHOdItTm39T_1A1s78lWXhY-ZmyIAzkdKxOwoKNDbIsqXRlf2MdNTh-B6K6Mp5k7YoEGeXobHgf2EMPd-AsSmHpkmSv5vZsCTbf8STQsrBpoZtPIhwC_nyTsj4Opr-h38Pm-gHgz-1pDCtsnwgXUXzLd-Qto36XFlmjfX81LDTsWQVhMdCwoy6pFPXZAB2uEGrqqYUFJbZj0mduvRL8pwOwkLbLKRJNDv4N3Db2ne5GosGe3G-mg6h2nkkHFWeBz_Kj2A_tqEzdV5lweYrFTUXzL5ocW0mNCzdvKnPls8Q0PkwlXfac7clxyvk-FP18IEUMNRhhnYfrDXcTr-bUiweU";

    private static Cache cache;
    private static Context context;

    private static Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    private static Cache getHttpCache(Context application) {
//        int cacheSize = 10 * 1024 * 1024;
        File httpCacheDirectory = new File(application.getCacheDir(), "cache_file");
        return new Cache(httpCacheDirectory, 10485760);
    }

    private static class ResponseCacheInterceptor1 implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60)
                    .build();
        }
    }

    private static class OfflineResponseCacheInterceptor1 implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!isNetworkAvailable()) {
                request = request.newBuilder()
                        .header("Cache-Control",
                                "public, only-if-cached, max-stale=" + 2419200)
                        .build();
            }
            return chain.proceed(request);
        }
    }


    private static OkHttpClient createCachedClient(Cache cache) {
        final int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
        final int maxAge = 60; // read from cache for 60 seconds even if there is internet connection

        return new OkHttpClient.Builder()
                // Enable response caching
                .addNetworkInterceptor(new ResponseCacheInterceptor())
                .addInterceptor(new OfflineResponseCacheInterceptor())
                // Set the cache location and size (5 MB)
                .cache(cache)
                .build();
    }

    public static Retrofit getRetrofitInstance(Context context) {
        RetrofitClient3.context = context;
        if (retrofit == null) {
            retrofit =  new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
//                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createCachedClient(getHttpCache(context)))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstance2(Context context) {
        RetrofitClient3.context = context;
        if (retrofit2 == null) {
            retrofit2 =  new Retrofit.Builder()
                    .baseUrl(BASE_URL_2)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
//                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createCachedClient(getHttpCache(context)))
                    .build();
        }
        return retrofit2;
    }

    //--------------------

    private static class ResponseCacheInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if(Boolean.valueOf(request.header("ApplyResponseCache"))) {
                Log.e("ResponseCache", "Response cache applied");
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .removeHeader("ApplyResponseCache")
                        .header("Cache-Control", "public, max-age=" + 60)
                        .build();
            } else {
                Log.e("ResponseCache", "Response cache not applied");
                return chain.proceed(chain.request());
            }
        }
    }

    private static class OfflineResponseCacheInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if(Boolean.valueOf(request.header("ApplyOfflineCache"))) {
                Log.e("OfflineResponse","Offline cache applied");
                if(!isNetworkAvailable()) {
                    request = request.newBuilder()
                            .removeHeader("ApplyOfflineCache")
                            .header("Cache-Control",
                                    "public, only-if-cached, max-stale=" + 2419200)
                                            .build();
                }
            } else
                Log.e("OfflineResponse","Offline cache not applied");

            return chain.proceed(request);
        }
    }

    //----------------


    public static boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
