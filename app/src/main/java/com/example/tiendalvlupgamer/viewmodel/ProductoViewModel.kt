package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.data.repository.ReviewRepository
import com.example.tiendalvlupgamer.model.ApiErrorResponse
import com.example.tiendalvlupgamer.model.ProductoResponse
import com.example.tiendalvlupgamer.model.ReviewRequest
import com.example.tiendalvlupgamer.model.ReviewResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val productoRepository: ProductoRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _productos = MutableLiveData<List<ProductoResponse>>(emptyList())
    val productos: LiveData<List<ProductoResponse>> = _productos

    private val _productoDetail = MutableLiveData<ProductoResponse?>()
    val productoDetail: LiveData<ProductoResponse?> = _productoDetail

    private val _reviews = MutableLiveData<List<ReviewResponse>>(emptyList())
    val reviews: LiveData<List<ReviewResponse>> = _reviews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingMore = MutableLiveData<Boolean>()
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _reviewAdded = MutableLiveData<Boolean>()
    val reviewAdded: LiveData<Boolean> = _reviewAdded

    private var currentPage = 0
    private var isLastPage = false
    private var currentQuery: String? = null

    fun loadNextPage() {
        if (isLoading.value == true || isLoadingMore.value == true || isLastPage) return

        viewModelScope.launch {
            if (currentPage == 0) {
                _isLoading.postValue(true)
            } else {
                _isLoadingMore.postValue(true)
            }

            try {
                val response = if (currentQuery.isNullOrEmpty()) {
                    productoRepository.getProductos(page = currentPage, size = 10)
                } else {
                    productoRepository.searchProductos(query = currentQuery!!, page = currentPage, size = 10)
                }

                if (response.isSuccessful) {
                    val newPage = response.body()
                    if (newPage != null) {
                        val currentList = _productos.value ?: emptyList()
                        _productos.postValue(currentList + newPage.content)
                        isLastPage = newPage.isLast
                        if (!isLastPage) {
                            currentPage++
                        }
                    }
                } else {
                    _error.postValue("Error al cargar productos: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al cargar productos: ${e.message}")
            }

            if (currentPage == 1) { 
                _isLoading.postValue(false)
            } else {
                _isLoadingMore.postValue(false)
            }
        }
    }

    fun search(query: String) {
        currentQuery = query
        resetAndLoad()
    }

    private fun resetAndLoad() {
        currentPage = 0
        isLastPage = false
        _productos.value = emptyList()
        loadNextPage()
    }

    fun getProductoById(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _productoDetail.value = null
            _reviews.value = emptyList()
            try {
                val response = productoRepository.getProductoById(id)
                if (response.isSuccessful) {
                    _productoDetail.postValue(response.body())
                    loadReviews(id) // Cargar reseñas después de obtener el producto
                } else {
                    _error.postValue("Error al obtener el detalle del producto: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al obtener detalle: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    private fun loadReviews(productId: Long) {
        viewModelScope.launch {
            try {
                val response = reviewRepository.getReviewsForProduct(productId)
                if (response.isSuccessful) {
                    _reviews.postValue(response.body())
                }
            } catch (e: Exception) {
                // Silencio
            }
        }
    }

    fun addReview(productId: Long, calificacion: Int, comentario: String) {
        viewModelScope.launch {
            val request = ReviewRequest(calificacion, comentario)
            try {
                val response = reviewRepository.createReview(productId, request)
                if (response.isSuccessful) {
                    _reviewAdded.postValue(true)
                    loadReviews(productId) 
                } else {
                    // Intentar decodificar el cuerpo del error
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody != null) {
                        try {
                            val errorResponse = Gson().fromJson(errorBody, ApiErrorResponse::class.java)
                            errorResponse.message
                        } catch (e: Exception) {
                            response.message() // Fallback al mensaje genérico
                        }
                    } else {
                        response.message()
                    }
                    _error.postValue(errorMessage)
                }
            } catch (e: Exception) {
                 _error.postValue("Excepción al añadir reseña: ${e.message}")
            }
        }
    }
    
    fun onReviewAddedConsumed() {
        _reviewAdded.value = false
    }

    fun clearError(){
        _error.value = null
    }
}
