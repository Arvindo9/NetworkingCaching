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
public class RetrofitClient4 {
    private static Retrofit retrofit;
    private static Retrofit retrofit2;
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final String BASE_URL_2 = "http://13.251.29.198/";


    public static final String token = "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjdlNzJhMGIzYmI2YjMzZDU4NWE1NmM1ZGU5ODNhOTczNDgzNDA1MDUzZmI2MDQ1NjcxMTRlNzExYzQ1NDUwYTYzMjBhMjQwZWNmNWFlNDhiIn0.eyJhdWQiOiIxIiwianRpIjoiN2U3MmEwYjNiYjZiMzNkNTg1YTU2YzVkZTk4M2E5NzM0ODM0MDUwNTNmYjYwNDU2NzExNGU3MTFjNDU0NTBhNjMyMGEyNDBlY2Y1YWU0OGIiLCJpYXQiOjE1NzgzNzkzODEsIm5iZiI6MTU3ODM3OTM4MSwiZXhwIjoxNjEwMDAxNzgxLCJzdWIiOiI4Iiwic2NvcGVzIjpbXX0.OyVGR37FUszAAhknQnxwVfsVPKl388dMkzRl3uXF--4o5Z7sVoLhBSaQWvUp5Q6RuTPr2z0JQHAxbc19bMDQl399BvxQTlW4Mzbnprq9o7M2xICneKoo6dc5nTSj_P2f9_9TK6Y77FMtQYhlKD_IZYwL6JDzP9oH4i2XxMbSf4RuU5xWTra6EshSkmc5BDnB_lMF72VfORi-08T-P_rS9jLzDHwtAjwDsnT7QCnDDalBeAs6US4Efe9HdILVjocWeEWiR7Tn4VNZFkoWMHJ1Mqk6QSWAXGdsPvESvBtjJpqGYA_UtpCfiPT6-MCKitUaCpFZ0N6RU29k4HMB2Hh2bHOdItTm39T_1A1s78lWXhY-ZmyIAzkdKxOwoKNDbIsqXRlf2MdNTh-B6K6Mp5k7YoEGeXobHgf2EMPd-AsSmHpkmSv5vZsCTbf8STQsrBpoZtPIhwC_nyTsj4Opr-h38Pm-gHgz-1pDCtsnwgXUXzLd-Qto36XFlmjfX81LDTsWQVhMdCwoy6pFPXZAB2uEGrqqYUFJbZj0mduvRL8pwOwkLbLKRJNDv4N3Db2ne5GosGe3G-mg6h2nkkHFWeBz_Kj2A_tqEzdV5lweYrFTUXzL5ocW0mNCzdvKnPls8Q0PkwlXfac7clxyvk-FP18IEUMNRhhnYfrDXcTr-bUiweU";

    private static Cache cache;

    private static Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    private static Cache getHttpCache(Context application) {
        if(cache == null) {
//        int cacheSize = 10 * 1024 * 1024;
            File httpCacheDirectory = new File(application.getCacheDir(), "cache_file");
            cache = new Cache(httpCacheDirectory, 10485760);
        }
        return cache;
    }

    private static Interceptor getInterccepter(final Context context){
        return new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request();
                if(!isNetworkAvailable(context)) {
                    int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
                    request = request.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    private static OkHttpClient getOkhttpClient(Context context, Cache cache) {
//    OkHttpClient provideOkhttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA)
                .build();

        return new OkHttpClient
                .Builder()
                .cache(cache)
//                .addInterceptor(interceptor)
                .addNetworkInterceptor(getInterccepter(context))
//                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(300, TimeUnit.SECONDS)
                .connectTimeout(300, TimeUnit.SECONDS)
//                .connectionSpecs(Collections.singletonList(ConnectionSpec.COMPATIBLE_TLS))
                .build();
    }

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            retrofit =  new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
//                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createCachedClient(context, getHttpCache(context)))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstance2(Context context) {
        if (retrofit2 == null) {
            retrofit2 =  new Retrofit.Builder()
                    .baseUrl(BASE_URL_2)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
//                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createCachedClient2(context, getHttpCache(context)))
                    .build();
        }
        return retrofit2;
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //---------------------------


    private static OkHttpClient createCachedClient(final Context context, Cache cache) {
        final int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
        final int maxAge = 60; // read from cache for 60 seconds even if there is internet connection


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient
                .Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();

                        if (!isNetworkAvailable(context)) {
                            Log.d("dff", "rewriting request");

                            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                            request = request.newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }

                        return chain.proceed(request);
                    }
                })
                .addNetworkInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        String cacheControl = originalResponse.header("Cache-Control");

                        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                            return originalResponse.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + 10)
                                    .build();
                        } else {
                            return originalResponse;
                        }
                    }
                })
                .build();
    }



    private static OkHttpClient createCachedClient2(final Context context, Cache cache) {
        final int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
        final int maxAge = 60; // read from cache for 60 seconds even if there is internet connection


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient
                .Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                       /* Request originalRequest = chain.request();
                        String cacheHeaderValue = isNetworkAvailable(context)
                                ? "public, max-age=" + maxAge
                                : "public, max-stale=" + maxStale;
                        Request request = originalRequest.newBuilder()
                                .build();
                        Response response = chain.proceed(request);
                        return response.newBuilder()
                                .header("Key", token)
                                .header("Cache-Control", cacheHeaderValue)
                                .build();*/

                        Request original = chain.request();

                        String cacheHeaderValue = isNetworkAvailable(context)
                                ? "public, max-age=" + maxAge
                                : "public, max-stale=" + maxStale;

                        Request request = original.newBuilder()
                                .header("Key", token)
                                .header("Cache-Control", cacheHeaderValue)
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .addNetworkInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();

                        String cacheHeaderValue = isNetworkAvailable(context)
                                ? "public, max-age=" + maxAge
                                : "public, max-stale=" + maxStale;

                        Request request = original.newBuilder()
                                .header("Key", token)
                                .header("Cache-Control", cacheHeaderValue)
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();
    }


}
