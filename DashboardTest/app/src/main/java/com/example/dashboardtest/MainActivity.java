package com.example.dashboardtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    public static String localhostip = "10.42.0.1";
    public static String port = "3000";
    Api myApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.urlWhatNow)
                .addConverterFactory(ScalarsConverterFactory.create()).build();
        myApi = retrofit.create(Api.class);

        whatNow();

        setContentView(R.layout.activity_main);
    }

    private void whatNow() {
        Call<String> whatNow = myApi.getData();

        whatNow.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                foo(data);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void foo(String data) {
        Log.v("dashboardTest", data);
        try {
            Log.v("dashboardTest", "fake some work");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logData();
    }

    public void logData(){
        Call<String> logData = myApi.logData();
        logData.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v("dashboardTest", "log done");
                done();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void done() {
        Call<String> logData = myApi.done();
        logData.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v("dashboardTest", "over");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}

interface Api {
    String urlWhatNow = "http://" + MainActivity.localhostip + ":"+MainActivity.port+"/";

    @GET("what_now")
    Call<String> getData();

    @GET("logdata")
    Call<String> logData();

    @GET("done")
    Call<String> done();

}