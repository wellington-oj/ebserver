package com.android.starter.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

private const val APP_ID = "com.android.starter"
private const val FRAMEWORK = "KOTLIN"
private const val TEST_TYPE = "example"

interface Api {
    @GET("/what_now")
    suspend fun started(
        @Header("device") device: String,
        @Header("test_type") currentTestType: String = TEST_TYPE
    ): Response<String>

    @GET("/logdata")
    suspend fun logData(
        @Header("device") device: String,
        @Header("application_id") appId: String = APP_ID,
        @Header("test_type") currentTestType: String = TEST_TYPE,
        @Header("framework") framework: String = FRAMEWORK,
    ): Response<String>

    @GET("/done")
    suspend fun done(
        @Header("device") device: String,
        @Header("test_type") currentTestType: String = TEST_TYPE,
        @Header("application_id") appId: String = APP_ID,
        @Header("activity") activity: String = ".MainActivity"
    ): Response<String>

    @GET("/")
    suspend fun test(): Response<String>
}
