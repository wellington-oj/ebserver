package com.android.starter.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

interface Api {

    @GET("/fetch")
    Call<String> started(
            @Header("device") String device,
            @Header("test_type") String currentTestType,
            @Header("application_id") String appId,
            @Header("activity") String activity,
            @Header("framework") String framework
    );

    @GET("/logdata")
    Call<String> logData(
            @Header("device") String device,
            @Header("test_type") String currentTestType,
            @Header("application_id") String appId,
            @Header("activity") String activity,
            @Header("framework") String framework
    );

    @GET("/done")
    Call<String> done(
            @Header("device") String device,
            @Header("test_type") String currentTestType,
            @Header("application_id") String appId,
            @Header("activity") String activity,
            @Header("framework") String framework
    );

    @GET("/")
    Call<String> test();

}
