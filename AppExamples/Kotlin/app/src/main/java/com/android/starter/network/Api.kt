package com.android.starter.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

private const val APP_ID = "com.android.starter"
private const val FRAMEWORK = "KOTLIN"
private const val TEST_TYPE = "example"
private const val ACTIVITY = ".MainActivity"

interface Api {
    @GET("/fetch")
    suspend fun started(
        @Header("device") device: String,
        @Header("application_id") appId: String,
        @Header("test_type") currentTestType: String = TEST_TYPE,
        @Header("activity") activity: String = ACTIVITY,
        @Header("framework") framework: String = FRAMEWORK
    ): Response<String>

    @GET("/logdata")
    suspend fun logData(
        @Header("device") device: String,
        @Header("application_id") appId: String,
        @Header("test_type") currentTestType: String = TEST_TYPE,
        @Header("activity") activity: String = ACTIVITY,
        @Header("framework") framework: String = FRAMEWORK
    ): Response<String>

    @GET("/done")
    suspend fun done(
        @Header("device") device: String,
        @Header("application_id") appId: String,
        @Header("test_type") currentTestType: String = TEST_TYPE,
        @Header("activity") activity: String = ACTIVITY,
        @Header("framework") framework: String = FRAMEWORK
    ): Response<String>

    @GET("/")
    suspend fun test(): Response<String>
}
