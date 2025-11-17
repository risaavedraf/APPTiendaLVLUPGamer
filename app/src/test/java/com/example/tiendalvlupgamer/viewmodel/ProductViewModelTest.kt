package com.example.tiendalvlupgamer.viewmodel

import com.example.tiendalvlupgamer.model.local.ProductDao
import com.example.tiendalvlupgamer.model.local.ProductEntity
import com.example.tiendalvlupgamer.model.local.ReviewDao
import com.example.tiendalvlupgamer.model.local.ReviewEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class ProductViewModelTest : StringSpec({

     val productDao: ProductDao = mockk(relaxed = true)
     val reviewDao: ReviewDao = mockk(relaxed = true)
     lateinit var viewModel: ProductViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = ProductViewModel(productDao, reviewDao)
    }

    "onSearchQueryChange should update searchQuery" {
        viewModel.onSearchQueryChange("test")
        viewModel.searchQuery.value shouldBe "test"
    }

    "getProductById should update selectedProduct" {
        val product = ProductEntity("1", "category", "name", 10, "description", 0)
        coEvery { productDao.obtenerPorId("1") } returns product

        viewModel.getProductById("1")

        viewModel.selectedProduct.value shouldBe product
    }

    "addReview should insert review" {
        val review = ReviewEntity(productId = "1", rating = 5, comment = "comment")
        coEvery { reviewDao.insert(review) } returns Unit

        viewModel.addReview("1", 5, "comment")
    }

})