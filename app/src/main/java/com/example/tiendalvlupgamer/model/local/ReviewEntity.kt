package com.example.tiendalvlupgamer.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "reviews", foreignKeys = [ForeignKey(
    entity = ProductEntity::class,
    parentColumns = ["id"],
    childColumns = ["productId"],
    onDelete = ForeignKey.CASCADE
)])
data class ReviewEntity (
    @PrimaryKey(autoGenerate = true)
    val reviewId: Int = 0,
    val productId: String,
    val rating: Int,
    val comment: String,
    val author: String = "Usuario Anónimo"
)