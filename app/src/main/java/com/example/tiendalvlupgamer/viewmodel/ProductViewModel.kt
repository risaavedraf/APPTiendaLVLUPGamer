package com.example.tiendalvlupgamer.viewmodel

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.model.local.ProductDao
import com.example.tiendalvlupgamer.model.local.ProductEntity
import com.example.tiendalvlupgamer.model.local.ReviewDao
import com.example.tiendalvlupgamer.model.local.ReviewEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val productDao: ProductDao, private val reviewDao: ReviewDao) : ViewModel() {

    val productsByCategory: StateFlow<Map<String, List<ProductEntity>>> =
        productDao.observarTodos()
            .map { products ->
                products.groupBy { it.category }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 2. Función para actualizar el texto de búsqueda desde la UI.
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // 3. Flow que reacciona a los cambios en searchQuery y obtiene los resultados de la BD.
    val searchResults: StateFlow<List<ProductEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                // Si la búsqueda está vacía, devuelve un flow con una lista vacía.
                productDao.observarTodos()
            } else {
                productDao.buscarProductos(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    // 1. StateFlow para almacenar el producto seleccionado actualmente.
    private val _selectedProduct = MutableStateFlow<ProductEntity?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    // 2. Función para obtener un producto por su ID y actualizar el StateFlow.
    fun getProductById(id: String) {
        viewModelScope.launch {
            // Se ejecuta en el hilo de fondo gracias a que la función del DAO es 'suspend'
            _selectedProduct.value = productDao.obtenerPorId(id)
        }
    }

    // 3. Función para limpiar el producto seleccionado (útil al salir de la pantalla).
    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }
    val reviewsForProduct: StateFlow<List<ReviewEntity>> =
        selectedProduct.flatMapLatest { product ->
            if (product != null) {
                reviewDao.getReviewsForProduct(product.id)
            } else {
                MutableStateFlow(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Función para añadir una nueva reseña
    fun addReview(productId: String, rating: Int, comment: String) {
        viewModelScope.launch {
            if (comment.isNotBlank()) {
                val newReview = ReviewEntity(
                    productId = productId,
                    rating = rating,
                    comment = comment
                )
                reviewDao.insert(newReview)
            }
        }
    }

}
