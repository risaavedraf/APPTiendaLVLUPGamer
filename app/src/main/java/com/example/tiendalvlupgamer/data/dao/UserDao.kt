package com.example.tiendalvlupgamer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tiendalvlupgamer.data.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE (email = :emailOrUsername OR username = :emailOrUsername) AND password = :password LIMIT 1")
    suspend fun login(emailOrUsername: String, password: String): User?
}