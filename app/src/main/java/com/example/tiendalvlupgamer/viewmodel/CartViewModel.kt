package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.CarritoRepository
import com.example.tiendalvlupgamer.data.repository.PedidoRepository
import com.example.tiendalvlupgamer.model.*
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CartViewModel(
    private val carritoRepository: CarritoRepository,
    private val pedidoRepository: PedidoRepository 
) : ViewModel() {

    private val _cartState = MutableLiveData<CarritoResponse?>()
    val cartState: LiveData<CarritoResponse?> = _cartState

    private val _checkoutResult = MutableLiveData<PedidoResponse?>()
    val checkoutResult: LiveData<PedidoResponse?> = _checkoutResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _couponMessage = MutableLiveData<String?>()
    val couponMessage: LiveData<String?> = _couponMessage

    init {
        loadCarrito()
    }

    fun loadCarrito() {
        viewModelScope.launch {
            handleResponse(carritoRepository.getCarrito()) { "Error al cargar el carrito" }
        }
    }

    fun addItem(productoId: Long, cantidad: Int) {
        viewModelScope.launch {
            val request = AddItemRequest(productoId, cantidad)
            handleResponse(carritoRepository.addItem(request)) { "Error al añadir producto" }
        }
    }

    fun updateQuantity(productoId: Long, cantidad: Int) {
        viewModelScope.launch {
            val request = UpdateQuantityRequest(cantidad)
            handleResponse(carritoRepository.updateItemQuantity(productoId, request)) { "Error al actualizar cantidad" }
        }
    }

    fun deleteItem(productoId: Long) {
        viewModelScope.launch {
            handleResponse(carritoRepository.deleteItem(productoId)) { "Error al eliminar producto" }
        }
    }

    fun applyCoupon(codigo: String) {
        viewModelScope.launch {
            val request = CuponRequest(codigo)
            handleResponse(carritoRepository.aplicarCupon(request),
                successMessage = "¡Cupón aplicado con éxito!",
                errorMessage = { "Error al aplicar el cupón" }
            )
        }
    }

    fun removeCoupon() {
        viewModelScope.launch {
            handleResponse(carritoRepository.quitarCupon()) { "Error al quitar el cupón" }
        }
    }

    fun realizarCheckout(direccionId: Long) {
        val items = _cartState.value?.items?.map {
            CheckoutItemRequest(productId = it.productoId, quantity = it.cantidad)
        }

        if (items.isNullOrEmpty()) {
            _error.postValue("El carrito está vacío.")
            return
        }

        val request = CheckoutRequest(direccionId = direccionId, items = items)

        viewModelScope.launch {
            try {
                val response = pedidoRepository.realizarCheckout(request)
                if (response.isSuccessful) {
                    _checkoutResult.postValue(response.body())
                    _cartState.postValue(null) // Limpiar el estado del carrito
                } else {
                    handleErrorResponse(response.errorBody()?.string(), response.message())
                }
            } catch (e: Exception) {
                _error.postValue("Excepción durante el checkout: ${e.message}")
            }
        }
    }
    
    // --- Métodos de Ayuda ---

    private suspend fun handleResponse(
        response: retrofit2.Response<CarritoResponse>,
        successMessage: String? = null,
        errorMessage: () -> String
    ) {
        try {
            if (response.isSuccessful) {
                _cartState.postValue(response.body())
                successMessage?.let { _couponMessage.postValue(it) }
            } else {
                handleErrorResponse(response.errorBody()?.string(), response.message())
            }
        } catch (e: Exception) {
            _error.postValue("Excepción: ${e.message}")
        }
    }

    private fun handleErrorResponse(errorBody: String?, genericMessage: String) {
        val specificMessage = if (errorBody != null) {
            try {
                Gson().fromJson(errorBody, ApiErrorResponse::class.java).message
            } catch (e: Exception) { genericMessage }
        } else { genericMessage }
        _error.postValue(specificMessage)
    }

    fun clearError() {
        _error.value = null
    }
    
    fun clearCouponMessage() {
        _couponMessage.value = null
    }

    fun onCheckoutConsumed(){
        _checkoutResult.value = null
    }
}
