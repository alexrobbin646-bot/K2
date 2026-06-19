package com.example

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE

interface ApiService {
    @GET("api/v1/doramas")
    suspend fun getDoramas(): DoramaResponse

    @GET("api/v1/doramas/{id}")
    suspend fun getDorama(@Path("id") id: String): Dorama

    // Admin endpoints
    @POST("api/v1/doramas")
    suspend fun addDorama(@Body dorama: Dorama): Dorama

    @DELETE("api/v1/doramas/{id}")
    suspend fun deleteDorama(@Path("id") id: String)
}
