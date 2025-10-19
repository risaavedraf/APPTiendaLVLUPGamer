package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.model.local.CartDao
import com.example.tiendalvlupgamer.model.local.CartItemEntity
import com.example.tiendalvlupgamer.model.local.ProductEntity
import com.example.tiendalvlupgamer.model.local.CartItemWithProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Esta clase de datos sigue siendo útil para la UI
data class CartUiItem(val product: ProductEntity, val quantity: Int)

class CartViewModel(private val cartDao: CartDao) : ViewModel() {

    // Ajustamos el mapeo para que funcione con la nueva estructura
    val cartState: Flow<Pair<List<CartUiItem>, Double>> = cartDao.getCartItems().map {
        // "it" es ahora una List<CartItemWithProduct>
        val itemList = it.map { item -> CartUiItem(item.product, item.quantity) }
        val totalPrice = itemList.sumOf { item -> item.product.price * item.quantity }
        Pair(itemList, totalPrice.toDouble())
    }

    fun addOrUpdateProduct(product: ProductEntity, quantity: Int) {
        viewModelScope.launch {
            if (quantity > 0) {
                val cartItem = CartItemEntity(productId = product.id, quantity = quantity)
                cartDao.insert(cartItem)
            } else {
                // Si la cantidad es 0 o menos, elimina el producto del carrito
                removeProduct(product)
            }
        }
    }

    fun removeProduct(product: ProductEntity) {
        viewModelScope.launch {
            // ¡CORREGIDO! Usamos la nueva función del DAO
            cartDao.deleteByProductId(product.id)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
        }
    }
}
