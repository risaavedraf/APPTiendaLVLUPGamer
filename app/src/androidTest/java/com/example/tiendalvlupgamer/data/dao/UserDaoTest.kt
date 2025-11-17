package com.example.tiendalvlupgamer.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tiendalvlupgamer.data.entity.User
import com.example.tiendalvlupgamer.model.local.AppDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        userDao = database.userDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetUserByEmail() = runTest {
        val userToInsert = User(
            name = "Test",
            lastName = "User",
            username = "testuser",
            birthDate = "2000-01-01",
            email = "test@example.com",
            password = "password"
        )
        userDao.insertUser(userToInsert)
        
        val retrievedUser = userDao.getUserByEmail("test@example.com")
        
        assertThat(retrievedUser).isNotNull()
        assertThat(retrievedUser?.username).isEqualTo(userToInsert.username)
        assertThat(retrievedUser?.email).isEqualTo(userToInsert.email)
    }

    @Test
    fun loginWithCorrectCredentials() = runTest {
        val userToInsert = User(
            name = "Test",
            lastName = "User",
            username = "testuser",
            birthDate = "2000-01-01",
            email = "test@example.com",
            password = "password"
        )
        userDao.insertUser(userToInsert)
        
        val loggedInUser = userDao.login("test@example.com", "password")
        
        assertThat(loggedInUser).isNotNull()
        assertThat(loggedInUser?.email).isEqualTo(userToInsert.email)
    }

    @Test
    fun loginWithIncorrectCredentials() = runTest {
        val userToInsert = User(
            name = "Test",
            lastName = "User",
            username = "testuser",
            birthDate = "2000-01-01",
            email = "test@example.com",
            password = "password"
        )
        userDao.insertUser(userToInsert)
        
        val loggedInUser = userDao.login("test@example.com", "wrongpassword")
        
        assertThat(loggedInUser).isNull()
    }
}