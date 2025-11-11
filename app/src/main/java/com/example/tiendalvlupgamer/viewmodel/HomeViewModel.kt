package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.model.CategoriaConProductos
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductoRepository) : ViewModel() {

    private val _homeState = MutableLiveData<List<CategoriaConProductos>>(emptyList())
    val homeState: LiveData<List<CategoriaConProductos>> = _homeState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadHomePageContent()
    }

    private fun loadHomePageContent() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Obtener la lista de categorías
                val categoriasResponse = repository.getCategorias()
                if (categoriasResponse.isSuccessful) {
                    val categorias = categoriasResponse.body()?.content ?: emptyList()

                    // 2. Para cada categoría, lanzar una tarea asíncrona para obtener sus productos
                    val categoriasConProductosDeferred = categorias.map { categoria ->
                        async {
                            val productosResponse = repository.getProductos(page = 0, size = 10, categoriaId = categoria.id)
                            if (productosResponse.isSuccessful) {
                                CategoriaConProductos(
                                    categoria = categoria,
                                    productos = productosResponse.body()?.content ?: emptyList()
                                )
                            } else {
                                null
                            }
                        }
                    }
                    
                    // 3. Esperar a que todas las llamadas terminen y filtrar las que fallaron
                    val resultados = categoriasConProductosDeferred.awaitAll().filterNotNull()
                    _homeState.postValue(resultados)

                } else {
                    _error.postValue("Error al cargar las categorías")
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al cargar el contenido: ${e.message}")
            }
            _isLoading.value = false
        }
    }
    
    fun clearError(){
        _error.value = null
    }
}
