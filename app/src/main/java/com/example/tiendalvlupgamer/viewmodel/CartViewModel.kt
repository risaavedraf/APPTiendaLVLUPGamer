package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.model.local.CartDao
import com.example.tiendalvlupgamer.model.local.CartItemEntity
import com.example.tiendalvlupgamer.model.local.ProductEntity
import com.example.tiendalvlupgamer.model.local.CartItemWithProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// --- NUEVAS CLASES DE DATOS PARA UN ESTADO MÁS CLARO ---
data class CartUiItem(val product: ProductEntity, val quantity: Int)

data class CartState(
    val cartItems: List<CartUiItem> = emptyList(),
    val subtotal: Double = 0.0,
    val discountAmount: Double = 0.0,
    val total: Double = 0.0,
    val appliedCoupon: String? = null,
    val couponMessage: String? = null
)

class CartViewModel(private val cartDao: CartDao) : ViewModel() {

    // 1. Mapa de cupones (código -> valor de descuento)
    private val coupons = mapOf(
        "DESCUENTO10" to 0.10, // 10% de descuento
        "PROMO20" to 0.20,     // 20% de descuento
        "LVLUP5000" to 5000.0   // 5000 CLP de descuento fijo
    )

    // 2. Estado para el código del cupón aplicado y mensajes para la UI
    private val _appliedCouponCode = MutableStateFlow<String?>(null)
    private val _couponMessage = MutableStateFlow<String?>(null)
    val couponMessage: StateFlow<String?> = _couponMessage

    // 3. El Flow principal que combina todo y emite un único estado del carrito
    val cartState: Flow<CartState> = combine(
        cartDao.getCartItems(),
        _appliedCouponCode
    ) { items, couponCode ->
        val cartUiItems = items.map { CartUiItem(it.product, it.quantity) }
        val subtotal = cartUiItems.sumOf { it.product.price.toDouble() * it.quantity }

        var discountAmount = 0.0
        if (couponCode != null) {
            val couponValue = coupons[couponCode]
            if (couponValue != null) {
                discountAmount = if (couponValue <= 1.0) {
                    subtotal * couponValue // Descuento porcentual
                } else {
                    couponValue // Descuento de monto fijo
                }
            }
        }

        // Asegurarse de que el descuento no sea mayor que el subtotal
        if (discountAmount > subtotal) {
            discountAmount = subtotal
        }

        val total = subtotal - discountAmount

        // Emitir el nuevo estado completo
        CartState(
            cartItems = cartUiItems,
            subtotal = subtotal,
            discountAmount = discountAmount,
            total = total,
            appliedCoupon = couponCode,
            couponMessage = _couponMessage.value
        )
    }

    // 4. Función para aplicar un cupón desde la UI
    fun applyCoupon(code: String) {
        val upperCaseCode = code.uppercase()
        if (coupons.containsKey(upperCaseCode)) {
            _appliedCouponCode.value = upperCaseCode
            _couponMessage.value = "¡Cupón '$upperCaseCode' aplicado con éxito!"
        } else {
            _couponMessage.value = "El cupón '$upperCaseCode' no es válido."
        }
    }
    
    fun removeCoupon() {
        _appliedCouponCode.value = null
        _couponMessage.value = null
    }

    // --- Las funciones existentes se mantienen, pero con pequeños ajustes ---
    fun addOrUpdateProduct(product: ProductEntity, quantity: Int) {
        viewModelScope.launch {
            if (quantity > 0) {
                val cartItem = CartItemEntity(productId = product.id, quantity = quantity)
                cartDao.insert(cartItem)
            } else {
                removeProduct(product)
            }
        }
    }

    fun removeProduct(product: ProductEntity) {
        viewModelScope.launch {
            cartDao.deleteByProductId(product.id)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
            removeCoupon() // También limpiar el cupón al vaciar el carrito
        }
    }
}