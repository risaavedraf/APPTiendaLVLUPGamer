package com.example.tiendalvlupgamer.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Nueva clase de datos para el resultado del JOIN
data class CartItemWithProduct(
    @Embedded val product: ProductEntity,
    val quantity: Int
)

@Dao
interface CartDao {

    // La consulta ahora devuelve un Flow de la nueva clase de datos
    @Query("""
        SELECT p.*, c.quantity
        FROM productos p 
        INNER JOIN cart_items c ON p.id = c.productId
    """)
    fun getCartItems(): Flow<List<CartItemWithProduct>>

    // Inserta un nuevo producto al carrito. Si ya existe, lo reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItemEntity)

    // Actualiza la cantidad de un producto en el carrito
    @Update
    suspend fun update(cartItem: CartItemEntity)

    // Elimina un producto del carrito (NO USAR, es propenso a errores)
    @Delete
    suspend fun delete(cartItem: CartItemEntity)

    // NUEVA FUNCIÓN: Elimina un artículo por el ID del producto
    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteByProductId(productId: String)

    // Vacía todo el carrito
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}