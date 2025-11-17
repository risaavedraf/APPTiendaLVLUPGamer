package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.model.ReviewRequest
import com.example.tiendalvlupgamer.model.ReviewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApiService {

    @GET("productos/{productoId}/reviews")
    suspend fun getReviewsForProduct(@Path("productoId") productoId: Long): Response<List<ReviewResponse>>

    @POST("productos/{productoId}/reviews")
    suspend fun createReview(
        @Path("productoId") productoId: Long,
        @Body reviewRequest: ReviewRequest
    ): Response<ReviewResponse>
}
