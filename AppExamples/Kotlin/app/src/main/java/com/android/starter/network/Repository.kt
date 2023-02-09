package com.android.starter.network

import android.util.Log
import com.android.starter.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class Repository {

    private val ip = "192.168.1.68"
    private val baseUrl = "http://$ip:3000/"

    private val service = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(Api::class.java)

    suspend fun started(): String {
        Log.i("REQUEST", "WHATNOW")
        val call = service.started(
            device = android.os.Build.MODEL,
            appId = BuildConfig.APPLICATION_ID
        )
        return call.body() ?: throw ApiError(call.errorBody()!!.toString())
    }

    suspend fun logData(): String {
        Log.i("REQUEST", "LOGDATA")
        val call = service.logData(
            device = android.os.Build.MODEL,
            appId = BuildConfig.APPLICATION_ID
        )
        return call.body() ?: throw ApiError(call.errorBody()!!.toString())
    }

    suspend fun done(): String {
        Log.i("REQUEST", "DONE")
        val call = service.done(
            device = android.os.Build.MODEL,
            appId = BuildConfig.APPLICATION_ID
        )
        return call.body() ?: throw ApiError(call.errorBody()!!.toString())
    }

    suspend fun test(): String {
        val call = service.test()
        return call.body() ?: throw ApiError(call.errorBody()!!.toString())
    }
}

class ApiError(body: String): Exception(body)