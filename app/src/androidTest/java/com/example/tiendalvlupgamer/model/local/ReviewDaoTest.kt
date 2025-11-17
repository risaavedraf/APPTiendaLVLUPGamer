package com.example.tiendalvlupgamer.model.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ReviewDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var reviewDao: ReviewDao
    private lateinit var productDao: ProductDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        reviewDao = database.reviewDao()
        productDao = database.productDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetReviews() = runTest {
        val product = ProductEntity("1", "category", "name", 10, "description", 0)
        productDao.insertar(listOf(product))
        val review = ReviewEntity(productId = "1", rating = 5, comment = "comment")
        reviewDao.insert(review)
        val retrievedReviews = reviewDao.getReviewsForProduct("1").first()
        assertThat(retrievedReviews).hasSize(1)
        assertThat(retrievedReviews[0].comment).isEqualTo("comment")
    }
}