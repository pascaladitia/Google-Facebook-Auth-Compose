package com.pascal.dansandroid.data.remote

import com.pascal.dansandroid.data.remote.dtos.ResponseDataItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    @GET("recruitment/positions.json")
    suspend fun search(
        @Query("description") description: String?,
        @Query("location") location: String?,
        @Query("full_time") full_time: String?
    ): Response<List<ResponseDataItem>>

    @GET("recruitment/positions.json")
    suspend fun listData(
        @Query("page") page: String
    ): Response<List<ResponseDataItem>>

    @GET("recruitment/positions/{id}")
    suspend fun detail(
        @Path("id") id: String
    ): Response<ResponseDataItem>
}