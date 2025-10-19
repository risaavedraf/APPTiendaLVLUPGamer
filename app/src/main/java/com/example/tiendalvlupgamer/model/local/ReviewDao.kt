package com.example.tiendalvlupgamer.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY reviewId DESC")
    fun getReviewsForProduct(productId: String): Flow<List<ReviewEntity>>
}