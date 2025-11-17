package com.example.tiendalvlupgamer.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysAllElements() {
        composeTestRule.setContent {
            LoginScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithTag("emailOrUsernameInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("passwordInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loginButton").assertIsDisplayed()
    }

    @Test
    fun loginScreen_successfulLogin() {
        composeTestRule.setContent {
            LoginScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithTag("emailOrUsernameInput").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("password")
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Here you would typically verify navigation or a success message.
        // For this test, we just ensure the button click doesn't crash.
    }
}