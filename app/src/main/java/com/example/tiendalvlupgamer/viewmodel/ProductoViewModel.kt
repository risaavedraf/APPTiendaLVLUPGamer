package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.model.ProductoResponse
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    private val _productos = MutableLiveData<List<ProductoResponse>>(emptyList())
    val productos: LiveData<List<ProductoResponse>> = _productos

    private val _productoDetail = MutableLiveData<ProductoResponse?>()
    val productoDetail: LiveData<ProductoResponse?> = _productoDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingMore = MutableLiveData<Boolean>()
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPage = 0
    private var isLastPage = false
    private var currentQuery: String? = null

    init {
        loadNextPage()
    }

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
                    repository.getProductos(page = currentPage, size = 10)
                } else {
                    repository.searchProductos(query = currentQuery!!, page = currentPage, size = 10)
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

            if (currentPage == 1) { // Fue la primera página
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
            try {
                val response = repository.getProductoById(id)
                if (response.isSuccessful) {
                    _productoDetail.postValue(response.body())
                } else {
                    _error.postValue("Error al obtener el detalle del producto: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al obtener detalle: ${e.message}")
            }
            _isLoading.value = false
        }
    }
    
    fun clearError(){
        _error.value = null
    }
}
