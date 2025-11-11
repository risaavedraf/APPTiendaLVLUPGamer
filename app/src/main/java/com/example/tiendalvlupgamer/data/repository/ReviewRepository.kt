package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.ReviewApiService
import com.example.tiendalvlupgamer.model.ReviewRequest

class ReviewRepository(private val reviewApiService: ReviewApiService) {

    suspend fun getReviewsForProduct(productId: Long) = reviewApiService.getReviewsForProduct(productId)

    suspend fun createReview(productId: Long, reviewRequest: ReviewRequest) = reviewApiService.createReview(productId, reviewRequest)
}
