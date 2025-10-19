package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendalvlupgamer.model.local.ProductDao
import com.example.tiendalvlupgamer.model.local.ReviewDao

class ProductViewModelFactory(private val productDao: ProductDao,private val reviewDao: ReviewDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(productDao,reviewDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
