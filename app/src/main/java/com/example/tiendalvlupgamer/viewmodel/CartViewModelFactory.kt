package com.example.tiendalvlupgamer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendalvlupgamer.data.repository.CarritoRepository
import com.example.tiendalvlupgamer.data.repository.PedidoRepository
import com.example.tiendalvlupgamer.model.local.AppDatabase

class CartViewModelFactory(
    private val context: Context,
    private val carritoRepository: CarritoRepository,
    private val pedidoRepository: PedidoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            val cartDao = AppDatabase.get(context).cartDao()
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(carritoRepository, pedidoRepository, cartDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
