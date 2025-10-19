package com.example.tiendalvlupgamer.model.local
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM productos ORDER BY name ASC, id ASC")
    fun observarTodos(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerPorId(id: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(productos: List<ProductEntity>)

    @Query("SELECT * FROM productos WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun buscarProductos(query: String): Flow<List<ProductEntity>>
}