package com.example.networkingcaching.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networkingcaching.R;
import com.example.networkingcaching.model.ApiResponse;
import com.example.networkingcaching.model.access.AccessResponse;
import com.example.networkingcaching.model.ikwento.IkwentoResponse;
import com.example.networkingcaching.service.ApiService;
import com.example.networkingcaching.service.RetrofitClient5;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private BookAdapter adapterBook;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewBook;
    private WebView webView;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.customRecyclerView);
        recyclerViewBook = findViewById(R.id.bookRecyclerView);
        webView = findViewById(R.id.webview);

        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Loading....");
//        progressDoalog.show();

    }

    private void api() {
        /*Create handle for the RetrofitInstance interface*/
        ApiService service = RetrofitClient5.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        Call<List<ApiResponse>> call = service.getAllPhotos();
        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<ApiResponse>> call, @NotNull Response<List<ApiResponse>> response) {
                progressDoalog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<ApiResponse>> call, @NotNull Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void api2() {

        /*Create handle for the RetrofitInstance interface*/
        ApiService service = RetrofitClient5.getRetrofitInstance2(getApplicationContext()).create(ApiService.class);
        Call<AccessResponse> call = service.restoreApp(RetrofitClient5.token);
        call.enqueue(new Callback<AccessResponse>() {
            @Override
            public void onResponse(@NotNull Call<AccessResponse> call, @NotNull Response<AccessResponse> response) {
                progressDoalog.dismiss();
                getBookData(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<AccessResponse> call, @NotNull Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void api3() {
        String json = "{\"user_id\":\"oRZ4Mmyin6A=\",\"ostype\":\"ttP30MOINT8\\/fUDZ\",\"version\":\"Lxj\\/AsCQyDs=\"}";

        /*Create handle for the RetrofitInstance interface*/
        ApiService service = RetrofitClient5.getRetrofitInstance3(getApplicationContext()).create(ApiService.class);
        Call<IkwentoResponse> call = service.getAboutUs(json);
        call.enqueue(new Callback<IkwentoResponse>() {
            @Override
            public void onResponse(@NotNull Call<IkwentoResponse> call, @NotNull Response<IkwentoResponse> response) {
                if(response.raw().cacheResponse() != null){
                    ResponseBody df = response.raw().cacheResponse().body();
                    okhttp3.Response dfd = response.raw().cacheResponse();
//                    getAboutus(response.raw().cacheResponse().body());
                }

                getAboutus(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<IkwentoResponse> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAboutus(IkwentoResponse body) {
        if(body != null){
//            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadDataWithBaseURL("", body.getData().getContent(),
                    "text/html", "UTF-8", "");
        }
        else{
            Log.e("generateDataList", "null");
        }
    }

    private void getBookData(AccessResponse body) {
        if(body != null) {
            Log.e("generateDataList", "data load");
            adapterBook = new BookAdapter(this, body.getData().getBookPremium());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerViewBook.setLayoutManager(layoutManager);
            recyclerViewBook.setAdapter(adapterBook);
        }
        else{
            Log.e("generateDataList", "null");
        }
    }


    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<ApiResponse> photoList) {
        if(photoList != null) {
            Log.e("generateDataList", "data load");
            recyclerView = findViewById(R.id.customRecyclerView);
            adapter = new MainAdapter(this, photoList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
        else{
            Log.e("generateDataList", "null");
        }
    }

    public void onClick(View view) {
        if(view.getId() == R.id.button){
            recyclerViewBook.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            api();
        }
        else if(view.getId() == R.id.buttonBook){
            recyclerViewBook.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            api2();
        }
        else if(view.getId() == R.id.button3){
            recyclerViewBook.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            api3();
        }
    }
}
