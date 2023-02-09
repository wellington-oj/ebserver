package com.android.starter.network;

import com.android.starter.BuildConfig;

import java.io.IOException;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Repository {

    private final String ip = "192.168.184.195";
    private final String baseUrl = "http://$ip:3000/";

    private final String FRAMEWORK = "JAVA";
    private final String TEST_TYPE = "example";
    private final String ACTIVITY = ".MainActivity";

    Api service = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(Api.class);

    public String started() throws IOException {
        return service.started(
                android.os.Build.MODEL,
                TEST_TYPE,
                BuildConfig.APPLICATION_ID,
                ACTIVITY,
                FRAMEWORK
        ).execute().body();
    }

    public String logData() throws IOException {
        return service.logData(
                android.os.Build.MODEL,
                TEST_TYPE,
                BuildConfig.APPLICATION_ID,
                ACTIVITY,
                FRAMEWORK
        ).execute().body();
    }

    public String done() throws IOException {
        return service.done(
                android.os.Build.MODEL,
                TEST_TYPE,
                BuildConfig.APPLICATION_ID,
                ACTIVITY,
                FRAMEWORK
        ).execute().body();
    }

}
