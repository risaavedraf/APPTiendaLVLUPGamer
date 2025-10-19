package com.example.tiendalvlupgamer.model.local

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductEntity(
    @PrimaryKey val id: String,
    val category: String,
    val name: String,
    val price: Int,
    val description: String,
    @DrawableRes val image: Int
)